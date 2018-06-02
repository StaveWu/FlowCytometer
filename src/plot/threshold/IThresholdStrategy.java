package plot.threshold;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * 区域划分策略：
 * 2D
 * 3D
 * 
 * @author wteng
 *
 */
public interface IThresholdStrategy {
	
	/**
	 * 获取X轴区
	 * @param r	图的整个区域
	 * @return
	 */
	Rectangle getAxisRegionX(Rectangle r);
	
	/**
	 * 获取Y轴区
	 * @param r 图的整个区域
	 * @return
	 */
	Rectangle getAxisRegionY(Rectangle r);
	
	/**
	 * 获取Z轴区
	 * @param r 图的整个区域
	 * @return
	 */
	Rectangle getAxisRegionZ(Rectangle r);
	
	/**
	 * 获取数据区
	 * @param r 图的整个区域
	 * @return
	 */
	Rectangle getDataRegion(Rectangle r);
	
	/**
	 * 获取原点坐标
	 * @param r 图的整个区域
	 * @return
	 */
	Point getOrigin(Rectangle r);
	
	/**
	 * 获取轴的长度数组
	 * @param r 图的整个区域
	 * @return
	 */
	int[] getAxisLengths(Rectangle r);

}
