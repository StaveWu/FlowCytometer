package plot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utils.ArrayUtils;

public class Histogram extends Plot {
	
	public Histogram() {
		super();
	}


	@Override
	public ExperimentData getRealCoords(ExperimentData srcData) {
		
		if(srcData == null) {
			return null;
		}
		double[] src = srcData.getDataByName(axis[0].getName());
		if (src == null || src.length == 0) {
			return null;
		}
		//对src进行统计
		Map<Double, Integer> map = new HashMap<Double,Integer>();
		for(double ele : src) {
			Integer num = map.get(ele);
			map.put(ele, num == null ? 1 : num + 1);
		}
		//按升序排序
		List<Double> keyAscent = new ArrayList<Double>();
		Set<Double> key = map.keySet();
		Iterator<Double> iter = key.iterator();
		while(iter.hasNext()) {
			keyAscent.add(iter.next());
		}
		Collections.sort(keyAscent);
		
		//返回结果
		double[] x = new double[keyAscent.size()];
		double[] y = new double[keyAscent.size()];
		for(int i = 0; i < x.length; i++) {
			x[i] = keyAscent.get(i);
			y[i] = map.get(x[i]);
		}
		
		axis[1].setName("Count");
		
		ExperimentData res = new ExperimentData();
		res.add(axis[0].getName(), x);
		res.add(axis[1].getName(), y);
		
		return res;
	}
	

	@Override
	public void paintData(Graphics g, ExperimentData pxCoords) {
		if (pxCoords == null) {
			return;
		}
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿
		//剪切出画数据的区域
		g2d.transform(new AffineTransform(new float[]{1, 0, 0, -1, 0, 0}));
		Rectangle dr = (Rectangle)super.getDataRegion();
		Rectangle localeDr = new Rectangle(0, 0, (int)dr.getWidth(), (int)dr.getHeight());
		g2d.clip(localeDr);
		
		//画折线
		g2d.setColor(Color.RED);
		double[] x = pxCoords.getDataByName(axis[0].getName());
		double[] y = pxCoords.getDataByName(axis[1].getName());
		if (x == null || x.length == 0 || y == null || y.length == 0) {
			return;
		}
		
		for(int j = 1; j < x.length; j++) {
			if(x[j] == -1 || y[j] == -1) {
				continue;
			}
			int x1 = (int) x[j - 1];
			int y1 = (int) y[j - 1];
			int x2 = (int) x[j];
			int y2 = (int) y[j];
			g2d.drawLine(x1, y1, x2, y2);
		}
	}


	@Override
	public String getPlotType() {
		return "Histogram";
	}


	@Override
	public List<Integer> getGatedIds(ExperimentData pxData, ExperimentData realCoords, 
			ExperimentData srcData) {
		if(gate == null || pxData == null || realCoords == null || srcData == null) {
			return null;
		}
		List<Integer> pxGatedIds = gate.getGatedIndex(
				pxData.getDataByName(axis[0].getName()), 
				pxData.getDataByName(axis[1].getName()), origin);

		if(pxGatedIds == null || pxGatedIds.size() <= 0) {
			return null;
		}
		List<Integer> res = new ArrayList<>();
		//获取被圈出的数据中的最小值和最大值
		double[] realx = realCoords.getDataByName(axis[0].getName());
		if (realx == null || realx.length == 0) {
			return null;
		}
		double[] tmpx = new double[pxGatedIds.size()];
		for (int i = 0; i < tmpx.length; i++) {
			tmpx[i] = realx[pxGatedIds.get(i)];
		}
		double maxx = ArrayUtils.getMax(tmpx);
		double minx = ArrayUtils.getMin(tmpx);
		//确定原始数据的索引
		double[] srcx = srcData.getDataByName(axis[0].getName());
		if (srcx == null || srcx.length == 0) {
			return null;
		}
		for (int i = 0; i < srcx.length; i++) {
			if(srcx[i] >= minx && srcx[i] < maxx) {
				res.add(i);
			}
		}
		return res;
	}

}
