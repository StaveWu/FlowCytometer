package plot;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import menu.ColorChooserBox;
import menu.DataRegionPopupMenu;
import menu.GatePopupMenu;
import plot.axis.Axis;
import plot.axis.AxisAlgebra;
import plot.axis.AxisDimension;
import plot.axis.AxisSettingBox;
import plot.axis.AxisType;
import plot.gate.Gate;
import plot.gate.HorizontalGate;
import plot.gate.PolygonGate;
import plot.gate.RectangleGate;
import plot.gate.VerticalGate;
import plot.gate.Vertice;
import plot.io.FCSDataWriter;
import plot.io.TXTDataWriter;
import plot.threshold.IThresholdStrategy;
import plot.threshold.RegionThreshold;
import plotContainer.PlotContainer;
import tube.ITubeModel;
import tube.TubeModelObserver;
import utils.ArrayUtils;
import utils.SwingUtils;

@SuppressWarnings("serial")
public abstract class Plot extends ArrowPane implements TubeModelObserver, PropertyChangeListener {
	
	/**
	 * ��Ȧ��״̬
	 */
	public static final int CREATE_GATE_STATUS = 1;
	
	/**
	 * Ĭ��״̬
	 */
	public static final int DEFAULT_STATUS = 0;
	
	/**
	 * ��ǰ״̬
	 */
	public int status = DEFAULT_STATUS;
	
	private int uid;
	private int previd = -1;
	private int nextid = -1;

	/**
	 * ��
	 */
	protected Axis[] axis;
	
	/**
	 * Ȧ��
	 */
	protected Gate gate;
	
	/**
	 * ͼ��
	 */
	protected String plotName = "";
	
	/**
	 * ���򻮷ֲ���
	 */
	private IThresholdStrategy rtStra;
	
	/**
	 * ��Ҫ���ֵ������Ƿ�ı�
	 */
	private boolean isCoordsChanged = false;
	/**
	 * ����ԭ��
	 */
	protected Point origin;
	
	/**
	 * ������
	 */
	private Rectangle dataRegion;
	
	/**
	 * ����
	 */
	private Rectangle[] axisRegions;
	
	/**
	 * ���ݵ�����
	 */
	private List<Integer> dataIds;
	
	/**
	 * Ȧ����������
	 */
	private List<Integer> gatedIds;
	
	/**
	 * ����ģ�ͣ�plot��ʾ�����ݵ��������ȡ
	 */
	private ITubeModel dataSource;
	
	/**
	 * ������
	 */
	public Plot() {
		this(new Axis[] {
				new Axis("Default", 0, 160000, AxisAlgebra.LINEAR, AxisType.X, AxisDimension.X2D),
				new Axis("Default", 0, 160000, AxisAlgebra.LINEAR, AxisType.Y, AxisDimension.Y2D)
		});
	}
	
	public Plot(Axis[] axis) {
		updatePlotName();
		
		this.axis = axis;
		rtStra = (axis.length == 3 ? RegionThreshold.T3D : RegionThreshold.T2D);
		axisRegions = new Rectangle[axis.length];
		
		for(Axis a : axis) {
			a.addPropertyChangeListener(this);//ע�����
		}
		
		this.setSize(250, 250);
		/*
		 * ��Ӽ�����ע�⣺
		 * ע���������ʱ�����������֮����
		 * �Ⱥ�˳��Ĺ�ϵ��һ��Ҫ��˳�����
		 */
		addMouseListener(new PopupMenuListener());
		
		GateGenerateListener ggl = new GateGenerateListener();
		addMouseListener(ggl);
		addMouseMotionListener(ggl);
		
		addMouseListener(new GateFocusListener());
		
		GateTranslateListener gtl = new GateTranslateListener();
		addMouseListener(gtl);
		addMouseMotionListener(gtl);
		
		GateResizableListener grl = new GateResizableListener();
		addMouseListener(grl);
		addMouseMotionListener(grl);
	}
	
	private void updatePlotName() {
		plotName = getPlotType() + "-" + getUid();
	}
	
	@Override
	public void refresh() {
		List<Integer> total = new ArrayList<>();
		for (int i = 0; i < dataSource.getEventsCount(); i++) {
			total.add(i);
		}
		setDataIds(total);
		setCoordsChanged(true);
	}
	
