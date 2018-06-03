package plot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class DotPlot extends Plot {
	
	public DotPlot() {
		super();
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
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿
		//剪切出画数据的区域
		g2d.transform(new AffineTransform(new float[]{1, 0, 0, -1, 0, 0}));
		Rectangle dr = (Rectangle)super.getDataRegion();
		Rectangle localeDr = new Rectangle(0, 0, (int)dr.getWidth(), (int)dr.getHeight());
		g2d.clip(localeDr);
		
		//自适应坐标点的大小
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
		
		//画点
		double[] x = pxCoords.getDataByName(axis[0].getName());
		double[] y = pxCoords.getDataByName(axis[1].getName());
		if (x == null || x.length == 0 || y == null || y.length == 0) {
			return;
		}
		g2d.setColor(Color.RED);
		for(int i = 0; i < x.length; i++) {
			//过滤不属于坐标轴范围内的点
			if(x[i] == -1 || y[i] == -1) {
				continue;
			}
			g2d.fillRect((int)x[i], (int)y[i], dotSizeX, dotSizeY);
		}
	}


	@Override
	public String getPlotType() {
		return "DotPlot";
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
