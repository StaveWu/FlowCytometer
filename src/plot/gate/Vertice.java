package plot.gate;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Vertice {
	
	public static final int RSIZE = 5;
	
	private Rectangle rect;
	private Point point;
	
	public Vertice(Point p) {
		this.point = p;
		rect = new Rectangle(p.x - RSIZE / 2, p.y - RSIZE / 2, RSIZE, RSIZE);
	}
	
	public Point getPoint() {
		return point;
	}
	
	public boolean contains(Point p) {
		return rect.contains(p);
	}
	
	public void paintRegion(Graphics2D g2d) {
		g2d.draw(rect);
	}

}
