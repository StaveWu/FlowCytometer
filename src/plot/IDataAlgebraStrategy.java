package plot;

import plot.axis.Axis;

/**
 * 代数策略:
 * log
 * linear
 * 
 * @author wteng
 *
 */
public interface IDataAlgebraStrategy {
	
	/**
	 * 预处理数据
	 */
	double[] process(double[] data);
	
	/**
	 * 像素化预处理后的数据
	 */
	double[] pixelate(double[] processed, Axis axis, int length);

}