	public abstract String getPlotType();
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//������
		if(plotName != null && !plotName.equals("")) {
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//�����
			g2d.setColor(Color.BLACK);
			g2d.setFont(new Font("Calibri", Font.BOLD, 14));
			
			int namewidth = 0;
			int nameheight = 0;
			FontMetrics fm = g2d.getFontMetrics();
			namewidth = fm.stringWidth(plotName);
			nameheight = fm.getAscent() - fm.getDescent();
			
			int basePointx = getWidth() / 2 - namewidth / 2;
			int basePointy = nameheight + FOCUSBORDER_THICKNESS + 8;
			g2d.drawString(plotName, basePointx, basePointy);
		}
		
		//ȷ������
		Rectangle plotRegion = new Rectangle(0, 0, getWidth(), getHeight());
		origin = rtStra.getOrigin(plotRegion);
		int[] lengths = rtStra.getAxisLengths(plotRegion);
		axisRegions[0] = rtStra.getAxisRegionX(plotRegion);
		axisRegions[1] = rtStra.getAxisRegionY(plotRegion);
		dataRegion = rtStra.getDataRegion(plotRegion);
		
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//�����
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.draw(dataRegion);
		
		//����
		Graphics gAxis = g.create();
		gAxis.translate(origin.x, origin.y);
		
		axis[0].paint(gAxis, new Point(0, 0), lengths[0]);
		axis[1].paint(gAxis, new Point(0, 0), lengths[1]);
		if(axis.length > 2) {
			axisRegions[2] = rtStra.getAxisRegionZ(plotRegion);
			Point endPointY = axis[2].getEnd(new Point(0, 0), lengths[2]);
			axis[2].paint(gAxis, endPointY, lengths[2]);
		}
		
		//������
		ExperimentData eData = getEData();
		ExperimentData realCoords = getRealCoords(eData);
		if(isCoordsChanged) {
			setAxesRange(realCoords);
			repaint();
			isCoordsChanged = false;
		}
		ExperimentData pxData = preProcessAndPixelate(realCoords, lengths);
		paintData(gAxis, pxData);
		
