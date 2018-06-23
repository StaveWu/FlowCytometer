package tube;

import java.util.List;
import java.util.Vector;

import mainPage.MainView;
import mainPage.Session;
import utils.SwingUtils;

public class TubeController implements ITubeController {
	
	private ITubeModel model;
	private MainView view;
	
	// 当前所属的tubeid
	private String tubeLid;
	
	public TubeController(ITubeModel model, MainView view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void loadData(String pathname) {
		if (!isTubeOpenned() || isTubeSwitched()) {
			try {
				tubeLid = Session.getSelectedTubeLid();
				model.init(pathname);
			} catch (Exception e) {
				e.printStackTrace();
				SwingUtils.showErrorDialog(view, "加载数据失败！" + e.toString());
			}
		}
		
	}

	@Override
	public void save() {
		if (!isTubeOpenned()) {
			SwingUtils.showErrorDialog(view, "无数据可保存");
			return;
		}
		try {
			model.save();
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "保存数据失败！" + e.toString());
		}
	}
	
	@Override
	public void clear() {
		tubeLid = null;
		model.clear();
		view.repaint();
	}
	
	private boolean isTubeOpenned() {
		return tubeLid != null;
	}
	
	private boolean isTubeSwitched() {
		return tubeLid != null && !tubeLid.equals(Session.getSelectedTubeLid());
	}

	@Override
	public void setFields(Vector<String> fields) {
		model.setFields(fields);
	}
	
	public void addEvents(List<Vector<Double>> es) {
		if (es == null) {
			return;
		}
		
		for (Vector<Double> ele : es) {
			model.addEvent(ele);
		}
	}
	
	@Override
	public void updateEvents(List<Vector<Double>> es) {
		if (es == null) {
			return;
		}
		
		model.removeAllEvents();
		for (Vector<Double> ele : es) {
			model.addEvent(ele);
		}
	}

}
