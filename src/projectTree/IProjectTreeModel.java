package projectTree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

/**
 * 树模型，提供一些粒子性操作，涉及到文件系统、数据库、JTree组件的TreeModel
 * @author wteng
 *
 */
public interface IProjectTreeModel {
	
	void insert(DefaultMutableTreeNode node, DefaultMutableTreeNode parent) throws Exception;
	
	void delete(DefaultMutableTreeNode node) throws Exception;
	
	void rename(DefaultMutableTreeNode node, String newName) throws Exception;
	
	TreeModel getDelegate();

}
