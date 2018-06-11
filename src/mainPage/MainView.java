package mainPage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
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
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import dao.beans.DirTreeBean;
import dao.beans.ParamSettingsBean;
import dashBoard.CounterPolicy;
import dashBoard.DashBoardController;
import dashBoard.DashBoardModel;
import dashBoard.IDashBoardModelObserver;
import dashBoard.IHandlerTask;
import dashBoard.ITickPolicy;
import dashBoard.TimerPolicy;
import menu.ContainerPopupMenu;
import menu.IContainerPopupMenuCommand;
import menu.IMainMenuBarCommand;
import menu.IProjectTreeCommand;
import menu.MainPageMenuBar;
import menu.PopupMenuX;
import menu.ProjectTreeMenu;
import paramSettings.ParamController;
import paramSettings.ParamModel;
import paramSettings.interfaces.IParamController;
import paramSettings.interfaces.IParamModel;
import paramSettings.interfaces.ParamModelObserver;
import plot.DensityPlot;
import plot.DotPlot;
import plot.Histogram;
import plot.Plot;
import plotContainer.PlotContainer;
import projectTree.IProjectTreeController;
import projectTree.IProjectTreeModel;
import projectTree.NodeType;
import projectTree.ProjectTreeController;
import projectTree.ProjectTreeModel;
import projectTree.ProjectTreeUtils;
import spectrum.SpectrumController;
import spectrum.SpectrumModel;
import spectrum.SpectrumModelObserver;
import tube.ITubeController;
import tube.ITubeModel;
import tube.TubeController;
import tube.TubeModel;
import tube.TubeModelObserver;
import utils.StringUtils;
import utils.SwingUtils;

@SuppressWarnings("serial")
public class MainView extends JFrame implements IMainMenuBarCommand {
	
	private ProjectTreeView projectTreeView;
	private ParamSettingsView paramSettingsView;
	private WorkSheetView workSheetView;
	private DashBoardView dashBoardView;
	private TubeView tubeView;
	
	private JLabel selectedLabel;
	private StatusBar statusBar;
	
	// ���
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel subLeft;
	private JPanel subRight;
	private JMenuBar mainMenuBar;
	
	// ������
	private IProjectTreeController treeController;
	private IParamController paramController;
	private DashBoardController dashBoardController;
	private ITubeController tubeController;
	private SpectrumController spectrumController;
	
	// ģ��
	private IProjectTreeModel treeModel;
	private IParamModel paramModel;
	private DashBoardModel dashBoardModel;
	private ITubeModel tubeModel;
	private SpectrumModel spectrumModel;
	
