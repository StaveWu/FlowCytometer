package plot.axis;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IAxisTypeStrategy {
	
	/**
	 * 确定刻度（包括大刻度和小刻度）的原始点集（即统一按水平方向计算）
	 * @param distance		点距
	 * @param origin		轴的起点
	 * @param scaleLength	刻度长
	 * @return				刻度的原始点集
	 */
	Map<Point, Point> calculateScaleSrcLocations(List<Integer> distance, Point origin, int scaleLength);
	
	/**
	 * 确定大刻度值的位置
	 * @param valueWidth		刻度值的宽
	 * @param valueHeight		刻度值的高
	 * @param bsSrcLocations	刻度值的纵向中心
	 * @return					所要画刻度值的位置
	 */
	List<Point> calculateBigValueSrcLocations(List<Integer> valueWidth, int valueHeight, Set<Point> bsSrcLocations);
	
	
	/**
	 * 确定轴名称的位置
	 * @param nameWidth		轴名称的宽
	 * @param nameHeight	轴名称的高
	 * @param origin		轴的起点
	 * @param length		轴长
	 * @return				所要画轴名称的位置
	 */
	Point calculateNameLocation(int nameWidth, int nameHeight, Point origin, int length);

}
