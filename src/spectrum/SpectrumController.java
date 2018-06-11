package spectrum;

import java.util.List;
import java.util.Map;

import dao.beans.ParamSettingsBean;
import mainPage.MainView;
import utils.SwingUtils;

public class SpectrumController {
	
	private SpectrumModel model;
	private MainView view;
	
	public SpectrumController(SpectrumModel model, MainView view) {
		this.model = model;
		this.view = view;
	}

	/**
	 * ���¹��ף��ò�������ɾ����������
	 * @param beans
	 */
	public void updateSpectrums(List<ParamSettingsBean> beans) {
		if (beans == null) {
			return;
		}
		for (ParamSettingsBean b : beans) {
			Spectrum s = model.getSpectrumByName(b.getParamName());
			if (s == null) {
				SwingUtils.showErrorDialog(view, "���¹���ʱ���ֹ������Ʋ�ƥ��");
				return;
			}
			s.setChannelId(b.getChannelId());
			s.setThreshold(b.getThreshold());
			s.setWavePolicy(getWavePolicy(b));
		}
	}
	
	/**
	 * �����������й��ף��÷�����������������
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
	
	private String getWavePolicy(ParamSettingsBean b) {
		if (b.isA()) {
			return "A";
		}
		else if (b.isH()) {
			return "H";
		}
		else if (b.isW()) {
			return "W";
		}
		else {
			return "A";
		}
	}
	
	public void addData(Map<String, Double> kvs) {
		try {
			model.addData(kvs);
		} catch (Exception e) {
			e.printStackTrace();
			SwingUtils.showErrorDialog(view, "��������ʧ�ܣ��쳣��Ϣ��" + e.getMessage());
		}
	}

}