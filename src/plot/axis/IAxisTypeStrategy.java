package plot.axis;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IAxisTypeStrategy {
	
	/**
	 * ȷ���̶ȣ�������̶Ⱥ�С�̶ȣ���ԭʼ�㼯����ͳһ��ˮƽ������㣩
	 * @param distance		���
	 * @param origin		������
	 * @param scaleLength	�̶ȳ�
	 * @return				�̶ȵ�ԭʼ�㼯
	 */
	Map<Point, Point> calculateScaleSrcLocations(List<Integer> distance, Point origin, int scaleLength);
	
	/**
	 * ȷ����̶�ֵ��λ��
	 * @param valueWidth		�̶�ֵ�Ŀ�
	 * @param valueHeight		�̶�ֵ�ĸ�
	 * @param bsSrcLocations	�̶�ֵ����������
	 * @return					��Ҫ���̶�ֵ��λ��
	 */
	List<Point> calculateBigValueSrcLocations(List<Integer> valueWidth, int valueHeight, Set<Point> bsSrcLocations);
	
	
	/**
	 * ȷ�������Ƶ�λ��
	 * @param nameWidth		�����ƵĿ�
	 * @param nameHeight	�����Ƶĸ�
	 * @param origin		������
	 * @param length		�᳤
	 * @return				��Ҫ�������Ƶ�λ��
	 */
	Point calculateNameLocation(int nameWidth, int nameHeight, Point origin, int length);

}
