package device;

import java.util.Map;

public class CommDataParser {
	
	public static final IDataParseStrategy PLAIN = new DataParseByPlainStrategy();
	
	private static class DataParseByPlainStrategy implements IDataParseStrategy {

		@Override
		public byte[] encode() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Map<String, Double> decode(byte[] data) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
