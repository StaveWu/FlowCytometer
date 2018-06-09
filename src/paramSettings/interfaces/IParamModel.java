package paramSettings.interfaces;

import javax.swing.table.TableModel;

import dao.beans.ParamSettingsBean;

public interface IParamModel {
	
	void init(String pathname) throws Exception;
	
	void save(String pathname) throws Exception;
	
	void clear();
	
	void addRow(Object[] rowData) throws Exception;
	
	void removeRow(int row) throws Exception;
	
	void setValueAt(Object value, int row, int column);
	
	ParamSettingsBean beanAt(int row);
	
	int getRowCount();
	
	int getColumnCount();
	
	TableModel getDelegate();
	
	void addObserver(ParamModelObserver o);
	
	void removeObserver(ParamModelObserver o);
	
	void notifyObservers();

}
