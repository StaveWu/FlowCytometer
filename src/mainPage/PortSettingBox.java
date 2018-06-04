package mainPage;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;

import device.CommDeviceType;
import device.ICommDevice;
import device.SerialTool;
import utils.SwingUtils;

@SuppressWarnings("serial")
public class PortSettingBox extends JFrame implements ActionListener {
	
	private JTabbedPane tabbedPane;
	private JButton btnConnect;
	private JButton btnDisconnect;
	private JRadioButton rbtnSerialPort;
	private JRadioButton rbtnUsb;
	private JComboBox<String> combo1;
	private JComboBox<Integer> combo2;
	
	private ICommDevice device;
	
	/**
	 * Create the application.
	 */
	public PortSettingBox() {
		initialize();
		this.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setBounds(500, 300, 300, 250);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("串口", makeSerialPortPane());
		tabbedPane.addTab("USB", makeUsbPortPane());
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.getContentPane().add(tabbedPane);
		
		/*
		 * 串口、USB选择栏
		 */
		rbtnSerialPort = new JRadioButton("串口");
		rbtnSerialPort.setSelected(true);
		rbtnUsb = new JRadioButton("USB");
		rbtnUsb.setSelected(false);
		
		ButtonGroup group = new ButtonGroup();
		group.add(rbtnSerialPort);
		group.add(rbtnUsb);
		if (FCMSettings.getCommDeviceType() == CommDeviceType.SERIALPORT) {
			rbtnSerialPort.setSelected(true);
		}
		else if (FCMSettings.getCommDeviceType() == CommDeviceType.USB) {
			rbtnUsb.setSelected(true);
		}
		
		JPanel rbtnPanel = new JPanel();
		rbtnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
		rbtnPanel.add(rbtnSerialPort);
		rbtnPanel.add(rbtnUsb);
		this.getContentPane().add(rbtnPanel);
		
		
		/*
		 * 连接按钮 
		 */
		btnConnect = new JButton("连接");
		btnConnect.addActionListener(this);
		btnConnect.setPreferredSize(new Dimension(80, 26));
		
		/*
		 * 断开按钮
		 */
		btnDisconnect = new JButton("断开");
		btnDisconnect.addActionListener(this);
		btnDisconnect.setPreferredSize(new Dimension(80, 26));
		
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
		btnPanel.add(btnConnect);
		btnPanel.add(btnDisconnect);
		
		this.getContentPane().add(btnPanel);
		this.pack();
	}
	
	private JPanel makeSerialPortPane() {
		JLabel label1 = new JLabel("端口:");
		JLabel label2 = new JLabel("波特率:");
		/*
		 * 端口下拉框
		 */
		combo1 = new JComboBox<>();
		int i = 0;
		for (String ele : SerialTool.getInstance().getPortNames()) {
			combo1.addItem(ele);
			if (ele.equals(FCMSettings.getSerialPortName())) {
				combo1.setSelectedIndex(i);
			}
			i++;
		}
		
		/*
		 * 波特率下拉框
		 */
		combo2 = new JComboBox<>();
		int[] baudrates = {4800, 9600, 19200, 38400, 57600, 115200};
		for (int j = 0; j < baudrates.length; j++) {
			combo2.addItem(baudrates[j]);
			if (baudrates[j] == FCMSettings.getBaudRate()) {
				combo2.setSelectedIndex(j);
			}
		}
		
		JPanel p = new JPanel();
		GroupLayout layout = new GroupLayout(p);
		p.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
		hGroup.addGroup(layout.createParallelGroup().addComponent(label1).addComponent(label2));
		hGroup.addGroup(layout.createParallelGroup().addComponent(combo1).addComponent(combo2));
		layout.setHorizontalGroup(hGroup);
		
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label1).addComponent(combo1));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label2).addComponent(combo2));
		layout.setVerticalGroup(vGroup);
		
		return p;
	}
	
	private JPanel makeUsbPortPane() {
		JPanel p = new JPanel();
		return p;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnConnect) {
			// 保存设置
			saveSerialPortConfig();
			saveUsbConfig();
			saveSelectedCommDevice();
			try {
				connectDevice();
				this.dispose();
			} catch (Exception e1) {
				e1.printStackTrace();
				SwingUtils.showErrorDialog(this, "连接设备失败！");
			}
		}
		else if (e.getSource() == btnDisconnect) {
			disconnectDevice();
			this.dispose();
		}
		else {
			throw new RuntimeException("不可能到达的位置");
		}
	}
	
	private void saveSerialPortConfig() {
		FCMSettings.setSerialPortName((String) combo1.getSelectedItem());
		FCMSettings.setBaudRate((int) combo2.getSelectedItem());
	}
	
	private void saveUsbConfig() {
		// 还没写
	}
	
	private void saveSelectedCommDevice() {
		if (rbtnSerialPort.isSelected()) {
			FCMSettings.setCommDeviceType(CommDeviceType.SERIALPORT);
		}
		else if (rbtnUsb.isSelected()) {
			FCMSettings.setCommDeviceType(CommDeviceType.USB);
		}
	}
	
	private void connectDevice() throws Exception {
		if (rbtnSerialPort.isSelected()) {
			device = SerialTool.getInstance();
			device.open();
		}
		else if (rbtnUsb.isSelected()) {
			// 还没写
		}
	}
	
	private void disconnectDevice() {
		if (device != null) {
			device.close();
		}
	}
	

}
