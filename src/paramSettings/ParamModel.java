package paramSettings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import dao.DAOFactory;
import dao.beans.ParamSettingsBean;
import paramSettings.interfaces.IParamModel;
import paramSettings.interfaces.ParamModelObserver;

public class ParamModel implements IParamModel {
	
	private DefaultTableModel delegate;
	
	private int[] editableColumns = {0, 1, 2, 3, 4, 5, 6};
	
	private static final String settingsTableName = "ParamSettings";
	
	private List<ParamModelObserver> observers = new ArrayList<>();
	
	@SuppressWarnings("serial")
	public ParamModel() {
		// 加载字段
		delegate = new DefaultTableModel() {
			@Override
			public void setValueAt(Object newValue, int row, int column) {
				/*
				 * 字符串格式校验，如果表格中checkBox被编辑了，
				 * 则不修改model
				 */
				if (column > 2 && column < 6 && !(newValue.equals(true) || newValue.equals(false))) {
					return;
				}
				
				if (column == 1 || column == 2 || column == 6) {
					super.setValueAt(Integer.valueOf((String) newValue), row, column);
				}
				else {
					super.setValueAt(newValue, row, column);
				}
				notifyObservers();
			}
			
			@Override
			public boolean isCellEditable(int row, int column) {
				if (editableColumns == null) {
					return false;
				}
				
				for (int i = 0; i < editableColumns.length; i++) {
					if (column == editableColumns[i]) {
						return true;
					}
				}
				return false;
			}
		};
		delegate.addColumn("参数名");
		delegate.addColumn("电压值");
		delegate.addColumn("阈值");
		delegate.addColumn("A");
		delegate.addColumn("H");
		delegate.addColumn("W");
		delegate.addColumn("通道");
		
	}
	
	public void setEditableColumns(int[] columns) {
		editableColumns = columns;
	}
	
	@Override
	public void init(String pathname) throws Exception {
		clear();
		// 从数据库中读取指定表，如果不存在，则创建表
		if (!DAOFactory.getIParamSettingsDAOInstance(pathname).isExist(settingsTableName)) {
			DAOFactory.getIParamSettingsDAOInstance(pathname).createTable(settingsTableName);
		}
		List<ParamSettingsBean> settingsBeans = 
				DAOFactory.getIParamSettingsDAOInstance(pathname).findAll(settingsTableName);
		Iterator<ParamSettingsBean> iter = settingsBeans.iterator();
		while (iter.hasNext()) {
			ParamSettingsBean bean = (ParamSettingsBean) iter.next();
			Object[] rowdata = {
					bean.getParamName(), 
					bean.getVoltage(),
					bean.getThreshold(),
					bean.isA(),
					bean.isH(),
					bean.isW(),
					bean.getChannelId()
					};
			addRow(rowdata);
		}
	}
	
	@Override
	public void clear() {
		int rows = delegate.getRowCount();
		for (int i = rows - 1; i >= 0; i--) {
			delegate.removeRow(i);
		}
	}
	
	@Override
	public void addRow(Object[] rowData) throws Exception {
		delegate.addRow(rowData);
		notifyObservers();
	}
	
	
	@Override
	public void removeRow(int row) throws Exception {
		delegate.removeRow(row);
		notifyObservers();
	}
	
	@Override
	public DefaultTableModel getDelegate() {
		return delegate;
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		delegate.setValueAt(value, row, column);
	}

	@Override
	public int getRowCount() {
		return delegate.getRowCount();
	}

	@Override
	public int getColumnCount() {
		return delegate.getColumnCount();
	}

	@Override
	public void addObserver(ParamModelObserver o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(ParamModelObserver o) {
		observers.remove(o);
	}

	@Override
	public void notifyObservers() {
		observers.stream().forEach(e -> {
			e.paramModelUpdated(getBeans());
		});
	}

	@Override
	public void save(String pathname) throws Exception {
		DAOFactory.getIParamSettingsDAOInstance(pathname).updateAll(settingsTableName, getBeans());
	}
	
	@Override
	public ParamSettingsBean beanAt(int row) {
		@SuppressWarnings("rawtypes")
		Vector v = (Vector) delegate.getDataVector().elementAt(row);
		ParamSettingsBean psBean = new ParamSettingsBean();
		
		psBean.setParamName((String) v.get(0));
		psBean.setVoltage((int) v.get(1));
		psBean.setThreshold((int) v.get(2));
		psBean.setA((boolean) v.get(3));
		psBean.setH((boolean) v.get(4));
		psBean.setW((boolean) v.get(5));
		psBean.setChannelId((int) v.get(6));
		
		return psBean;
	}
	
	private List<ParamSettingsBean> getBeans() {
		List<ParamSettingsBean> beans = new ArrayList<>();
		for (int i = 0; i < delegate.getRowCount(); i++) {
			beans.add(beanAt(i));
		}
		return beans;
	}
}
