package protocol;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import kr.re.nsr.crypto.BlockCipher.Mode;
import kr.re.nsr.crypto.BlockCipherMode;
import kr.re.nsr.crypto.symm.LEA;

public class SsdCryptoModule {
	private byte[] key = new byte[16];
	private BlockCipherMode crypt_module = null;
	
	//배열 전체에 랜덤한 값을 넣어 키 생성
	public void keyGenerate()	{
		for(int i = 0;i<key.length;i++)		{
			key[i] = (byte) ((int)(Math.random() * 256) - 128); 
		}
	}
	
	//입력받은 문자열로 해쉬함수를 돌려 키를 생성
	/*
	 * 예제
	 * SsdCryptoModule ssd = new SsdCryptoModule();
		ssd.keyGenerate("가나다라");
		for(int i=0;i<ssd.getKey().length;i++){
			System.out.printf("%x",ssd.getKey()[i]);
		}
	 */
	public void keyGenerate(String pass){
		byte[] shabyte = null;
		try{
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			sha.update(pass.getBytes());
			shabyte = sha.digest();
		}catch(NoSuchAlgorithmException e){
			shabyte = null;
			System.out.println("키 생성 실패");
		}
		if(shabyte != null){
			for(int i=0;i<key.length;i++){
				key[i] = shabyte[i];
			}
		}
	}
	
	//생성자
	public SsdCryptoModule(){
		crypt_module = new LEA.ECB();
	}
	
	//패킷을 받아서 암호화된 byte배열로 리턴
	public byte[] enCrypt(Packet p){
		byte[][] b = p.getPacket();
		byte[] block1 = null;
		byte[] block2 = null;
		byte[] tmpkey = new byte[16];
		byte[] enblock = new byte[32];
		
		if(key != null)		{
			this.crypt_module.init(Mode.ENCRYPT,this.key);
			block1 = this.crypt_module.update(b[0]);
			for(int i=0;i<16;i++){
				tmpkey[i] = (byte) (key[i] ^ block1[i]);
			}
			this.crypt_module.init(Mode.ENCRYPT,tmpkey);
			block2 = this.crypt_module.update(b[1]);
		}
		for(int i=0;i<16;i++)		{
			enblock[i] = block1[i];
		}
		for(int i=0;i<16;i++)		{
			enblock[i+16] = block2[i];
		}
		return enblock;
	}
	//입력받은 바이트 배열을 복호화 하여 바이트 배열로 리턴
	public byte[] deCrypt(byte[] a){
		byte[] deblock = new byte[32];
		byte[] block1 = new byte[16];
		byte[] block2 = new byte[16];
		byte[] tmpkey = new byte[16];
		int size = a.length/16;
		byte[][] tmp = new byte[size][16];
		for(int j=0;j<size;j++){
			for(int i=0;i<16;i++){
				tmp[j][i] = a[i+(j*16)];
			}
		}
		
		if(key != null){
			for(int i=0;i<16;i++){
				tmpkey[i] = (byte)(tmp[0][i] ^ key[i]);
			}
			this.crypt_module.init(Mode.DECRYPT, key);
			block1 = this.crypt_module.update(tmp[0]);
			this.crypt_module.init(Mode.DECRYPT, tmpkey);
			block2 = this.crypt_module.update(tmp[1]);
			for(int i=0;i<16;i++)		{
				deblock[i] = block1[i];
			}
			for(int i=0;i<16;i++)		{
				deblock[i+16] = block2[i];
			}
		}
		return deblock;
	}

	public byte[] getKey() {
		return key;
	}
}
