package plot;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import utils.SwingUtils;

/**
 * �ṩһ����ͷ��������
 * @author wteng
 *
 */
@SuppressWarnings("serial")
public class ArrowPaneContainer extends JPanel implements ArrowPaneObserver {
	
	protected List<ArrowPane> panes = new ArrayList<>();
	
	public ArrowPaneContainer() {
		setLayout(null);
		setBackground(Color.WHITE);
	}
	
	@Override
	public Component add(Component comp) {
		if (comp != null && comp instanceof ArrowPane) {
			ArrowPane pane = (ArrowPane) comp;
			pane.addObserver(this);
			panes.add(pane);
			
		}
		Component res = super.add(comp);
		focusLastPane();
		return res;
	}
	
	private void focusLastPane() {
		if (panes.size() <= 0) {
			return;
		}
		panes.get(panes.size() - 1).setFocusable(true);
	}
	
	@Override
	public void remove(Component comp) {
		if (comp instanceof ArrowPane && panes.contains(comp)) {
			panes.remove(comp);
		}
		super.remove(comp);
		focusLastPane();
	}
	
	@Override
	public void removeAll() {
		panes.clear();
		super.removeAll();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Iterator<ArrowPane> iter = panes.iterator();
		while (iter.hasNext()) {
			ArrowPane pane = (ArrowPane) iter.next();
			Arrowhead arrow = pane.getArrow();
			if (arrow == null) {
				arrow = tryGenerateArrow(pane, pane.getNext());
			}
			if (arrow != null) {
				arrow.paint(g);
			}
		}
	}
	
	private Arrowhead tryGenerateArrow(ArrowPane sender, ArrowPane receiver) {
		if (sender == null || receiver == null) {
			return null;
		}
		Arrowhead res = new Arrowhead();
		correctArrowhead(res, sender, receiver);
		return res;
	}

	public ArrowPane whichContains(Point p) {
		Iterator<ArrowPane> iter = panes.iterator();
		while(iter.hasNext()) {
			ArrowPane ap = iter.next();
			boolean containsx = p.x > ap.getX() && p.x < ap.getX() + ap.getWidth();
			boolean containsy = p.y > ap.getY() && p.y < ap.getY() + ap.getHeight();
			if(containsx && containsy) {
				return ap;
			}
		}
		return null;
	}

	@Override
	public void repaintArrows() {
		repaint();
	}

	@Override
	public void endShoot(ArrowPane sender, Point end) {
		ArrowPane receiver = whichContains(end);
		if (receiver == null || receiver == sender) {
			// ɾ����ͷ
			sender.deleteNext();
		} else {
			// ������ͷ
			correctArrowhead(sender.getArrow(), sender, receiver);
			bind(sender, receiver);
		}
		repaint();
		revalidate();
	}
	
	public void bind(ArrowPane sender, ArrowPane receiver) {
		// ���ܳ�Ϊ�ջ�
		if (willBeClosedLoop(sender, receiver)) {
			SwingUtils.showErrorDialog(this, "�Ƿ���������ֹ�γɱջ���");
			sender.deleteNext();
		} else {
			ArrowPane old = receiver.getPrev();
			if (old != null) {
				old.deleteNext();
			}
			receiver.setPrev(sender);
			sender.setNext(receiver);
			// ����������������һ��ͼ
			sender.transmit();
		}
	}
	
	/**
	 * �Ƿ���γɱջ�
	 * @param sender
	 * @param receiver
	 * @return
	 */
	public boolean willBeClosedLoop(ArrowPane sender, ArrowPane receiver) {
		return !getNoLinkedPane().contains(receiver) 
				&& !getNoLinkedPane().contains(sender);
	}
	
	public List<ArrowPane> getNoLinkedPane() {
		List<ArrowPane> res = new ArrayList<>();
		for (ArrowPane ap : panes) {
			if (ap.getPrev() == null && ap.getNext() == null) {
				res.add(ap);
			}
		}
		return res;
	}

	@Override
	public void correctArrowhead(Arrowhead arrow, ArrowPane sender, ArrowPane receiver) {
		if(arrow == null || sender == null || receiver == null) {
			return;
		}
		//ȡ����ͼ������
		Point srcCenter;
		Point dstCenter;
		srcCenter = new Point(sender.getX() + sender.getWidth() / 2, sender.getY() + sender.getHeight() / 2);
		dstCenter = new Point(receiver.getX() + receiver.getWidth() / 2, receiver.getY() + receiver.getHeight() / 2);
		
		Point bestStart = new Point();
		Point bestEnd = new Point();
		//����б��a�ͽؾ�b: y = ax + b
		if(dstCenter.x != srcCenter.x) {
			double a = (double)(dstCenter.y - srcCenter.y) / (double)(dstCenter.x - srcCenter.x);
			double b = srcCenter.y - a * srcCenter.x;
			//������ͼ�ϸ�ȡһ�����������ཻ�ı߽�
			if(a > -1 && a < 1) {
				if(dstCenter.x > srcCenter.x) {
					//srcȡ�ң�dstȡ��
					bestStart.x = sender.getX() + sender.getWidth();
					bestEnd.x = receiver.getX();
				}
				else {
					//srcȡ��dstȡ��
					bestStart.x = sender.getX();
					bestEnd.x = receiver.getX() + receiver.getHeight();
				}
				bestStart.y = (int) (a * bestStart.x + b);
				bestEnd.y = (int) (a * bestEnd.x + b);
			}
			else {
				if(dstCenter.y > srcCenter.y) {
					//srcȡ�£�dstȡ��
					bestStart.y = sender.getY() + sender.getHeight();
					bestEnd.y = receiver.getY();
				}
				else {
					//srcȡ�ϣ�dstȡ��
					bestStart.y = sender.getY();
					bestEnd.y = receiver.getY() + receiver.getHeight();
				}
				bestStart.x = (int) (((double)bestStart.y - b) / a);
				bestEnd.x = (int) (((double)bestEnd.y - b) / a);
			}
		}
		else {
			if(dstCenter.y > srcCenter.y) {
				//srcȡ�£�dstȡ��
				bestStart.y = sender.getY() + sender.getHeight();
				bestEnd.y = receiver.getY();
				
			}
			else {
				//srcȡ�ϣ�dstȡ��
				bestStart.y = sender.getY();
				bestEnd.y = receiver.getY() + receiver.getHeight();
			}
			bestStart.x = srcCenter.x;
			bestEnd.x = dstCenter.x;
		}
		arrow.setStart(bestStart);
		arrow.setEnd(bestEnd);
		repaint();
	}

}
