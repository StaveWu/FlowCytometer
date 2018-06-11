package spectrum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpectrumModel {
	
	/**
	 * ���й�������
	 * ͬ���̣߳�һ�β��������������ͬ�����
	 */
	private List<Spectrum> spectrums = Collections.synchronizedList(new ArrayList<>());
	
	private ExecutorService pool;
	
	private List<Vector<Double>> events = new ArrayList<>();
	
	public SpectrumModel() {
		pool = Executors.newCachedThreadPool();
	}
	
	private List<SpectrumModelObserver> observers = new ArrayList<>();
	
	public void addData(Map<String, Double> kvs) throws Exception {
		for (String key : kvs.keySet()) {
			int id = matchChannelId(key);
			if (id < 0) {
				throw new IllegalArgumentException("���յ����ݸ�ʽ����");
			}
			Spectrum s = getSpectrumByChannelId(id);
			if (s == null) {
				throw new NoSuchElementException("�Ҳ�����Ӧͨ���Ĺ���");
			}
			s.addData(kvs.get(key));
		}
		// �첽����
		pool.execute(new SpectrumIntensityHandler(this));
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
	
	/**
	 * ��ȡ����
	 * @return	���ع��׵�һ�ݿ���
	 */
	public List<Spectrum> getSpectrums() {
		List<Spectrum> res = new ArrayList<>();
		for (Spectrum s : spectrums) {
			res.add(s);
		}
		return res;
	}
	
	public void addObserver(SpectrumModelObserver o) {
		observers.add(o);
	}
	
	public void removeObserver(SpectrumModelObserver o) {
		observers.remove(o);
	}
	
	public void notifyObservers(List<Vector<Double>> data) {
		observers.stream().forEach(e -> e.newEventGenerated(data));
	}
	
	/**
	 * �����̻߳�ִ����һ����
	 * @param es
	 */
	public void setEvents(List<Vector<Double>> es) {
		if (es == null) {
			return;
		}
		
		List<Vector<Double>> old = this.events;
		this.events = es;
		
		if (es.size() < old.size()) {
			throw new RuntimeException("���Ӵ����µļ���������һ�μ�����С");
		}
		if (es.size() == old.size()) {
			return;
		}
		// ��ȡ����ӵ��Թ�����
		List<Vector<Double>> newIn = new ArrayList<>();
		for (int i = old.size(); i < es.size(); i++) {
			newIn.add(es.get(i));
		}
		notifyObservers(newIn);
	}

}
