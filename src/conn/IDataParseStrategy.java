package conn;

public interface IDataParseStrategy {
	
	byte[] encode();
	
	double[] decode(byte[] data);

}
