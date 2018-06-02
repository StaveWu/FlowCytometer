package plot;

import plot.axis.Axis;

public class DataAlgebraType {
	
	public static final IDataAlgebraStrategy LOG = new DataLogStrategy();
	public static final IDataAlgebraStrategy LINEAR = new DataLinearStrategy();
	
	
	/**
	 * 两个静态内部策略类的实现
	 */
	private static class DataLogStrategy implements IDataAlgebraStrategy {

		@Override
		public double[] process(double[] data) {
			if(data == null || data.length == 0) {
				return null;
			}
			//求log
			double[] res = new double[data.length];
			for (int i = 0; i < res.length; i++) {
				res[i] = Math.log10(data[i]);
			}
			return res;
		}

		@Override
		public double[] pixelate(double[] processed, Axis axis, int length) {
			if(processed == null) {
				return null;
			}
			double[] res = new double[processed.length];
			double rate = 0.0;
			double logMax = Math.log10(axis.getMaxValue());
			double logMin = Math.log10(axis.getMinValue());
			rate = (double)length / (logMax - logMin);
			for (int i = 0; i < res.length; i++) {
				double d = processed[i];
				if (d <= logMin || d >= logMax) {
					//对于不属于坐标轴规定范围内的值赋值为-1
					res[i] = -1;
				}
				else {
					res[i] = rate * (d - logMin);
				}
			}
			return res;
		}
		
	}
	
	private static class DataLinearStrategy implements IDataAlgebraStrategy {

		@Override
		public double[] process(double[] data) {
			//线性不需要处理
			return data;
		}

		@Override
		public double[] pixelate(double[] processed, Axis axis, int length) {
			if (processed == null || processed.length == 0 || axis == null) {
				return null;
			}
			
			double[] pxCoords = new double[processed.length];
			double rate = 0.0;
			
			rate = (double) length / (axis.getMaxValue() - axis.getMinValue());
			for (int i = 0; i < pxCoords.length; i++) {
				double d = processed[i];
				if (d < axis.getMinValue() || d > axis.getMaxValue()) {
					//对于不属于坐标轴规定范围内的值赋值为-1
					pxCoords[i] = -1;
				}
				else {
					pxCoords[i] = rate * (d - axis.getMinValue());
				}
			}
			
			return pxCoords;
		}
		
	}

}
