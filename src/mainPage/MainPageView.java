package mainPage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import menu.MainPageMenuBar;
import utils.SwingUtils;

@SuppressWarnings("serial")
public class MainPageView extends JFrame {
	
	private MainPageController controller;
	
	private JPanel dirTree;
	private JPanel dashBoard;
	private JPanel paramSettings;
	private JPanel workSheet;
	private JPanel tubeView;
	
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel subLeft;
	private JPanel subRight;
	
	private JMenuBar mainMenuBar;
	
	
	public MainPageView(MainPageController controller) {
		this.controller = controller;
	}
	
	public void createGUI() {
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
		dirTree.setPreferredSize(new Dimension(220, 500));
		leftPanel.add(SwingUtils.createPanelForComponent(dirTree, "项目列表"));
		
		/*
		 * 添加参数列表
		 */
		paramSettings.setPreferredSize(new Dimension(450, 300));
		subLeft.add(SwingUtils.createPanelForComponent(paramSettings, "参数列表"));
		
		/*
		 * 添加dashboard
		 */
		dashBoard.setPreferredSize(new Dimension(450, 250));
		subLeft.add(SwingUtils.createPanelForComponent(dashBoard, "DashBoard"));
		
		/*
		 * 添加tube数据表
		 */
		tubeView.setPreferredSize(new Dimension(450, 200));
		subLeft.add(SwingUtils.createPanelForComponent(tubeView, "Tube数据"));
		
		/*
		 * 添加workSheet
		 */
		subRight.add(SwingUtils.createPanelForComponent(workSheet, "WorkSheet"), BorderLayout.CENTER);
		
		JSplitPane subSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subLeft, subRight);
		subSplit.setOneTouchExpandable(true);
		rightPanel.add(subSplit, BorderLayout.CENTER);
		
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		splitPane.setOneTouchExpandable(true);
		this.add(splitPane, BorderLayout.CENTER);
		
		mainMenuBar = new MainPageMenuBar(controller);
		this.add(mainMenuBar, BorderLayout.NORTH);
		
		this.setVisible(true);
	}

	public void setDirTree(JPanel dirTree) {
		this.dirTree = dirTree;
	}

	public void setDashBoard(JPanel dashBoard) {
		this.dashBoard = dashBoard;
	}

	public void setParamSettings(JPanel paramSettings) {
		this.paramSettings = paramSettings;
	}

	public void setWorkSheet(JPanel workSheet) {
		this.workSheet = workSheet;
	}
	
	public void setTubeView(JPanel tubeView) {
		this.tubeView = tubeView;
	}
}
