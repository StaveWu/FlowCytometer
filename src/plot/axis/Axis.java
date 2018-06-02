package plot.axis;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Axis {
	
	/**
	 * ��̶ȵĳ�
	 */
	public static final int BIG_SCALE_LENGTH = 7;
	
	/**
	 * С�̶ȵĳ�
	 */
	public static final int SMALL_SCALE_LENGTH = 4;
	
	/**
	 * �̶ȵ������ʽ
	 */
	public static final Font VALUE_FONT = new Font("Calibri", Font.PLAIN, 12);
	
	/**
	 * �����Ƶ������ʽ
	 */
	public static final Font NAME_FONT = new Font("Calibri", Font.PLAIN, 14);
	
	/**
	 * �̶�ֵ��������ߵ�ƫ����
	 */
	public static final int VALUE_OFFSET = 10;
	
	/**
	 * ��������������ߵ�ƫ����
	 */
	public static final int NAME_OFFSET = 26;
	
	PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	private String name;
	private double minValue;
	private double maxValue;
	
	IAxisTypeStrategy typeStrategy;
	IAxisAlgebraStrategy algebraStrategy;
	IDimensionStrategy dimensionStrategy;
	
	public Axis(String name, 
			double minValue, 
			double maxValue, 
			IAxisAlgebraStrategy algebraStrategy,
			IAxisTypeStrategy typeStrategy,
			IDimensionStrategy dimensionStrategy) {
		this.name = name;
		this.maxValue = maxValue;
		this.minValue = minValue;
		this.dimensionStrategy = dimensionStrategy;
		this.algebraStrategy = algebraStrategy;
		this.typeStrategy = typeStrategy;
	}
	public Axis() {
		this("", 0, 16000, AxisAlgebra.LINEAR, AxisType.X, AxisDimension.X2D);
	}
	
	public void paint(Graphics g, Point start, int pixLen) {
		if(start == null || pixLen == 0) {
			return;
		}
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//�����
		
		/*
		 * ��任
		 */
		dimensionStrategy.tranform(g2d);
		
		/*
		 * ��ȡ�̶�ֵ��
		 * ��Ӧ�����ص��
		 */
		List<Double> bigScaleValues;
		List<Double> smallScaleValues;
		List<Integer> bigScaleDistance;
		List<Integer> smallScaleDistance;
		
		bigScaleValues = algebraStrategy.calibrateBigScale(minValue, maxValue);
		smallScaleValues = algebraStrategy.calibrateSmallScale(bigScaleValues);
		bigScaleDistance = algebraStrategy.pixelateBigScale(bigScaleValues, pixLen);
		smallScaleDistance = algebraStrategy.pixelateSmallScale(smallScaleValues, minValue,
				maxValue, pixLen);
		
		/*
		 * ����̶�ֵ�ַ����Ŀ��
		 */
		FontMetrics fm = g2d.getFontMetrics(VALUE_FONT);

		List<Integer> valueWidth = new ArrayList<Integer>();
		int valueHeight = 0;
		for(double bsv : bigScaleValues) {
			int vw = fm.stringWidth(bsv + "");
			valueWidth.add(vw);
		}
		valueHeight = fm.getAscent() - fm.getDescent();
		
		/*
		 * ���������ƵĿ��
		 */
		fm = g2d.getFontMetrics(NAME_FONT);
		
		int nameWidth = 0;
		int nameHeight = 0;
		nameWidth = fm.stringWidth(name);
		nameHeight = fm.getAscent() - fm.getDescent();
		
		/*
		 * ��ȡ��С�̶ȵ�ԭʼ�㼯��
		 * �̶�ֵ��λ���Լ������Ƶ�λ��
		 */
		Map<Point, Point> bigScaleSrcLocations;
		Map<Point, Point> smallScaleSrcLocations;
		Point nameLocation;
		List<Point> bigValueSrcLocations;
		
		bigScaleSrcLocations = typeStrategy.calculateScaleSrcLocations(
				bigScaleDistance, 
				start, 
				BIG_SCALE_LENGTH
				);
		if(bigScaleSrcLocations != null && bigScaleSrcLocations.size() != bigScaleDistance.size()) {
			throw new RuntimeException("�̶Ȼ��ִ���bigScaleSrcLocations����Ϊ��" + bigScaleSrcLocations.size()
					 + "��bigScaleDistance����Ϊ��" + bigScaleDistance.size());
		}
		smallScaleSrcLocations = typeStrategy.calculateScaleSrcLocations(
				smallScaleDistance, 
				start, 
				SMALL_SCALE_LENGTH
				);
		if(smallScaleSrcLocations != null && smallScaleSrcLocations.size() != smallScaleDistance.size()) {
			throw new RuntimeException("�̶Ȼ��ִ���smallScaleSrcLocations����Ϊ��" + smallScaleSrcLocations.size()
		 + "��smallScaleDistance����Ϊ��" + smallScaleDistance.size());
		}
		bigValueSrcLocations = typeStrategy.calculateBigValueSrcLocations(
				valueWidth, 
				valueHeight, 
				bigScaleSrcLocations.keySet()
				);
		nameLocation = typeStrategy.calculateNameLocation(nameWidth, nameHeight, start, pixLen);
		
		/*
		 * ����������õĽ������
		 */
		g2d.setColor(Color.BLACK);
		drawScale(g2d, bigScaleSrcLocations);	//����̶�
		drawScale(g2d, smallScaleSrcLocations);	//��С�̶�
		
		g2d.setFont(NAME_FONT);
		g2d.drawString(name, nameLocation.x, nameLocation.y);	//������
		
		g2d.setFont(VALUE_FONT);
		
		//����β�̶�ֵ
		g2d.drawString(
				bigScaleValues.get(0) + "", 
				bigValueSrcLocations.get(0).x, 
				bigValueSrcLocations.get(0).y
				);
		g2d.drawString(
				bigScaleValues.get(bigScaleValues.size() - 1) + "", 
				bigValueSrcLocations.get(bigValueSrcLocations.size() - 1).x, 
				bigValueSrcLocations.get(bigValueSrcLocations.size() - 1).y
				);
		
		//�����ص����м�̶�ֵ
		for (int i = 1; i < bigScaleValues.size() - 1; i++)
		{
			Point p = bigValueSrcLocations.get(i);
			Point pBefore = bigValueSrcLocations.get(i - 1);
			Point pAfter = bigValueSrcLocations.get(i + 1);
			String s = bigScaleValues.get(i) + "";
			
			boolean overLapBefore = p.x - valueWidth.get(i) / 2 < pBefore.x + valueWidth.get(i - 1) / 2;
			boolean overLapAfter = p.x + valueWidth.get(i) / 2 > pAfter.x - valueWidth.get(i - 1) / 2;
			if (!overLapAfter && !overLapBefore) {
				g2d.drawString(s, p.x, p.y);
			}
		}
		
		//��ȡ�任ǰ��ĩ������
		Point end = new Point();
		Set<Point> keys = bigScaleSrcLocations.keySet();
		Iterator<Point> iter = keys.iterator();
		int i = 0;
		while (iter.hasNext()) {
			Point p = iter.next();
			if (i == keys.size() - 1) {
				end = p;
			}
			i++;
		}
		
		/*
		 * ������
		 */
		g2d.drawLine(start.x, start.y, end.x, end.y);
	}
	
	private void drawScale(Graphics2D g2d, Map<Point, Point> scaleLocations) {
		//���̶�
		if(scaleLocations == null) {
			return;
		}
		Set<Point> keys = scaleLocations.keySet();
		Iterator<Point>	iter = keys.iterator();
		while (iter.hasNext()) {
			Point k = iter.next();
			Point v = scaleLocations.get(k);
			g2d.drawLine(k.x, k.y, v.x, v.y);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		String old = this.name;
		this.name = name;
		pcs.firePropertyChange("name", old, name);
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		double old = this.minValue;
		this.minValue = minValue;
		pcs.firePropertyChange("minValue", old, minValue);
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		double old = this.maxValue;
		this.maxValue = maxValue;
		pcs.firePropertyChange("maxValue", old, maxValue);
	}

	public IAxisTypeStrategy getTypeStrategy() {
		return typeStrategy;
	}

	public void setTypeStrategy(IAxisTypeStrategy typeStrategy) {
		IAxisTypeStrategy old = this.typeStrategy;
		this.typeStrategy = typeStrategy;
		pcs.firePropertyChange("typeStrategy", old, typeStrategy);
	}

	public IAxisAlgebraStrategy getAlgebraStrategy() {
		return algebraStrategy;
	}

	public void setAlgebraStrategy(IAxisAlgebraStrategy algebraStrategy) {
		IAxisAlgebraStrategy old = this.algebraStrategy;
		this.algebraStrategy = algebraStrategy;
		pcs.firePropertyChange("algebraStrategy", old, algebraStrategy);
	}

	public IDimensionStrategy getDimensionStrategy() {
		return dimensionStrategy;
	}

	public void setDimensionStrategy(IDimensionStrategy dimensionStrategy) {
		IDimensionStrategy old = this.dimensionStrategy;
		this.dimensionStrategy = dimensionStrategy;
		pcs.firePropertyChange("dimensionStrategy", old, dimensionStrategy);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}
	
	public void RemovePropertyChangeListener(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}
	
	/**
	 * ��ȡ���ĩ������
	 * @return
	 */
	public Point getEnd(Point start, int length) {
		return dimensionStrategy.getEnd(start, length);
	}

}
