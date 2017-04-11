package protocol;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Packet {
	private byte code;
	private byte id;
	private List<Byte> data = new LinkedList<Byte>();
	private byte padding_size;
	private byte[] padding = null;
	private byte[] nonce = new byte[4];
	
	public byte getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = (byte)code;
	}
	public byte getId() {
		return id;
	}
	public void setId(int id) {
		this.id = (byte)id;
	}
	public List<Byte> getData() {
		return data;
	}
	public void setData(byte[] b) {
		data.clear();
		for(int i=0;i<b.length;i++)
		{
			data.add(b[i]);
		}
	}
	public void getNonce(byte[] nonce) {
		for(int i = 0;i<this.nonce.length;i++){
			nonce[i] = this.nonce[i];
		}
	}
	public int getNonce(){
		int nonce = 0;
		nonce = ((this.nonce[0]<<24)|(this.nonce[1]<<16)|(this.nonce[2]<<8)|(this.nonce[3]));
		return nonce;
	}
	
	
	public void setNonce(int nonce) {
		this.nonce[0] = (byte)((0xFF000000 & nonce)>>24);
		this.nonce[1] = (byte)((0x00FF0000 & nonce)>>16);
		this.nonce[2] = (byte)((0x0000FF00 & nonce)>>8);
		this.nonce[3] = (byte)(0x000000FF & nonce);
	}
	
	public void setNonce(byte[] nonce) {
		for(int i=0;i<nonce.length;i++){
			this.nonce[i] = nonce[i];
		}
	}
	
	public byte getPadding_size() {
		return padding_size;
	}
	
	public byte[][] getPacket(){
		List<Byte> tmp = new ArrayList<>();
		tmp.add(code);
		
		tmp.add(this.padding_size);
		
		for(int i=0;i<this.padding_size;i++)		{
			tmp.add(this.padding[i]);
		}
		
		for(int i=0;i<this.nonce.length;i++)		{
			tmp.add(this.nonce[i]);
		}
		
		for(int i=0;i<this.data.size();i++)		{
			tmp.add(this.data.get(i));
		}
		int size = tmp.size()/16;
		byte[][] b = new byte[size][tmp.size()/size];
		for(int j = 0;j<size;j++){
			for(int i=0;i<tmp.size()/size;i++)		{
				b[j][i] = tmp.get(i);
			}
		}
		
		return b;
	}
	
	public void fillPadding(){
		this.padding_size = (byte) (26 - data.size());
		this.padding = new byte[padding_size];
		for(int i=0;i<this.padding_size;i++){
			this.padding[i] = (byte) ((int)(Math.random() * 256) - 128); 
		}
	}
	
	abstract public byte[] castingPacket();
}
