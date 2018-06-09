package paramSettings.interfaces;

import java.util.List;

import dao.beans.ParamSettingsBean;

public interface ParamModelObserver {
	
	public void paramModelUpdated(List<ParamSettingsBean> beans);

}
