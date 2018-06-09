package spectrum;

import java.util.List;
import java.util.Map;

import dao.beans.ParamSettingsBean;
import mainPage.MainView;
import spectrum.wave.IWavePolicy;
import spectrum.wave.WavePolicy;
import utils.SwingUtils;

public class SpectrumController {
	
	private SpectrumModel model;
	private MainView view;
	
	public SpectrumController(SpectrumModel model, MainView view) {
		this.model = model;
		this.view = view;
	}

	/**
	 * 更新光谱，该操作不会删除光谱数据
	 * @param beans
	 */
	public void updateSpectrums(List<ParamSettingsBean> beans) {
		if (beans == null) {
			return;
		}
		for (ParamSettingsBean b : beans) {
			Spectrum s = model.getSpectrumByName(b.getParamName());
			if (s == null) {
				SwingUtils.showErrorDialog(view, "更新光谱时发现光谱名称不匹配");
				return;
			}
			s.setChannelId(b.getChannelId());
			s.setThreshold(b.getThreshold());
			s.setWavePolicy(getWavePolicy(b));
		}
	}
	
	/**
	 * 重新添加所有光谱，该方法不保留光谱数据
	 * @param beans
	 */
	public void refreshSpectrums(List<ParamSettingsBean> beans) {
		if (beans == null) {
			return;
		}
		model.clear();
		for (ParamSettingsBean b : beans) {
			Spectrum s = new Spectrum();
			s.setName(b.getParamName());
			s.setChannelId(b.getChannelId());
			s.setThreshold(b.getThreshold());
			s.setWavePolicy(getWavePolicy(b));
			model.addSpectrum(s);
		}
	}
	
	private IWavePolicy getWavePolicy(ParamSettingsBean b) {
		if (b.isA()) {
			return WavePolicy.AREA;
		}
		else if (b.isH()) {
			return WavePolicy.HEIGHT;
		}
		else if (b.isW()) {
			return WavePolicy.WIDTH;
		}
		else {
			throw new IllegalArgumentException();
		}
	}
	
	public void addData(Map<String, Double> kvs) {
		try {
			model.addData(kvs);
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "添加数据失败！异常信息：" + e.getMessage());
		}
	}

}
