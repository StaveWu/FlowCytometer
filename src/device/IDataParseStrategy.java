package device;

import java.util.Map;

public interface IDataParseStrategy {
	
	String encode(String cmd, Object param) throws Exception;
	
	Map<String, Double> decode(String data) throws Exception;

}
