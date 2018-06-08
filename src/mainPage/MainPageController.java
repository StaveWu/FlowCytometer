package mainPage;

import java.util.TooManyListenersException;

import javax.swing.JPanel;

import dashBoard.DashBoardController;
import dashBoard.DashBoardModel;
import device.SerialTool;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
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
import paramSettings.SpectrumAnalysis;
import paramSettings.interfaces.IParamController;
import paramSettings.interfaces.IParamModel;
import projectTree.IProjectTreeController;
import projectTree.IProjectTreeModel;
import projectTree.ProjectTreeController;
import projectTree.ProjectTreeModel;
import tube.ITubeController;
import tube.ITubeModel;
import tube.TubeController;
import tube.TubeModel;
import utils.SwingUtils;
import worksheet.WorkSheetController;
import worksheet.interfaces.IWorkSheetController;

public class MainPageController implements DirTreeObserver, DashBoardObserver, 
	WorkSheetObserver, ParamSettingsObserver, SerialPortEventListener {
	
	private MainPageView view;
	
	private IProjectTreeController dirTreeController;
	private IParamController paramController;
	private DashBoardController dashBoardController;
	private IWorkSheetController workSheetController;
	private ITubeController tubeController;
	
	private SpectrumAnalysis spectrumAnalysis;
	
	public MainPageController() {
		view = new MainPageView(this);
		// 加载各个子控件
		try {
			view.setDirTree(loadDirTree());
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "初始化项目树失败！异常信息：模型时无法连接数据库");
		}
		view.setParamSettings(loadParamSettings());
		view.setDashBoard(loadDashBoard());
		view.setWorkSheet(loadWorkSheet());	
		view.setTubeView(loadTubeView());
		
		view.createGUI();
		
		spectrumAnalysis = new SpectrumAnalysis();
		Thread t = new Thread(spectrumAnalysis);
		t.setDaemon(true);
		t.start();
	}
	
	private JPanel loadDirTree() throws Exception {
		IProjectTreeModel treeModel = new ProjectTreeModel();
		dirTreeController = new ProjectTreeController(treeModel);
		dirTreeController.addObserver(this);
		return dirTreeController.getView();
	}
	
	private JPanel loadParamSettings() {
		IParamModel paramModel = new ParamModel();
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
		workSheetController = new WorkSheetController();
		workSheetController.addObserver(this);
		return workSheetController.getView();
	}
	
	private JPanel loadTubeView() {
		ITubeModel tubeModel = new TubeModel();
		tubeController = new TubeController(tubeModel);
		return tubeController.getView();
	}

	@Override
	public void dirTreeUpdated(DirTreeEvent ev) {
		if (ev.getSource() == dirTreeController) {
			String pathname = FCMSettings.getWorkSpacePath() + ev.getRelaPath();
			if (ev.getActionCommand() == DirTreeEvent.OPEN_SETTINGS) {
				// 初始化参数列表
				paramController.loadSettings(pathname);
			}
			else if (ev.getActionCommand() == DirTreeEvent.OPEN_WORKSHEET) {
				// 初始化worksheet
				workSheetController.loadWorkSheet(pathname);
			} 
			else if (ev.getActionCommand() == DirTreeEvent.OPEN_TUBE) {
				// 初始化试管数据
				tubeController.loadData(pathname);
				workSheetController.addDataSource(tubeController.geTubeModel());
			}
		}
	}
	

	@Override
	public void paramSettingsUpdated(ParamSettingsEvent ev) {
		if (ev.getSource() == paramController) {
			if (ev.getActionCommand() == ParamSettingsEvent.ADD
					|| ev.getActionCommand() == ParamSettingsEvent.REMOVE
					|| ev.getActionCommand() == ParamSettingsEvent.UPDATE) {
				tubeController.setFields(ev.getDataNames());
			}
		}
	}

	@Override
	public void dashBoardUpdated(DashBoardEvent ev) {
		if (ev.getSource() == dashBoardController) {
			if (ev.getActionCommand() == DashBoardEvent.START_SAMPLING) {
				paramController.disableTableEdit();
				// 添加监听
				try {
					SerialTool.getInstance().addEventListener(tubeController.geTubeModel());
				} catch (TooManyListenersException e) {
					e.printStackTrace();
					SwingUtils.showErrorDialog(view, e.toString());
				}
			} 
			else if (ev.getActionCommand() == DashBoardEvent.STOP_SAMPLING) {
				paramController.enableTableEdit();
				// 停止监听
				SerialTool.getInstance().removeEventListener();
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
	
	///////////// 菜单命令 start /////////////
	
	public void ImportProject() {
		
	}
	
	public void SaveAll() {
		
	}
	
	public void ChangeWorkspace() {
		
	}
	
	public void Exit() {
		System.exit(0);
	}
	
	public void CommDeviceSetting() {
		new PortSettingBox();
	}
	
	public void Version() {
		
	}
	
	public void Help() {
		
	}
	
	///////////// 菜单命令 end ///////////////

	@Override
	public void serialEvent(SerialPortEvent e) {
		if (e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				System.out.println(new String(SerialTool.getInstance().read()));
			} catch (Exception e1) {
				e1.printStackTrace();
				SwingUtils.showErrorDialog(view, "串口解析数据出错");
			}
		}
	}

}
