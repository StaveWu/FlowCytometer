package worksheet;

import java.util.Iterator;
import java.util.List;

import dao.DAOFactory;
import mainPage.FCMSettings;
import plot.Plot;
import plotContainer.PlotContainerModel;
import tube.ITubeModel;
import worksheet.interfaces.IWorkSheetModel;

public class WorkSheetModel implements IWorkSheetModel {
	
	private PlotContainerModel delegate;
	private String pathname;
	private static final String worksheetTableName = "WorkSheet";
	
	public WorkSheetModel() {
		delegate = new PlotContainerModel();
	}

	@Override
	public void init(String relaPathname) throws Exception {
		delegate.clear();
		this.pathname = FCMSettings.getWorkSpacePath() + relaPathname;
		if (!DAOFactory.getIPlotsDAOInstance(pathname).isExist(worksheetTableName)) {
			DAOFactory.getIPlotsDAOInstance(pathname).createTable(worksheetTableName);
		}
		List<Plot> beans = DAOFactory.getIPlotsDAOInstance(pathname).findAll(worksheetTableName);
		if (beans != null) {
			Iterator<Plot> iter = beans.iterator();
			while (iter.hasNext()) {
				Plot plot = (Plot) iter.next();
				// 绑定plot前后关系
				Plot prev = findByUid(plot.getPrevid(), beans);
				plot.setPrev(prev);
				Plot next = findByUid(plot.getNextid(), beans);
				plot.setNext(next);
				addPlot(plot);
			}
		}
	}
	
	private Plot findByUid(int uid, List<Plot> plots) {
		for (Plot ele : plots) {
			if (ele.getUid() == uid) {
				return ele;
			}
		}
		return null;
	}
	
	/**
	 * 创建一个新的索引，该索引不与当前已存在
	 * 的任意索引重复
	 * @return
	 */
	@Override
	public int getNewPlotId() {
		return delegate.getNextUid();
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
		DAOFactory.getIPlotsDAOInstance(pathname).addAll(worksheetTableName, delegate.getPlots());
	}

	@Override
	public PlotContainerModel getDelegate() {
		return delegate;
	}

	@Override
	public void addDataSource(ITubeModel data) {
		delegate.addDataSource(data);
	}

}
