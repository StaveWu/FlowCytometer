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
	 * 画圈门状态
	 */
	public static final int CREATE_GATE_STATUS = 1;
	
	/**
	 * 默认状态
	 */
	public static final int DEFAULT_STATUS = 0;
	
	/**
	 * 当前状态
	 */
	public int status = DEFAULT_STATUS;
	
	private int uid;
	private int previd = -1;
	private int nextid = -1;

	/**
	 * 轴
	 */
	protected Axis[] axis;
	
	/**
	 * 圈门
	 */
	protected Gate gate;
	
	/**
	 * 图名
	 */
	protected String plotName = "";
	
	/**
	 * 区域划分策略
	 */
	private IThresholdStrategy rtStra;
	
	/**
	 * 需要呈现的坐标是否改变
	 */
	private boolean isCoordsChanged = false;
	/**
	 * 坐标原点
	 */
	protected Point origin;
	
	/**
	 * 数据区
	 */
	private Rectangle dataRegion;
	
	/**
	 * 轴区
	 */
	private Rectangle[] axisRegions;
	
	/**
	 * 数据点索引
	 */
	private List<Integer> dataIds;
	
	/**
	 * 圈门数据索引
	 */
	private List<Integer> gatedIds;
	
	/**
	 * 数据模型，plot显示的数据点从这里面取
	 */
	private ITubeModel dataSource;
	
	/**
	 * 构造器
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
			a.addPropertyChangeListener(this);//注册监听
		}
		
		this.setSize(250, 250);
		/*
		 * 添加监听，注意：
		 * 注册监听器的时候，如果监听器之间有
		 * 先后顺序的关系，一定要按顺序添加
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
		
		//画名称
		if(plotName != null && !plotName.equals("")) {
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿
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
		
		//确定区域
		Rectangle plotRegion = new Rectangle(0, 0, getWidth(), getHeight());
		origin = rtStra.getOrigin(plotRegion);
		int[] lengths = rtStra.getAxisLengths(plotRegion);
		axisRegions[0] = rtStra.getAxisRegionX(plotRegion);
		axisRegions[1] = rtStra.getAxisRegionY(plotRegion);
		dataRegion = rtStra.getDataRegion(plotRegion);
		
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.draw(dataRegion);
		
		//画轴
		Graphics gAxis = g.create();
		gAxis.translate(origin.x, origin.y);
		
		axis[0].paint(gAxis, new Point(0, 0), lengths[0]);
		axis[1].paint(gAxis, new Point(0, 0), lengths[1]);
		if(axis.length > 2) {
			axisRegions[2] = rtStra.getAxisRegionZ(plotRegion);
			Point endPointY = axis[2].getEnd(new Point(0, 0), lengths[2]);
			axis[2].paint(gAxis, endPointY, lengths[2]);
		}
		
		//画数据
		ExperimentData eData = getEData();
		ExperimentData realCoords = getRealCoords(eData);
		if(isCoordsChanged) {
			setAxesRange(realCoords);
			repaint();
			isCoordsChanged = false;
		}
		ExperimentData pxData = preProcessAndPixelate(realCoords, lengths);
		paintData(gAxis, pxData);
		
		//画gate
		if(gate != null) {
			gate.paint(g);
			if (gate.isGenerated()) {
				gatedIds = getGatedIds(pxData, realCoords, eData);
			}
		}
		transmit();// 将索引一直往下抛
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
		for (int i = 0; i < dataSource.getFieldsCount(); i++) {		//i列
			double[] var = new double[dataIds.size()];				//每一列的变量
			for (int j = 0; j < var.length; j++) {					//j行
				int id = dataIds.get(j);							//第id行
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
	 * 设置轴范围
	 * @param realCoords
	 */
	void setAxesRange(ExperimentData realCoords) {
		if (realCoords == null) {
			return;
		}
		//更新轴的最大值
		double[] x = realCoords.getDataByName(axis[0].getName());
		double[] y = realCoords.getDataByName(axis[1].getName());
		if (x == null || y == null || x.length == 0 || y.length == 0) {
			return;
		}
		//如果不为空数组
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
	 * 预处理并像素化
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
			//同步数据处理策略
			daS[i] = axis[i].getAlgebraStrategy() == AxisAlgebra.LOG ? 
					DataAlgebraType.LOG : DataAlgebraType.LINEAR;
			//数据预处理
			String dataName = axis[i].getName();
			double[] processed = daS[i].process(realCoords.getDataByName(dataName));
			double[] pxData = daS[i].pixelate(processed, axis[i], pixLens[i]);
			res.add(dataName, pxData);
		}
		return res;
	}
	
	/**
	 * 获取被圈数据id
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
	 * 获取真实坐标
	 * 假设图为直方图，他的坐标应该是经过统计之后
	 * 横轴为经过排序了的真实值，纵轴为Count值的坐标
	 * @param srcData	实验数据
	 * @return
	 */
	protected abstract ExperimentData getRealCoords(ExperimentData srcData);
	
	/**
	 * 画数据
	 * @param g
	 * @param pxCoords
	 */
	public abstract void paintData(Graphics g, ExperimentData pxCoords);
	
	/**
	 * 获取数据区
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
	 * 圈门生成监听
	 * @author wteng
	 *
	 */
	private class GateGenerateListener extends MouseAdapter {
		
		private boolean isClicked = false;
		
		@Override
		public void mousePressed(MouseEvent arg0) {
			if(status == CREATE_GATE_STATUS) {
				/*
				 * 该gate在下拉菜单被点击的时候创建
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
	 * 圈门平移监听
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
	 * 圈门聚焦监听，当聚焦时显示小正方形
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
	 * 改变gate顶点位置的监听
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
	 * 下拉菜单监听
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
					//显示圈门菜单
					new GatePopupMenu(Plot.this, p).show(Plot.this, p.x, p.y);;
				}
				else if(dataRegion.contains(p)) {
					//显示数据区菜单
					new DataRegionPopupMenu(Plot.this).show(Plot.this, p.x, p.y);
				}
				else {
					//显示轴菜单
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
			//插入轴的设置item
			JMenuItem axisSetting = new JMenuItem("AxisSetting");
			axisSetting.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent arg0) {
					//弹出轴的设置框
					new AxisSettingBox(Plot.this.getLocationOnScreen(), axis,
							names);
				}
			});
			pm.add(axisSetting);
			pm.addSeparator();
			//插入现有的数据名item
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
	
	//////////gate菜单命令///////////
	/**
	 * 删除圈门
	 */
	public void deleteGate(Point p) {
		gate = null;
		repaint();
	}
	
	/**
	 * 重命名
	 */
	public void rename(Point p) {
		//在点击处添加文本域
		JTextField tf = new JTextField();
		tf.setBounds(p.x, p.y, 60, 25);
		tf.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//改变名称
				gate.setName(tf.getText());
				//删除文本域
				Plot.this.remove(tf);
				repaint();
			}
		});
		Plot.this.add(tf);
	}
	
	/**
	 * 改变颜色
	 */
	public void changeColor(Point p) {
		//弹出颜色选择框
		new ColorChooserBox(Plot.this, p);
	}

	
	
	
	//////////plot菜单命令///////////
	/**
	 * 删除本图
	 */
	public void deletePane() {
		dispose();
	}
	
	/**
	 * 创建矩形圈门
	 */
	public void createRectangleGate() {
		setGate(new RectangleGate());
		setStatus(CREATE_GATE_STATUS);
		repaint();
	}
	
	/**
	 * 创建多边形圈门
	 */
	public void createPolygonGate() {
		setGate(new PolygonGate());
		setStatus(CREATE_GATE_STATUS);
		repaint();
	}
	
	/**
	 * 创建水平界圈门
	 */
	public void createHorizontalGate() {
		setGate(new HorizontalGate(dataRegion));
		setStatus(CREATE_GATE_STATUS);
		repaint();
	}
	
	/**
	 * 创建竖直界圈门
	 */
	public void createVerticalGate() {
		setGate(new VerticalGate(dataRegion));
		setStatus(CREATE_GATE_STATUS);
		repaint();
	}
	
	/**
	 * 删除输入引用
	 */
	public void deleteInputReference() {
		deletePrev();
		observer.repaintArrows();
	}
	
	/**
	 * 删除导出引用
	 */
	public void deleteOutputReference() {
		deleteNext();
		observer.repaintArrows();
	}
	
	/**
	 * 删除所有引用
	 */
	public void deleteAllRefenrence() {
		deleteInputReference();
		deleteOutputReference();
	}
	
	/**
	 * 从fcs文件里导入数据
	 */
	public void importDataFromFCS() {
		//创建文件选择器
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.fcs", "fcs");
		fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			// 导入数据
			File file = fc.getSelectedFile();
			try {
				// 初始化tubeModel并添加dataIds
				dataSource.initFromFcs(file);
				List<Integer> total = new ArrayList<>();
				for (int i = 0; i < dataSource.getEventsCount(); i++) {
					total.add(i);
				}
				setDataIds(total);
				notifyParentDataIuputed();
			} catch (Exception e) {
				SwingUtils.showErrorDialog(this, "文件读取错误！异常信息：" + e.getMessage());
			}
		}
	}
	
	/**
	 * 从txt文件里导入数据
	 */
	public void importDataFromTXT() {
		//创建文件选择器
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt");
		fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			//读取并导入数据
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
				SwingUtils.showErrorDialog(this, "文件读取错误！异常信息：" + e.getMessage());
			}
			repaint();
		}
	}
	
	/**
	 * 通知容器数据导入了
	 */
	private void notifyParentDataIuputed() {
		PlotContainer parent = (PlotContainer) this.getParent();
		parent.notifyDataInputed();
	}
	
	/**
	 * 导出数据为FCS文件
	 */
	public void exportDataToFCS() {
		//创建文件选择器
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.fcs", "fcs");
		fc.setFileFilter(filter);
		int returnVal = fc.showSaveDialog(this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String fileName = file.getName();
			String[] s = fileName.split("\\.");
			System.out.println(fileName + " " + s.length);
			//检查文件名是否为fcs格式
			if(s.length == 2 && s[1].equals("fcs")) {
				String path = fc.getCurrentDirectory() + File.separator + fileName;
				file = new File(path);
				//检查文件是否存在
				if(!file.exists()) {
					//写入文件
					FCSDataWriter.write(getEData(), file);
				}
				else {
					int n = SwingUtils.showConfirmDialog(this, 
							"文件名" + fileName + "已存在，确定要覆盖吗？");
					if(n == JOptionPane.YES_OPTION) {
						//覆盖文件
						FCSDataWriter.write(getEData(), file);
					}
					else if(n == JOptionPane.NO_OPTION) {
						return;
					}
					else {
						throw new RuntimeException("未知错误：弹窗没有返回规定值");
					}
				}
			}
			else {
				SwingUtils.showErrorDialog(this, "文件名" + fileName + "格式错误！");
			}
			
		}
	}
	
	/**
	 * 导出数据为TXT文件
	 */
	public void exportDataToTXT() {
		//创建文件选择器
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt");
		fc.setFileFilter(filter);
		int returnVal = fc.showSaveDialog(this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			
			File file = fc.getSelectedFile();
			String fileName = file.getName();
			String[] s = fileName.split("\\.");
			//检查文件名是否为txt格式
			if(s.length == 2 && s[1].equals("txt")) {
				String path = fc.getCurrentDirectory() + File.separator + fileName;
				file = new File(path);
				//检查文件是否存在
				if(!file.exists()) {	//写入文件
					try {
						TXTDataWriter.write(getEData(), file);
					} catch (IOException e) {
						SwingUtils.showErrorDialog(this, "写入文件错误！异常信息：" + e.getMessage());
					}
					
				}
				else {
					int n = SwingUtils.showConfirmDialog(this, "文件名" + fileName + "已存在，确定要覆盖吗？");
					if(n == JOptionPane.YES_OPTION) {
						//覆盖文件
						try {
							TXTDataWriter.write(getEData(), file);
						} catch (IOException e) {
							e.printStackTrace();
							SwingUtils.showErrorDialog(this, "写入文件错误！");
						}
					}
					else if(n == JOptionPane.NO_OPTION) {
						return;
					}
					else {
						throw new RuntimeException("未知错误：弹窗没有返回规定值");
					}
				}
			}
			else {
				SwingUtils.showErrorDialog(this, "文件名" + fileName + "格式错误！");
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
