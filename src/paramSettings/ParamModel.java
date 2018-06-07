package paramSettings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import dao.DAOFactory;
import dao.GroupCondition;
import dao.beans.ParamSettingsBean;
import paramSettings.interfaces.ITableModel;
import paramSettings.interfaces.ParamModelObserver;

public class ParamModel implements ITableModel, IIntensityObserver {
	
	private DefaultTableModel delegate;
	
	private static final String settingsTableName = "ParamSettings";
	private String pathname;
	
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
				if (column > 2 && !(newValue.equals(true) || newValue.equals(false))) {
					return;
				}
				try {
					updateTable(newValue, row, column);
					super.setValueAt(newValue, row, column);
					notifyObservers();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		delegate.addColumn("参数名");
		delegate.addColumn("电压值");
		delegate.addColumn("阈值");
		delegate.addColumn("A");
		delegate.addColumn("H");
		delegate.addColumn("W");
		
	}
	
	@Override
	public void init(String pathname) throws Exception {
		clear();
		// 从数据库中读取指定表，如果不存在，则创建表
		this.pathname = pathname;
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
					bean.isW()
					};
			delegate.addRow(rowdata);
		}
	}
	
	private void clear() {
		int rows = delegate.getRowCount();
		for (int i = rows - 1; i >= 0; i--) {
			delegate.removeRow(i);
		}
	}
	
	@Override
	public void addRow(Object[] rowData) throws Exception {
		// 数据库添加
		ParamSettingsBean psBean = new ParamSettingsBean();
		
		psBean.setParamName((String) rowData[0]);
		psBean.setVoltage((int) rowData[1]);
		psBean.setThreshold((int) rowData[2]);
		psBean.setA((boolean) rowData[3]);
		psBean.setH((boolean) rowData[4]);
		psBean.setW((boolean) rowData[5]);
		
		boolean isAdded = DAOFactory.getIParamSettingsDAOInstance(pathname).
				addParamSetting(settingsTableName, psBean);
		// 委托对象添加行
		if (isAdded) {
			delegate.addRow(rowData);
		}
		
	}
	
	
	@Override
	public void removeRow(int row) throws Exception {
		// 数据库删除指定行
		List<Integer> ids = DAOFactory.getIParamSettingsDAOInstance(pathname).findId(settingsTableName);
		DAOFactory.getIParamSettingsDAOInstance(pathname).deleteParamSetting(settingsTableName, ids.get(row));
		// 委托对象删除指定行
		delegate.removeRow(row);
	}
	
	@Override
	public DefaultTableModel getDelegate() {
		return delegate;
	}
	
	private String getDataName(int row) {
		try {
			String partA = (String) delegate.getValueAt(row, 0);
			String partB = "";
			for (int i = 3; i < 6; i++) {
				boolean flag = (boolean) delegate.getValueAt(row, i);
				if (flag) {
					switch (i) {
					case 3:
						partB = "A";
						break;
					case 4:
						partB = "H";
						break;
					case 5:
						partB = "W";
						break;
					default:
						break;
					}
					break;
				}
			}
			return partA + "-" + partB;
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
		
	}
	
	@Override
	public Vector<String> getDataNames() {
		Vector<String> res = new Vector<>();
		int rowCount = delegate.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			res.add(getDataName(i));
		}
		return res;
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		delegate.setValueAt(value, row, column);
	}
	
	private void updateTable(Object value, int row, int column) throws Exception {
		// 更新数据库
		String newValue = String.valueOf(value);
		List<GroupCondition> gcs = new ArrayList<>();
		gcs.add(new GroupCondition(delegate.getColumnName(column), "=", newValue));
		List<Integer> ids = DAOFactory.getIParamSettingsDAOInstance(pathname).findId(settingsTableName);
		DAOFactory.getIParamSettingsDAOInstance(pathname).update(settingsTableName, gcs, ids.get(row));
		
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
		observers.stream().forEach(e -> e.updated());
	}

	@Override
	public void intensitiesMayChanged(Map<String, double[]> intensities) {
		
	}
}
