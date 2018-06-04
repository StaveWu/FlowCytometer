package paramSettings.interfaces;

import java.util.Vector;

import javax.swing.table.TableModel;

public interface ITableModel {
	
	void init(String pathname) throws Exception;
	
	void addRow(Object[] rowData) throws Exception;
	
	void removeRow(int row) throws Exception;
	
	void setValueAt(Object value, int row, int column);
	
	int getRowCount();
	
	int getColumnCount();
	
	/**
	 * ����������
	 * @return
	 */
	Vector<String> getDataNames();
	
	TableModel getDelegate();
	
	void addObserver(ParamModelObserver o);
	
	void removeObserver(ParamModelObserver o);
	
	void notifyObservers();

}
