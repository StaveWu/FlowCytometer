package worksheet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import mainPage.events.WorkSheetEvent;
import menu.ContainerPopupMenu;
import plotContainer.PLotContainerObserver;
import plotContainer.PlotContainer;
import worksheet.interfaces.IWorkSheetController;
import worksheet.interfaces.IWorkSheetModel;

public class WorkSheetView extends JPanel implements PLotContainerObserver, MouseListener {
	
	private IWorkSheetModel model;
	private IWorkSheetController controller;
	
	private PlotContainer plotContainer;
	private JToolBar toolBar;
	private JButton saveButton;
	
	private boolean showPopupMenu = false;
	
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
		saveButton.addMouseListener(this);
		toolBar.add(saveButton);
		
		this.add(toolBar, BorderLayout.NORTH);
	}
	
	/**
	 * ÓÒ»÷²Ëµ¥¼àÌý
	 * @author wteng
	 *
	 */
	private class PopupMenuListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON3 && showPopupMenu) {
				new ContainerPopupMenu(controller, e.getPoint()).show(
						WorkSheetView.this, e.getX(), e.getY());
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

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
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
