package plot.axis;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class AxisSettingBox {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JComboBox<String> comboBox;
	private JRadioButton rdbtnNewRadioButton_1;
	private JRadioButton rdbtnNewRadioButton;
	
	private Axis axis;
	private Point location;
	private List<String> names;

	/**
	 * Create the application.
	 */
	public AxisSettingBox(Point location, Axis axis, List<String> names) {
		this.location = location;
		this.axis = axis;
		this.names = names;
		initialize();
		
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Calibri", Font.PLAIN, 16));
		frame.setBounds(location.x, location.y, 244, 240);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("AxisName:");
		lblNewLabel.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 10, 83, 28);
		frame.getContentPane().add(lblNewLabel);
		
		if (names == null) {
			comboBox = new JComboBox<>();
		} else {
			String[] nameStr = new String[names.size()];
			for (int i = 0; i < nameStr.length; i++) {
				nameStr[i] = names.get(i);
			}
			comboBox = new JComboBox<>(nameStr);
		}
		comboBox.setFont(new Font("Calibri", Font.PLAIN, 12));
		comboBox.setBounds(10, 34, 91, 21);
		comboBox.setSelectedItem(axis.getName());
		frame.getContentPane().add(comboBox);
		
		JLabel lblMinvalue = new JLabel("MinValue");
		lblMinvalue.setHorizontalAlignment(SwingConstants.CENTER);
		lblMinvalue.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblMinvalue.setBounds(13, 77, 77, 28);
		frame.getContentPane().add(lblMinvalue);
		
		JLabel lblMaxvalue = new JLabel("MaxValue");
		lblMaxvalue.setHorizontalAlignment(SwingConstants.CENTER);
		lblMaxvalue.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblMaxvalue.setBounds(118, 77, 77, 28);
		frame.getContentPane().add(lblMaxvalue);
		/*
		 * min文本
		 */
		textField = new JTextField();
		textField.setText("0");
		textField.setFont(new Font("Calibri", Font.PLAIN, 12));
		textField.setBounds(10, 105, 83, 21);
		textField.setText(axis.getMinValue() + "");
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		/*
		 * max文本
		 */
		textField_1 = new JTextField();
		textField_1.setText("160000");
		textField_1.setFont(new Font("Calibri", Font.PLAIN, 12));
		textField_1.setBounds(115, 105, 83, 21);
		textField_1.setText(axis.getMaxValue() + "");
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		rdbtnNewRadioButton = new JRadioButton("log");
		rdbtnNewRadioButton.setSelected(true);
		rdbtnNewRadioButton.setFont(new Font("Calibri", Font.PLAIN, 12));
		rdbtnNewRadioButton.setBounds(125, 13, 70, 23);
		frame.getContentPane().add(rdbtnNewRadioButton);
		
		rdbtnNewRadioButton_1 = new JRadioButton("linear");
		rdbtnNewRadioButton_1.setSelected(true);
		rdbtnNewRadioButton_1.setFont(new Font("Calibri", Font.PLAIN, 12));
		rdbtnNewRadioButton_1.setBounds(125, 48, 68, 23);
		frame.getContentPane().add(rdbtnNewRadioButton_1);
		
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnNewRadioButton_1);
		group.add(rdbtnNewRadioButton);
		if(axis.getAlgebraStrategy() == AxisAlgebra.LINEAR) {
			rdbtnNewRadioButton_1.setSelected(true);
		}
		else {
			rdbtnNewRadioButton.setSelected(true);
		}
		
		JLabel label = new JLabel("-");
		label.setFont(new Font("Calibri", Font.PLAIN, 12));
		label.setBounds(101, 107, 23, 15);
		frame.getContentPane().add(label);
		
		JLabel lblNewLabel_1 = new JLabel("(range = 0 - 16,777,215)");
		lblNewLabel_1.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(10, 136, 130, 15);
		frame.getContentPane().add(lblNewLabel_1);
		
		JButton button = new JButton("确定");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				/*
				 * 检验有效性
				 */
				String minStr = textField.getText();
				String maxStr = textField_1.getText();
				double min = checkNumber(minStr) ? Double.parseDouble(minStr) : -1;
				double max = checkNumber(maxStr) ? Double.parseDouble(maxStr) : -1;
				if(min != -1) {
					textField.setBackground(Color.WHITE);
				}
				else {
					textField.setBackground(Color.YELLOW);
					return;
				}
				if(max != -1) {
					textField_1.setBackground(Color.WHITE);
				}
				else {
					textField_1.setBackground(Color.YELLOW);
					return;
				}
				if(max <= min) {
					JOptionPane.showMessageDialog(frame,
                            "区间错误！");
					return;
				}
				
				//修改大小值
				axis.setMinValue(min);
				axis.setMaxValue(max);
				//修改名称
				axis.setName((String)comboBox.getSelectedItem());
				//修改代数策略
				if(rdbtnNewRadioButton_1.isSelected()) {
					axis.setAlgebraStrategy(AxisAlgebra.LINEAR);
				}
				else {
					// 过滤最小值为0的情况
					if (axis.getMinValue() == 0) {
						axis.setMinValue(1);
					}
					axis.setAlgebraStrategy(AxisAlgebra.LOG);
				}
				frame.dispose();
				
			}
		});
		button.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		button.setBounds(72, 162, 60, 23);
		frame.getContentPane().add(button);
		
		JButton button_1 = new JButton("关闭");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				//关闭当前窗体
				frame.dispose();
			}
		});
		button_1.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		button_1.setBounds(150, 162, 60, 23);
		frame.getContentPane().add(button_1);
	}
	
	private boolean checkNumber(String text) {
		//检查参数有效性
		return Pattern.matches("^\\d+\\.\\d+$", text) ||
				Pattern.matches("^\\d+$", text);
	}
}
