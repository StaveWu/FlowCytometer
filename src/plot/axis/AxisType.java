package plot.axis;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AxisType {
	
	public static final IAxisTypeStrategy X = new XStrategy();
	public static final IAxisTypeStrategy Y = new YStrategy();
	public static final IAxisTypeStrategy Z = new ZStrategy();
	
	/**
	 * x÷·≤ﬂ¬‘
	 * 
	 * @author STAVE WU
	 *
	 */
	private static class XStrategy implements IAxisTypeStrategy {

		@Override
		public Map<Point, Point> calculateScaleSrcLocations(List<Integer> distance, Point origin, int scaleLength) {
			
			if (distance == null || origin == null || scaleLength < 0) {
				return null;
			}
			
			Map<Point, Point> res = new LinkedHashMap<Point, Point>();
			
			for(int ele : distance) {
				Point k = new Point(origin.x + ele, origin.y);
				Point v = new Point(k.x, k.y + scaleLength);
				res.put(k, v);
			}
			return res;
		}

		@Override
		public List<Point> calculateBigValueSrcLocations(List<Integer> valueWidth, int valueHeight,
				Set<Point> bsSrcLocations) {
			
			List<Point> res = new ArrayList<Point>();
			
			Iterator<Point> iter = bsSrcLocations.iterator();
			int i = 0;
			while(iter.hasNext()) {
				Point p = iter.next();
				Point r = new Point(p.x - valueWidth.get(i) / 2, p.y + valueHeight + Axis.VALUE_OFFSET);
				res.add(r);
				i++;
			}
			return res;
		}

		@Override
		public Point calculateNameLocation(int nameWidth, int nameHeight, Point origin, int length) {
			
			return new Point(origin.x + length / 2 - nameWidth / 2, origin.y + nameHeight + Axis.NAME_OFFSET);
		}
		
	}
	
	/**
	 * y÷·≤ﬂ¬‘
	 * 
	 * @author STAVE WU
	 *
	 */
	private static class YStrategy implements IAxisTypeStrategy {

		@Override
		public Map<Point, Point> calculateScaleSrcLocations(List<Integer> distance, Point origin, int scaleLength) {
			
			if (distance == null || origin == null || scaleLength < 0) {
				return null;
			}
			
			Map<Point, Point> res = new LinkedHashMap<Point, Point>();
			
			for(int ele : distance) {
				Point k = new Point(origin.x + ele, origin.y);
				Point v = new Point(k.x, k.y - scaleLength);
				res.put(k, v);
			}
			return res;
		}

		@Override
		public List<Point> calculateBigValueSrcLocations(List<Integer> valueWidth, int valueHeight,
				Set<Point> bsSrcLocations) {
			
			List<Point> res = new ArrayList<Point>();
			
			Iterator<Point> iter = bsSrcLocations.iterator();
			int i = 0;
			while(iter.hasNext()) {
				Point p = iter.next();
				Point r = new Point(p.x - valueWidth.get(i) / 2, p.y - Axis.VALUE_OFFSET);
				res.add(r);
				i++;
			}
			return res;
		}

		@Override
		public Point calculateNameLocation(int nameWidth, int nameHeight, Point origin, int length) {
			
			return new Point(origin.x + length / 2 - nameWidth / 2, origin.y - Axis.NAME_OFFSET);
		}
		
	}
	
	/**
	 * z÷·≤ﬂ¬‘
	 * @author STAVE WU
	 *
	 */
	private static class ZStrategy implements IAxisTypeStrategy {

		@Override
		public Map<Point, Point> calculateScaleSrcLocations(List<Integer> distance, Point origin, int scaleLength) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Point> calculateBigValueSrcLocations(List<Integer> valueWidth, int valueHeight,
				Set<Point> bsSrcLocations) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Point calculateNameLocation(int nameWidth, int nameHeight, Point origin, int length) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
