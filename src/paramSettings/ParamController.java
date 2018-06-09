package paramSettings;

import java.awt.Component;
import java.util.Map;

import mainPage.FCMSettings;
import mainPage.MainView;
import mainPage.Session;
import paramSettings.interfaces.IParamController;
import paramSettings.interfaces.IParamModel;
import utils.SwingUtils;

public class ParamController implements IParamController {
	
	private MainView view;
	private IParamModel paramModel;
	
	// 记录当前model所属的项目lid
	private String lid;
	
	
	public ParamController(IParamModel tableModel, MainView view) {
		this.paramModel = tableModel;
		this.view = view;
	}
	
	@Override
	public void removeRow(int row) {
		if (!isParamSettingsOpenned()) {
			SwingUtils.showErrorDialog(view, "请先打开项目");
			return;
		}
		try {
			paramModel.removeRow(row);
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "删除失败！异常信息：" + e.getMessage());
		}
	}
	
	@Override
	public void addRow() {
		if (!isParamSettingsOpenned()) {
			SwingUtils.showErrorDialog(view, "请先打开项目");
			return;
		}
		try {
			Object[] rowdata = {"NEW", -1, -1, false, false, false, -1};
			paramModel.addRow(rowdata);
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "添加失败！异常信息：" + e.getMessage());
		}
	}
	
	
	@Override
	public void loadSettings() {
		// 如果项目还没打开或者项目已经打开了但是项目切换了，则重新加载settings
		if (!isParamSettingsOpenned() || isProjectSwitched()) {
			lid = Session.getSelectedProjectLid();
			String pathname = FCMSettings.getWorkSpacePath() 
					+ "/" + Session.getSelectedProjectName() + "/" + "Settings";
			try {
				paramModel.init(pathname);
			} catch (Exception e) {
				e.printStackTrace();
				SwingUtils.showErrorDialog(view, "加载ParamSettings失败！异常信息：" + e.getMessage());
			}
		}
	}
	
	private boolean isParamSettingsOpenned() {
		return lid != null;
	}
	
	private boolean isProjectSwitched() {
		return lid != null && !lid.equals(Session.getSelectedProjectLid());
	}
	
	@Override
	public void modifySelection(int row, int column, Map<Integer, Component> checkBoxsMap) {
		if (checkBoxsMap == null) {
			return;
		}
		for (int key : checkBoxsMap.keySet()) {
			if (column == key) {
				paramModel.setValueAt(true, row, key);
			}
			else {
				paramModel.setValueAt(false, row, key);
			}
		}
	}

	@Override
	public void saveSettings() {
		if (!isParamSettingsOpenned()) {
			SwingUtils.showErrorDialog(view, "请先打开项目");
			return;
		}
		String pathname = FCMSettings.getWorkSpacePath() 
				+ "/" + Session.getSelectedProjectName() + "/" + "Settings";
		try {
			paramModel.save(pathname);
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "保存ParamSettings失败！异常信息：" + e.getMessage());
		}
	}

	@Override
	public void clear() {
		lid = null;
		paramModel.clear();
		view.repaint();
	}

}
