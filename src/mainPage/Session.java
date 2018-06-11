package mainPage;

import projectTree.ProjectTreeObserver;

/**
 * 程序运行期间保存的一些变量
 * @author wteng
 *
 */
public class Session implements ProjectTreeObserver {
	
	private static String selectedProjectLid;
	
	private static String selectedProjectName;
	
	private static String selectedTubeLid;
	
	private static String selectedTubeName;
	
	private static boolean isOnSampling;

	
	public static String getSelectedProjectLid() {
		return selectedProjectLid;
	}

	public static void setSelectedProjectLid(String selectedProjectLid) {
		Session.selectedProjectLid = selectedProjectLid;
	}

	public static String getSelectedProjectName() {
		return selectedProjectName;
	}

	public static void setSelectedProjectName(String selectedProjectName) {
		Session.selectedProjectName = selectedProjectName;
	}

	public static String getSelectedTubeLid() {
		return selectedTubeLid;
	}

	public static void setSelectedTubeLid(String selectedTubeLid) {
		Session.selectedTubeLid = selectedTubeLid;
	}

	public static String getSelectedTubeName() {
		return selectedTubeName;
	}

	public static void setSelectedTubeName(String selectedTubeName) {
		Session.selectedTubeName = selectedTubeName;
	}

	public static boolean isOnSampling() {
		return isOnSampling;
	}

	public static void setOnSampling(boolean isOnSampling) {
		Session.isOnSampling = isOnSampling;
	}
	
	/**
	 * 如果树重命名了，在这边校验一下是否是被选中的lid重命名
	 * 是则修改
	 */
	@Override
	public void renamed(String lid, String newName) {
		if (lid.equals(selectedProjectLid)) {
			selectedProjectName = newName;
		}
		if (lid.equals(selectedTubeLid)) {
			selectedTubeName = newName;
		}
	}

	

}
