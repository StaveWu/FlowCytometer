package conn;

public class DataParser {
	
	public static final IDataParseStrategy PLAIN = new DataParseByPlainStrategy();
	
	private static class DataParseByPlainStrategy implements IDataParseStrategy {

		@Override
		public byte[] encode() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public double[] decode(byte[] data) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
