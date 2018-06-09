package test;

import java.util.LinkedHashMap;
import java.util.Map;

public class Test {
	
	public static void main(String[] args) {
		String dataStr = "FSC:10.0_SSC:21.22_FITC:3.99";
		System.out.println(new Test().decode(dataStr.getBytes()));
	}
	
	private Map<String, Double> decode(byte[] data) {
		// 数据格式为：FSC:XXX_SSC:XXX_FITC:XXX
		Map<String, Double> res = new LinkedHashMap<>();
		String dataStr = new String(data);
		for (String ele : dataStr.split("_")) {
			String[] kv = ele.split(":");
			res.put(kv[0], Double.valueOf(kv[1]));
		}
		return res;
	}

}
