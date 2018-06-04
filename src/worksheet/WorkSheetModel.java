package worksheet;

import plot.Plot;
import plotContainer.IPlotContainerModel;
import plotContainer.PlotContainerModel;
import tube.ITubeModel;
import worksheet.interfaces.IWorkSheetModel;


public class WorkSheetModel implements IWorkSheetModel {
	
	private IPlotContainerModel delegate;
	private String pathname;
	private static final String worksheetTableName = "WorkSheet";
	
	public WorkSheetModel() {
		delegate = new PlotContainerModel();
	}

	@Override
	public void init(String pathname) throws Exception {
		this.pathname = pathname;
		delegate.loadPlots(pathname, worksheetTableName);
	}

	@Override
	public void addPlot(Plot plot) {
		delegate.addPlot(plot);
	}

	@Override
	public void removePlot(Plot plot) {
		delegate.removePlot(plot);
	}

	@Override
	public void save() throws Exception {
		delegate.savePlots(pathname, worksheetTableName);
	}

	@Override
	public IPlotContainerModel getDelegate() {
		return delegate;
	}

	@Override
	public void addDataSource(ITubeModel data) {
		delegate.addDataSource(data);
	}

}
