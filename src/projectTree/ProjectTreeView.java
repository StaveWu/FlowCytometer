package projectTree;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import menu.PopupMenuX;
import menu.ProjectTreeMenu;

@SuppressWarnings("serial")
public class ProjectTreeView extends JPanel {
	
	private IProjectTreeModel model;
	
	private IProjectTreeController controller;
	
	private JTree tree;
	private PopupMenuX treeMenu;

	public ProjectTreeView(IProjectTreeModel model, IProjectTreeController controller) {
		this.model = model;
		this.controller = controller;
	}
	
	public void initializeComponent() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		tree = new JTree(model.getDelegate());
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addMouseListener(new PopupMenuListener());
		tree.addMouseListener(new FileOpenActionListener());
		tree.setInvokesStopCellEditing(true);
		tree.setEditable(true);
		
		JScrollPane scrollPane = new JScrollPane(tree);
		this.add(scrollPane, BorderLayout.CENTER);
		
		treeMenu = new ProjectTreeMenu(controller);
	}
	
	private class PopupMenuListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				if (tree.getSelectionPath() == null) {
					return;
				}
				DefaultMutableTreeNode curSelection = 
						(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				treeMenu.setSource(curSelection);
				treeMenu.show(ProjectTreeView.this, e.getX(), e.getY());
			}
		}
	}
	
	private class FileOpenActionListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
				// 根据路径打开文件
				DefaultMutableTreeNode curSelection = 
						(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (curSelection == null) {
					return;
				}
				controller.openNode(curSelection);
			}
		}
	}
	
	public void startEditing(DefaultMutableTreeNode node) {
		tree.startEditingAtPath(new TreePath(node.getPath()));
	}
	
	public void scrollPathToVisible(DefaultMutableTreeNode node) {
		tree.scrollPathToVisible(new TreePath(node.getPath()));
	}

}