	public MainView() {
		try {
			createModels();
			createGUI();
			createControllers();
			createStatusMonitor();
		}
		catch (SQLException e) {
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
		tubeModel = new TubeModel();
		dashBoardModel = new DashBoardModel();
		spectrumModel = new SpectrumModel();
	}
	
	private void createControllers() {
		treeController = new ProjectTreeController(treeModel, this);
		paramController = new ParamController(paramModel, this);
		dashBoardController = new DashBoardController(dashBoardModel, paramModel, this);
		tubeController = new TubeController(tubeModel, this);
		spectrumController = new SpectrumController(spectrumModel, this);
	}

	
	private void createGUI() {
		this.setBounds(50, 50, 1600, 900);
//		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
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
		projectTreeView = new ProjectTreeView();
		leftPanel.add(SwingUtils.createPanelForComponent(projectTreeView, "��Ŀ�б�"));
		
		/*
		 * ��Ӳ����б�
		 */
		paramSettingsView = new ParamSettingsView();
		subLeft.add(SwingUtils.createPanelForComponent(paramSettingsView, "�����б�"));
		
		/*
		 * ���dashboard
		 */
		dashBoardView = new DashBoardView();
		subLeft.add(SwingUtils.createPanelForComponent(dashBoardView, "DashBoard"));
		
		/*
		 * ���tube���ݱ�
		 */
		tubeView = new TubeView();
		subLeft.add(SwingUtils.createPanelForComponent(tubeView, "�Թ�����"));
		
		/*
		 * ���workSheet
		 */
		workSheetView = new WorkSheetView();
		subRight.add(SwingUtils.createPanelForComponent(workSheetView, "WorkSheet"), BorderLayout.CENTER);
		
		JSplitPane subSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subLeft, subRight);
		subSplit.setOneTouchExpandable(true);
		rightPanel.add(subSplit, BorderLayout.CENTER);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		splitPane.setOneTouchExpandable(true);
		this.add(splitPane, BorderLayout.CENTER);
		
		// ��Ӳ˵���
		mainMenuBar = new MainPageMenuBar(this);
		this.add(mainMenuBar, BorderLayout.NORTH);
		
		// ���״̬��
		selectedLabel = new JLabel("-");
		statusBar = new StatusBar(
				new Component[] {selectedLabel, Box.createHorizontalGlue()});
		this.add(statusBar, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	
	private void createStatusMonitor() {
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					String text = "";
					if (Session.getSelectedProjectName() != null) {
						text += Session.getSelectedProjectName();
					}
					else {
						text += "-";
					}
					text += " : ";
					if (Session.getSelectedTubeName() != null) {
						text += Session.getSelectedTubeName();
					}
					else {
						text += "-";
					}
					selectedLabel.setText(text);
				}
			}
		});
		t.setDaemon(true);
		t.start();
	}
	
	
	private class ProjectTreeView extends JPanel implements MouseListener, IProjectTreeCommand {
		
		private JTree tree;
		private PopupMenuX treeMenu;
		
		public ProjectTreeView() {
			initComponents();
		}
		
		private void initComponents() {
			this.setLayout(new BorderLayout());
			this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			this.setPreferredSize(new Dimension(220, 500));
			
			tree = new JTree(treeModel.getDelegate());
			tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			tree.addMouseListener(this);
			tree.setInvokesStopCellEditing(true);
			tree.setEditable(true);
			
			JScrollPane scrollPane = new JScrollPane(tree);
			this.add(scrollPane, BorderLayout.CENTER);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
				// ����·�����ļ�
				DefaultMutableTreeNode curSelection = 
						(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (curSelection == null) {
					return;
				}
				
				recordSession(curSelection);
				
				DirTreeBean nodeInfo = (DirTreeBean) curSelection.getUserObject();
				if (nodeInfo.getNodeType() == NodeType.SETTINGS) {
					loadSettings();
				}
				if (nodeInfo.getNodeType() == NodeType.WORKSHEET) {
					loadWorkSheet();
				}
				if (nodeInfo.getNodeType() == NodeType.TUBE) {
					loadTubeData(curSelection);
				}
			}
		}
		
		private void loadSettings() {
			paramController.loadSettings();
		}
		
		private void loadWorkSheet() {
			workSheetView.loadPlots();
			paramController.loadSettings();
		}
		
		private void loadTubeData(DefaultMutableTreeNode node) {
			String pathname = FCMSettings.getWorkSpacePath() + ProjectTreeUtils.getRelaPath(node);
			tubeController.loadData(pathname);
			loadWorkSheet();
		}
		
		private void recordSession(DefaultMutableTreeNode node) {
			if (ProjectTreeUtils.getOwnedProjectLid(node) == null) {
				return;
			}
			// ��¼��ǰѡ�����Ŀ
			Session.setSelectedProjectLid(ProjectTreeUtils.getOwnedProjectLid(node));
			Session.setSelectedProjectName(ProjectTreeUtils.getOwnedProjectName(node));
			
			// ��¼��ǰѡ���Tube
			DirTreeBean nodeinfo = (DirTreeBean) node.getUserObject();
			if (nodeinfo.getNodeType() == NodeType.TUBE) {
				Session.setSelectedTubeLid(nodeinfo.getLid());
				Session.setSelectedTubeName(nodeinfo.getName());
			}
			else {
				Session.setSelectedTubeLid(null);
				Session.setSelectedTubeName(null);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				if (tree.getSelectionPath() == null) {
					return;
				}
				DefaultMutableTreeNode curSelection = 
						(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (treeMenu == null) {
					treeMenu = new ProjectTreeMenu(this);
				}
				treeMenu.setSource(curSelection);
				treeMenu.show(tree, e.getX(), e.getY());
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
		
		public void startEditing(DefaultMutableTreeNode node) {
			tree.startEditingAtPath(new TreePath(node.getPath()));
		}
		
		public void scrollPathToVisible(DefaultMutableTreeNode node) {
			tree.scrollPathToVisible(new TreePath(node.getPath()));
		}

		@Override
		public void createExperimentSolution(DefaultMutableTreeNode parent) {
			treeController.createExperimentSolution(parent);
		}

		@Override
		public void createSpecimen(DefaultMutableTreeNode parent) {
			treeController.createSpecimen(parent);
		}

		@Override
		public void createTube(DefaultMutableTreeNode parent) {
			treeController.createTube(parent);
		}

		@Override
		public void delete(DefaultMutableTreeNode node) {
			// �����صĻỰ��¼
			DirTreeBean info = (DirTreeBean) node.getUserObject();
			boolean isSelectedTube = info.getLid().equals(Session.getSelectedTubeLid());
			boolean includeSelectedTube = Session.getSelectedTubeLid() != null
					&& info.getLid().split("\\.")[0]
					.equals(Session.getSelectedTubeLid().split("\\.")[0]);
			if (isSelectedTube || includeSelectedTube) {
				tubeController.clear();
			}
			
			if (info.getLid().equals(Session.getSelectedProjectLid())) {
				paramController.clear();
				workSheetView.clear();
			}
			
			// ɾ���ڵ�
			treeController.delete(node);
		}

		@Override
		public void rename(DefaultMutableTreeNode node) {
			treeController.rename(node);
		}
	}
	
	private class ParamSettingsView extends JPanel implements MouseListener {

		private JTable paramTable;
		private JButton btnAdd;
		private JButton btnDelete;
		private JButton btnSave;
		private JToolBar toolBar;
		
		private boolean isClicked;
		
		public ParamSettingsView() {
			initComponents();
		}
		
		private void initComponents() {
			this.setLayout(new BorderLayout());
			this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			this.setPreferredSize(new Dimension(450, 300));
			
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
			this.add(scrollPane, BorderLayout.CENTER);
			
			/*
			 * ������Ӱ�ť
			 */
			btnAdd = new JButton("���");
			btnAdd.addMouseListener(this);
			
			/*
			 * ����ɾ����ť
			 */
			btnDelete = new JButton("ɾ��");
			btnDelete.addMouseListener(this);
			
			btnSave = new JButton("����");
			btnSave.addMouseListener(this);
			
			toolBar = new JToolBar();
			toolBar.setPreferredSize(new Dimension(50, 30));
			toolBar.add(btnAdd);
			toolBar.add(btnDelete);
			toolBar.add(btnSave);
			this.add(toolBar, BorderLayout.NORTH);
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getSource() == paramTable) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					isClicked = true;
				}
			}
			if (e.getSource() == btnAdd) {
				if (Session.isOnSampling()) {
					SwingUtils.showErrorDialog(MainView.this, "�޷���ӣ����ڲ�����");
					return;
				}
				
				paramController.addRow();
			}
			if (e.getSource() == btnDelete) {
				if (Session.isOnSampling()) {
					SwingUtils.showErrorDialog(MainView.this, "�޷�ɾ�������ڲ�����");
					return;
				}
				
				paramController.removeRow(paramTable.getSelectedRow());
			}
			
			if (e.getSource() == btnSave) {
				paramController.saveSettings();
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
	}
	
	private class DashBoardView extends JPanel implements ItemListener, ActionListener, 
		IDashBoardModelObserver, ParamModelObserver, IHandlerTask {
		
		private JLabel limitLabel;
		private JComboBox<String> comboBox;
		private JCheckBox checkBox;
		private JButton btnStart;
		private JButton btnStop;
		private JButton btnReset;
		private JLabel timeLabel;
		private JTextField tf;
		private JLabel statusLabel;
		
		private ITickPolicy tickPolicy;
		
		public DashBoardView() {
			initComponents();
			checkSelected();
			dashBoardModel.addObserver(this);
			paramModel.addObserver(this);
		}
		
		private void initComponents() {
			this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			this.setPreferredSize(new Dimension(450, 250));
			
			limitLabel = new JLabel("Լ������:");
			
			comboBox = new JComboBox<>(new String[] {"��ʱ��", "������"});
			comboBox.addItemListener(this);
			
			tf = new JTextField();
			tf.setPreferredSize(new Dimension(100, 26));

			timeLabel = new JLabel("minutes");
			
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
			limitPanel.add(timeLabel);
			limitPanel.add(checkBox);
			this.add(SwingUtils.createPanelForComponent(limitPanel, "Լ��"));
			
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
			
			btnReset = new JButton("ϵͳ��λ");
			btnReset.addActionListener(this);
			
			/*
			 * ��ť���
			 */
			JPanel btnPanel = new JPanel();
			btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
			btnPanel.add(btnStart);
			btnPanel.add(btnStop);
			btnPanel.add(btnReset);
			this.add(SwingUtils.createPanelForComponent(btnPanel, "����"));
			
			/*
			 * ״̬Label
			 */
			statusLabel = new JLabel("��ǰ״̬���޶���");
			statusLabel.setBounds(10, 0, 150, 26);
			JPanel statusPanel = new JPanel();
			statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			statusPanel.add(statusLabel);
			this.add(SwingUtils.createPanelForComponent(statusPanel, "����"));
		}
		
		private void checkSelected() {
			if (baseTime()) {
				timeLabel.setText("minutes");;
				checkBox.setSelected(false);
				checkBox.setEnabled(false);
			} else {
				timeLabel.setText("events");
				checkBox.setEnabled(true);
			}
		}
		
		private boolean baseTime() {
			return comboBox.getSelectedItem().equals("��ʱ��");
		}
		
		private boolean checkNumberSetting() {
			if (!StringUtils.isNumber(tf.getText())) {
				SwingUtils.showErrorDialog(MainView.this, "������趨ֵ��ʽ����ȷ��");
				return false;
			}
			return true;
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			checkSelected();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!dashBoardModel.isConnected()) {
				SwingUtils.showErrorDialog(MainView.this, "���������豸");
				return;
			}
			
			if (e.getSource() == btnStart) {
				if (checkNumberSetting()) {
					dashBoardController.startSampling();
					tickPolicy = getTickPolicy();
					tickPolicy.tick(this);
				}
			} 
			if (e.getSource() == btnStop) {
				dashBoardController.stopSampling();
				tickPolicy.stop();
			}
			if (e.getSource() == btnReset) {
				dashBoardController.resetSystem();
			}
		}
		
		private ITickPolicy getTickPolicy() {
			if (baseTime()) {
				return new TimerPolicy((int)(Double.parseDouble(tf.getText()) * 60));
			}
			else {
				return new CounterPolicy(
						workSheetView.getPlotContainer(), 
						(int)(Double.parseDouble(tf.getText())), checkBox.isSelected());
			}
		}

		@Override
		public void statusChanged(boolean isOnSampling) {
			Session.setOnSampling(isOnSampling);
			if (isOnSampling) {
				statusLabel.setText("��ǰ״̬�����ڲ���...");
				paramController.setSamplingEditableMode();
			}
			else {
				statusLabel.setText("��ǰ״̬������ֹͣ");
				paramController.setNormalEditableMode();
			}
		}

		@Override
		public void paramModelUpdated(List<ParamSettingsBean> beans) {
			dashBoardController.changeVoltage();
			
			if (Session.isOnSampling()) {
				spectrumController.updateSpectrums(beans);
			}
			else {
				spectrumController.refreshSpectrums(beans);
			}
		}

		@Override
		public void dataAvailable(Map<String, Double> data) {
			if (Session.isOnSampling()) {
				spectrumController.addData(data);
			}
		}

		@Override
		public void execute() {
			dashBoardController.stopSampling();
		}
	}
	
	private class WorkSheetView extends JPanel implements MouseListener, IContainerPopupMenuCommand {

		private PlotContainer plotContainer;
		private JToolBar wsToolBar;
		private JButton wsSaveButton;
		private boolean showPopupMenu;
		
		// ��ǰworksheet��������Ŀlid
		private String lid;
		
		public WorkSheetView() {
			initComponents();
			disableEdit();
		}
		
		private void initComponents() {
			this.setLayout(new BorderLayout());
			this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			
			/*
			 * ͼ����
			 */
			plotContainer = new PlotContainer();
//			plotContainer.addObserver(this);
			plotContainer.addMouseListener(this);
			plotContainer.setDataSource(tubeModel);
			plotContainer.setPreferredSize(new Dimension(500, 3000));
			
			/*
			 * Ϊ������ӹ�����
			 */
			JScrollPane scrollPane = new JScrollPane(plotContainer);
			this.add(scrollPane, BorderLayout.CENTER);
			
			wsToolBar = new JToolBar();
			wsToolBar.setPreferredSize(new Dimension(100, 30));
			
			/*
			 * ��ť
			 */
			wsSaveButton = new JButton("����");
			wsSaveButton.addMouseListener(this);
			wsToolBar.add(wsSaveButton);
			
			this.add(wsToolBar, BorderLayout.NORTH);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getSource() == plotContainer) {
				if(e.getButton() == MouseEvent.BUTTON3 && showPopupMenu) {
					Point p = e.getPoint();
					new ContainerPopupMenu(this, p).show(
							plotContainer, p.x, p.y);
				}
			}
			
			if (e.getSource() == wsSaveButton) {
				this.save();
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
		
		public PlotContainer getPlotContainer() {
			return plotContainer;
		}
		
		public void enableEdit() {
			plotContainer.setBackground(Color.WHITE);
			showPopupMenu = true;
			repaint();
		}
		
		public void disableEdit() {
			plotContainer.setBackground(Color.LIGHT_GRAY);
			showPopupMenu = false;
			repaint();
		}
		
		public void clear() {
			lid = null;
			plotContainer.clear();
			repaint();
		}
		
		private boolean isWorkSheetOpenned() {
			return lid != null;
		}
		
		private boolean isProjectSwitched() {
			return lid != null && !lid.equals(Session.getSelectedProjectLid());
		}
		
		public void loadPlots() {
			if (!isWorkSheetOpenned() || isProjectSwitched()) {
				lid = Session.getSelectedProjectLid();
				String pathname = FCMSettings.getWorkSpacePath() 
						+ "/" + Session.getSelectedProjectName() + "/" + "WorkSheet";
				try {
					plotContainer.loadPlots(pathname);
					enableEdit();
				} catch (Exception e) {
					e.printStackTrace();
					SwingUtils.showErrorDialog(MainView.this, e.getMessage());
				}
				repaint();
			}
		}
		
		public void save() {
			if (!isWorkSheetOpenned()) {
				SwingUtils.showErrorDialog(MainView.this, "���ȴ���Ŀ");
				return;
			}
			String pathname = FCMSettings.getWorkSpacePath() 
					+ "/" + Session.getSelectedProjectName() + "/" + "WorkSheet";
			try {
				plotContainer.savePlots(pathname);
			} catch (Exception e) {
				e.printStackTrace();
				SwingUtils.showErrorDialog(MainView.this, "����WorkSheetʧ�ܣ��쳣��Ϣ��" + e.getMessage());
			}
		}
		
		public void addPlot(Plot plot) {
			plotContainer.add(plot);
			repaint();
		}

		@Override
		public void save(Point location) {
			try {
				save();
			} catch (Exception e) {
				e.printStackTrace();
				SwingUtils.showErrorDialog(MainView.this, "����ʧ�ܣ��쳣��Ϣ��" + e.getMessage());
			}
		}

		@Override
		public void createDotPlot(Point location) {
			Plot dotPlot = new DotPlot();
			dotPlot.setLocation(location);
			addPlot(dotPlot);
			repaint();
		}

		@Override
		public void createHistogram(Point location) {
			Plot histogram = new Histogram();
			histogram.setLocation(location);
			addPlot(histogram);
			repaint();
		}

		@Override
		public void createDensityPlot(Point location) {
			Plot densityPlot = new DensityPlot();
			densityPlot.setLocation(location);
			addPlot(densityPlot);
			repaint();
		}
	}
	
	
	private class TubeView extends JPanel implements ActionListener, 
		ParamModelObserver, SpectrumModelObserver, TubeModelObserver {
		
		public JButton tubeSaveButton;
		public JToolBar tubeToolBar;
		public JTable tubeTable;
		
		public TubeView() {
			initComponents();
			paramModel.addObserver(this);
			spectrumModel.addObserver(this);
		}
		
		private void initComponents() {
			
			this.setLayout(new BorderLayout());
			this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			this.setPreferredSize(new Dimension(450, 200));
			
			/*
			 * ���
			 */
			tubeTable = new JTable(tubeModel.getDelegate());
			JScrollPane scrollPane = new JScrollPane(tubeTable);
			this.add(scrollPane, BorderLayout.CENTER);
			
			tubeToolBar = new JToolBar();
			tubeToolBar.setPreferredSize(new Dimension(50, 30));
			
			/*
			 * ��ť
			 */
			tubeSaveButton = new JButton("����");
			tubeSaveButton.addActionListener(this);
			tubeToolBar.add(tubeSaveButton);
			
			this.add(tubeToolBar, BorderLayout.NORTH);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			tubeController.save();
		}

		@Override
		public void paramModelUpdated(List<ParamSettingsBean> beans) {
			Vector<String> dataNames = new Vector<>();
			for (ParamSettingsBean ele : beans) {
				String partA = ele.getParamName();
				String partB = "";
				if (ele.isA()) {
					partB = "A";
				}
				else if (ele.isH()) {
					partB = "H";
				}
				else if (ele.isW()) {
					partB = "W";
				}
				dataNames.add(partA + "-" + partB);
			}
			tubeController.setFields(dataNames);
			
		}

		@Override
		public void newEventGenerated(List<Vector<Double>> data) {
			tubeController.addEvents(data);
		}

		@Override
		public void refresh() {
			this.repaint();
		}
	}
	
	
	public void startEditing(DefaultMutableTreeNode node) {
		projectTreeView.startEditing(node);
	}
	
	public void scrollPathToVisible(DefaultMutableTreeNode node) {
		projectTreeView.scrollPathToVisible(node);
	}
	
	public void enableEdit() {
		workSheetView.enableEdit();
	}
	
	public void disableEdit() {
		workSheetView.disableEdit();
	}

	@Override
	public void ImportProject() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SaveAll() {
		paramController.saveSettings();
		tubeController.save();
		workSheetView.save();
	}

	@Override
	public void ChangeWorkspace() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Exit() {
		System.exit(0);
	}

	@Override
	public void CommDeviceSetting() {
		new PortSettingBox(dashBoardModel);
	}

	@Override
	public void Version() {
		new VersionBox();
	}

	@Override
	public void Help() {
		// TODO Auto-generated method stub
		
	}

}
