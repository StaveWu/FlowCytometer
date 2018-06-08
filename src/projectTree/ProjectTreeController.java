package projectTree;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import dao.beans.DirTreeBean;
import mainPage.MainView;
import utils.SwingUtils;

public class ProjectTreeController implements IProjectTreeController {
	
	private static final String DefaultName_Settings = "Settings";
	private static final String DefaultName_Worksheet = "WorkSheet";
	
	private IProjectTreeModel model;
	private MainView view;
	
	public ProjectTreeController(IProjectTreeModel model, MainView view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void createExperimentSolution(DefaultMutableTreeNode parent) {
		final String name = SwingUtils.showInputDialog(view, "������ʵ�����ƣ�");
		if (name == null || name.equals("")) {
			return;
		}
		
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
		if (name == null || name.equals("")) {
			return;
		}
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
		if (name == null || name.equals("")) {
			return;
		}
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

}
