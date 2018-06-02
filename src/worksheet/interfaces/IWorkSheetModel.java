package worksheet.interfaces;

import plot.Plot;
import plotContainer.PlotContainerModel;
import tube.ITubeModel;

public interface IWorkSheetModel {
	
	void init(String relaPathname) throws Exception;
	
	void addPlot(Plot plot);
	
	void removePlot(Plot plot);
	
	void save() throws Exception;
	
	PlotContainerModel getDelegate();
	
	int getNewPlotId();
	
	void addTubeModel(ITubeModel tubeModel);

}
