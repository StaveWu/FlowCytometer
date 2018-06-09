package mainPage;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.SwingUtils;

@SuppressWarnings("serial")
public class WorkSpaceChooserBox extends JFrame implements MouseListener, WindowListener {
	
	private JLabel queryLabel;
	private JButton yesBtn;
	private JButton cancelBtn;
	private JTextField pathField;
	private JButton pathChooseBtn;
	private JCheckBox checkBox;
	private JLabel label;
	
	private JPanel topPanel;
	private JPanel midPanel;
	private JPanel bottomPanel;

	/**
	 * Create the application.
	 */
	public WorkSpaceChooserBox() {
		initialize();
		this.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setBounds(500, 300, 400, 170);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		this.addWindowListener(this);
		
		/*
		 * 上面板
		 */
		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		
		/*
		 * 中间面板
		 */
		midPanel = new JPanel();
		midPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
		
		/*
		 * 下面板
		 */
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
		
		/*
		 * 标签
		 */
		queryLabel = new JLabel("工作空间:");
		topPanel.add(queryLabel);
		
		/*
		 * 文本域
		 */
		pathField = new JTextField();
		pathField.setPreferredSize(new Dimension(230, 26));
		topPanel.add(pathField);
		
		/*
		 * 文件选择按钮
		 */
		pathChooseBtn = new JButton("...");
		pathChooseBtn.addMouseListener(this);
		topPanel.add(pathChooseBtn);
		
		/*
		 * 确定按钮
		 */
		yesBtn = new JButton("确定");
		yesBtn.addMouseListener(this);
		midPanel.add(yesBtn);
		
		/*
		 * 取消按钮
		 */
		cancelBtn = new JButton("取消");
		cancelBtn.addMouseListener(this);
		midPanel.add(cancelBtn);
		
		/*
		 * checkBox
		 */
		checkBox = new JCheckBox();
		bottomPanel.add(checkBox);
		
		/*
		 * label
		 */
		label = new JLabel("下次不再提示");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		bottomPanel.add(label);
		
		this.getContentPane().add(topPanel);
		this.getContentPane().add(midPanel);
		this.getContentPane().add(bottomPanel);
		
	}

	@Override
	public void mouseClicked(MouseEvent ev) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent ev) {
		if (ev.getButton() == MouseEvent.BUTTON1) {
			if (ev.getSource() == pathChooseBtn) {
				//打开文件选择器
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					String path = fc.getCurrentDirectory() + File.separator + file.getName();
					pathField.setText(path);
				}
			} else if (ev.getSource() == yesBtn) {
				// 检查文件是否存在
				File file = new File(pathField.getText());
				if (!file.exists()) {
					SwingUtils.showErrorDialog(this, "路径不存在！请检查路径是否输入正确");
				} else {
					// 写入配置文件
					String path = pathField.getText();
					String res = path.replaceAll("\\\\", "/");
					FCMSettings.setWorkSpacePath(res);
					FCMSettings.setBootSpaceChooserBox(checkBox.isSelected());
					this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
				}
			} else if (ev.getSource() == cancelBtn) {
				// 直接关闭程序
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// 弹出主窗体
		new MainView();
	}

	@Override
	public void windowClosing(WindowEvent ev) {
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
