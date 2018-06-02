package tube;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import utils.SwingUtils;

public class TubeView extends JPanel implements MouseListener {
	
	private ITubeModel model;
	
	public JButton saveButton;
	public JToolBar toolBar;
	public JTable table;
	
	public TubeView(ITubeModel model) {
		this.model = model;
		initComponents();
	}
	
	public void initComponents() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		/*
		 * 表格
		 */
		table = new JTable(model.getDelegate());
		JScrollPane scrollPane = new JScrollPane(table);
		this.add(scrollPane, BorderLayout.CENTER);
		
		/*
		 * 工具栏
		 */
		toolBar = new JToolBar();
		
		/*
		 * 按钮
		 */
		saveButton = new JButton("保存");
		saveButton.addMouseListener(this);
		toolBar.add(saveButton);
		
		this.add(toolBar, BorderLayout.NORTH);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
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
				if(!file.exists()) {
					//写入文件
					try {
						model.save(file);
					} catch (Exception e) {
						e.printStackTrace();
						SwingUtils.showErrorDialog(this, "保存失败！异常信息：" + e.getMessage());
					}
				}
				else {
					int n = SwingUtils.showConfirmDialog(this, "文件名" + fileName + "已存在，确定要覆盖吗？");
					if(n == JOptionPane.YES_OPTION) {
						//覆盖文件
						try {
							model.save(file);
						} catch (Exception e) {
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
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
