package mainPage;

import projectTree.ProjectTreeObserver;

/**
 * ���������ڼ䱣���һЩ����
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
	 * ������������ˣ������У��һ���Ƿ��Ǳ�ѡ�е�lid������
	 * �����޸�
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
