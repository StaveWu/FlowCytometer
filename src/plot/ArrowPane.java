package plot;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class ArrowPane extends PaneX {
	
	private Outlet outlet;
	private ArrowPane prev;
	private ArrowPane next;
	
	/**
	 * 只保存指向下一张图的箭头
	 */
	private Arrowhead arrow;
	protected ArrowPaneObserver observer;
	
	public ArrowPane() {
		outlet = new Outlet();
		
		OutletListener oll = new OutletListener();
		addMouseListener(oll);
		addMouseMotionListener(oll);
		
		ArrowHeadListener ahl = new ArrowHeadListener();
		addMouseListener(ahl);
		addMouseMotionListener(ahl);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (isFocusable()) {
			outlet.paint(g);
		}
	}
	
	public boolean isInOutlet(Point p) {
		return outlet.contains(p);
	}
	
	public abstract void transmit();
	
	/**
	 * 释放资源
	 */
	public void dispose() {
		deletePrev();
		deleteNext();
		Container parent = getParent();
		parent.remove(this);
		parent.repaint();
	}
	
	public void deletePrev() {
		if (prev != null) {
			prev.setNext(null);
			prev.setArrow(null);
			setPrev(null);
		}
	}
	
	public void deleteNext() {
		if (next != null) {
			next.setPrev(null);
			setNext(null);
		}
		setArrow(null);
	}
	
	public void repaintNext(ArrowPane p) {
		ArrowPane next = p.getNext();
		if (next != null) {
			next.repaint();
			repaintNext(next);
		}
	}

	
	/**
	 * 输出口
	 * @author wteng
	 *
	 */
	class Outlet {
		/**
		 * 宽度
		 */
		private static final int SIZE = 20;
		private Rectangle delegate;
		private int thickness;
		
		public Outlet() {
			this(FOCUSBORDER_THICKNESS);
		}
		
		public Outlet(int thickness) {
			this.thickness = thickness;
			delegate = new Rectangle();
		}
		
		public void paint(Graphics g) {
	        int x = (ArrowPane.this.getWidth() - SIZE) / 2;
	        int y = ArrowPane.this.getHeight() - thickness;
	        int width = SIZE;
	        int height = thickness;
	        delegate.setBounds(x, y, width, height);
	        
	        Graphics2D g2d = (Graphics2D) g.create();
	        g2d.setColor(Color.RED);
	        g2d.fill(delegate);
		}
		
		public boolean contains(Point p) {
			return delegate.contains(p);
		}
	}

	public ArrowPane getPrev() {
		return prev;
	}

	public void setPrev(ArrowPane prev) {
		this.prev = prev;
	}

	public ArrowPane getNext() {
		return next;
	}

	public void setNext(ArrowPane next) {
		this.next = next;
	}

	public Arrowhead getArrow() {
		return arrow;
	}

	public void setArrow(Arrowhead arrow) {
		this.arrow = arrow;
	}
	
	public void removeNextAndArrow() {
		setNext(null);
		setArrow(null);
	}
	
	public void addObserver(ArrowPaneObserver observer) {
		this.observer = observer;
	}
	
	public void notifyRepaintArrows() {
		if (observer != null) {
			observer.repaintArrows();
		}
	}
	
	public void notifyCorrectArrowhead(Arrowhead arrow, ArrowPane sender, ArrowPane receiver) {
		if (observer != null) {
			observer.correctArrowhead(arrow, sender, receiver);
		}
	}
	
	public void notifyEndShoot(ArrowPane sender, Point end) {
		if (observer != null) {
			observer.endShoot(sender, end);
		}
	}
	
	/**
	 * 输出口监听器，当被点击时拖出箭头
	 * @author wteng
	 *
	 */
	private class OutletListener extends MouseAdapter {
		
		private boolean enabled = false; //允许拉出箭头标志
		
		@Override
		public void mousePressed(MouseEvent e) {
			if(outlet.contains(e.getPoint())) {
				enabled = true;
				// 转为父组件坐标
				int x = e.getPoint().x + getLocation().x;
				int y = e.getPoint().y + getLocation().y;
				// 清空next
				deleteNext();
				// 设置箭头
				Arrowhead arrowhead = new Arrowhead();
				arrowhead.setStart(new Point(x, y));
				ArrowPane.this.setArrow(arrowhead);
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if(enabled && arrow != null) {
				int x = e.getPoint().x + getLocation().x;
				int y = e.getPoint().y + getLocation().y;
				arrow.setEnd(new Point(x, y));
				notifyRepaintArrows();
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if(enabled) {
				enabled = false;
				int x = e.getPoint().x + getLocation().x;
				int y = e.getPoint().y + getLocation().y;
				notifyEndShoot(ArrowPane.this, new Point(x, y));
			}
			
		}
	}
	
	/**
	 * 箭头监听器，当图表移动或者缩放的时候更新箭头位置
	 * @author wteng
	 *
	 */
	private class ArrowHeadListener extends MouseAdapter {
		
		private boolean isDraggable = false;
		private boolean isResizable = false;
		@Override
		public void mousePressed(MouseEvent e) {
			isDraggable = e.getY() < FOCUSBORDER_THICKNESS ||
					e.getX() < FOCUSBORDER_THICKNESS;
			isResizable = ArrowPane.super.isInResizableRegion(e.getPoint());
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if(isDraggable || isResizable) {
				notifyCorrectArrowhead(arrow, ArrowPane.this, next);
				if (prev != null) {
					notifyCorrectArrowhead(prev.getArrow(), prev, ArrowPane.this);
				}
				
			}
		}
	}

}
