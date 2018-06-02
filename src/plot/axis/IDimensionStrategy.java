package plot.axis;

import java.awt.Graphics2D;
import java.awt.Point;

public interface IDimensionStrategy {
	
	/**
	 * ��������һ���任
	 * 2d��x�������任
	 * 3d��x��������->ƽ���ı���->��ת
	 */
	void tranform(Graphics2D g2d);
	
	/**
	 * ��ȡ�任���ĩ������
	 * @param start
	 * @param length
	 * @return
	 */
	Point getEnd(Point start, int length);

}
