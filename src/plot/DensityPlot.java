package plot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class DensityPlot extends Plot {
	
//	private static int count = 0;
	
	public DensityPlot() {
		super();
//		plotName += "DensityPlot-" + count;
//		count++;
	}

	@Override
	public ExperimentData getRealCoords(ExperimentData realCoords) {
		//不需要处理
		return realCoords;
	}

	@Override
	public void paintData(Graphics g, ExperimentData pxCoords) {
		if (pxCoords == null) {
			return;
		}
		/*
		 * 剪切出画数据的区域
		 */
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿
		
		g2d.transform(new AffineTransform(new float[]{1, 0, 0, -1, 0, 0}));
		Rectangle dr = (Rectangle)super.getDataRegion();
		Rectangle localeDr = new Rectangle(0, 0, (int)dr.getWidth(), (int)dr.getHeight());
		g2d.clip(localeDr);
		
		/*
		 * 自适应坐标点大小
		 */
		double rX = axis[0].getMaxValue() - axis[0].getMinValue();
		double rY = axis[1].getMaxValue() - axis[1].getMinValue();
		int dotSizeX = 1;
		int dotSizeY = 1;
		if(dr.getWidth() / rX > 1) {
			dotSizeX = (int) (dr.getWidth() / rX + 1);
		}
		if(dr.getHeight() / rY > 1) {
			dotSizeY = (int) (dr.getHeight() / rY + 1);
		}
		
		/*
		 * 统计密度
		 */
		double[] x = pxCoords.getDataByName(axis[0].getName());
		double[] y = pxCoords.getDataByName(axis[1].getName());
		
		if (x == null || x.length == 0 || y == null || y.length == 0) {
			return;
		}
		
		Map<Point, Integer> map = new HashMap<Point, Integer>();
		for (int i = 0; i < x.length; i++) {
			Point p = new Point((int)x[i], (int)y[i]);
			Integer density = map.get(p);
			map.put(p, density == null ? 1 : density + 1);
		}
		
		List<Integer> densities = new ArrayList<Integer>();
		Iterator<Point> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			Point key = iter.next();
			densities.add(map.get(key));
		}
		/*
		 * 画密度图
		 */
		if (densities.isEmpty()) {
			return;
		}
		int dmax = Collections.max(densities);
		int interval = 255 * 3;
		double rate = (double)interval / (double)dmax;
		
		Iterator<Point> iter2 = map.keySet().iterator();
		while (iter2.hasNext()) {
			Point p = iter2.next();
			Integer den = map.get(p);
			if(p.x == -1 || p.y == -1) {
				continue;
			}
			/*
			 * 设置颜色
			 * 浅蓝色(0, 255, 255) -> 红色(255, 0, 0)变化规律如下：
			 * b 	255  ->  0
			 * r   	0    ->  255
			 * g	255	 ->	 0
			 */
			int level = (int) (rate * den);
			int red = 0;
			int green = 255;
			int blue = 255;
			int tmp = blue - level;
			blue = tmp < 0 ? 0 : tmp;
			if(blue == 0) {
				tmp = red - tmp;
				red = tmp > 255 ? 255 : tmp;
			}
			if(red == 255) {
				tmp = tmp - red;
				green -= tmp;
			}
			g2d.setColor(new Color(red, green, blue));
			g2d.fillRect(p.x, p.y, dotSizeX, dotSizeY);
		}
	}

	@Override
	public String getPlotType() {
		return "DensityPlot";
	}

	@Override
	public List<Integer> getGatedIds(ExperimentData pxData, ExperimentData realCoords, 
			ExperimentData srcData, List<Integer> dataIds) {
		if(gate == null || pxData == null || realCoords == null || srcData == null) {
			return null;
		}
		List<Integer> pxGatedIds = gate.getGatedIndex(
				pxData.getDataByName(axis[0].getName()), 
				pxData.getDataByName(axis[1].getName()), origin);
		List<Integer> res = new ArrayList<Integer>();
		for (int i = 0; i < pxGatedIds.size(); i++) {
			res.add(dataIds.get(pxGatedIds.get(i)));
		}
		return res;
	}

}
