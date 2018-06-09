package spectrum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpectrumModel {
	
	private List<Spectrum> spectrums = new ArrayList<>();
	
	private List<SpectrumModelObserver> observers = new ArrayList<>();
	
	private int prevEventsCount = 0;
	
	public void addData(Map<String, Double> kvs) throws Exception {
		for (String key : kvs.keySet()) {
			int id = matchChannelId(key);
			if (id < 0) {
				throw new IllegalArgumentException("接收的数据格式有误");
			}
			Spectrum s = getSpectrumByChannelId(id);
			if (s == null) {
				throw new NoSuchElementException("找不到对应通道的光谱");
			}
			s.addData(kvs.get(key));
		}
	}
	
	private void notifyIfNewEventGenerated() {
		List<Map<Integer, Integer>> intensities = new ArrayList<>();
		for (Spectrum s : spectrums) {
			intensities.add(s.getIntensities());
		}
	}
	
	private int matchChannelId(String str) {
		Matcher m = Pattern.compile("\\d+").matcher(str);
		if (m.find()) {
			return Integer.valueOf(str.substring(m.start(), m.end()));
		}
		return -1;
	}
	
	private Spectrum getSpectrumByChannelId(int id) {
		for (Spectrum s : spectrums) {
			if (s.getChannelId() == id) {
				return s;
			}
		}
		return null;
	}
	
	public int getSpectrumsCount() {
		return spectrums.size();
	}
	
	public void addSpectrum(Spectrum s) {
		spectrums.add(s);
	}
	
	public void removeSpectrum(Spectrum s) {
		spectrums.remove(s);
	}
	
	public void clear() {
		spectrums.clear();
	}
	
	public Spectrum getSpectrumByName(String name) {
		for (Spectrum s : spectrums) {
			if (s.getName().equals(name)) {
				return s;
			}
		}
		return null;
	}
	
	public void addObserver(SpectrumModelObserver o) {
		observers.add(o);
	}
	
	public void removeObserver(SpectrumModelObserver o) {
		observers.remove(o);
	}
	
	public void notifyObservers(double[] data) {
		observers.stream().forEach(e -> e.newEventGenerated(data));
	}

}
