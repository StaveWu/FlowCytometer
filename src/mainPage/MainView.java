package mainPage;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import menu.MainPageMenuBar;
import menu.PopupMenuX;
import menu.ProjectTreeMenu;
import paramSettings.ParamController;
import paramSettings.ParamModel;
import paramSettings.interfaces.IParamController;
import paramSettings.interfaces.ITableModel;
import plotContainer.PlotContainer;
import projectTree.IProjectTreeController;
import projectTree.IProjectTreeModel;
import projectTree.ProjectTreeController;
import projectTree.ProjectTreeModel;
import projectTree.ProjectTreeView;
import utils.SwingUtils;
import worksheet.WorkSheetView;

@SuppressWarnings("serial")
public class MainView extends JFrame implements ActionListener, MouseListener {
	
	// 项目树组件
	private JTree tree;
	private PopupMenuX treeMenu;
	
	// 参数设置组件
	private JTable paramTable;
	private JButton btnAdd;
	private JButton btnDelete;
	private boolean isClicked;
	
	// dashboard组件
	private JLabel limitLabel;
	private JComboBox<String> comboBox;
	private JCheckBox checkBox;
	private JButton btnStart;
	private JButton btnStop;
	private JLabel unitLabel;
	private JTextField tf;
	private JLabel statusLabel;
	
	// worksheet组件
	private PlotContainer plotContainer;
	private JToolBar wsToolBar;
	private JButton wsSaveButton;
	
	// tube组件
	public JButton tubeSaveButton;
	public JToolBar tubeToolBar;
	public JTable tubeTable;
	
	// 框架
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel subLeft;
	private JPanel subRight;
	private JMenuBar mainMenuBar;
	
	// 控制器
	private IProjectTreeController treeController;
	private IParamController paramController;
	
	// 模型
	private IProjectTreeModel treeModel;
	private ITableModel paramModel;
	
