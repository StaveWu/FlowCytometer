package paramSettings;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import paramSettings.interfaces.IParamController;
import paramSettings.interfaces.ITableModel;

@SuppressWarnings("serial")
public class ParamView extends JPanel implements ActionListener, MouseListener {
	
	private ITableModel model;
	private IParamController controller;
	
	//组件
	private JTable table;
	private JButton btnAdd;
	private JButton btnDelete;
	
	private boolean isClicked = false;
	
	public ParamView(ITableModel model, IParamController controller) {
		this.controller = controller;
		this.model = model;
	}
	
	public void initializeComponent() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		/*
		 * 参数表格
		 */
		table = new JTable(model.getDelegate());
		table.addMouseListener(this);
		
		Map<Integer, Component> checkBoxsMap = new HashMap<>();
		checkBoxsMap.put(3, new JCheckBox());
		checkBoxsMap.put(4, new JCheckBox());
		checkBoxsMap.put(5, new JCheckBox());
		
		Iterator<Integer> keys = checkBoxsMap.keySet().iterator();
		while (keys.hasNext()) {
			int key = (int) keys.next();
			Component comp = checkBoxsMap.get(key);
			if (comp instanceof JCheckBox) {
				table.getColumnModel().getColumn(key).setCellRenderer(new TableCellRenderer() {
					
					@Override
					public Component getTableCellRendererComponent(JTable table, Object value, 
							boolean isSelected, boolean hasFocus, int row,
							int column) {
						
						JCheckBox cb = (JCheckBox) comp;
						cb.setHorizontalAlignment((int) 0.5f);
						//检查参数值是否为布尔值
						if(value.equals(true) || value.equals(false)) {
							boolean bv = (boolean)value;
							// 鼠标每次点击的时候检查倒数三列的某一个单元格是否被聚焦
							if(isSelected && hasFocus && isClicked) {
								//同一行的checkbox有且只有一个被聚焦
								controller.modifySelection(row, column, checkBoxsMap);
								isClicked = false;
							}
							cb.setSelected(bv);
						}
						return cb;
					}
				});
			}
		}
		
		/*
		 * 为表格添加滚动条
		 */
		JScrollPane scrollPane = new JScrollPane(table);
		this.add(scrollPane);
		
		/*
		 * 参数添加按钮
		 */
		btnAdd = new JButton("添加");
		btnAdd.addActionListener(this);
		btnAdd.setPreferredSize(new Dimension(100, 26));
		
		/*
		 * 参数删除按钮
		 */
		btnDelete = new JButton("删除");
		btnDelete.addActionListener(this);
		btnDelete.setPreferredSize(new Dimension(100, 26));
		
		/*
		 * FlowLayout布局
		 */
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 10));
		btnPanel.add(btnAdd);
		btnPanel.add(btnDelete);
		
		this.add(btnPanel, BorderLayout.SOUTH);
	}
	
	public void hideButtons() {
		btnAdd.setVisible(false);
		btnDelete.setVisible(false);
		repaint();
	}
	
	public void displayButtons() {
		btnAdd.setVisible(true);
		btnDelete.setVisible(true);
		repaint();
	}
	
	public void disableTable() {
		table.setEnabled(false);
	}
	
	public void enableTable() {
		table.setEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == btnAdd) {
			controller.addRow();
		} else if(ev.getSource() == btnDelete) {
			if(table.getSelectedRow() != -1) {
				controller.removeRow(table.getSelectedRow());
			}
		} else {
			throw new RuntimeException("参数界面响应的事件跟按钮无关！");
		}
	}

	@Override
	public void mouseClicked(MouseEvent ev) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent ev) {
		if (ev.getButton() == MouseEvent.BUTTON1) {
			isClicked = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
