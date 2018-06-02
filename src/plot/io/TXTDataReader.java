package plot.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import plot.ExperimentData;

public class TXTDataReader {
	
	/**
	 * ��ȡ�ļ���д��ExperimentData��
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public static ExperimentData read(File file) throws IOException {
		
		ExperimentData data = new ExperimentData();
		
		ArrayList<ArrayList<Double>> total = new ArrayList<>();//�������ݾ���
		ArrayList<String> names = new ArrayList<>();//��������
		
		BufferedReader in = new BufferedReader(new FileReader(file));
		String s;
		int i = 0;
		while((s = in.readLine()) != null) {
			String[] str = s.split("\t");
			if(i == 0) {
				for (String ele : str) {
					names.add(ele);
				}
			}
			else {
				ArrayList<Double> row = new ArrayList<>();
				for (String ele : str) {
					row.add(Double.parseDouble(ele));
				}
				total.add(row);
			}
			i++;
		}
		in.close();
		//д��ExperimentData
		for (int j = 0; j < total.get(0).size(); j++) {	//j��
			double[] column = new double[total.size()];
			for (int k = 0; k < column.length; k++) {	//k��
				column[k] = total.get(k).get(j);
			}
			data.add(names.get(j), column);
		}
		
		return data;
	}

}
