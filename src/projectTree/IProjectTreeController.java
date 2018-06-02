package projectTree;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import mainPage.events.DirTreeEvent;
import mainPage.interfaces.DirTreeObserver;

public interface IProjectTreeController {
	
	void createExperimentSolution(DefaultMutableTreeNode parent);
	
	void createSpecimen(DefaultMutableTreeNode parent);
	
	void createTube(DefaultMutableTreeNode parent);
	
	void delete(DefaultMutableTreeNode node);
	
	void rename(DefaultMutableTreeNode node);
	
	void openNode(DefaultMutableTreeNode node);
	
	void addObserver(DirTreeObserver observer);
	
	void removeObserver(DirTreeObserver observer);
	
	void notifyObservers(DirTreeEvent event);
	
	JPanel getView();

}
