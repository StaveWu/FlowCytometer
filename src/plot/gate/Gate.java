package plot.gate;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Gate {
	
	/**
	 * С�����εĳߴ�
	 */
	protected static final int RSIZE = 6;
	
	/**
	 * ����
	 */
	protected String name;
	
	/**
	 * ��ɫ
	 */
	protected Color color = Color.BLUE;
	
	/**
	 * �Ƿ��ȡ�������־
	 */
	protected boolean isFocusable = false;
	
	/**
	 * ����
	 */
	protected List<Vertice> vertices = new ArrayList<>();
	
	/**
	 * ������
	 */
	protected Point runningPoint;
	
	/**
	 * �Ƿ�Ҫ���ض��������־
	 */
	private boolean hideVerticesRegion = false;
	
	/**
	 * �Ƿ��������Ƶı�־
	 */
	private boolean hideGateName = false;
	
	/**
	 * ��ȡgate����������
	 * @param x			��������x
	 * @param y			��������y
	 * @param origin	����ϵԭ��
	 * @return
	 */
	public abstract List<Integer> getGatedIndex(double[] x, double[] y, Point origin);
	
	/**
	 * ���ö���
	 * @param ver	����
	 */
	public abstract void addVertice(Vertice ver);
	
	/**
	 * ����gate
	 */
	public void paint(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//�����
		g2d.setColor(color);
		//����������
		if (!hideVerticesRegion && vertices.size() > 0) {
			for (Vertice ele : vertices) {
				ele.paintRegion(g2d);
			}
		}
		//������
		if (name != null && !name.equals("") && !hideGateName &&
				vertices.size() > 0 && getGateRegion() != null) {
			//�����ĵ�
			int meanx;
			int meany;
			int sumx = 0;
			int sumy = 0;
			Iterator<Vertice> iter = vertices.iterator();
			while (iter.hasNext()) {
				Point p = iter.next().getPoint();
				sumx += p.x;
				sumy += p.y;
			}
			meanx = sumx / vertices.size();
			meany = sumy / vertices.size();
			//���ı��ߴ�
			int namewidth = 0;
			int nameheight = 0;
			g2d.setFont(new Font("Calibri", Font.PLAIN, 14));
			FontMetrics fm = g2d.getFontMetrics();
			namewidth = fm.stringWidth(name);
			nameheight = fm.getAscent() - fm.getDescent();
			//���������
			int basePointx = meanx - namewidth / 2;
			int basePointy = meany + nameheight / 2;
			g2d.drawString(name, basePointx, basePointy);
		}
	}
	
	/**
	 * �Ƿ�������
	 * @return
	 */
	public abstract boolean isGenerated();
	
	/**
	 * ƽ��
	 * @param deltaX	����x
	 * @param deltaY	����y
	 */
	public abstract void translate(int deltaX, int deltaY);
	
	/**
	 * ��ȡ����������
	 */
	public abstract Shape getGateRegion();
	
	/**
	 * ��ƽ����֮����¶��㣬ʹ�����������
	 */
	public abstract void updateVertice();
	
	/**
	 * ����gate
	 * @param verid	���ı�Ķ���id
	 */
	public abstract void rebuildGate(int verid);

	/**
	 * ��ȡ����
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * ��������
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * ��ȡ�Ƿ񱻾۽�
	 * @return
	 */
	public boolean isFocusable() {
		return isFocusable;
	}
	
	/**
	 * �����Ƿ񱻾۽�
	 * @param isFocusable
	 */
	public void setFocusable(boolean isFocusable) {
		this.isFocusable = isFocusable;
	}
	
	/**
	 * ��ȡ����
	 * @return
	 */
	public List<Vertice> getVertices() {
		return vertices;
	}
	
	/**
	 * ��ȡ������
	 * @return
	 */
	public Point getRunningPoint() {
		return runningPoint;
	}
	
	/**
	 * ���ô�����
	 * @param runningPoint
	 */
	public void setRunningPoint(Point runningPoint) {
		this.runningPoint = runningPoint;
	}
	
	/**
	 * ��ȡ���������Ƿ�����
	 * @return
	 */
	public boolean isHideVerticesRegion() {
		return hideVerticesRegion;
	}
	
	/**
	 * ���ö�����������
	 * @param hideVerticesRegion
	 */
	public void setHideVerticesRegion(boolean hideVerticesRegion) {
		this.hideVerticesRegion = hideVerticesRegion;
	}
	
	/**
	 * ��ȡ�����Ƿ�����
	 * @return
	 */
	public boolean isHideGateName() {
		return hideGateName;
	}
	
	/**
	 * ���������Ƿ�����
	 * @param hideGateName
	 */
	public void setHideGateName(boolean hideGateName) {
		this.hideGateName = hideGateName;
	}
	
	/**
	 * ��ȡ��ɫ
	 * @return
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * ������ɫ
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	public abstract String getGateType();
	
	/**
	 * �Ƿ���Ȧ��������
	 * @param p
	 * @return
	 */
	public boolean isInGateRegion(Point p) {
		return getGateRegion() != null && getGateRegion().contains(p);
	}
	
	/**
	 * �Ƿ��ڶ���������
	 * @param p
	 * @return
	 */
	public boolean isInVerticesRegion(Point p) {
		return verticesRegionOfWhichContains(p) != -1;
	}
	
	/**
	 * ��ȡ���������Ķ�����������
	 * @param p
	 * @return
	 */
	public int verticesRegionOfWhichContains(Point p) {
		int i = 0;
		for (Vertice ele : vertices) {
			if (ele.contains(p)) {
				return i;
			}
			i++;
		}
		return -1;
	}

}
