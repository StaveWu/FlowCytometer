package plot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * 一个由空白画布和可隐藏边框组成的面板，具有以下三个
 * 基本功能：
 * 		1.当面板获取到焦点的时候，面板会显示边框。注意：
 * 		一个容器内只会有一个面板显示边框；
 * 		2.当边框右下角被拖动时，面板大小将跟随鼠标位置
 * 		发生变化；
 * 		3.当边框左侧和上侧被拖动时，面板将跟随鼠标移动。
 * @author wteng
 *
 */
@SuppressWarnings("serial")
public class PaneX extends JPanel {
	
	protected static final int FOCUSBORDER_THICKNESS = 6;
	
	private FocusBorder focusBorder;
	private EmptyCanvas emptyCanvas;
	private TriggerCorner corner;
	
	/**
	 * 是否允许缩放
	 */
	private boolean isResizable;
	
	public PaneX() {
		focusBorder = new FocusBorder();
		corner = new TriggerCorner();
		emptyCanvas = new EmptyCanvas();
		//设置pane为透明的
		setOpaque(false);
		setResizable(true);
		
		addMouseListener(new FocusListener());
		
		DraggableListener dragLis = new DraggableListener();
		addMouseListener(dragLis);
		addMouseMotionListener(dragLis);
		
		ResizableListener resizableLis = new ResizableListener();
		addMouseListener(resizableLis);
		addMouseMotionListener(resizableLis);
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(isFocusable()) {
			focusBorder.paint(g);
			corner.paint(g);
		}
		emptyCanvas.paint(g);
	}
	
	@Override
	public void setFocusable(boolean focusable) {
		if (focusable) {
			//设置该组件被聚焦，其他兄弟组件取消聚焦
			JComponent c = (JComponent) this.getParent();
			for (int i = 0; i < c.getComponentCount(); i++) {
				if(this != c.getComponent(i)) {
					c.getComponent(i).setFocusable(false);
					c.repaint();
				}
			}
		}
		super.setFocusable(focusable);
	}
	
	
	/**
	 * 触发角
	 * @author wteng
	 *
	 */
	class TriggerCorner {
		private Rectangle delegate;
		private Color color;
		
		public TriggerCorner() {
			this(FOCUSBORDER_THICKNESS, Color.GRAY);
		}
		
		public TriggerCorner(int size, Color color) {
			this.color = color;
			this.delegate = new Rectangle();
			delegate.setSize(size, size);
		}
		
		public void paint(Graphics g) {
			 Graphics2D g2d = (Graphics2D) g.create();
			 g2d.setColor(color);
			 int x = (int) (PaneX.this.getWidth() - delegate.getWidth());
			 int y = (int) (PaneX.this.getHeight() - delegate.getHeight());
			 delegate.setLocation(x, y);
		     g2d.fill(delegate);
		}
		
		public boolean contains(Point p) {
			return delegate.contains(p);
		}
	}
	
	/**
	 * 聚焦框
	 * @author wteng
	 *
	 */
	class FocusBorder {
		private Color color;
		
		public FocusBorder() {
			this(new Color(205, 205, 205));
		}
		
		public FocusBorder(Color color) {
			this.color = color;
		}
		
		public void paint(Graphics g) {
			if(getWidth() < 0 || getHeight() < 0) {
				return;
			}
			Shape outer;
	        Shape inner;

	        int offs = FOCUSBORDER_THICKNESS;
	        int size = offs + offs;
	        outer = new Rectangle(0, 0, getWidth(), getHeight());
	        inner = new Rectangle(offs, offs, getWidth() - size, getHeight() - size);
	        
	        Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
	        path.append(outer, false);
	        path.append(inner, false);
	        
	        Graphics2D g2d = (Graphics2D) g.create();
	        g2d.setColor(color);
	        g2d.fill(path);
	        
		}
	}
	
	/**
	 * 空白画布
	 * @author wteng
	 *
	 */
	class EmptyCanvas {
		private Rectangle delegate;
		private Color color;
		
		public EmptyCanvas() {
			this(Color.WHITE);
		}
		
		public EmptyCanvas(Color color) {
			this.color = color;
			delegate = new Rectangle();
			delegate.setLocation(FOCUSBORDER_THICKNESS, 
					FOCUSBORDER_THICKNESS);
		}
		
		public void paint(Graphics g) {
			if (getWidth() < 0 || getHeight() < 0) {
				return;
			}
			Graphics2D g2d = (Graphics2D) g.create();
			
			int width = PaneX.this.getWidth() - 2 * FOCUSBORDER_THICKNESS;
			int height = PaneX.this.getHeight() - 2 * FOCUSBORDER_THICKNESS;
			delegate.setSize(width, height);
			
			g2d.setColor(color);
			g2d.fill(delegate);
			
			g2d.setColor(Color.BLACK);
			g2d.draw(delegate);
		}
		
		public boolean contains(int x, int y) {
			return delegate.contains(x, y) ? true : false;
		}
		
		public int getX() {
			return (int) delegate.getX();
		}
		
		public int getY() {
			return (int) delegate.getY();
		}
		
		public int getWidth() {
			return (int) delegate.getWidth();
		}
		
		public int getHeight() {
			return (int) delegate.getHeight();
		}
		
	}
	
	public boolean isResizable() {
		return isResizable;
	}

	public void setResizable(boolean isResizable) {
		this.isResizable = isResizable;
	}
	
	public boolean isInResizableRegion(Point p) {
		return corner.contains(p);
	}

	/**
	 * 鼠标拖动面板监听
	 * @author wteng
	 *
	 */
	private class DraggableListener extends MouseAdapter {
		
		private boolean isDraggable = false;
		private Point pressPos;
		
		@Override
		public void mousePressed(MouseEvent e) {
			//确认按压的位置是否可拖
			boolean checkDraggable = e.getY() < FOCUSBORDER_THICKNESS ||
					e.getX() < FOCUSBORDER_THICKNESS;
			if(checkDraggable) {
				isDraggable = true;
				pressPos = e.getPoint();
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if(isDraggable) {
				final int OFFSETX = e.getX() - pressPos.x;
				final int OFFSETY = e.getY() - pressPos.y;
				PaneX.this.setLocation(PaneX.this.getLocation().x + OFFSETX, 
						PaneX.this.getLocation().y + OFFSETY);
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			isDraggable = false;
		}

	}
	
	/**
	 * 获取焦点监听
	 * @author wteng
	 *
	 */
	private class FocusListener extends MouseAdapter {
		
		@Override
		public void mousePressed(MouseEvent e) {
			PaneX.this.setFocusable(true);
		}

	}
	
	/**
	 * 面板缩放监听
	 * @author wteng
	 *
	 */
	private class ResizableListener extends MouseAdapter {
		
		private boolean canResizable = false;
		
		@Override
		public void mousePressed(MouseEvent e) {
			//确认按下的坐标是否允许改变大小
			if(isInResizableRegion(e.getPoint()) && isResizable) {
				canResizable = true;
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if(canResizable) {
				PaneX.this.setSize(e.getX(), e.getY());
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			canResizable = false;
		}

	}

	
}
