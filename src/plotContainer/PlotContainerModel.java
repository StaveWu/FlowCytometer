package plotContainer;

import java.util.ArrayList;
import java.util.List;

import plot.Plot;

public class PlotContainerModel {
	
	private List<Plot> plots = new ArrayList<>();
	
	private PlotContainerModelObserver observer;
	
	public void addPlot(Plot plot) {
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
	
	public Plot getPlotAt(int index) {
		if (index >= getPlotCount()) {
			return null;
		}
		return plots.get(index);
	}

}
