package plot.axis;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.StringUtils;

@SuppressWarnings("serial")
public class HistogramAxisSettingBox extends JFrame implements ActionListener {
	
	private Axis axis;
	private Point location;
	
	private JLabel lbMax;
	private JLabel lbMin;
	private JTextField tfMax;
	private JTextField tfMin;
	
	private JButton btnYes;
	private JButton btnNo;
	
	/**
	 * Create the application.
	 */
	public HistogramAxisSettingBox(Point location, Axis axis) {
		this.location = location;
		this.axis = axis;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.getContentPane().setFont(new Font("Calibri", Font.PLAIN, 16));
		this.setBounds(location.x, location.y, 244, 240);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		
		this.getContentPane().add(createMinMaxPanel());
		this.getContentPane().add(createButtonPanel());
		
		this.pack();
		this.setVisible(true);
	}
	
	private JPanel createMinMaxPanel() {
		lbMin = new JLabel("最小值:");
		lbMax = new JLabel("最大值:");
		tfMin = new JTextField();
		tfMax = new JTextField();
		
		JPanel res = new JPanel();
		
		GroupLayout layout = new GroupLayout(res);
		res.setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
		hGroup.addGroup(layout.createParallelGroup().addComponent(lbMin).addComponent(lbMax));
		hGroup.addGroup(layout.createParallelGroup().addComponent(tfMin).addComponent(tfMax));
		layout.setHorizontalGroup(hGroup);
		
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(lbMin).addComponent(tfMin));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(lbMax).addComponent(tfMax));
		layout.setVerticalGroup(vGroup);
		
		return res;
	}
	
	private JPanel createButtonPanel() {
		btnYes = new JButton("确定");
		btnYes.addActionListener(this);
		btnNo = new JButton("取消");
		btnNo.addActionListener(this);
		
		JPanel res = new JPanel();
		res.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 10));
		res.add(btnYes);
		res.add(btnNo);
		
		return res;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnYes) {
			if (checkValid()) {
				axis.setMinValue(Double.parseDouble(tfMin.getText()));
				axis.setMaxValue(Double.parseDouble(tfMax.getText()));
				this.dispose();
			}
		}
		if (e.getSource() == btnNo) {
			this.dispose();
		}
	}
	
	private boolean checkValid() {
		return checkValid(tfMin) && checkValid(tfMax);
	}
	
	private boolean checkValid(JTextField tf) {
		if (StringUtils.isNumber(tf.getText())) {
			tf.setBackground(Color.WHITE);
			return true;
		}
		else {
			tf.setBackground(Color.YELLOW);
			return false;
		}
	}

}
