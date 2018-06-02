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
	 * 小正方形的尺寸
	 */
	protected static final int RSIZE = 6;
	
	/**
	 * 名称
	 */
	protected String name;
	
	/**
	 * 颜色
	 */
	protected Color color = Color.BLUE;
	
	/**
	 * 是否获取到焦点标志
	 */
	protected boolean isFocusable = false;
	
	/**
	 * 顶点
	 */
	protected List<Vertice> vertices = new ArrayList<>();
	
	/**
	 * 待定点
	 */
	protected Point runningPoint;
	
	/**
	 * 是否要隐藏顶点区域标志
	 */
	private boolean hideVerticesRegion = false;
	
	/**
	 * 是否隐藏名称的标志
	 */
	private boolean hideGateName = false;
	
	/**
	 * 获取gate内数据索引
	 * @param x			像素坐标x
	 * @param y			像素坐标y
	 * @param origin	坐标系原点
	 * @return
	 */
	public abstract List<Integer> getGatedIndex(double[] x, double[] y, Point origin);
	
	/**
	 * 设置顶点
	 * @param ver	顶点
	 */
	public abstract void addVertice(Vertice ver);
	
	/**
	 * 画出gate
	 */
	public void paint(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿
		g2d.setColor(color);
		//画顶点区域
		if (!hideVerticesRegion && vertices.size() > 0) {
			for (Vertice ele : vertices) {
				ele.paintRegion(g2d);
			}
		}
		//画名称
		if (name != null && !name.equals("") && !hideGateName &&
				vertices.size() > 0 && getGateRegion() != null) {
			//求中心点
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
			//求文本尺寸
			int namewidth = 0;
			int nameheight = 0;
			g2d.setFont(new Font("Calibri", Font.PLAIN, 14));
			FontMetrics fm = g2d.getFontMetrics();
			namewidth = fm.stringWidth(name);
			nameheight = fm.getAscent() - fm.getDescent();
			//求基点坐标
			int basePointx = meanx - namewidth / 2;
			int basePointy = meany + nameheight / 2;
			g2d.drawString(name, basePointx, basePointy);
		}
	}
	
	/**
	 * 是否已生成
	 * @return
	 */
	public abstract boolean isGenerated();
	
	/**
	 * 平移
	 * @param deltaX	增量x
	 * @param deltaY	增量y
	 */
	public abstract void translate(int deltaX, int deltaY);
	
	/**
	 * 获取包含的区域
	 */
	public abstract Shape getGateRegion();
	
	/**
	 * 当平移完之后更新顶点，使顶点跟随区域
	 */
	public abstract void updateVertice();
	
	/**
	 * 重塑gate
	 * @param verid	被改变的顶点id
	 */
	public abstract void rebuildGate(int verid);

	/**
	 * 获取名称
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 设置名称
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 获取是否被聚焦
	 * @return
	 */
	public boolean isFocusable() {
		return isFocusable;
	}
	
	/**
	 * 设置是否被聚焦
	 * @param isFocusable
	 */
	public void setFocusable(boolean isFocusable) {
		this.isFocusable = isFocusable;
	}
	
	/**
	 * 获取顶点
	 * @return
	 */
	public List<Vertice> getVertices() {
		return vertices;
	}
	
	/**
	 * 获取待定点
	 * @return
	 */
	public Point getRunningPoint() {
		return runningPoint;
	}
	
	/**
	 * 设置待定点
	 * @param runningPoint
	 */
	public void setRunningPoint(Point runningPoint) {
		this.runningPoint = runningPoint;
	}
	
	/**
	 * 获取顶点区域是否隐藏
	 * @return
	 */
	public boolean isHideVerticesRegion() {
		return hideVerticesRegion;
	}
	
	/**
	 * 设置顶点区域隐藏
	 * @param hideVerticesRegion
	 */
	public void setHideVerticesRegion(boolean hideVerticesRegion) {
		this.hideVerticesRegion = hideVerticesRegion;
	}
	
	/**
	 * 获取名称是否隐藏
	 * @return
	 */
	public boolean isHideGateName() {
		return hideGateName;
	}
	
	/**
	 * 设置名称是否隐藏
	 * @param hideGateName
	 */
	public void setHideGateName(boolean hideGateName) {
		this.hideGateName = hideGateName;
	}
	
	/**
	 * 获取颜色
	 * @return
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * 设置颜色
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	public abstract String getGateType();
	
	/**
	 * 是否在圈门区域里
	 * @param p
	 * @return
	 */
	public boolean isInGateRegion(Point p) {
		return getGateRegion() != null && getGateRegion().contains(p);
	}
	
	/**
	 * 是否在顶点区域里
	 * @param p
	 * @return
	 */
	public boolean isInVerticesRegion(Point p) {
		return verticesRegionOfWhichContains(p) != -1;
	}
	
	/**
	 * 获取包含这个点的顶点区域索引
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
