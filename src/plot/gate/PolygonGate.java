package plot.gate;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

public class PolygonGate extends Gate {
	
	/**
	 * ���������
	 */
	private Polygon po;
	
	private static int count = 0;
	
	public PolygonGate() {
		name = getGateType() + count;
		count++;
	}

	@Override
	public List<Integer> getGatedIndex(double[] x, double[] y, Point origin) {
		//����ζ���ӳ�䵽ͼ����ϵ��
		if(po == null || x == null || x.length == 0
				|| y == null || y.length == 0 || origin == null) {
			return null;
		}
		int[] xpoints = po.xpoints;
		int[] ypoints = po.ypoints;
		int[] txs = new int[xpoints.length];
		int[] tys = new int[ypoints.length];
		for (int i = 0; i < ypoints.length; i++) {
			txs[i] = xpoints[i] - origin.x;
			tys[i] = origin.y - ypoints[i];
		}
		Polygon tpo = new Polygon(txs, tys, txs.length);
		//ȷ����Ȧס������
		List<Integer> gi = new ArrayList<Integer>();
		for (int i = 0; i < x.length; i++) {
			if(tpo.contains(x[i], y[i])) {
				gi.add(i);
			}
		}
		
		return gi;
	}

	@Override
	public void addVertice(Vertice ver) {
		if (vertices.size() <= 0 
				|| !vertices.get(0).contains(ver.getPoint())) {
			vertices.add(ver);
		} else if (vertices.get(0).contains(ver.getPoint())) {
			// ���������
			int[] xpoints = new int[vertices.size()];
			int[] ypoints = new int[vertices.size()];
			for (int i = 0; i < xpoints.length; i++) {
				xpoints[i] = vertices.get(i).getPoint().x;
				ypoints[i] = vertices.get(i).getPoint().y;
			}
			po = new Polygon(xpoints, ypoints, xpoints.length);
		}
	}

	@Override
	public void paint(Graphics g) {
		
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//�����
		g2d.setColor(color);
		
		//���po���ڣ�ֱ�ӻ������
		if(po != null) {
			g2d.draw(po);
		}
		else {
			if(vertices.size() > 0 && runningPoint != null) {
				//����ȷ���㻭��
				Point last = vertices.get(0).getPoint();
				for (int i = 1; i < vertices.size(); i++) {
					Point cur = vertices.get(i).getPoint();
					g2d.drawLine(cur.x, cur.y, last.x, last.y);
					last = cur;
				}
				//�㼯�����һ�����Ӵ�����
				g2d.drawLine(last.x, last.y, runningPoint.x, runningPoint.y);
			}
		}
	}

	@Override
	public boolean isGenerated() {
		return po == null ? false : true;
	}

	@Override
	public void translate(int deltaX, int deltaY) {
		if(po == null) {
			return;
		}
		int[] oldxpoints = new int[vertices.size()];
		int[] oldypoints = new int[vertices.size()];
		for (int i = 0; i < oldypoints.length; i++) {
			oldxpoints[i] = vertices.get(i).getPoint().x;
			oldypoints[i] = vertices.get(i).getPoint().y;
		}
		int[] newxpoints = new int[oldxpoints.length];
		int[] newypoints = new int[oldypoints.length];
		for (int i = 0; i < oldypoints.length; i++) {
			newxpoints[i] = oldxpoints[i] + deltaX;
			newypoints[i] = oldypoints[i] + deltaY;
		}
		
		po = new Polygon(newxpoints, newypoints, newypoints.length);
		
	}

	@Override
	public Shape getGateRegion() {
		return po;
	}
	
	@Override
	public void updateVertice() {
		if(po == null) {
			return;
		}
		vertices.clear();
		int[] xpoints = po.xpoints;
		int[] ypoints = po.ypoints;
		for (int i = 0; i < ypoints.length; i++) {
			vertices.add(new Vertice(new Point(xpoints[i], ypoints[i])));
		}
	}

	@Override
	public void rebuildGate(int verid) {
		//�������ж���ı�����
		if(vertices.size() <= 0) {
			return;
		}
		int[] xpoints = po.xpoints;
		int[] ypoints = po.ypoints;
		for (int i = 0; i < ypoints.length; i++) {
			if(i == verid) {
				xpoints[i] = vertices.get(i).getPoint().x;
				ypoints[i] = vertices.get(i).getPoint().y;
			}
		}
		
		po = new Polygon(xpoints, ypoints, vertices.size());
	}

	@Override
	public String getGateType() {
		return "P";
	}
	
}
