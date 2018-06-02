package plot.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import plot.ExperimentData;

public class TXTDataWriter {
	
	/**
	 * д���ļ�����\t�ֿ�
	 * @param data
	 * @param file
	 * @throws IOException 
	 */
	public static void write(ExperimentData data, File file) throws IOException {
		if(data == null) {
			return;
		}
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		String[] names = data.getDataNames();
		if(names == null || names.length <= 0) {
			out.print("");
		}
		else {
			//��ӡ������
			int i = 0;
			for (String ele : names) {
				out.print(ele);
				if(i != names.length - 1) {
					out.print("\t");
				}
				i++;
			}
			out.println();
			//��ӡ����
			for (int j = 0; j < data.getDataByName(names[0]).length; j++) {	//j��
				for (int k = 0; k < names.length; k++) {					//k��
					double[] column = data.getDataByName(names[k]);
					out.print((int)column[j]);
					if(k != names.length - 1) {
						out.print("\t");
					}
				}
				out.println();
			}
		}
		out.close();
	}

}
