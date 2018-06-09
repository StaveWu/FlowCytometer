package device;

import java.util.List;
import java.util.Map;

public class CommDataParser {
	
	public static final IDataParseStrategy PLAIN = new DataParseByPlainStrategy();
	
	private static class DataParseByPlainStrategy implements IDataParseStrategy {
		
		/**
		 * 格式如下：
		 * StartSamping:CH1,CH2,CH3
		 * StartSamping:CH1-10,CH2-10,CH3-10
		 */
		@Override
		public String encode(String cmd, Object param) throws Exception {
			return cmd + ":" + getParamMessage(param) + "\r\n";
		}
		
		private String getParamMessage(Object param) throws Exception {
			if (param == null) {
				return "";
			}
			String res = "";
			if (param instanceof Map<?, ?>) {
				Map<String, Integer> p = (Map<String, Integer>) param;
				for (String key : p.keySet()) {
					res += key + "-" + p.get(key);
					res += ",";
				}
			}
			else if (param instanceof List<?>) {
				List<String> p = (List<String>) param;
				for (String ele : p) {
					res += ele;
					res += ",";
				}
			}
			else {
				throw new NoSuchMethodException("传入的参数类型错误");
			}
			
			return res.substring(0, res.length() - 1);
		}

		@Override
		public Map<String, Double> decode(String data) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
