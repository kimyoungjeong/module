import protocol.AndroidPacket;
import protocol.Packet;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*byte[] non = {1,1,1,1};
		SsdCryptoModule ssd = new SsdCryptoModule();
		AndroidPacket pack = new AndroidPacket();
		pack.setCode(1);
		pack.setId(1);
		pack.setNonce(non);
		pack.setData(non);
		pack.fillPadding();
		for(int i=0;i<pack.getPacket().length;i++){
			for(int j=0;j<pack.getPacket()[i].length;j++){
				System.out.print(pack.getPacket()[i][j] + " ");
			}
		}
		System.out.println();
		ssd.keyGenerate();
		byte[] tmp = ssd.enCrypt(pack);
		for(int i =0;i<tmp.length;i++)
		{
			System.out.print(tmp[i] + " ");
		}
		byte[] tmp1 = ssd.deCrypt(tmp);
		System.out.println();
		for(int i =0;i<tmp.length;i++)
		{
			System.out.print(tmp1[i] + " ");
		}*/
		
		/*UdpComPacket udp = new UdpComPacket("127.0.0.1", "5000");
		udp.setProcess(new PacketProcess() {
			
			@Override
			public void doProcess() {
				for(int i=0;i<udp.getBuffer().length;i++){
					System.out.print(udp.getBuffer()[i] + " ");
				}
			}
		});
		for(;;){
			udp.receive();
		}*/
		
		int i = 1024;
		Packet pac = new AndroidPacket();
		pac.setNonce(i);
		byte[] nonce = new byte[4];
		pac.getNonce(nonce);
		for(int j=0;j<nonce.length;j++){
			System.out.printf("%02X ",nonce[j]);
		}
		System.out.println();
		System.out.println(pac.getNonce());
	}

}
