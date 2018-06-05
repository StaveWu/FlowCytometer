package dashBoard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import mainPage.events.DashBoardEvent;
import mainPage.interfaces.DashBoardObserver;
import utils.SwingUtils;

public class DashBoardController {
	
	private DashBoardModel model;
	private DashBoardView view;
	private List<DashBoardObserver> observers = new ArrayList<>();
	
	public DashBoardController(DashBoardModel model) {
		this.model = model;
		view = new DashBoardView(model, this);
		view.initializeComponent();
		checkSelected();
	}
	
	public void startSampling() {
		try {
			model.startSampling();
			view.setStatusStart();
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "发送数据失败！" + e.toString());
		}
		notifyDashBoardObservers(new DashBoardEvent(this, DashBoardEvent.START_SAMPLING));
	}
	
	public void stopSampling() {
		try {
			model.stopSampling();
			view.setStatusStop();
			notifyDashBoardObservers(new DashBoardEvent(this, DashBoardEvent.STOP_SAMPLING));
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "发送数据失败！" + e.toString());
		}
		
	}
	
	public void checkSelected() {
		if (view.isSelectTimeCondition()) {
			view.setHourUnit();
			view.setCheckBox(false);
			view.disableCheckBox();
		} else {
			view.setEventUnit();
			view.enableCheckBox();
		}
	}
	
	public JPanel getView() {
		return view;
	}

	public void addObserver(DashBoardObserver o) {
		observers.add(o);
	}

	public void removeObserver(DashBoardObserver o) {
		if (observers.contains(o)) {
			observers.remove(o);
		}
	}

	public void notifyDashBoardObservers(DashBoardEvent event) {
		Iterator<DashBoardObserver> iter = observers.iterator();
		while (iter.hasNext()) {
			DashBoardObserver o = (DashBoardObserver) iter.next();
			o.dashBoardUpdated(event);
		}
	}

}
