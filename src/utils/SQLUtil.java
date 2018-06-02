package utils;

import java.util.Iterator;
import java.util.List;

import dao.DAOFactory;
import dao.beans.DirTreeBean;
import dao.beans.MenuBean;
import dao.beans.ParamSettingsBean;

public class SQLUtil {
	
	public static void main(String[] args) {
//		List<ParamSettingsBean> mbs = SQLTextReader.readParamSettings("C:\\Users\\wteng\\Desktop/paramSettings.txt");
//		Iterator<ParamSettingsBean> iter = mbs.iterator();
//		while (iter.hasNext()) {
//			ParamSettingsBean tb = iter.next();
//			System.out.println(tb);
//			try {
//				DAOFactory.getIParamSettingsDAOInstance().createTable("ParamSettings");
//				DAOFactory.getIParamSettingsDAOInstance().addParamSetting("ParamSettings", tb);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		
//		List<MenuBean> mbs = SQLTextReader.readMenu("E:\\04�ĵ�\\������\\��ʽϸ����\\����Ŀ¼����/mainBar.txt");
//		Iterator<MenuBean> iter = mbs.iterator();
//		while (iter.hasNext()) {
//			MenuBean tb = iter.next();
//			System.out.println(tb);
//			try {
//				DAOFactory.newIMenuDAOInstance().createTable("MainPageMenuBar");
//				DAOFactory.newIMenuDAOInstance().addMenu("MainPageMenuBar", tb);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		
		List<DirTreeBean> mbs = SQLTextReader.readTree("E:\\04�ĵ�\\������\\��ʽϸ����\\����Ŀ¼����/DirTree.txt");
		Iterator<DirTreeBean> iter = mbs.iterator();
		while (iter.hasNext()) {
			DirTreeBean tb = iter.next();
			System.out.println(tb.Message());
			try {
				DAOFactory.getIDirTreeDAOInstance().createTable("E04�ĵ���������ʽϸ���������Ŀ������ ");
				DAOFactory.getIDirTreeDAOInstance().addDirTree("E04�ĵ���������ʽϸ���������Ŀ������ ", tb);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
