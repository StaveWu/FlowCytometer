package device;

import java.util.Map;

public interface IDataParseStrategy {
	
	byte[] encode();
	
	Map<String, Double> decode(byte[] data);

}
