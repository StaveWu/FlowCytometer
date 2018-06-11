package projectTree;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import dao.DAOFactory;
import dao.GroupCondition;
import dao.beans.DirTreeBean;
import mainPage.FCMSettings;
import utils.StringUtils;

/**
 * 项目树定义如下：
 * root
 * 	|- 实验方案1
 * 	|- 实验方案2
 * 		|- 样品1
 * 		|- 样品2
 * 			|- 试管1
 * 			|- 试管2
 * 		|- Settings
 * 		|- WorkSheet
 * 
 * @author wteng
 *
 */
public class ProjectTreeModel implements IProjectTreeModel {
	
	private DefaultTreeModel delegate;
	private FileModel fileModel;
	
	private List<ProjectTreeObserver> observers = new ArrayList<>();
	
	private static String path = FCMSettings.getWorkSpacePath();
	private String tableName;
	
	@SuppressWarnings("serial")
	public ProjectTreeModel() throws SQLException {
		fileModel = new FileModel();
		DefaultMutableTreeNode root = createRoot();
		loadTree(path, root);
		
		delegate = new DefaultTreeModel(root) {
			/*
			 * 当目录树被重命名后，默认方法是将Object转成String，
			 * 故这边需要重写该方法以保留NodeInfo信息
			 * @see javax.swing.tree.DefaultTreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
			 */
			@Override
			public void valueForPathChanged(TreePath oldpath, Object newValue) {
				DefaultMutableTreeNode aNode = (DefaultMutableTreeNode)oldpath.getLastPathComponent();
				// 屏蔽空值或未命名操作
				DirTreeBean info = (DirTreeBean) aNode.getUserObject();
				if (newValue == null || newValue.equals("") || newValue.equals(info.getName())) {
					return;
				}
				
				try {
					rename(aNode, (String)newValue);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
		        nodeChanged(aNode);
			}
		};
	}
	
	private DefaultMutableTreeNode createRoot() {
		return new DefaultMutableTreeNode(
				new DirTreeBean(null, StringUtils.getTail(path), NodeType.WORKSPACE, FileType.FOLDER));
	}
	
	private void loadTree(String path, DefaultMutableTreeNode root) throws SQLException {
		// 分别查找0级 1级 2级节点，按顺序添加（涉及到bean的排序）
		tableName = StringUtils.specialCharFilter(path);
		DAOFactory.getIDirTreeDAOInstance().createTable(tableName);
		
		HashMap<String, DefaultMutableTreeNode> nodeMap = new HashMap<>();
		/*
		 * 添加0级菜单，默认类型是实验方案
		 */
		List<GroupCondition> gcs1 = new ArrayList<>();
		gcs1.add(new GroupCondition("level", "=", "0"));
		List<DirTreeBean> beans1 = 
				DAOFactory.getIDirTreeDAOInstance().find(tableName, gcs1);
		Collections.sort(beans1);
		
		if (beans1 != null) {
			Iterator<DirTreeBean> iter = beans1.iterator();
			while (iter.hasNext()) {
				DirTreeBean bean = (DirTreeBean) iter.next();
				DefaultMutableTreeNode beanNode = new DefaultMutableTreeNode(bean);
				nodeMap.put(bean.getLid(), beanNode);
				root.add(beanNode);
			}
		}
		
		/*
		 * 添加1级菜单，默认类型是Settings、WorkSheet或者样品
		 */
		List<GroupCondition> gcs2 = new ArrayList<>();
		gcs2.add(new GroupCondition("level", "=", "1"));
		List<DirTreeBean> beans2 = 
				DAOFactory.getIDirTreeDAOInstance().find(tableName, gcs2);
		Collections.sort(beans2);
		if (beans2 != null) {
			Iterator<DirTreeBean> iter = beans2.iterator();
			while (iter.hasNext()) {
				DirTreeBean bean = (DirTreeBean) iter.next();
				DefaultMutableTreeNode beanNode = new DefaultMutableTreeNode(bean);
				nodeMap.put(bean.getLid(), beanNode);
				nodeMap.get(bean.getFather()).add(beanNode);
			}
		}
		
		/*
		 * 添加2级菜单，默认类型是试管
		 */
		List<GroupCondition> gcs3 = new ArrayList<>();
		gcs3.add(new GroupCondition("level", "=", "2"));
		List<DirTreeBean> beans3 = 
				DAOFactory.getIDirTreeDAOInstance().find(tableName, gcs3);
		Collections.sort(beans3);
		if (beans3 != null) {
			Iterator<DirTreeBean> iter = beans3.iterator();
			while (iter.hasNext()) {
				DirTreeBean bean = (DirTreeBean) iter.next();
				DefaultMutableTreeNode beanNode = new DefaultMutableTreeNode(bean);
				nodeMap.get(bean.getFather()).add(beanNode);
			}
		}
	}

	@Override
	public void insert(DefaultMutableTreeNode node, DefaultMutableTreeNode parent) throws Exception {
		// 节点
		delegate.insertNodeInto(node, parent, parent.getChildCount());
		// 文件系统
		DirTreeBean info = (DirTreeBean) node.getUserObject();
		boolean success = false;
		if (info.getFileType() == FileType.FILE) {
			success = fileModel.createFile(absolutePath(node));
		}
		else {
			success = fileModel.createFolder(absolutePath(node));
		}
		if (!success) {// 回滚
			delegate.removeNodeFromParent(node);
			throw new RuntimeException("创建" + node + "失败");
		}
		// 数据库
		try {
			info.setLevel(info.getLid().split("\\.").length - 1);
			if (info.getLid().length() > 1) {
				info.setFather(info.getLid().substring(0, info.getLid().length() - 2));
			}
			DAOFactory.getIDirTreeDAOInstance().addDirTree(tableName, info);
		}
		catch (Exception e) {// 回滚
			fileModel.delete(absolutePath(node));
			delegate.removeNodeFromParent(node);
			throw e;
		}
		
	}
	
	private String absolutePath(DefaultMutableTreeNode node) {
		// 获取node的绝对路径"C:/A/B"
		return path + ProjectTreeUtils.getRelaPath(node);
	}

	@Override
	public void delete(DefaultMutableTreeNode node) throws Exception {
		String absPath = absolutePath(node); // 先获取路径，否则节点一删除，路径就找不到了
		// 节点
		delegate.removeNodeFromParent(node);
		// 文件系统
		fileModel.delete(absPath);
		// 数据库
		removeSelfAndBranchs(node);
		
	}
	
	private void removeSelfAndBranchs(MutableTreeNode node) throws SQLException {
		// 是目录的话遍历所有孩子
		if (!node.isLeaf()) {
			@SuppressWarnings("unchecked")
			Enumeration<MutableTreeNode> children = (Enumeration<MutableTreeNode>) node.children();
			while (children.hasMoreElements()) {
				MutableTreeNode child = (MutableTreeNode) children.nextElement();
				removeSelfAndBranchs(child);	// 递归
			}
		}
		//delete self
		DirTreeBean info = (DirTreeBean) ((DefaultMutableTreeNode) node).getUserObject();
		DAOFactory.getIDirTreeDAOInstance().deleteDirTree(tableName, info.getLid());
	}

	@Override
	public void rename(DefaultMutableTreeNode node, String newName) throws Exception {
		// 文件系统
		boolean success = false;
		if (!fileModel.isExisting(getNewPathname(node, newName))) {
			success = fileModel.rename(absolutePath(node), getNewPathname(node, newName));
		}
		if (!success) {
			throw new RuntimeException("重命名失败！");
		}
		// 数据库
		DirTreeBean message = (DirTreeBean) node.getUserObject();
		List<GroupCondition> gcs = new ArrayList<>();
		gcs.add(new GroupCondition("name", "=", newName));
		DAOFactory.getIDirTreeDAOInstance().update(tableName, gcs, message.getLid());
		// 节点
		message.setName(newName);
		
		// 通知Session
		String lid = ((DirTreeBean)node.getUserObject()).getLid();
		notifyObservers(lid, newName);
	}
	
	private String getNewPathname(DefaultMutableTreeNode node, String newName) {
		String oldname = StringUtils.getTail(absolutePath(node));
		int i = 0;
		String n = "";
		for (String ele : oldname.split("\\.")) {
			if (i == 0) {
				n = newName;
			} else {
				n += ("." + ele);
			}
			i++;
		}
		return StringUtils.getParentPath(absolutePath(node)) + File.separator + n;
	}

	@Override
	public TreeModel getDelegate() {
		return delegate;
	}

	@Override
	public void addObserver(ProjectTreeObserver o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(ProjectTreeObserver o) {
		observers.remove(o);
	}

	@Override
	public void notifyObservers(String lid, String newName) {
		observers.forEach(e -> e.renamed(lid, newName));
	}

}