	public MainView() {
		try {
			createModels();
			createControllers();
			createGUI();
		} catch (SQLException e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(this, e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		new MainView();
	}
	
	private void createModels() throws SQLException {
		treeModel = new ProjectTreeModel();
		paramModel = new ParamModel();
	}
	
	private void createControllers() {
		treeController = new ProjectTreeController(treeModel);
		paramController = new ParamController(paramModel);
	}

	
	private void createGUI() {
//		this.setBounds(50, 50, 1800, 500);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setLocation(0, 0);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout());
		
		/*
		 * 先将整个界面划分为左右两半
		 */
		leftPanel = SwingUtils.createVerticalBoxPanel();
		rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		
		/*
		 * 右边一半再对半分
		 */
		subLeft = SwingUtils.createVerticalBoxPanel();
		subRight = new JPanel();
		subRight.setLayout(new BorderLayout());
		
		/*
		 * 添加项目列表
		 */
		leftPanel.add(createProjectTreePanel());
		
		/*
		 * 添加参数列表
		 */
		subLeft.add(createParamSettingsPanel());
		
		/*
		 * 添加dashboard
		 */
//		subLeft.add(createDashBoardPanel());
		
		/*
		 * 添加tube数据表
		 */
//		subLeft.add(createTubePanel());
		
		/*
		 * 添加workSheet
		 */
//		subRight.add(createWorksheetPanel(), BorderLayout.CENTER);
		
		JSplitPane subSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subLeft, subRight);
		subSplit.setOneTouchExpandable(true);
		rightPanel.add(subSplit, BorderLayout.CENTER);
		
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		splitPane.setOneTouchExpandable(true);
		this.add(splitPane, BorderLayout.CENTER);
		
//		mainMenuBar = new MainPageMenuBar(controller);
//		this.add(mainMenuBar, BorderLayout.NORTH);
		
		this.setVisible(true);
	}
	
	public JPanel createProjectTreePanel() {
		JPanel res = new JPanel();
		res.setLayout(new BorderLayout());
		res.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		res.setPreferredSize(new Dimension(220, 500));
		
		tree = new JTree(treeModel.getDelegate());
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addMouseListener(this);
		tree.setInvokesStopCellEditing(true);
		tree.setEditable(true);
		
		JScrollPane scrollPane = new JScrollPane(tree);
		res.add(scrollPane, BorderLayout.CENTER);
		
		treeMenu = new ProjectTreeMenu(treeController);
		return SwingUtils.createPanelForComponent(res, "项目列表");
	}
	
	public JPanel createParamSettingsPanel() {
		JPanel res = new JPanel();
		res.setLayout(new BorderLayout());
		res.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		res.setPreferredSize(new Dimension(450, 300));
		
		/*
		 * 参数表格
		 */
		paramTable = new JTable(paramModel.getDelegate());
		paramTable.addMouseListener(this);
		
		Map<Integer, Component> checkBoxsMap = new HashMap<>();
		checkBoxsMap.put(3, new JCheckBox());
		checkBoxsMap.put(4, new JCheckBox());
		checkBoxsMap.put(5, new JCheckBox());
		
		Iterator<Integer> keys = checkBoxsMap.keySet().iterator();
		while (keys.hasNext()) {
			int key = (int) keys.next();
			Component comp = checkBoxsMap.get(key);
			if (comp instanceof JCheckBox) {
				paramTable.getColumnModel().getColumn(key).setCellRenderer(new TableCellRenderer() {
					
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
								paramController.modifySelection(row, column, checkBoxsMap);
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
		JScrollPane scrollPane = new JScrollPane(paramTable);
		res.add(scrollPane);
		
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
		
		res.add(btnPanel, BorderLayout.SOUTH);
		return SwingUtils.createPanelForComponent(res, "参数列表");
	}
	
	public JPanel createDashBoardPanel() {
		JPanel res = new JPanel();
		res.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		res.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		res.setPreferredSize(new Dimension(450, 250));
		
		/*
		 * 约束条件
		 */
		limitLabel = new JLabel("约束条件:");
		
		/*
		 * 下拉框
		 */
		comboBox = new JComboBox<>(new String[] {"按时间", "按个数"});
		comboBox.addItemListener(this);
		
		/*
		 * 文本域
		 */
		tf = new JTextField();
		tf.setPreferredSize(new Dimension(100, 26));

		/*
		 * 单位
		 */
		unitLabel = new JLabel("hours");
		
		/*
		 * checkBox
		 */
		checkBox = new JCheckBox("筛选");
		
		/*
		 * 约束面板
		 */
		JPanel limitPanel = new JPanel();
		limitPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		limitPanel.setPreferredSize(new Dimension(450, 60));
		limitPanel.add(limitLabel);
		limitPanel.add(comboBox);
		limitPanel.add(tf);
		limitPanel.add(unitLabel);
		limitPanel.add(checkBox);
		res.add(SwingUtils.createPanelForComponent(limitPanel, "约束"));
		
		/*
		 * 开始采样按钮
		 */
		btnStart = new JButton("开始采样");
		btnStart.addActionListener(this);
		/*
		 * 停止采样按钮
		 */
		btnStop = new JButton("停止采样");
		btnStop.addActionListener(this);
		
		/*
		 * 按钮面板
		 */
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
		btnPanel.add(btnStart);
		btnPanel.add(btnStop);
		res.add(SwingUtils.createPanelForComponent(btnPanel, "操作"));
		
		/*
		 * 状态Label
		 */
		statusLabel = new JLabel("当前状态：无动作");
		statusLabel.setBounds(10, 0, 150, 26);
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		statusPanel.add(statusLabel);
		res.add(SwingUtils.createPanelForComponent(statusPanel, "监视"));
		
		return SwingUtils.createPanelForComponent(res, "DashBoard");
	}
	
	public JPanel createWorksheetPanel() {
		JPanel res = new JPanel();
		
		res.setLayout(new BorderLayout());
		res.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		/*
		 * 图容器
		 */
		plotContainer = new PlotContainer();
		plotContainer.addObserver(this);
		plotContainer.addMouseListener(new PopupMenuListener());
		plotContainer.setPreferredSize(new Dimension(500, 3000));
		
		/*
		 * 为容器添加滚动条
		 */
		JScrollPane scrollPane = new JScrollPane(plotContainer);
		res.add(scrollPane, BorderLayout.CENTER);
		
		wsToolBar = new JToolBar();
		
		/*
		 * 按钮
		 */
		wsSaveButton = new JButton("保存");
		wsSaveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					save();
				} catch (Exception e1) {
					e1.printStackTrace();
					SwingUtils.showErrorDialog(WorkSheetView.this, "保存失败！" + e1.getMessage());
				}
			}
		});
		wsToolBar.add(wsSaveButton);
		
		res.add(wsToolBar, BorderLayout.NORTH);
		
		return SwingUtils.createPanelForComponent(res, "WorkSheet");
	}
	
	public JPanel createTubePanel() {
		JPanel res = new JPanel();
		res.setLayout(new BorderLayout());
		res.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		res.setPreferredSize(new Dimension(450, 200));
		
		/*
		 * 表格
		 */
		tubeTable = new JTable(model.getDelegate());
		JScrollPane scrollPane = new JScrollPane(tubeTable);
		res.add(scrollPane, BorderLayout.CENTER);
		
		tubeToolBar = new JToolBar();
		
		/*
		 * 按钮
		 */
		tubeSaveButton = new JButton("保存");
		tubeSaveButton.addActionListener(this);
		tubeToolBar.add(tubeSaveButton);
		
		res.add(tubeToolBar, BorderLayout.NORTH);
		
		return SwingUtils.createPanelForComponent(res, "TubeData");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == tree) {
			if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
				// 根据路径打开文件
				DefaultMutableTreeNode curSelection = 
						(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (curSelection == null) {
					return;
				}
				// ---------------------------------
//				controller.openNode(curSelection);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == tree) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				if (tree.getSelectionPath() == null) {
					return;
				}
				DefaultMutableTreeNode curSelection = 
						(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				treeMenu.setSource(curSelection);
				treeMenu.show(tree, e.getX(), e.getY());
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
