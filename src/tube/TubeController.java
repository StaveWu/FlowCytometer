package tube;

import java.util.Vector;

import javax.swing.JPanel;

import utils.SwingUtils;

public class TubeController implements ITubeController {
	
	private ITubeModel model;
	private TubeView view;
	
	public TubeController(ITubeModel model) {
		this.model = model;
		view = new TubeView(this, model);
		view.initComponents();
	}

	@Override
	public void loadData(String pathname) {
		try {
			model.init(pathname);
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "加载数据失败！" + e.toString());
		}
	}

	@Override
	public void save() {
		try {
			model.save();
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "保存数据失败！" + e.toString());
		}
	}

	@Override
	public void setFields(Vector<String> fields) {
		model.setFields(fields);
	}

	@Override
	public JPanel getView() {
		return view;
	}

	@Override
	public ITubeModel geTubeModel() {
		return model;
	}

}
