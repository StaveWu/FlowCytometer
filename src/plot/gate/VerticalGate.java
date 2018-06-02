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
 * 顶点顺序：左 --> 右
 * @author wteng
 *
 */

public class VerticalGate extends Gate {
	
	/**
	 * 矩形区域
	 */
	private Rectangle rect;
	
	/**
	 * 边界
	 */
	private Rectangle bounds;
	
	private static int count = 0;
	
	/**
	 * 构造函数
	 * @param bounds
	 */
	public VerticalGate(Rectangle bounds) {
		this.bounds = bounds;
		name = getGateType() + count;
		count++;
	}
	
	private boolean checkBounds() {
		return bounds != null;
	}

	@Override
	public List<Integer> getGatedIndex(double[] x, double[] y, Point origin) {
		//将区域映射到坐标系中
		if(rect == null || x == null || x.length == 0
				|| y == null || y.length == 0 || origin == null) {
			return null;
		}
		Point start = rect.getLocation();
		Point end = new Point(rect.x + rect.width, rect.y + rect.height);
		int upl = end.x - origin.x;		//右界
		int lol = start.x - origin.x;	//左界
		//筛选
		List<Integer> res = new ArrayList<Integer>();
		for (int i = 0; i < x.length; i++) {
			if(x[i] >= lol && x[i] <= upl) {
				res.add(i);
			}
		}
		return res;
	}

	@Override
	public void addVertice(Vertice ver) {
		if(vertices.size() < 2) {
			vertices.add(ver);
			if(vertices.size() == 2) {
				//创建区域
				rect = getRect(vertices.get(0).getPoint(), vertices.get(1).getPoint());
				meanVertices(rect);
			}
		}
	}
	
	private Rectangle getRect(Point p1, Point p2) {
		//根据两点确定矩形区域
		if (!checkBounds()) {
			return null;
		}
		int minx = Math.min(p1.x, p2.x);
		int maxx = Math.max(p1.x, p2.x);
		return new Rectangle(minx, (int)bounds.getY(), maxx - minx, (int)bounds.getHeight());
	}
	
	private void meanVertices(Rectangle rect) {
		//取中值
		vertices.set(0, new Vertice(new Point(rect.x, rect.y + rect.height / 2)));
		vertices.set(1, new Vertice(new Point(rect.x + rect.width, rect.y + rect.height / 2)));
	}

	@Override
	public void paint(Graphics g) {
		
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿
		g2d.setColor(color);
		
		if (!checkBounds()) {
			return;
		}
		int up = (int)bounds.getY();
		int height = (int)bounds.getHeight();
		if(rect != null) {
			int minx = (int) rect.getX();
			int maxx = (int) (minx + rect.getWidth());
			g2d.drawLine(minx, up, minx, up + height);
			g2d.drawLine(maxx, up, maxx, up + height);
			g2d.drawLine(minx, up + height / 2, maxx, up + height / 2);
		}
		else {
			if(vertices.size() > 0 && runningPoint != null) {
				g2d.drawLine(vertices.get(0).getPoint().x, up, vertices.get(0).getPoint().x, up + height);
				g2d.drawLine(runningPoint.x, up, runningPoint.x, up + height);
				g2d.drawLine(vertices.get(0).getPoint().x, up + height / 2, runningPoint.x, up + height / 2);
			}
		}
	}

	@Override
	public boolean isGenerated() {
		return rect == null ? false : true;
	}

	@Override
	public void translate(int deltaX, int deltaY) {
		// TODO Auto-generated method stub
		Point start = vertices.get(0).getPoint();
		Point newStart = new Point(start.x + deltaX, start.y);
		if (checkBounds()) {
			rect.setBounds(newStart.x, rect.y,  rect.width, (int)bounds.getHeight());
		}
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
		meanVertices(rect);
	}

	@Override
	public void rebuildGate(int verid) {
		// TODO Auto-generated method stub
		if(rect == null) {
			return;
		}
		int otherid = verid == 0 ? 1 : 0;
		rect = getRect(vertices.get(verid).getPoint(), vertices.get(otherid).getPoint());
	}

	@Override
	public String getGateType() {
		// TODO Auto-generated method stub
		return "V";
	}

}
