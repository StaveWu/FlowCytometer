package spectrum;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class SpectrumIntensityHandler implements Runnable {
	
	private SpectrumModel model;
	
	private List<Vector<Double>> debug = new ArrayList<>();
	
	public SpectrumIntensityHandler(SpectrumModel model) {
		this.model = model;
	}

	@Override
	public void run() {
		model.setEvents(calculateIntensities(model.getSpectrums()));
	}
	
	/**
	 * 计算光谱强度
	 * @param spectrums	光谱数据
	 * @return	强度events
	 */
	private List<Vector<Double>> calculateIntensities(List<Spectrum> spectrums) {
		System.out.println("计算强度了.." + spectrums.get(0).getData().size());
		
		// 伪造数据发送到tube上
		List<Vector<Double>> res = debug;
		Vector<Double> event = new Vector<>();
		Random rand = new Random();
		
		for (int i = 0; i < spectrums.get(0).getData().size(); i++) {
			for (Spectrum s : spectrums) {
				event.add((double) rand.nextInt(1000));
			}
			res.add(event);
		}
		
		
		
		return res;
	}

}
