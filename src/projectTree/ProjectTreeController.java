package projectTree;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import dao.beans.DirTreeBean;
import mainPage.events.DirTreeEvent;
import mainPage.interfaces.DirTreeObserver;
import utils.SwingUtils;

public class ProjectTreeController implements IProjectTreeController {
	
	private static final String DefaultName_Settings = "Settings";
	private static final String DefaultName_Worksheet = "WorkSheet";
	
	private IProjectTreeModel model;
	private ProjectTreeView view;
	private List<DirTreeObserver> observers = new ArrayList<>();
	
	public ProjectTreeController(IProjectTreeModel model) {
		this.model = model;
		view = new ProjectTreeView(model, this);
		view.initializeComponent();
	}

	@Override
	public void createExperimentSolution(DefaultMutableTreeNode parent) {
		final String name = SwingUtils.showInputDialog(view, "������ʵ�����ƣ�");
		
		DirTreeBean nodeInfo = new DirTreeBean(ProjectTreeUtils.getNextLid(parent),
				name, NodeType.EXPERIMENT_SOLUTION, FileType.FOLDER);
		DefaultMutableTreeNode experSolution = new DefaultMutableTreeNode(nodeInfo);
		try {
			// ����ʵ�鷽���ļ���
			model.insert(experSolution, parent);
			
			// ����Settings�ļ�
			nodeInfo = new DirTreeBean(ProjectTreeUtils.getNextLid(experSolution), 
					DefaultName_Settings, NodeType.SETTINGS, FileType.FILE);
			model.insert(new DefaultMutableTreeNode(nodeInfo), experSolution);
			
			// ����WorkSheet�ļ�
			nodeInfo = new DirTreeBean(ProjectTreeUtils.getNextLid(experSolution), 
					DefaultName_Worksheet, NodeType.WORKSHEET, FileType.FILE);
			model.insert(new DefaultMutableTreeNode(nodeInfo), experSolution);
		} 
		catch (Exception e) {
			SwingUtils.showErrorDialog(view, e.getMessage());
			return;
		}
		
		view.scrollPathToVisible(experSolution);
	}

	@Override
	public void createSpecimen(DefaultMutableTreeNode parent) {
		final String name = SwingUtils.showInputDialog(view, "��������Ʒ���ƣ�");
		// ������Ʒ�ļ���
		DirTreeBean nodeInfo = new DirTreeBean(ProjectTreeUtils.getNextLid(parent), 
				name, NodeType.SPECIMEN, FileType.FOLDER);
		DefaultMutableTreeNode specimenNode = new DefaultMutableTreeNode(nodeInfo);
		try {
			model.insert(specimenNode, parent);
		} catch (Exception e) {
			SwingUtils.showErrorDialog(view, e.getMessage());
			e.printStackTrace();
			return;
		}
		
		view.scrollPathToVisible(specimenNode);
	}

	@Override
	public void createTube(DefaultMutableTreeNode parent) {
		final String name = SwingUtils.showInputDialog(view, "�������Թ����ƣ�");
		// �����Թ��ļ�
		DirTreeBean nodeInfo = new DirTreeBean(ProjectTreeUtils.getNextLid(parent),
				name, NodeType.TUBE, FileType.FILE);
		DefaultMutableTreeNode tubeNode = new DefaultMutableTreeNode(nodeInfo);
		try {
			model.insert(tubeNode, parent);
		} 
		catch (Exception e) {
			SwingUtils.showErrorDialog(view, e.getMessage());
			e.printStackTrace();
			return;
		}
		view.scrollPathToVisible(tubeNode);
	}

	@Override
	public void delete(DefaultMutableTreeNode node) {
		try {
			model.delete(node);
		} catch (Exception e) {
			SwingUtils.showErrorDialog(view, "ɾ��ʧ�ܣ�" + e.getMessage());
			e.printStackTrace();
			return;
		}
	}
	
