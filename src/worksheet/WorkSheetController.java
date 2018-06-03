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
import worksheet.interfaces.IWorkSheetModel;

public class WorkSheetController implements IWorkSheetController {
	
	private IWorkSheetModel workSheetModel;
	private WorkSheetView view;
	
	private List<WorkSheetObserver> observers = new ArrayList<>();
	
	public WorkSheetController(IWorkSheetModel model) {
		this.workSheetModel = model;
		view = new WorkSheetView(model, this);
		view.initializeComponents();
//		view.disableEdit();
	}

	@Override
	public void loadWorkSheet(String relaPathname) {
		try {
			workSheetModel.init(relaPathname);
			view.repaint();
//			view.enableEdit();
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "加载WorkSheet失败！异常信息：" + e.getMessage());
		}
		
	}
	
	
	@Override
	public void addDataSource(ITubeModel data) {
		workSheetModel.addDataSource(data);
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
			workSheetModel.save();
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "保存失败！异常信息：" + e.getMessage());
		}
	}

	@Override
	public void createDotPlot(Point location) {
		Plot dotPlot = new DotPlot();
		dotPlot.setLocation(location);
		workSheetModel.addPlot(dotPlot);
		view.repaint();
	}

	@Override
	public void createHistogram(Point location) {
		Plot histogram = new Histogram();
		histogram.setLocation(location);
		workSheetModel.addPlot(histogram);
		view.repaint();
	}

	@Override
	public void createDensityPlot(Point location) {
		Plot densityPlot = new DensityPlot();
		densityPlot.setLocation(location);
		workSheetModel.addPlot(densityPlot);
		view.repaint();
	}
}
