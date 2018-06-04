package tube;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

@SuppressWarnings("serial")
public class TubeView extends JPanel implements ActionListener {
	
	private ITubeModel model;
	private ITubeController controller;
	
	public JButton saveButton;
	public JToolBar toolBar;
	public JTable table;
	
	public TubeView(ITubeController controller, ITubeModel model) {
		this.controller = controller;
		this.model = model;
	}
	
	public void initComponents() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		/*
		 * 表格
		 */
		table = new JTable(model.getDelegate());
		JScrollPane scrollPane = new JScrollPane(table);
		this.add(scrollPane, BorderLayout.CENTER);
		
		/*
		 * 工具栏
		 */
		toolBar = new JToolBar();
		
		/*
		 * 按钮
		 */
		saveButton = new JButton("保存");
		saveButton.addActionListener(this);
		toolBar.add(saveButton);
		
		this.add(toolBar, BorderLayout.NORTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controller.save();
	}

}