	@Override
	public void rename(DefaultMutableTreeNode node) {
		view.startEditing(node);
	}

//	@Override
//	public JPanel getView() {
//		return view;
//	}

//	@Override
//	public void openNode(DefaultMutableTreeNode node) {
//		DirTreeBean nodeInfo = (DirTreeBean) node.getUserObject();
//		switch (nodeInfo.getNodeType()) {
//		// ��˳�����Ҫ
//		case SETTINGS:
//			openSettings(node);
//			break;
//		case WORKSHEET:
//			openWorkSheet(node);
//			openSettings(getParamSettingsNode(node));
//			break;
//		case TUBE:
//			openTube(node);
//			openWorkSheet(getWorkSheetNode(node));
//			openSettings(getParamSettingsNode(node));
//			break;
//
//		default:
//			// duplicate
//			break;
//		}
//	}
	
	private DefaultMutableTreeNode getParamSettingsNode(DefaultMutableTreeNode node) {
		// ��ȡ�ýڵ㸽����paramSettings
		try {
			DefaultMutableTreeNode experiment = searchExperimentNode(node);
			@SuppressWarnings("unchecked")
			Enumeration<MutableTreeNode> children = (Enumeration<MutableTreeNode>) experiment.children();
			while (children.hasMoreElements()) {
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) children.nextElement();
				DirTreeBean nodeInfo = (DirTreeBean) treeNode.getUserObject();
				if (nodeInfo.getNodeType() == NodeType.SETTINGS) {
					return treeNode;
				}
			}
			return null;
		} catch (NullPointerException e) {
			return null;
		}
	}

	private DefaultMutableTreeNode getWorkSheetNode(DefaultMutableTreeNode node) {
		// ��ȡ�ýڵ㸽����worksheet
		try {
			DefaultMutableTreeNode experiment = searchExperimentNode(node);
			@SuppressWarnings("unchecked")
			Enumeration<MutableTreeNode> children = (Enumeration<MutableTreeNode>) experiment.children();
			while (children.hasMoreElements()) {
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) children.nextElement();
				DirTreeBean nodeInfo = (DirTreeBean) treeNode.getUserObject();
				if (nodeInfo.getNodeType() == NodeType.WORKSHEET) {
					return treeNode;
				}
			}
			return null;
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	private DefaultMutableTreeNode searchExperimentNode(DefaultMutableTreeNode node) {
		// �ݹ��ѯExperiment�ڵ�
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
		DirTreeBean nodeInfo = (DirTreeBean) parent.getUserObject();
		if (nodeInfo.getNodeType() == NodeType.EXPERIMENT_SOLUTION) {
			return parent;
		} else {
			return searchExperimentNode(parent);
		}
	}
	
//	private void openSettings(DefaultMutableTreeNode node) {
//		notifyObservers(new DirTreeEvent(
//				this, 
//				DirTreeEvent.OPEN_SETTINGS, 
//				ProjectTreeUtils.getRelaPath(node)));
////		System.out.println(ProjectTreeUtils.getRelaPath(node));
//	}
//
//	private void openWorkSheet(DefaultMutableTreeNode node) {
//		notifyObservers(new DirTreeEvent(
//				this, 
//				DirTreeEvent.OPEN_WORKSHEET, 
//				ProjectTreeUtils.getRelaPath(node)));
////		System.out.println(ProjectTreeUtils.getRelaPath(node));
//	}
//
//	private void openTube(DefaultMutableTreeNode node) {
//		notifyObservers(new DirTreeEvent(
//				this, 
//				DirTreeEvent.OPEN_TUBE, 
//				ProjectTreeUtils.getRelaPath(node)));
////		System.out.println(ProjectTreeUtils.getRelaPath(node));
//	}

//	@Override
//	public void addObserver(DirTreeObserver observer) {
//		observers.add(observer);
//	}
//
//	@Override
//	public void removeObserver(DirTreeObserver observer) {
//		observers.remove(observer);
//	}
//
//	@Override
//	public void notifyObservers(DirTreeEvent event) {
//		observers.stream().forEach(o -> o.dirTreeUpdated(event));
//	}

}
