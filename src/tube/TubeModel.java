package tube;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import utils.StringUtils;


public class TubeModel implements ITubeModel {
	
	private DefaultTableModel delegate;
	
	private String pathname;
	
	private List<TubeModelObserver> observers = new ArrayList<>();
	
	@SuppressWarnings("serial")
	public TubeModel() {
		delegate = new DefaultTableModel() {
			@Override
			public void setValueAt(Object aValue, int row, int column) {
				double data = 0.0;
				if (aValue instanceof String) {
					data = Double.valueOf((String) aValue);
				}
				// 通知plots刷新
				notifyObservers();
				super.setValueAt(data, row, column);
			}
		};
	}

	@Override
	public void init(String pathname) throws Exception {
		clear();
		this.pathname = pathname;
		if (StringUtils.getExtension(pathname).equals("txt") 
				|| StringUtils.getExtension(pathname).equals("out")) {
			initFromTxt(pathname);
		}
		else if (StringUtils.getExtension(pathname).equals("fcs")) {
			initFromFcs(pathname);
		}
	}

	private void initFromTxt(String pathname) throws Exception {
		clear();
		BufferedReader in = new BufferedReader(new FileReader(new File(pathname)));
		String s;
		int i = 0;
		while((s = in.readLine()) != null) {
			String[] str = s.split("\t");
			if(i == 0) {
				for (String ele : str) {
					delegate.addColumn(ele);
				}
			}
			else {
				Vector<Double> rowdata = new Vector<>();
				for (String ele : str) {
					rowdata.add(Double.parseDouble(ele));
				}
				delegate.addRow(rowdata);
			}
			i++;
		}
		in.close();
		notifyObservers();
	}
	
	private void initFromFcs(String pathname) throws Exception {
		throw new NoSuchMethodException("不支持fcs解析");
	}
	
	@Override
	public void save() throws Exception {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(new File(pathname))));
		if (isEmpty()) {
			out.print("");
		} else {
			// 打印数据名
			for (int i = 0; i < getFieldsCount(); i++) {
				out.print(getFieldAt(i));
				if(i != getFieldsCount() - 1) {
					out.print("\t");
				}
			}
			out.println();
			// 打印数据
			for (int j = 0; j < getEventsCount(); j++) {			//j行
				for (int k = 0; k < getFieldsCount(); k++) {		//k列
					Vector<Double> event = getEventAt(j);
					out.print(event.get(k));
					if(k != getFieldsCount() - 1) {
						out.print("\t");
					}
				}
				out.println();
			}
		}
		out.close();
	}
	
	@Override
	public void clear() {
		// 清空所有数据
		Vector<Double> vector = null;
		delegate.setColumnIdentifiers(vector);
		notifyObservers();
	}

	@Override
	public void addField(String field) {
		delegate.addColumn(field);
		notifyObservers();
	}

	@Override
	public boolean isEmpty() {
		return getFieldsCount() == 0 && getEventsCount() == 0;
	}

	@Override
	public void addEvent(double[] rowData) {
		Object[] objs = new Object[rowData.length];
		for (int i = 0; i < objs.length; i++) {
			objs[i] = rowData[i];
		}
		delegate.addRow(objs);
		notifyObservers();
	}
	
	@Override
	public void addEvent(Vector<Double> rowData) {
		delegate.addRow(rowData);
		notifyObservers();
	}

	@Override
	public void removeEvent(int row) {
		delegate.removeRow(row);
		notifyObservers();
	}

	@Override
	public void setValueAt(double aValue, int row, int column) {
		delegate.setValueAt(aValue, row, column);
		
	}

	@Override
	public int getEventsCount() {
		return delegate.getRowCount();
	}

	@Override
	public int getFieldsCount() {
		return delegate.getColumnCount();
	}

	@Override
	public Vector<String> getFields() {
		Vector<String> res = new Vector<>();
		for (int i = 0; i < getFieldsCount(); i++) {
			res.add(getFieldAt(i));
		}
		return res;
	}

	@Override
	public Vector<Double> getEventAt(int row) {
		Vector<Double> res = new Vector<>();
		for (int i = 0; i < getFieldsCount(); i++) {
			res.add(getValueAt(row, i));
		}
		return res;
	}

	@Override
	public double getValueAt(int row, int column) {
		return (double) delegate.getValueAt(row, column);
	}

	@Override
	public String getFieldAt(int column) {
		return delegate.getColumnName(column);
	}

	@Override
	public void setFields(Vector<String> fields) {
		delegate.setColumnIdentifiers(fields);
		notifyObservers();
	}

	@Override
	public void setFields(String[] fields) {
		delegate.setColumnIdentifiers(fields);
		notifyObservers();
	}

	@Override
	public void addObserver(TubeModelObserver observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(TubeModelObserver observer) {
		if (observers.contains(observer)) {
			observers.remove(observer);
		}
	}

	@Override
	public void notifyObservers() {
		for (TubeModelObserver ele : observers) {
			ele.refresh();
		}
	}

	@Override
	public TableModel getDelegate() {
		return delegate;
	}


}
