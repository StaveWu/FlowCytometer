package plotContainer;

import java.awt.Component;

import plot.ArrowPaneContainer;
import plot.Plot;

public class PlotContainer extends ArrowPaneContainer implements PlotContainerModelObserver {
	
	private PlotContainerModel model;
	
	private PLotContainerObserver observer;
	
	public PlotContainer(PlotContainerModel model) {
		this.model = model;
		model.addObserver(this);
	}
	
	@Override
	public void remove(Component comp) {
		super.remove(comp);
		model.removePlot((Plot) comp);
	}

	@Override
	public void addPlot(Plot plot) {
		add(plot);
	}
	
	public void notifyDataInputed() {
		if (observer != null) {
			observer.notifyDataInputed();
		}
	}
	
	public void addObserver(PLotContainerObserver observer) {
		this.observer = observer;
	}
	
	public void removeObserver(PLotContainerObserver observer) {
		if (observer != null) {
			observer = null;
		}
	}

}
