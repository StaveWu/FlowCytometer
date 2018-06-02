package plot.axis;

import java.awt.Graphics2D;
import java.awt.Point;

public interface IDimensionStrategy {
	
	/**
	 * 将画布做一个变换
	 * 2d轴x：不做变换
	 * 3d轴x：长方形->平行四边形->旋转
	 */
	void tranform(Graphics2D g2d);
	
	/**
	 * 获取变换后的末端坐标
	 * @param start
	 * @param length
	 * @return
	 */
	Point getEnd(Point start, int length);

}
