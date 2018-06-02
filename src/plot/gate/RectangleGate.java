package plot.gate;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

/**
 * 顶点排列顺序如下：
 * 		0------3
 * 		|	   |
 * 		2------1
 * @author wteng
 *
 */
public class RectangleGate extends Gate {
	
	/**
	 * 矩形区域
	 */
	private Rectangle rect;
	
	private static int count = 0;
	
	public RectangleGate() {
		name = getGateType() + count;
		count++;
	}

	@Override
	public List<Integer> getGatedIndex(double[] x, double[] y, Point origin) {
		//区域映射到坐标系中
		if(rect == null || x == null || x.length == 0
				|| y == null || y.length == 0 || origin == null) {
			return null;
		}
		Point start = rect.getLocation();
		Point end = new Point(rect.x + rect.width, rect.y + rect.height);
		int up = origin.y - start.y;		//上界
		int down = origin.y - end.y;		//下界
		int left = rect.x - origin.x;		//左界
		int right = end.x - origin.x;		//右界
		
		List<Integer> gi = new ArrayList<Integer>();
		for (int i = 0; i < x.length; i++) {
			boolean isInnerX = x[i] >= left && x[i] <= right;
			boolean isInnerY = y[i] >= down && y[i] <= up;
			if(isInnerX && isInnerY) {
				gi.add(i);
			}
		}
		
		return gi;
	}

	@Override
	public void addVertice(Vertice ver) {
		if(vertices.size() < 2) {
			vertices.add(ver);
			if(vertices.size() == 2) {
				//创建矩形
				rect = getRect(vertices.get(0).getPoint(), vertices.get(1).getPoint());
				//补全顶点
				vertices.add(new Vertice(new Point(rect.x, rect.y + rect.height)));
				vertices.add(new Vertice(new Point(rect.x + rect.width, rect.y)));
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿
		g2d.setColor(color);
		if(rect != null) {
			g2d.draw(rect);
		}
		else {
			if(vertices.size() > 0 && runningPoint != null) {
				g2d.draw(getRect(vertices.get(0).getPoint(), runningPoint));
			}
		}
	}
	
	private Rectangle getRect(Point start, Point end) {
		int x = Math.min(start.x, end.x);
		int y = Math.min(start.y, end.y);
		int width = Math.abs(start.x - end.x);
		int height = Math.abs(start.y - end.y);
		return new Rectangle(x, y, width, height);
	}

	@Override
	public boolean isGenerated() {
		return rect == null ? false : true;
	}

	@Override
	public void translate(int deltaX, int deltaY) {
		Point start = vertices.get(0).getPoint();
		Point end = vertices.get(1).getPoint();
		int oldx = Math.min(start.x, end.x);
		int oldy = Math.min(start.y, end.y);
		
		rect.setLocation(oldx + deltaX, oldy + deltaY);
	}

	@Override
	public Shape getGateRegion() {
		return rect;
	}
	
	@Override
	public void updateVertice() {
		if(rect == null) {
			return;
		}
		vertices.set(0, new Vertice(rect.getLocation()));
		vertices.set(1, new Vertice(new Point(rect.x + rect.width, rect.y + rect.height)));
		vertices.set(2, new Vertice(new Point(rect.x, rect.y + rect.height)));
		vertices.set(3, new Vertice(new Point(rect.x + rect.width, rect.y)));
	}

	@Override
	public void rebuildGate(int verid) {
		//根据现有顶点改变区域
		if(vertices.size() <= 0) {
			return;
		}
		//寻找对角点
		Point ver = vertices.get(verid).getPoint();
		int diagId;
		switch(verid) {
		case 0: 
			diagId = 1;
			break;
		case 1: 
			diagId = 0;
			break;
		case 2: 
			diagId = 3;
			break;
		case 3: 
			diagId = 2;
			break;
		default:
			throw new RuntimeException("rebuildGate错误！请确认矩形索引值是否正确");
		}
		Point diagonal = vertices.get(diagId).getPoint();
		rect = getRect(ver, diagonal);
	}

	@Override
	public String getGateType() {
		return "R";
	}
}
