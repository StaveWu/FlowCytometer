package plot.threshold;

import java.awt.Point;
import java.awt.Rectangle;

public class RegionThreshold {
	
	/**
	 * ����
	 */
	public static final int LEFT = 45;
	
	/**
	 * �ϼ��
	 */
	public static final int TOP = 30;
	
	/**
	 * �¼��
	 */
	public static final int BOTTOM = 45;
	
	/**
	 * �Ҽ��
	 */
	public static final int RIGHT = 30;
	
	public static final IThresholdStrategy T2D = new Threshold2D();
	public static final IThresholdStrategy T3D = new Threshold3D();
	
	private static class Threshold2D implements IThresholdStrategy {

		@Override
		public Rectangle getAxisRegionY(Rectangle r) {
			
			return new Rectangle(
					r.x, 
					r.y + TOP, 
					LEFT,
					r.height - TOP - BOTTOM
					);
		}

		@Override
		public Rectangle getAxisRegionX(Rectangle r) {
			
			return new Rectangle(
					r.x + LEFT,
					r.y + r.height - BOTTOM, 
					r.width - LEFT - RIGHT,
					BOTTOM
					);
		}

		@Override
		public Rectangle getAxisRegionZ(Rectangle r) {
			//2dû��z��
			return null;
		}

		@Override
		public Rectangle getDataRegion(Rectangle r) {
			
			return new Rectangle(
					r.x + LEFT,
					r.y + TOP,
					r.width - LEFT - RIGHT,
					r.height - TOP - BOTTOM
					);
		}

		@Override
		public Point getOrigin(Rectangle r) {
			
			return new Point(r.x + LEFT, r.y + r.height - BOTTOM);
		}

		@Override
		public int[] getAxisLengths(Rectangle r) {
			
			int[] lengths = {
					r.width - LEFT - RIGHT,
					r.height - TOP - BOTTOM
			};
			return lengths;
		}
		
	}
	private static class Threshold3D implements IThresholdStrategy {

		@Override
		public Rectangle getAxisRegionX(Rectangle r) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Rectangle getAxisRegionY(Rectangle r) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Rectangle getAxisRegionZ(Rectangle r) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Rectangle getDataRegion(Rectangle r) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Point getOrigin(Rectangle r) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int[] getAxisLengths(Rectangle r) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
