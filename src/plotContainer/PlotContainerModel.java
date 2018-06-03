package plotContainer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dao.DAOFactory;
import plot.Plot;
import tube.ITubeModel;
import tube.TubeModel;

public class PlotContainerModel implements IPlotContainerModel {
	
	private List<Plot> plots = new ArrayList<>();
	
	private ITubeModel dataSource;
	
	private PlotContainerModelObserver observer;
	
	@Override
	public void addPlot(Plot plot) {
		if (plot.getUid() < 0) {	// 区分新建的:(id = -1)和数据库加载的:(id >= 0)
			plot.setUid(getNextUid());
		}
		hookDataSource(plot);
		plots.add(plot);
		notifyAdd(plot);
	}
	
	@Override
	public void removePlot(Plot plot) {
		if (plots.contains(plot)) {
			plots.remove(plot);
		}
	}
	
	@Override
	public void addObserver(PlotContainerModelObserver observer) {
		this.observer = observer;
	}
	
	@Override
	public void removeObserver(PlotContainerModelObserver observer) {
		observer = null;
	}
	
	@Override
	public void notifyAdd(Plot plot) {
		if (observer != null) {
			observer.addPlot(plot);
		}
	}
	
	@Override
	public int getPlotCount() {
		return plots.size();
	}
	
	@Override
	public boolean isEmpty() {
		return plots.size() <= 0;
	}
	
	@Override
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
	
	@Override
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

	@Override
	public void loadPlots(String pathname, String tablename) throws Exception {
		clear();
		if (!DAOFactory.getIPlotsDAOInstance(pathname).isExist(tablename)) {
			DAOFactory.getIPlotsDAOInstance(pathname).createTable(tablename);
		}
		List<Plot> beans = DAOFactory.getIPlotsDAOInstance(pathname).findAll(tablename);
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

	@Override
	public void savePlots(String pathname, String tablename) throws Exception {
		DAOFactory.getIPlotsDAOInstance(pathname).addAll(tablename, plots);
	}
	

}
