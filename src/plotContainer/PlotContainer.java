package plotContainer;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dao.DAOFactory;
import dashBoard.IDataCounter;
import plot.ArrowPane;
import plot.ArrowPaneContainer;
import plot.Plot;
import tube.ITubeModel;
import tube.TubeModel;

@SuppressWarnings("serial")
public class PlotContainer extends ArrowPaneContainer implements IDataCounter {
	
	private PlotContainerObserver observer;
	
	private static final String worksheetTableName = "WorkSheet";
	
	private ITubeModel dataSource;
	
	public PlotContainer() {}
	
	public void loadPlots(String pathname) throws Exception {
		clear();
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
				add(plot);
			}
		}
	}
	
	public void savePlots(String pathname) throws Exception {
		DAOFactory.getIPlotsDAOInstance(pathname).addAll(worksheetTableName, getPlots());
	}
	
	private List<Plot> getPlots() {
		List<Plot> res = new ArrayList<>();
		for (ArrowPane ele : panes) {
			res.add((Plot) ele);
		}
		return res;
	}
	
	private Plot findByUid(int uid, List<Plot> plots) {
		for (Plot ele : plots) {
			if (ele.getUid() == uid) {
				return ele;
			}
		}
		return null;
	}
	
	public void clear() {
		removeAll();
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
	
	public void setDataSource(ITubeModel data) {
		this.dataSource = data;
	}
	
	@Override
	public Component add(Component comp) {
		Plot plot = (Plot) comp;
		if (plot.getUid() < 0) {	// 区分新建的:(id = -1)和数据库加载的:(id >= 0)
			plot.setUid(getNextUid());
		}
		hookDataSource(plot);
		return super.add(comp);
	}
	
	public int getNextUid() {
		int id = 0;
		for (int i = 0; i < getPlots().size(); i++) {
			Plot plot = getPlots().get(i);
			if (plot.getUid() >= id) {
				id = plot.getUid() + 1;
			}
		}
		return id;
	}
	
	public void notifyDataInputed() {
		if (observer != null) {
			observer.notifyDataInputed();
		}
	}
	
	public void addObserver(PlotContainerObserver observer) {
		this.observer = observer;
	}
	
	public void removeObserver(PlotContainerObserver observer) {
		if (observer != null) {
			observer = null;
		}
	}

	@Override
	public int getDataCount(boolean constaint) {
		// 如果有图并且指定要筛选，则获取最终筛选的数量
		if (constaint && panes.size() > 0) {
			Plot lastPlot = (Plot) panes.get(panes.size() - 1);
			System.out.println(lastPlot);
			System.out.println("返回：" + lastPlot.getDataCount());
			return lastPlot.getDataCount();
		} // 否则返回数据源的数量
		else {
			System.out.println("from DataSource");
			return dataSource.getEventsCount();
		}
	}

}
