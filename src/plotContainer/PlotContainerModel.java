package plotContainer;

import java.util.ArrayList;
import java.util.List;

import plot.Plot;
import tube.ITubeModel;
import tube.TubeModel;

public class PlotContainerModel {
	
	private List<Plot> plots = new ArrayList<>();
	
	private ITubeModel dataSource;
	
	private PlotContainerModelObserver observer;
	
	public void addPlot(Plot plot) {
		hookDataSource(plot);
		plots.add(plot);
		notifyAdd(plot);
	}
	
	public void removePlot(Plot plot) {
		if (plots.contains(plot)) {
			plots.remove(plot);
		}
	}
	
	public void addObserver(PlotContainerModelObserver observer) {
		this.observer = observer;
	}
	
	public void removeObserver(PlotContainerModelObserver observer) {
		observer = null;
	}
	
	public void notifyAdd(Plot plot) {
		if (observer != null) {
			observer.addPlot(plot);
		}
	}
	
	public int getPlotCount() {
		return plots.size();
	}
	
	public boolean isEmpty() {
		return plots.size() <= 0;
	}
	
	public void clear() {
		for (int i = getPlotCount() - 1; i >= 0; i--) {
			removePlot(plots.get(i));
		}
	}
	
	/**
	 * 获取plots的克隆
	 * @return
	 */
	public List<Plot> getPlots() {
		List<Plot> res = new ArrayList<>();
		for (int i = 0; i < plots.size(); i++) {
			res.add(plots.get(i));
		}
		return res;
	}
	
	public void addDataSource(ITubeModel data) {
		this.dataSource = data;
		for (Plot plot : plots) {
			hookDataSource(plot);
		}
	}
	
	public int getNextUid() {
		int id = 0;
		for (int i = 0; i < getPlotCount(); i++) {
			Plot plot = plots.get(i);
			if (plot.getUid() >= id) {
				id = plot.getUid() + 1;
			}
		}
		return id;
	}
	
	private void hookDataSource(Plot plot) {
		nonNullDataSource();
		plot.setTubeModel(dataSource);
	}
	
	private void nonNullDataSource() {
		if (dataSource == null) {
			dataSource = new TubeModel();
		}
	}
	

}
