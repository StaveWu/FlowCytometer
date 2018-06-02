package paramSettings;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import java.util.Map;

import mainPage.events.ParamSettingsEvent;
import mainPage.interfaces.ParamSettingsObserver;
import paramSettings.interfaces.IParamController;
import paramSettings.interfaces.ITableModel;
import paramSettings.interfaces.ParamModelObserver;
import utils.SwingUtils;

public class ParamController implements IParamController, ParamModelObserver {
	
	private ParamView view;
	private ITableModel tableModel;
	
	private List<ParamSettingsObserver> observers = new ArrayList<>();
	
	public ParamController(ITableModel tableModel) {
		this.tableModel = tableModel;
		tableModel.addObserver(this);
		this.view = new ParamView(tableModel, this);
		view.initializeComponent();
		view.hideButtons();
	}
	
	@Override
	public void removeRow(int row) {
		try {
			tableModel.removeRow(row);
			notifyObservers(new ParamSettingsEvent(this, 
					ParamSettingsEvent.REMOVE, tableModel.getDataNames()));
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "删除失败！异常信息：" + e.getMessage());
		}
	}
	
	@Override
	public void addRow() {
		try {
			Object[] rowdata = {"New", 0, 0, false, false, false};
			tableModel.addRow(rowdata);
			notifyObservers(new ParamSettingsEvent(this, 
					ParamSettingsEvent.ADD, tableModel.getDataNames()));
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "添加失败！异常信息：" + e.getMessage());
		}
	}
	
	@Override
	public JPanel getView() {
		return view;
	}
	
	@Override
	public void disableTableEdit() {
		view.disableTable();
		view.hideButtons();
	}
	
	@Override
	public void enableTableEdit() {
		view.enableTable();
		view.displayButtons();
	}
	
	@Override
	public void loadSettings(String relaPath) {
		try {
			tableModel.init(relaPath);
			view.displayButtons();
			notifyObservers(new ParamSettingsEvent(this, 
					ParamSettingsEvent.UPDATE, tableModel.getDataNames()));
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "加载ParamSettings失败！异常信息：" + e.getMessage());
		}
	}

	@Override
	public void addObserver(ParamSettingsObserver observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(ParamSettingsObserver observer) {
		if (observers.contains(observer)) {
			observers.remove(observer);
		}
	}

	@Override
	public void notifyObservers(ParamSettingsEvent event) {
		for (ParamSettingsObserver o : observers) {
			o.paramSettingsUpdated(event);
		}
	}
	
	@Override
	public void modifySelection(int row, int column, Map<Integer, Component> checkBoxsMap) {
		if (checkBoxsMap == null) {
			return;
		}
		for (int key : checkBoxsMap.keySet()) {
			if (column == key) {
				tableModel.setValueAt(true, row, key);
			}
			else {
				tableModel.setValueAt(false, row, key);
			}
		}
	}

	@Override
	public void updated() {
		notifyObservers(new ParamSettingsEvent(this, 
				ParamSettingsEvent.UPDATE, tableModel.getDataNames()));
	}
}
