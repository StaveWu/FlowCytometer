package plotContainer;

import plot.Plot;
import tube.ITubeModel;

public interface IPlotContainerModel {
	
	void addPlot(Plot plot);
	
	void removePlot(Plot plot);
	
	void loadPlots(String pathname, String tablename) throws Exception;
	
	void savePlots(String pathname, String tablename) throws Exception;
	
	void addDataSource(ITubeModel data);
	
	int getPlotCount();
	
	boolean isEmpty();
	
	void clear();
	
	void addObserver(PlotContainerModelObserver observer);
	
	void removeObserver(PlotContainerModelObserver observer);
	
	void notifyAdd(Plot plot);
}
