package plot.axis;

import java.util.List;

public interface IAxisAlgebraStrategy {
	
	/**
	 * 根据最大值最小值划分大刻度
	 */
	List<Double> calibrateBigScale(double minValue, double maxValue);
	
	
	/**
	 * 根据大刻度值划分小刻度
	 */
	List<Double> calibrateSmallScale(List<Double> bsValues);
	
	
	/**
	 * 根据大刻度值像素化为一系列坐标点距（每个点与第一个点的距离）
	 */
	List<Integer> pixelateBigScale(List<Double> bsValues, int length);
	
	
	/**
	 * 根据小刻度值像素化为一系列坐标点距（每个点与第一个点的距离）
	 */
	List<Integer> pixelateSmallScale(List<Double> ssValues, double minValue, 
			double maxValue, int length);


}
