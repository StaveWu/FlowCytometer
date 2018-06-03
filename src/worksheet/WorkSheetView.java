package worksheet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import mainPage.events.WorkSheetEvent;
import menu.ContainerPopupMenu;
import plotContainer.PLotContainerObserver;
import plotContainer.PlotContainer;
import utils.SwingUtils;
import worksheet.interfaces.IWorkSheetController;
import worksheet.interfaces.IWorkSheetModel;

@SuppressWarnings("serial")
public class WorkSheetView extends JPanel implements PLotContainerObserver {
	
	private IWorkSheetModel model;
	private IWorkSheetController controller;
	
	private PlotContainer plotContainer;
	private JToolBar toolBar;
	private JButton saveButton;
	
	private boolean showPopupMenu = true;
	
	public WorkSheetView(IWorkSheetModel model, IWorkSheetController controller) {
		this.model = model;
		this.controller = controller;
	}
	
	public void initializeComponents() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		/*
		 * Í¼ÈÝÆ÷
		 */
		plotContainer = new PlotContainer(model.getDelegate());
		plotContainer.addObserver(this);
		plotContainer.addMouseListener(new PopupMenuListener());
		plotContainer.setPreferredSize(new Dimension(500, 3000));
		
		/*
		 * ÎªÈÝÆ÷Ìí¼Ó¹ö¶¯Ìõ
		 */
		JScrollPane scrollPane = new JScrollPane(plotContainer);
		add(scrollPane, BorderLayout.CENTER);
		
		/*
		 * ¹¤¾ßÀ¸
		 */
		toolBar = new JToolBar();
		
		/*
		 * °´Å¥
		 */
		saveButton = new JButton("±£´æ");
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					model.save();
				} catch (Exception e1) {
					e1.printStackTrace();
					SwingUtils.showErrorDialog(WorkSheetView.this, "±£´æÊ§°Ü£¡" + e1.getMessage());
				}
			}
		});
		toolBar.add(saveButton);
		
//		JButton btnload = new JButton("load");
//		btnload.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				try {
//					model.init("");
//					repaint();
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//		});
//		toolBar.add(btnload);
		
		this.add(toolBar, BorderLayout.NORTH);
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = WorkSheetView.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
	
	/**
	 * ÓÒ»÷²Ëµ¥¼àÌý
	 * @author wteng
	 *
	 */
	private class PopupMenuListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON3 && showPopupMenu) {
				Point p = e.getPoint();
				new ContainerPopupMenu(controller, p).show(
						plotContainer, p.x, p.y);
			}
		}
	}
	
	public void enableEdit() {
		plotContainer.setBackground(Color.WHITE);
		showPopupMenu = true;
		repaint();
	}
	
	public void disableEdit() {
		plotContainer.setBackground(Color.LIGHT_GRAY);
		showPopupMenu = false;
		repaint();
	}

	@Override
	public void notifyDataInputed() {
		controller.notifyObservers(
				new WorkSheetEvent(controller, WorkSheetEvent.DataInputFromOutside));
	}
}
