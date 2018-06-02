package plot.axis;

import java.awt.Graphics2D;
import java.awt.Point;

public class AxisDimension {
	
	public static final IDimensionStrategy X2D = new X2DStrategy();
	public static final IDimensionStrategy Y2D = new Y2DStrategy();
	public static final IDimensionStrategy X3D = new X3DStrategy();
	public static final IDimensionStrategy Y3D = new Y3DStrategy();
	public static final IDimensionStrategy Z3D = new Z3DStrategy();
	
	/**
	 * 五个静态内部策略类的实现
	 */
	private static class X2DStrategy implements IDimensionStrategy {

		@Override
		public void tranform(Graphics2D g2d) {
			//不需要变换
			return;
		}

		@Override
		public Point getEnd(Point start, int length) {
			return new Point(start.x + length, start.y);
		}
		
	}
	
	private static class Y2DStrategy implements IDimensionStrategy {

		@Override
		public void tranform(Graphics2D g2d) {
			//逆时针旋转90度
			g2d.rotate(3 * Math.PI / 2);
		}

		@Override
		public Point getEnd(Point start, int length) {
			
			double[] srcEnd = {start.x + length, start.y};
			double[][] matrix = {
					{ Math.cos(3 * Math.PI / 2), - Math.sin(3 * Math.PI / 2) },
					{ Math.sin(3 * Math.PI / 2), Math.cos(3 * Math.PI / 2) }
			};
			
			double x = matrix[0][0] * srcEnd[0] + matrix[0][1] * srcEnd[1];
			double y = matrix[1][0] * srcEnd[0] + matrix[1][1] * srcEnd[1];
			
			return new Point((int)x, (int)y);
		}
		
	}
	
	private static class X3DStrategy implements IDimensionStrategy {

		@Override
		public void tranform(Graphics2D g2d) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Point getEnd(Point start, int length) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	private static class Y3DStrategy implements IDimensionStrategy {

		@Override
		public void tranform(Graphics2D g2d) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Point getEnd(Point start, int length) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	private static class Z3DStrategy implements IDimensionStrategy {

		@Override
		public void tranform(Graphics2D g2d) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Point getEnd(Point start, int length) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
