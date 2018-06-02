package dashBoard;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.SwingUtils;

public class DashBoardView extends JPanel implements ActionListener, ItemListener {
	
	private DashBoardController controller;
	private DashBoardModel model;
	
	//���
	private JLabel limitLabel;
	private JComboBox<String> comboBox;
	private JCheckBox checkBox;
	private JButton startBtn;
	private JButton stopBtn;
	private JLabel unitLabel;
	private JTextField tf;
	private JLabel statusLabel;
	
	public DashBoardView(DashBoardModel model, DashBoardController controller) {
		this.controller = controller;
		this.model = model;
	}
	
	public void initializeComponent() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		/*
		 * Լ������
		 */
		limitLabel = new JLabel("Լ������:");
		
		/*
		 * ������
		 */
		comboBox = new JComboBox<>(new String[] {"��ʱ��", "������"});
		comboBox.addItemListener(this);
		
		/*
		 * �ı���
		 */
		tf = new JTextField();
		tf.setPreferredSize(new Dimension(100, 26));

		/*
		 * ��λ
		 */
		unitLabel = new JLabel("hours");
		
		/*
		 * checkBox
		 */
		checkBox = new JCheckBox("ɸѡ");
		
		/*
		 * Լ�����
		 */
		JPanel limitPanel = new JPanel();
		limitPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		limitPanel.setPreferredSize(new Dimension(450, 60));
		limitPanel.add(limitLabel);
		limitPanel.add(comboBox);
		limitPanel.add(tf);
		limitPanel.add(unitLabel);
		limitPanel.add(checkBox);
		this.add(SwingUtils.createPanelForComponent(limitPanel, "Լ��"));
		
		/*
		 * ��ʼ������ť
		 */
		startBtn = new JButton("��ʼ����");
		startBtn.addActionListener(this);
		/*
		 * ֹͣ������ť
		 */
		stopBtn = new JButton("ֹͣ����");
		stopBtn.addActionListener(this);
		
		/*
		 * ��ť���
		 */
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
		btnPanel.add(startBtn);
		btnPanel.add(stopBtn);
		this.add(SwingUtils.createPanelForComponent(btnPanel, "����"));
		
		/*
		 * ״̬Label
		 */
		statusLabel = new JLabel("��ǰ״̬���޶���");
		statusLabel.setBounds(10, 0, 150, 26);
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		statusPanel.add(statusLabel);
		this.add(SwingUtils.createPanelForComponent(statusPanel, "����"));
	}
	
	public void enableCheckBox() {
		checkBox.setEnabled(true);
	}
	
	public void disableCheckBox() {
		checkBox.setEnabled(false);
	}
	
	public void setHourUnit() {
		unitLabel.setText("hours");
	}
	
	public void setEventUnit() {
		unitLabel.setText("events");
	}
	
	public void setStatusStart() {
		statusLabel.setText("��ǰ״̬�����ڲ���...");
	}
	public void setStatusStop() {
		statusLabel.setText("��ǰ״̬������ֹͣ");
	}
	
	public void enableStartButton() {
		startBtn.setEnabled(true);
	}
	
	public void disableStartButton() {
		startBtn.setEnabled(false);
	}
	
	public void enableStopButton() {
		stopBtn.setEnabled(true);
	}
	
	public void disableStopButton() {
		stopBtn.setEnabled(false);
	}
	
	public boolean isSelectTimeCondition() {
		return comboBox.getSelectedItem().equals("��ʱ��");
	}
	
	public boolean isSelectEventCondition() {
		return comboBox.getSelectedItem().equals("������");
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if (ev.getSource() == startBtn) {
			controller.startSampling();
		} else if (ev.getSource() == stopBtn) {
			controller.stopSampling();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent ev) {
		controller.checkSelected();
	}
	
	public void setCheckBox(boolean select) {
		checkBox.setSelected(select);
	}

}
