package mainPage;

import javax.swing.JPanel;

import dashBoard.DashBoardController;
import dashBoard.DashBoardModel;
import mainPage.events.DashBoardEvent;
import mainPage.events.DirTreeEvent;
import mainPage.events.ParamSettingsEvent;
import mainPage.events.WorkSheetEvent;
import mainPage.interfaces.DashBoardObserver;
import mainPage.interfaces.DirTreeObserver;
import mainPage.interfaces.ParamSettingsObserver;
import mainPage.interfaces.WorkSheetObserver;
import paramSettings.ParamController;
import paramSettings.ParamModel;
import paramSettings.interfaces.IParamController;
import paramSettings.interfaces.ITableModel;
import projectTree.IProjectTreeController;
import projectTree.IProjectTreeModel;
import projectTree.ProjectTreeController;
import projectTree.ProjectTreeModel;
import tube.ITubeModel;
import tube.TubeModel;
import utils.SwingUtils;
import worksheet.WorkSheetController;
import worksheet.WorkSheetModel;
import worksheet.interfaces.IWorkSheetController;
import worksheet.interfaces.IWorkSheetModel;

public class MainPageController implements DirTreeObserver, DashBoardObserver, 
	WorkSheetObserver, ParamSettingsObserver {
	
	private MainPageView view;
	
	private IProjectTreeController dirTreeController;
	private IParamController paramController;
	private DashBoardController dashBoardController;
	private IWorkSheetController workSheetController;
	
	private ITubeModel tubeModel;
	
	public MainPageController() {
		tubeModel = new TubeModel();
		view = new MainPageView(this, tubeModel);
		// 加载项目树
		try {
			view.setDirTree(loadDirTree());
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "初始化项目树失败！异常信息：模型时无法连接数据库");
		}
		// 加载参数列表
		view.setParamSettings(loadParamSettings());
		// 加载dashboard
		view.setDashBoard(loadDashBoard());
		// 加载worksheet
		view.setWorkSheet(loadWorkSheet());	
		view.createGUI();
	}
	
	private JPanel loadDirTree() throws Exception {
		IProjectTreeModel treeModel = new ProjectTreeModel();
		dirTreeController = new ProjectTreeController(treeModel);
		dirTreeController.addObserver(this);
		return dirTreeController.getView();
	}
	
	private JPanel loadParamSettings() {
		ITableModel paramModel = new ParamModel();
		paramController = new ParamController(paramModel);
		paramController.addObserver(this);
		return paramController.getView();
	}
	
	private JPanel loadDashBoard() {
		DashBoardModel dashBoardModel = new DashBoardModel();
		dashBoardController = new DashBoardController(dashBoardModel);
		dashBoardController.addObserver(this);
		return dashBoardController.getView();
	}
	
	private JPanel loadWorkSheet() {
		IWorkSheetModel workSheetModel = new WorkSheetModel();
		workSheetController = new WorkSheetController(workSheetModel);
		workSheetController.addObserver(this);
		return workSheetController.getView();
	}

	@Override
	public void dirTreeUpdated(DirTreeEvent ev) {
		if (ev.getSource() == dirTreeController) {
			if (ev.getActionCommand() == DirTreeEvent.OPEN_SETTINGS) {
				// 初始化参数设置
				paramController.loadSettings(ev.getRelaPath());
			}
			else if (ev.getActionCommand() == DirTreeEvent.OPEN_WORKSHEET) {
				// 初始化worksheet
				try {
					workSheetController.loadWorkSheet(ev.getRelaPath());
				} catch (Exception e) {
					SwingUtils.showErrorDialog(view, "初始化WorkSheet失败！\r\n异常信息：\r\n" + e.getMessage());
				}
			} 
			else if (ev.getActionCommand() == DirTreeEvent.OPEN_TUBE) {
				// 初始化试管数据
				tubeModel.initFromSrc(ev.getRelaPath());
				workSheetController.addDataSource(tubeModel);
			}
		}
	}
	

	@Override
	public void paramSettingsUpdated(ParamSettingsEvent ev) {
		if (ev.getSource() == paramController) {
			if (ev.getActionCommand() == ParamSettingsEvent.ADD
					|| ev.getActionCommand() == ParamSettingsEvent.REMOVE
					|| ev.getActionCommand() == ParamSettingsEvent.UPDATE) {
				tubeModel.setFields(ev.getDataNames());
			}
		}
	}

	@Override
	public void dashBoardUpdated(DashBoardEvent ev) {
		if (ev.getSource() == dashBoardController) {
			if (ev.getActionCommand() == DashBoardEvent.START_SAMPLING) {
				// 设置settings面板不可编辑
				paramController.disableTableEdit();
			} else if (ev.getActionCommand() == DashBoardEvent.STOP_SAMPLING) {
				
			}
		}
	}

	@Override
	public void workSheetUpdated(WorkSheetEvent event) {
		if (event.getSource() == workSheetController) {
			if (event.getActionCommand() == WorkSheetEvent.DataInputFromOutside) {
				paramController.disableTableEdit();
			}
		}
	}
	
	///////////// 菜单命令 /////////////
	
	public void ImportProject() {
		
	}
	
	public void SaveAll() {
		
	}
	
	public void ChangeWorkspace() {
		
	}
	
	public void Exit() {
		System.exit(0);
	}
	
	public void Version() {
		// 弹出版本信息窗口
	}
	
	public void Help() {
		
	}

}
