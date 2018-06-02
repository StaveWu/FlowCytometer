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
 * ��������˳�����£�
 * 		0------3
 * 		|	   |
 * 		2------1
 * @author wteng
 *
 */
public class RectangleGate extends Gate {
	
	/**
	 * ��������
	 */
	private Rectangle rect;
	
	private static int count = 0;
	
	public RectangleGate() {
		name = getGateType() + count;
		count++;
	}

	@Override
	public List<Integer> getGatedIndex(double[] x, double[] y, Point origin) {
		//����ӳ�䵽����ϵ��
		if(rect == null || x == null || x.length == 0
				|| y == null || y.length == 0 || origin == null) {
			return null;
		}
		Point start = rect.getLocation();
		Point end = new Point(rect.x + rect.width, rect.y + rect.height);
		int up = origin.y - start.y;		//�Ͻ�
		int down = origin.y - end.y;		//�½�
		int left = rect.x - origin.x;		//���
		int right = end.x - origin.x;		//�ҽ�
		
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
				//��������
				rect = getRect(vertices.get(0).getPoint(), vertices.get(1).getPoint());
				//��ȫ����
				vertices.add(new Vertice(new Point(rect.x, rect.y + rect.height)));
				vertices.add(new Vertice(new Point(rect.x + rect.width, rect.y)));
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//�����
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
		//�������ж���ı�����
		if(vertices.size() <= 0) {
			return;
		}
		//Ѱ�ҶԽǵ�
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
			throw new RuntimeException("rebuildGate������ȷ�Ͼ�������ֵ�Ƿ���ȷ");
		}
		Point diagonal = vertices.get(diagId).getPoint();
		rect = getRect(ver, diagonal);
	}

	@Override
	public String getGateType() {
		return "R";
	}
}
