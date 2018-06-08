package dashBoard;

import mainPage.MainView;
import utils.SwingUtils;

public class DashBoardController {
	
	private DashBoardModel model;
	private MainView view;
	
	public DashBoardController(DashBoardModel model, MainView view) {
		this.model = model;
		this.view = view;
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
	}
	
	public void stopSampling() {
		try {
			model.stopSampling();
			view.setStatusStop();
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

}
