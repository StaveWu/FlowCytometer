package tube;

import java.util.Vector;

import javax.swing.table.TableModel;

public interface ITubeModel {
	
	void init(String pathname) throws Exception;

	void save() throws Exception;
	
	void clear();
	
	void addField(String field);
	
	void setFields(Vector<String> fields);
	
	void setFields(String[] fields);
	
	boolean isEmpty();
	
	void addEvent(double[] rowData);
	
	void addEvent(Vector<Double> rowData);
	
	void removeEvent(int row);
	
	void setValueAt(double aValue, int row, int column);
	
	int getEventsCount();
	
	int getFieldsCount();
	
	Vector<String> getFields();
	
	Vector<Double> getEventAt(int row);
	
	double getValueAt(int row, int column);
	
	String getFieldAt(int column);
	
	void addObserver(TubeModelObserver observer);
	
	void removeObserver(TubeModelObserver observer);
	
	void notifyObservers();
	
	TableModel getDelegate();

}
