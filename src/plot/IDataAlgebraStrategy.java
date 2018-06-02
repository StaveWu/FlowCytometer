package plot;

import plot.axis.Axis;

/**
 * ��������:
 * log
 * linear
 * 
 * @author wteng
 *
 */
public interface IDataAlgebraStrategy {
	
	/**
	 * Ԥ��������
	 */
	double[] process(double[] data);
	
	/**
	 * ���ػ�Ԥ����������
	 */
	double[] pixelate(double[] processed, Axis axis, int length);

}
