package dashBoard;

import java.util.ArrayList;
import java.util.List;

import dao.beans.ParamSettingsBean;
import mainPage.MainView;
import mainPage.Session;
import paramSettings.interfaces.IParamModel;
import utils.SwingUtils;

public class DashBoardController {
	
	private MainView view;
	private IParamModel paramModel;
	private DashBoardModel dashBoardModel;
	
	public DashBoardController(DashBoardModel dashBoardModel, IParamModel paramModel, MainView view) {
		this.dashBoardModel = dashBoardModel;
		this.paramModel = paramModel;
		this.view = view;
	}
	
	public void startSampling() {
		if (Session.getSelectedTubeLid() == null) {
			SwingUtils.showErrorDialog(view, "请先打开试管文件");
			return;
		}
		
		try {
			List<String> param = new ArrayList<>();
			for (int i = 0; i < paramModel.getRowCount(); i++) {
				ParamSettingsBean bean = paramModel.beanAt(i);
				param.add("CH" + bean.getChannelId());
			}
			
			dashBoardModel.startSampling(param);
		} 
		catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "发送数据失败！" + e.toString());
		}
	}
	
	public void stopSampling() {
		if (Session.getSelectedTubeLid() == null) {
			SwingUtils.showErrorDialog(view, "请先打开试管文件");
			return;
		}
		
		try {
			dashBoardModel.stopSampling();
		}
		catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "发送数据失败！" + e.toString());
		}
		
	}
	
	public void changeVoltage() {
		if (!dashBoardModel.isOnSampling()) {
			return;
		}
		
		try {
			List<Integer> param = new ArrayList<>();
			for (int i = 0; i < paramModel.getRowCount(); i++) {
				ParamSettingsBean bean = paramModel.beanAt(i);
				param.add(bean.getVoltage());
			}
			
			dashBoardModel.changeVoltage(param);
		}
		catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "发送数据失败！" + e.toString());
		}
	}
	
	public void resetSystem() {
		if (!dashBoardModel.isOnSampling()) {
			return;
		}
		
		try {
			dashBoardModel.resetSystem();
		} 
		catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "发送数据失败！" + e.toString());
		}
	}

}
