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
	
	// ��Ŀ�����
	private JTree tree;
	private PopupMenuX treeMenu;
	
	// �����������
	private JTable paramTable;
	private JButton btnAdd;
	private JButton btnDelete;
	private boolean isClicked;
	
	// dashboard���
	private JLabel limitLabel;
	private JComboBox<String> comboBox;
	private JCheckBox checkBox;
	private JButton btnStart;
	private JButton btnStop;
	private JLabel unitLabel;
	private JTextField tf;
	private JLabel statusLabel;
	
	// worksheet���
	private PlotContainer plotContainer;
	private JToolBar wsToolBar;
	private JButton wsSaveButton;
	
	// tube���
	public JButton tubeSaveButton;
	public JToolBar tubeToolBar;
	public JTable tubeTable;
	
	// ���
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel subLeft;
	private JPanel subRight;
	private JMenuBar mainMenuBar;
	
	// ������
	private IProjectTreeController treeController;
	private IParamController paramController;
	
	// ģ��
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
		 * �Ƚ��������滮��Ϊ��������
		 */
		leftPanel = SwingUtils.createVerticalBoxPanel();
		rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		
		/*
		 * �ұ�һ���ٶ԰��
		 */
		subLeft = SwingUtils.createVerticalBoxPanel();
		subRight = new JPanel();
		subRight.setLayout(new BorderLayout());
		
		/*
		 * �����Ŀ�б�
		 */
		leftPanel.add(createProjectTreePanel());
		
		/*
		 * ��Ӳ����б�
		 */
		subLeft.add(createParamSettingsPanel());
		
		/*
		 * ���dashboard
		 */
//		subLeft.add(createDashBoardPanel());
		
		/*
		 * ���tube���ݱ�
		 */
//		subLeft.add(createTubePanel());
		
		/*
		 * ���workSheet
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
		return SwingUtils.createPanelForComponent(res, "��Ŀ�б�");
	}
	
	public JPanel createParamSettingsPanel() {
		JPanel res = new JPanel();
		res.setLayout(new BorderLayout());
		res.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		res.setPreferredSize(new Dimension(450, 300));
		
		/*
		 * �������
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
						//������ֵ�Ƿ�Ϊ����ֵ
						if(value.equals(true) || value.equals(false)) {
							boolean bv = (boolean)value;
							// ���ÿ�ε����ʱ���鵹�����е�ĳһ����Ԫ���Ƿ񱻾۽�
							if(isSelected && hasFocus && isClicked) {
								//ͬһ�е�checkbox����ֻ��һ�����۽�
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
		 * Ϊ�����ӹ�����
		 */
		JScrollPane scrollPane = new JScrollPane(paramTable);
		res.add(scrollPane);
		
		/*
		 * ������Ӱ�ť
		 */
		btnAdd = new JButton("���");
		btnAdd.addActionListener(this);
		btnAdd.setPreferredSize(new Dimension(100, 26));
		
		/*
		 * ����ɾ����ť
		 */
		btnDelete = new JButton("ɾ��");
		btnDelete.addActionListener(this);
		btnDelete.setPreferredSize(new Dimension(100, 26));
		
		/*
		 * FlowLayout����
		 */
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 10));
		btnPanel.add(btnAdd);
		btnPanel.add(btnDelete);
		
		res.add(btnPanel, BorderLayout.SOUTH);
		return SwingUtils.createPanelForComponent(res, "�����б�");
	}
	
	public JPanel createDashBoardPanel() {
		JPanel res = new JPanel();
		res.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		res.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		res.setPreferredSize(new Dimension(450, 250));
		
		/*
		 * Լ������
		 */
		limitLabel = new JLabel("Լ������:");
		
		/*
		 * ������
		 */
		comboBox = new JComboBox<>(new String[] {"��ʱ��", "������"});
		comboBox.addItemListener(this);
		
		/*
		 * �ı���
		 */
		tf = new JTextField();
		tf.setPreferredSize(new Dimension(100, 26));

		/*
		 * ��λ
		 */
		unitLabel = new JLabel("hours");
		
		/*
		 * checkBox
		 */
		checkBox = new JCheckBox("ɸѡ");
		
		/*
		 * Լ�����
		 */
		JPanel limitPanel = new JPanel();
		limitPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		limitPanel.setPreferredSize(new Dimension(450, 60));
		limitPanel.add(limitLabel);
		limitPanel.add(comboBox);
		limitPanel.add(tf);
		limitPanel.add(unitLabel);
		limitPanel.add(checkBox);
		res.add(SwingUtils.createPanelForComponent(limitPanel, "Լ��"));
		
		/*
		 * ��ʼ������ť
		 */
		btnStart = new JButton("��ʼ����");
		btnStart.addActionListener(this);
		/*
		 * ֹͣ������ť
		 */
		btnStop = new JButton("ֹͣ����");
		btnStop.addActionListener(this);
		
		/*
		 * ��ť���
		 */
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
		btnPanel.add(btnStart);
		btnPanel.add(btnStop);
		res.add(SwingUtils.createPanelForComponent(btnPanel, "����"));
		
		/*
		 * ״̬Label
		 */
		statusLabel = new JLabel("��ǰ״̬���޶���");
		statusLabel.setBounds(10, 0, 150, 26);
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		statusPanel.add(statusLabel);
		res.add(SwingUtils.createPanelForComponent(statusPanel, "����"));
		
		return SwingUtils.createPanelForComponent(res, "DashBoard");
	}
	
	public JPanel createWorksheetPanel() {
		JPanel res = new JPanel();
		
		res.setLayout(new BorderLayout());
		res.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		/*
		 * ͼ����
		 */
		plotContainer = new PlotContainer();
		plotContainer.addObserver(this);
		plotContainer.addMouseListener(new PopupMenuListener());
		plotContainer.setPreferredSize(new Dimension(500, 3000));
		
		/*
		 * Ϊ������ӹ�����
		 */
		JScrollPane scrollPane = new JScrollPane(plotContainer);
		res.add(scrollPane, BorderLayout.CENTER);
		
		wsToolBar = new JToolBar();
		
		/*
		 * ��ť
		 */
		wsSaveButton = new JButton("����");
		wsSaveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					save();
				} catch (Exception e1) {
					e1.printStackTrace();
					SwingUtils.showErrorDialog(WorkSheetView.this, "����ʧ�ܣ�" + e1.getMessage());
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
		 * ���
		 */
		tubeTable = new JTable(model.getDelegate());
		JScrollPane scrollPane = new JScrollPane(tubeTable);
		res.add(scrollPane, BorderLayout.CENTER);
		
		tubeToolBar = new JToolBar();
		
		/*
		 * ��ť
		 */
		tubeSaveButton = new JButton("����");
		tubeSaveButton.addActionListener(this);
		tubeToolBar.add(tubeSaveButton);
		
		res.add(tubeToolBar, BorderLayout.NORTH);
		
		return SwingUtils.createPanelForComponent(res, "TubeData");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == tree) {
			if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
				// ����·�����ļ�
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
