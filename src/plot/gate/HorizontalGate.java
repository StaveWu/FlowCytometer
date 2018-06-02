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
 * ����˳���� --> ��
 * ��֧����άͼ
 * @author wteng
 *
 */
public class HorizontalGate extends Gate {
	
	/**
	 * ����
	 */
	private Rectangle rect;
	
	/**
	 * �߽�
	 */
	private Rectangle bounds;
	
	private static int count = 0;
	
	/**
	 * ���캯��
	 * @param bounds
	 */
	public HorizontalGate(Rectangle bounds) {
		this.bounds = bounds;
		name = getGateType() + count;
		count++;
	}
	
	private boolean checkBounds() {
		return bounds != null;
	}

	@Override
	public List<Integer> getGatedIndex(double[] x, double[] y, Point origin) {
		//������ӳ�䵽����ϵ��
		if(rect == null || x == null || x.length == 0
				|| y == null || y.length == 0 || origin == null) {
			return null;
		}
		Point start = rect.getLocation();
		Point end = new Point(rect.x + rect.width, rect.y + rect.height);
		int upl = origin.y - start.y;	//�Ͻ�
		int lol = origin.y - end.y;		//�½�
		//ɸѡ
		List<Integer> res = new ArrayList<Integer>();
		for (int i = 0; i < y.length; i++) {
			if(y[i] >= lol && y[i] <= upl) {
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
				//��������
				rect = getRect(vertices.get(0).getPoint(), vertices.get(1).getPoint());
				meanVertices(rect);
			}
		}
	}
	
	private Rectangle getRect(Point p1, Point p2) {
		//���������ȡ��������
		int miny = Math.min(p1.y, p2.y);
		int maxy = Math.max(p1.y, p2.y);
		if (checkBounds()) {
			return new Rectangle((int)bounds.getX(), miny, (int)bounds.getWidth(), maxy - miny);
		}
		return null;
	}
	
	private void meanVertices(Rectangle rect) {
		//ȡ��ֵ
		vertices.set(0, new Vertice(new Point(rect.x + rect.width / 2, rect.y)));
		vertices.set(1, new Vertice(new Point(rect.x + rect.width / 2, rect.y + rect.height)));
	}

	@Override
	public void paint(Graphics g) {
		
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);//�����
		g2d.setColor(color);
		
		if (!checkBounds()) {
			return;
		}
		int left = (int)bounds.getX();
		int width = (int)bounds.getWidth();
		if(rect != null) {
			int miny = (int) rect.getY();
			int maxy = (int) (miny + rect.getHeight());
			g2d.drawLine(left, miny, left + width, miny);
			g2d.drawLine(left, maxy, left + width, maxy);
			g2d.drawLine(left + width / 2, miny, left + width / 2, maxy);
		}
		else {
			if(vertices.size() > 0 && runningPoint != null) {
				g2d.drawLine(left, vertices.get(0).getPoint().y, left + width, vertices.get(0).getPoint().y);
				g2d.drawLine(left, runningPoint.y, left + width, runningPoint.y);
				g2d.drawLine(left + width / 2,  vertices.get(0).getPoint().y, left + width / 2, runningPoint.y);
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
		Point newStart = new Point(start.x, start.y + deltaY);
		if (checkBounds()) {
			rect.setBounds(rect.x, newStart.y, (int)bounds.getWidth(), rect.height);
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
		return "H";
	}

}
