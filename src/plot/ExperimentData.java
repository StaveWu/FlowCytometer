package plot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ExperimentData {
	
	private HashMap<String, double[]> map = new HashMap<String, double[]>();
	
	public ExperimentData(String[] names, double[][] data) {
		for(int i = 0; i < names.length; i++) {
			map.put(names[i], data[i]);
		}
	}
	
	public ExperimentData() {}
	
	public void add(String name, double[] data) {
		if (name == null) {
			throw new RuntimeException("添加失败！请检查参数是否正确");
		}
		map.put(name, data);
	}
	
	public void remove(String name) {
		if(map.containsKey(name)) {
			map.remove(name);
		}
	}
	
	public double[] getDataByName(String name) {
		if(map.containsKey(name)) {
			return map.get(name);
		}
		return null;
	}
	
	public boolean isEmpty() {
		return map.size() <= 0;
	}
	
	/**
	 * 获取行数
	 * @return
	 */
	public int getDataIdsCount() {
		if (getDataNamesCount() > 0) {
			for (String ele : map.keySet()) {
				double[] aData = getDataByName(ele);
				if (aData != null) {
					return aData.length;
				}
				break;
			}
		}
		return -1;
	}
	
	/**
	 * 获取列数
	 * @return
	 */
	public int getDataNamesCount() {
		return map.size();
	}
	
	public String[] getDataNames() {
		if (map.size() <= 0) {
			return null;
		}
		List<String> nl = new ArrayList<String>();
		Iterator<String> iter = map.keySet().iterator();
		while(iter.hasNext()) {
			nl.add(iter.next());
		}
		return convertToArray(nl);
	}
	
	private String[] convertToArray(List<String> arrlist) {
		if (arrlist == null) {
			return null;
		}
		String[] res = new String[arrlist.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = arrlist.get(i);
		}
		return res;
	}
	
	public List<Double> getDataById(int id) {
		if(map.size() <= 0) {
			return null;
		}
		List<Double> res = new ArrayList<Double>();
		for(String ele : map.keySet()) {
			double[] data = getDataByName(ele);
			if (data != null) {
				res.add(getDataByName(ele)[id]);
			}
		}
		return res;
	}

}
