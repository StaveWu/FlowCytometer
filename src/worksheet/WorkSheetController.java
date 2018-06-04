package worksheet;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import mainPage.events.WorkSheetEvent;
import mainPage.interfaces.WorkSheetObserver;
import plot.DensityPlot;
import plot.DotPlot;
import plot.Histogram;
import plot.Plot;
import tube.ITubeModel;
import utils.SwingUtils;
import worksheet.interfaces.IWorkSheetController;

public class WorkSheetController implements IWorkSheetController {
	
	private WorkSheetView view;
	
	private List<WorkSheetObserver> observers = new ArrayList<>();
	
	public WorkSheetController() {
		view = new WorkSheetView(this);
		view.initializeComponents();
		view.disableEdit();
	}

	@Override
	public void loadWorkSheet(String pathname) {
		try {
			view.loadPlots(pathname);
			view.enableEdit();
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "加载WorkSheet失败！异常信息：" + e.getMessage());
		}
		
	}
	
	
	@Override
	public void addDataSource(ITubeModel data) {
		view.addDataSource(data);
	}
	
	
	@Override
	public JPanel getView() {
		return view;
	}

	@Override
	public void addObserver(WorkSheetObserver observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(WorkSheetObserver observer) {
		if (observers.contains(observer)) {
			observers.remove(observer);
		}
	}

	@Override
	public void notifyObservers(WorkSheetEvent event) {
		for (WorkSheetObserver ele : observers) {
			ele.workSheetUpdated(event);
		}
	}


	
	/////////// 菜单命令 ///////////
	
	@Override
	public void save(Point location) {
		try {
			view.save();
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "保存失败！异常信息：" + e.getMessage());
		}
	}

	@Override
	public void createDotPlot(Point location) {
		Plot dotPlot = new DotPlot();
		dotPlot.setLocation(location);
		view.addPlot(dotPlot);
		view.repaint();
	}

	@Override
	public void createHistogram(Point location) {
		Plot histogram = new Histogram();
		histogram.setLocation(location);
		view.addPlot(histogram);
		view.repaint();
	}

	@Override
	public void createDensityPlot(Point location) {
		Plot densityPlot = new DensityPlot();
		densityPlot.setLocation(location);
		view.addPlot(densityPlot);
		view.repaint();
	}
}
