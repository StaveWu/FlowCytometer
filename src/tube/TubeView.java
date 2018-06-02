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
		 * ���
		 */
		table = new JTable(model.getDelegate());
		JScrollPane scrollPane = new JScrollPane(table);
		this.add(scrollPane, BorderLayout.CENTER);
		
		/*
		 * ������
		 */
		toolBar = new JToolBar();
		
		/*
		 * ��ť
		 */
		saveButton = new JButton("����");
		saveButton.addMouseListener(this);
		toolBar.add(saveButton);
		
		this.add(toolBar, BorderLayout.NORTH);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
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
				if(!file.exists()) {
					//д���ļ�
					try {
						model.save(file);
					} catch (Exception e) {
						e.printStackTrace();
						SwingUtils.showErrorDialog(this, "����ʧ�ܣ��쳣��Ϣ��" + e.getMessage());
					}
				}
				else {
					int n = SwingUtils.showConfirmDialog(this, "�ļ���" + fileName + "�Ѵ��ڣ�ȷ��Ҫ������");
					if(n == JOptionPane.YES_OPTION) {
						//�����ļ�
						try {
							model.save(file);
						} catch (Exception e) {
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