		//��gate
		if(gate != null) {
			gate.paint(g);
			if (gate.isGenerated()) {
				gatedIds = getGatedIds(pxData, realCoords, eData);
			}
		}
		transmit();// ������һֱ������
	}
	
	public void setTubeModel(ITubeModel tubeModel) {
		this.dataSource = tubeModel;
		if (tubeModel != null) {
			tubeModel.addObserver(this);
		}
	}

	ExperimentData getEData() {
		if (dataSource == null || dataIds == null) {
			return null;
		}
		ExperimentData res = new ExperimentData();
		for (int i = 0; i < dataSource.getFieldsCount(); i++) {		//i��
			double[] var = new double[dataIds.size()];				//ÿһ�еı���
			for (int j = 0; j < var.length; j++) {					//j��
				int id = dataIds.get(j);							//��id��
				Vector<Double> event = dataSource.getEventAt(id);
				var[j] = event.get(i);
			}
			res.add(dataSource.getFieldAt(i), var);
		}
		return res;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		repaint();
	}
	
	/**
	 * �����᷶Χ
	 * @param realCoords
	 */
	void setAxesRange(ExperimentData realCoords) {
		if (realCoords == null) {
			return;
		}
		//����������ֵ
		double[] x = realCoords.getDataByName(axis[0].getName());
		double[] y = realCoords.getDataByName(axis[1].getName());
		if (x == null || y == null || x.length == 0 || y.length == 0) {
			return;
		}
		//�����Ϊ������
		axis[0].setMaxValue(ArrayUtils.getMax(x));
		axis[0].setMinValue(ArrayUtils.getMin(x));
		axis[1].setMaxValue(ArrayUtils.getMax(y));
		axis[1].setMinValue(ArrayUtils.getMin(y));
		if(axis.length > 2) {
			double[] z = realCoords.getDataByName(axis[2].getName());
			if (z == null || z.length == 0) {
				return;
			}
			axis[2].setMaxValue(ArrayUtils.getMax(z));
			axis[2].setMinValue(ArrayUtils.getMin(z));
		}
	}
	
	/**
	 * Ԥ�������ػ�
	 * @param realCoords
	 * @return
	 */
	ExperimentData preProcessAndPixelate(ExperimentData realCoords, int[] pixLens) {
		if (realCoords == null) {
			return null;
		}
		ExperimentData res = new ExperimentData();
		IDataAlgebraStrategy[] daS = new IDataAlgebraStrategy[axis.length];
		for (int i = 0; i < daS.length; i++) {
			//ͬ�����ݴ������
			daS[i] = axis[i].getAlgebraStrategy() == AxisAlgebra.LOG ? 
					DataAlgebraType.LOG : DataAlgebraType.LINEAR;
			//����Ԥ����
			String dataName = axis[i].getName();
			double[] processed = daS[i].process(realCoords.getDataByName(dataName));
			double[] pxData = daS[i].pixelate(processed, axis[i], pixLens[i]);
			res.add(dataName, pxData);
		}
		return res;
	}
	
	/**
	 * ��ȡ��Ȧ����id
	 * @param pxData
	 */
	public abstract List<Integer> getGatedIds(ExperimentData pxData, ExperimentData realCoords, 
			ExperimentData srcData);
	
	public List<Integer> getGatedIds() {
		return gatedIds;
	}
	
	@Override
	public void transmit() {
		Plot next = (Plot) getNext();
		if (next == null) {
			return;
		}
		if (gate != null) {
			next.setDataIds(gatedIds);
		} else {
			next.setDataIds(dataIds);
		}
		
		setNextid(next.getUid());
		next.setPrevid(getUid());
	}
	
	public List<Integer> getDataIds() {
		return dataIds;
	}

	public void setDataIds(List<Integer> dataIds) {
		this.dataIds = dataIds;
		setCoordsChanged(true);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Gate getGate() {
		return gate;
	}

	public void setGate(Gate gate) {
		this.gate = gate;
	}
	
	public boolean isCoordsChanged() {
		return isCoordsChanged;
	}

	public void setCoordsChanged(boolean isCoordsChanged) {
		this.isCoordsChanged = isCoordsChanged;
	}
	
	public void setGateColor(Color color) {
		if (gate != null) {
			gate.setColor(color);
		}
	}

	/**
	 * ��ȡ��ʵ����
	 * ����ͼΪֱ��ͼ����������Ӧ���Ǿ���ͳ��֮��
	 * ����Ϊ���������˵���ʵֵ������ΪCountֵ������
	 * @param srcData	ʵ������
	 * @return
	 */
	protected abstract ExperimentData getRealCoords(ExperimentData srcData);
	
	/**
	 * ������
	 * @param g
	 * @param pxCoords
	 */
	public abstract void paintData(Graphics g, ExperimentData pxCoords);
	
	/**
	 * ��ȡ������
	 * @return
	 */
	public Shape getDataRegion() {
		return rtStra.getDataRegion(new Rectangle(0, 0, getWidth(), getHeight()));
	}
	
	public boolean isInAxisRegion(int axisId, Point p) {
		try {
			return axisRegions[axisId].contains(p);
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	/**
	 * Ȧ�����ɼ���
	 * @author wteng
	 *
	 */
	private class GateGenerateListener extends MouseAdapter {
		
		private boolean isClicked = false;
		
		@Override
		public void mousePressed(MouseEvent arg0) {
			if(status == CREATE_GATE_STATUS) {
				/*
				 * ��gate�������˵��������ʱ�򴴽�
				 */
				if(gate.isGenerated()) {
					status = DEFAULT_STATUS;
					isClicked = false;
					Plot.this.repaint();
					return;
				}
				gate.addVertice(new Vertice(arg0.getPoint()));
				isClicked = true;
				Plot.this.repaint();
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent arg0) {
			if(status == CREATE_GATE_STATUS) {
				if(gate.isGenerated()) {
					status = DEFAULT_STATUS;
					isClicked = false;
					Plot.this.repaint();
					return;
				}
			}
		}
		
		@Override
		public void mouseMoved(MouseEvent arg0) {
			if(isClicked) {
				if(!gate.isGenerated()) {
					gate.setRunningPoint(arg0.getPoint());
					Plot.this.repaint();
				}
			}
		}

	}
	
	/**
	 * Ȧ��ƽ�Ƽ���
	 * @author wteng
	 *
	 */
	private class GateTranslateListener extends MouseAdapter {
		
		private boolean canTranslate = false;
		private Point pressPos;
		
		@Override
		public void mousePressed(MouseEvent e) {
			if(gate == null) {
				return;
			}
			if (gate.isInGateRegion(e.getPoint())) {
				canTranslate = true;
				pressPos = e.getPoint();
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if(canTranslate) {
				int deltaX = e.getX() - pressPos.x;
				int deltaY = e.getY() - pressPos.y;
				gate.translate(deltaX, deltaY);
				gate.setHideVerticesRegion(true);
				gate.setHideGateName(true);
				Plot.this.repaint();
				repaintNext(Plot.this);
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if(canTranslate) {
				canTranslate = false;
				gate.updateVertice();
				gate.setHideVerticesRegion(false);
				gate.setHideGateName(false);
				Plot.this.repaint();
			}
		}
	}
	
	/**
	 * Ȧ�ž۽����������۽�ʱ��ʾС������
	 * @author wteng
	 *
	 */
	private class GateFocusListener extends MouseAdapter {
		
		@Override
		public void mousePressed(MouseEvent arg0) {
			if(gate == null) {
				return;
			}
			Point p = arg0.getPoint();
			if(gate.isInGateRegion(p) || gate.isInVerticesRegion(p)) {
				gate.setFocusable(true);
				gate.setHideVerticesRegion(false);
			}
			else {
				gate.setFocusable(false);
				gate.setHideVerticesRegion(true);
			}
		}
	}
	
	/**
	 * �ı�gate����λ�õļ���
	 * @author wteng
	 *
	 */
	private class GateResizableListener extends MouseAdapter {
		
		private boolean enabled = false;
		private int index = -1;
		@Override
		public void mousePressed(MouseEvent e) {
			if(gate == null || !gate.isFocusable()) {
				return;
			}
			index = gate.verticesRegionOfWhichContains(e.getPoint());
			enabled = gate.isGenerated() && gate.isInVerticesRegion(e.getPoint());
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if(enabled) {
				gate.setHideVerticesRegion(true);
				gate.getVertices().set(index, new Vertice(e.getPoint()));
				gate.rebuildGate(index);
				gate.setHideGateName(true);
				Plot.this.repaint();
				repaintNext(Plot.this);
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if(enabled) {
				gate.updateVertice();
				gate.setHideVerticesRegion(false);
				gate.setHideGateName(false);
				Plot.this.repaint();
				enabled = false;
				index = -1;
				
			}
		}
	}
	
	/**
	 * �����˵�����
	 * @author wteng
	 *
	 */
	private class PopupMenuListener extends MouseAdapter {
		
		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON3) {
				Point p = e.getPoint();
				if(gate != null && gate.isGenerated() &&
						gate.getGateRegion().contains(p)) {
					//��ʾȦ�Ų˵�
					new GatePopupMenu(Plot.this, p).show(Plot.this, p.x, p.y);;
				}
				else if(dataRegion.contains(p)) {
					//��ʾ�������˵�
					new DataRegionPopupMenu(Plot.this).show(Plot.this, p.x, p.y);
				}
				else {
					//��ʾ��˵�
					if (dataSource == null) {
						return;
					}
					for (int i = 0; i < axis.length; i++) {
						if (isInAxisRegion(i, p)) {
							showAxisRegionMenu(p, axis[i], dataSource.getFields());
							break;
						}
					}
					
				}
			}
		}
		private void showAxisRegionMenu(Point p, Axis axis, List<String> names) {
			if (axis == null || names == null || names.size() == 0) {
				return;
			}
			if (axis == Plot.this.axis[1] && Plot.this instanceof Histogram) {
				return;
			}
			JPopupMenu pm = new JPopupMenu();
			//�����������item
			JMenuItem axisSetting = new JMenuItem("AxisSetting");
			axisSetting.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent arg0) {
					//����������ÿ�
					new AxisSettingBox(Plot.this.getLocationOnScreen(), axis,
							names);
				}
			});
			pm.add(axisSetting);
			pm.addSeparator();
			//�������е�������item
			JMenuItem[] mis = new JMenuItem[names.size()];
			for (int i = 0; i < mis.length; i++) {
				mis[i] = new JMenuItem(names.get(i));
			}
			for(JMenuItem ele : mis) {
				ele.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent arg0) {
						axis.setName(ele.getText());
						Plot.this.setCoordsChanged(true);
					}
				});
				pm.add(ele);
			}
			pm.show(Plot.this, p.x, p.y);
			
		}

	}
	
	//////////gate�˵�����///////////
	/**
	 * ɾ��Ȧ��
	 */
	public void deleteGate(Point p) {
		gate = null;
		repaint();
	}
	
	/**
	 * ������
	 */
	public void rename(Point p) {
		//�ڵ��������ı���
		JTextField tf = new JTextField();
		tf.setBounds(p.x, p.y, 60, 25);
		tf.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//�ı�����
				gate.setName(tf.getText());
				//ɾ���ı���
				Plot.this.remove(tf);
				repaint();
			}
		});
		Plot.this.add(tf);
	}
	
	/**
	 * �ı���ɫ
	 */
	public void changeColor(Point p) {
		//������ɫѡ���
		new ColorChooserBox(Plot.this, p);
	}

	
	
	
	//////////plot�˵�����///////////
	/**
	 * ɾ����ͼ
	 */
	public void deletePane() {
		dispose();
	}
	
	/**
	 * ��������Ȧ��
	 */
	public void createRectangleGate() {
		setGate(new RectangleGate());
		setStatus(CREATE_GATE_STATUS);
		repaint();
	}
	
	/**
	 * ���������Ȧ��
	 */
	public void createPolygonGate() {
		setGate(new PolygonGate());
		setStatus(CREATE_GATE_STATUS);
		repaint();
	}
	
	/**
	 * ����ˮƽ��Ȧ��
	 */
	public void createHorizontalGate() {
		setGate(new HorizontalGate(dataRegion));
		setStatus(CREATE_GATE_STATUS);
		repaint();
	}
	
	/**
	 * ������ֱ��Ȧ��
	 */
	public void createVerticalGate() {
		setGate(new VerticalGate(dataRegion));
		setStatus(CREATE_GATE_STATUS);
		repaint();
	}
	
	/**
	 * ɾ����������
	 */
	public void deleteInputReference() {
		deletePrev();
		observer.repaintArrows();
	}
	
	/**
	 * ɾ����������
	 */
	public void deleteOutputReference() {
		deleteNext();
		observer.repaintArrows();
	}
	
	/**
	 * ɾ����������
	 */
	public void deleteAllRefenrence() {
		deleteInputReference();
		deleteOutputReference();
	}
	
	/**
	 * ��fcs�ļ��ﵼ������
	 */
	public void importDataFromFCS() {
		//�����ļ�ѡ����
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.fcs", "fcs");
		fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			// ��������
			File file = fc.getSelectedFile();
			try {
				// ��ʼ��tubeModel�����dataIds
				dataSource.initFromFcs(file);
				List<Integer> total = new ArrayList<>();
				for (int i = 0; i < dataSource.getEventsCount(); i++) {
					total.add(i);
				}
				setDataIds(total);
				notifyParentDataIuputed();
			} catch (Exception e) {
				SwingUtils.showErrorDialog(this, "�ļ���ȡ�����쳣��Ϣ��" + e.getMessage());
			}
		}
	}
	
	/**
	 * ��txt�ļ��ﵼ������
	 */
	public void importDataFromTXT() {
		//�����ļ�ѡ����
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt");
		fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			//��ȡ����������
			File file = fc.getSelectedFile();
			try {
				dataSource.initFromTxt(file);
				List<Integer> total = new ArrayList<>();
				for (int i = 0; i < dataSource.getEventsCount(); i++) {
					total.add(i);
				}
				setDataIds(total);
				notifyParentDataIuputed();
			} catch (Exception e) {
				e.printStackTrace();
				SwingUtils.showErrorDialog(this, "�ļ���ȡ�����쳣��Ϣ��" + e.getMessage());
			}
			repaint();
		}
	}
	
	/**
	 * ֪ͨ�������ݵ�����
	 */
	private void notifyParentDataIuputed() {
		PlotContainer parent = (PlotContainer) this.getParent();
		parent.notifyDataInputed();
	}
	
	/**
	 * ��������ΪFCS�ļ�
	 */
	public void exportDataToFCS() {
		//�����ļ�ѡ����
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.fcs", "fcs");
		fc.setFileFilter(filter);
		int returnVal = fc.showSaveDialog(this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String fileName = file.getName();
			String[] s = fileName.split("\\.");
			System.out.println(fileName + " " + s.length);
			//����ļ����Ƿ�Ϊfcs��ʽ
			if(s.length == 2 && s[1].equals("fcs")) {
				String path = fc.getCurrentDirectory() + File.separator + fileName;
				file = new File(path);
				//����ļ��Ƿ����
				if(!file.exists()) {
					//д���ļ�
					FCSDataWriter.write(getEData(), file);
				}
				else {
					int n = SwingUtils.showConfirmDialog(this, 
							"�ļ���" + fileName + "�Ѵ��ڣ�ȷ��Ҫ������");
					if(n == JOptionPane.YES_OPTION) {
						//�����ļ�
						FCSDataWriter.write(getEData(), file);
					}
					else if(n == JOptionPane.NO_OPTION) {
						return;
					}
					else {
						throw new RuntimeException("δ֪���󣺵���û�з��ع涨ֵ");
					}
				}
			}
			else {
				SwingUtils.showErrorDialog(this, "�ļ���" + fileName + "��ʽ����");
			}
			
		}
	}
	
	/**
	 * ��������ΪTXT�ļ�
	 */
	public void exportDataToTXT() {
		//�����ļ�ѡ����
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt");
		fc.setFileFilter(filter);
		int returnVal = fc.showSaveDialog(this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			
			File file = fc.getSelectedFile();
			String fileName = file.getName();
			String[] s = fileName.split("\\.");
			//����ļ����Ƿ�Ϊtxt��ʽ
			if(s.length == 2 && s[1].equals("txt")) {
				String path = fc.getCurrentDirectory() + File.separator + fileName;
				file = new File(path);
				//����ļ��Ƿ����
				if(!file.exists()) {	//д���ļ�
					try {
						TXTDataWriter.write(getEData(), file);
					} catch (IOException e) {
						SwingUtils.showErrorDialog(this, "д���ļ������쳣��Ϣ��" + e.getMessage());
					}
					
				}
				else {
					int n = SwingUtils.showConfirmDialog(this, "�ļ���" + fileName + "�Ѵ��ڣ�ȷ��Ҫ������");
					if(n == JOptionPane.YES_OPTION) {
						//�����ļ�
						try {
							TXTDataWriter.write(getEData(), file);
						} catch (IOException e) {
							e.printStackTrace();
							SwingUtils.showErrorDialog(this, "д���ļ�����");
						}
					}
					else if(n == JOptionPane.NO_OPTION) {
						return;
					}
					else {
						throw new RuntimeException("δ֪���󣺵���û�з��ع涨ֵ");
					}
				}
			}
			else {
				SwingUtils.showErrorDialog(this, "�ļ���" + fileName + "��ʽ����");
			}
			
		}
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
		updatePlotName();
	}

	public Axis[] getAxis() {
		return axis;
	}
	
	public void setAxis(List<Axis> a) {
		if (a == null) {
			return;
		}
		axis = new Axis[a.size()];
		for (int i = 0; i < axis.length; i++) {
			axis[i] = a.get(i);
			axis[i].addPropertyChangeListener(this);
		}
	}

	public String getPlotName() {
		return plotName;
	}

	public void setPlotName(String plotName) {
		this.plotName = plotName;
	}

	public int getPrevid() {
		return previd;
	}

	public void setPrevid(int previd) {
		this.previd = previd;
		
	}

	public int getNextid() {
		return nextid;
	}

	public void setNextid(int nextid) {
		this.nextid = nextid;
	}
	
	public int getDimension() {
		return axis.length;
	}
	
	public void setDimension(int d) {
		rtStra = d == 3 ? RegionThreshold.T3D : RegionThreshold.T2D;
	}
	
}
