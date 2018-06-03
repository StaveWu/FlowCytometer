package plotContainer;

import plot.Plot;
import tube.ITubeModel;

public interface IPLotContainerModel {
	
	void addPlot(Plot plot);
	
	void removePlot(Plot plot);
	
	void loadPLots(String pathname) throws Exception;
	
	void savePlots(String pathname) throws Exception;
	
	void addDataSource(ITubeModel data);
	
	int getPlotCount();
	
	Plot getPlotAt(int index);

}
