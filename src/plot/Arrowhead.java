package plot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

public class Arrowhead {
	
	private Point start;
	private Point end;
	
	public Arrowhead() {
		this(null, null);
	}
	
	public Arrowhead(Point start, Point end) {
		this.start = start;
		this.end = end;
	}
	
	public void paint(Graphics g) {
		// 画箭头的方法
		if(start == null || end == null) {
			return;
		}
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(Color.DARK_GRAY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿
		
		//画箭身
		g2d.drawLine(start.x, start.y, end.x, end.y);
		//画箭头
		int dx = end.x - start.x;
		int dy = end.y - start.y;
		double rate = 0.12;
		Point tp = new Point(-(int)(rate * (double)dx), -(int)(rate * (double)dy));
		
		Graphics2D g2d1 = (Graphics2D) g2d.create();
		g2d1.translate(end.x, end.y);
		g2d1.rotate(Math.PI / 9);
		g2d1.drawLine(tp.x, tp.y, 0, 0);
		
		Graphics2D g2d2 = (Graphics2D) g2d.create();
		g2d2.translate(end.x, end.y);
		g2d2.rotate(-Math.PI / 9);
		g2d2.drawLine(tp.x, tp.y, 0, 0);
	}

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}
	
}
