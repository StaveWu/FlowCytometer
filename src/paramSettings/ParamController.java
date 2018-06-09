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
	
	// ��¼��ǰmodel��������Ŀlid
	private String lid;
	
	
	public ParamController(IParamModel tableModel, MainView view) {
		this.paramModel = tableModel;
		this.view = view;
	}
	
	@Override
	public void removeRow(int row) {
		if (!isParamSettingsOpenned()) {
			SwingUtils.showErrorDialog(view, "���ȴ���Ŀ");
			return;
		}
		try {
			paramModel.removeRow(row);
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "ɾ��ʧ�ܣ��쳣��Ϣ��" + e.getMessage());
		}
	}
	
	@Override
	public void addRow() {
		if (!isParamSettingsOpenned()) {
			SwingUtils.showErrorDialog(view, "���ȴ���Ŀ");
			return;
		}
		try {
			Object[] rowdata = {"NEW", -1, -1, false, false, false, -1};
			paramModel.addRow(rowdata);
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "���ʧ�ܣ��쳣��Ϣ��" + e.getMessage());
		}
	}
	
	
	@Override
	public void loadSettings() {
		// �����Ŀ��û�򿪻�����Ŀ�Ѿ����˵�����Ŀ�л��ˣ������¼���settings
		if (!isParamSettingsOpenned() || isProjectSwitched()) {
			lid = Session.getSelectedProjectLid();
			String pathname = FCMSettings.getWorkSpacePath() 
					+ "/" + Session.getSelectedProjectName() + "/" + "Settings";
			try {
				paramModel.init(pathname);
			} catch (Exception e) {
				e.printStackTrace();
				SwingUtils.showErrorDialog(view, "����ParamSettingsʧ�ܣ��쳣��Ϣ��" + e.getMessage());
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
			SwingUtils.showErrorDialog(view, "���ȴ���Ŀ");
			return;
		}
		String pathname = FCMSettings.getWorkSpacePath() 
				+ "/" + Session.getSelectedProjectName() + "/" + "Settings";
		try {
			paramModel.save(pathname);
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "����ParamSettingsʧ�ܣ��쳣��Ϣ��" + e.getMessage());
		}
	}

	@Override
	public void clear() {
		lid = null;
		paramModel.clear();
		view.repaint();
	}

}
