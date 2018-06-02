package plot.axis;

import java.util.List;

public interface IAxisAlgebraStrategy {
	
	/**
	 * �������ֵ��Сֵ���ִ�̶�
	 */
	List<Double> calibrateBigScale(double minValue, double maxValue);
	
	
	/**
	 * ���ݴ�̶�ֵ����С�̶�
	 */
	List<Double> calibrateSmallScale(List<Double> bsValues);
	
	
	/**
	 * ���ݴ�̶�ֵ���ػ�Ϊһϵ�������ࣨÿ�������һ����ľ��룩
	 */
	List<Integer> pixelateBigScale(List<Double> bsValues, int length);
	
	
	/**
	 * ����С�̶�ֵ���ػ�Ϊһϵ�������ࣨÿ�������һ����ľ��룩
	 */
	List<Integer> pixelateSmallScale(List<Double> ssValues, double minValue, 
			double maxValue, int length);


}
