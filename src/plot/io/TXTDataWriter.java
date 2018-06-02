package plot.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import plot.ExperimentData;

public class TXTDataWriter {
	
	/**
	 * 写入文件，用\t分开
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
			//打印数据名
			int i = 0;
			for (String ele : names) {
				out.print(ele);
				if(i != names.length - 1) {
					out.print("\t");
				}
				i++;
			}
			out.println();
			//打印数据
			for (int j = 0; j < data.getDataByName(names[0]).length; j++) {	//j行
				for (int k = 0; k < names.length; k++) {					//k列
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
