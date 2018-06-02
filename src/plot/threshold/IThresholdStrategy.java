package plot.threshold;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * ���򻮷ֲ��ԣ�
 * 2D
 * 3D
 * 
 * @author wteng
 *
 */
public interface IThresholdStrategy {
	
	/**
	 * ��ȡX����
	 * @param r	ͼ����������
	 * @return
	 */
	Rectangle getAxisRegionX(Rectangle r);
	
	/**
	 * ��ȡY����
	 * @param r ͼ����������
	 * @return
	 */
	Rectangle getAxisRegionY(Rectangle r);
	
	/**
	 * ��ȡZ����
	 * @param r ͼ����������
	 * @return
	 */
	Rectangle getAxisRegionZ(Rectangle r);
	
	/**
	 * ��ȡ������
	 * @param r ͼ����������
	 * @return
	 */
	Rectangle getDataRegion(Rectangle r);
	
	/**
	 * ��ȡԭ������
	 * @param r ͼ����������
	 * @return
	 */
	Point getOrigin(Rectangle r);
	
	/**
	 * ��ȡ��ĳ�������
	 * @param r ͼ����������
	 * @return
	 */
	int[] getAxisLengths(Rectangle r);

}
