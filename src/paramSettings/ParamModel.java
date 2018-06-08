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
				if (column > 2 && !(newValue.equals(true) || newValue.equals(false))) {
					return;
				}
				try {
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
	public void save(String pathname) throws Exception {
		
		List<ParamSettingsBean> beans = new ArrayList<>();
		for (int i = 0; i < delegate.getRowCount(); i++) {
			
			@SuppressWarnings("rawtypes")
			Vector v = (Vector) delegate.getDataVector().elementAt(i);
			ParamSettingsBean psBean = new ParamSettingsBean();
			
			psBean.setParamName((String) v.get(0));
			psBean.setVoltage((int) v.get(1));
			psBean.setThreshold((int) v.get(2));
			psBean.setA((boolean) v.get(3));
			psBean.setH((boolean) v.get(4));
			psBean.setW((boolean) v.get(5));
			
			beans.add(psBean);
			
		}
		
		DAOFactory.getIParamSettingsDAOInstance(pathname).updateAll(settingsTableName, beans);
	}
}
