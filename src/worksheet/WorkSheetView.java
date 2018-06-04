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
import plot.Plot;
import plotContainer.PLotContainerObserver;
import plotContainer.PlotContainer;
import tube.ITubeModel;
import utils.SwingUtils;
import worksheet.interfaces.IWorkSheetController;

@SuppressWarnings("serial")
public class WorkSheetView extends JPanel implements PLotContainerObserver {
	
	private IWorkSheetController controller;
	
	private PlotContainer plotContainer;
	private JToolBar toolBar;
	private JButton saveButton;
	
	private boolean showPopupMenu = true;
	
	public WorkSheetView(IWorkSheetController controller) {
		this.controller = controller;
	}
	
	public void initializeComponents() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		/*
		 * 图容器
		 */
		plotContainer = new PlotContainer();
		plotContainer.addObserver(this);
		plotContainer.addMouseListener(new PopupMenuListener());
		plotContainer.setPreferredSize(new Dimension(500, 3000));
		
		/*
		 * 为容器添加滚动条
		 */
		JScrollPane scrollPane = new JScrollPane(plotContainer);
		add(scrollPane, BorderLayout.CENTER);
		
		/*
		 * 工具栏
		 */
		toolBar = new JToolBar();
		
		/*
		 * 按钮
		 */
		saveButton = new JButton("保存");
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					save();
				} catch (Exception e1) {
					e1.printStackTrace();
					SwingUtils.showErrorDialog(WorkSheetView.this, "保存失败！" + e1.getMessage());
				}
			}
		});
		toolBar.add(saveButton);
		
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
	 * 右击菜单监听
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
	
	
	///////// plotContainer Controller ///////////
	// 因为model分不出来，所以把Controller写在这里
	
	public void loadPlots(String pathname) throws Exception {
		plotContainer.loadPlots(pathname);
		repaint();
	}
	
	public void save() throws Exception {
		plotContainer.savePlots();
	}
	
	public void addPlot(Plot plot) {
		plotContainer.add(plot);
		repaint();
	}
	
	public void addDataSource(ITubeModel data) {
		plotContainer.setDataSource(data);
	}
}
