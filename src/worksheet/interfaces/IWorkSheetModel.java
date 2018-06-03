package worksheet.interfaces;

import plot.Plot;
import plotContainer.IPlotContainerModel;
import tube.ITubeModel;

public interface IWorkSheetModel {
	
	void init(String relaPathname) throws Exception;
	
	void addPlot(Plot plot);
	
	void removePlot(Plot plot);
	
	void save() throws Exception;
	
	IPlotContainerModel getDelegate();
	
	void addDataSource(ITubeModel tubeModel);

}
