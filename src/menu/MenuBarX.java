package menu;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import dao.DAOFactory;
import dao.GroupCondition;
import dao.beans.MenuBean;

public abstract class MenuBarX extends JMenuBar {
	
	/**
	 * 保存所有菜单条目
	 */
	private HashMap<String, JMenuItem> itemMap = new HashMap<>();
	
	protected Object source;
	
	/**
	 * 设置默认字体
	 */
	public static final Font MENUFONT = new Font("微软雅黑", Font.PLAIN, 13);
	
	public MenuBarX() {
		
		HashMap<String, JMenu> menuMap = new HashMap<String, JMenu>();
		String tableName = getTableName();
		/*
		 * 添加0级menu
		 */
		try {
			List<GroupCondition> gcs = new ArrayList<>();
			gcs.add(new GroupCondition("type", "=", "Menu"));
			gcs.add(new GroupCondition("level", "=", "0"));
			List<MenuBean> mbs = DAOFactory.newIMenuDAOInstance().find(tableName, gcs);
			if(mbs != null) {
				Iterator<MenuBean> iter = mbs.iterator();
				while(iter.hasNext()) {
					MenuBean mb = iter.next();
					JMenu jm = new JMenu(mb.getName());
					jm.setFont(MENUFONT);
					itemMap.put(mb.getName(), jm);
					menuMap.put(mb.getLid(), jm);
					this.add(jm);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("数据库查询" + tableName + "的0级Menu时没有得到正确返回");
		}
		
		/*
		 * 添加0级menuItem
		 */
		try {
			List<GroupCondition> gcs = new ArrayList<>();
			gcs.add(new GroupCondition("type", "=", "MenuItem"));
			gcs.add(new GroupCondition("level", "=", "0"));
			List<MenuBean> mbs = DAOFactory.newIMenuDAOInstance().find(tableName, gcs);
			if(mbs != null) {
				Iterator<MenuBean> iter = mbs.iterator();
				while(iter.hasNext()) {
					MenuBean mb = iter.next();
					JMenuItem jmi = new JMenuItem(mb.getName());
					jmi.setFont(MENUFONT);
					jmi.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent arg0) {
							invoke(mb.getCommand());
						}
					});
					this.add(jmi);
					itemMap.put(mb.getName(), jmi);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("数据库查询" + tableName + "的0级MenuItem时没有得到正确返回");
		}
		/*
		 * 添加0级以上的menu
		 */
		try {
			List<GroupCondition> gcs = new ArrayList<>();
			gcs.add(new GroupCondition("type", "=", "Menu"));
			gcs.add(new GroupCondition("level", ">", "0"));
			List<MenuBean> mbs = DAOFactory.newIMenuDAOInstance().find(tableName, gcs);
			if(mbs != null) {
				Iterator<MenuBean> iter = mbs.iterator();
				while(iter.hasNext()) {
					MenuBean mb = iter.next();
					JMenu jm = new JMenu(mb.getName());
					jm.setFont(MENUFONT);
					menuMap.put(mb.getLid(), jm);
					menuMap.get(mb.getFather()).add(jm);
					itemMap.put(mb.getName(), jm);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("数据库查询" + tableName + "0级以上的Menu时没有得到正确返回");
		}
		
		/*
		 * 添加0级以上的MenuItem
		 */
		try {
			List<GroupCondition> gcs = new ArrayList<>();
			gcs.add(new GroupCondition("type", "=", "MenuItem"));
			gcs.add(new GroupCondition("level", ">", "0"));
			List<MenuBean> mbs = DAOFactory.newIMenuDAOInstance().find(tableName, gcs);
			if(mbs != null) {
				Iterator<MenuBean> iter = mbs.iterator();
				while(iter.hasNext()) {
					MenuBean mb = iter.next();
					JMenuItem jmi = new JMenuItem(mb.getName());
					jmi.setFont(MENUFONT);
					jmi.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent arg0) {
							//调用对应方法
							invoke(mb.getCommand());
						}
					});
					menuMap.get(mb.getFather()).add(jmi);
					itemMap.put(mb.getName(), jmi);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("数据库查询" + tableName + "0级以上的MenuItem时没有得到正确返回");
		}
	}
	
	/**
	 * 获取表格名称
	 * @return
	 */
	public abstract String getTableName();
	
	/**
	 * 调用相应函数
	 * @param command
	 */
	public abstract void invoke(String command);
	
	/**
	 * 定制菜单
	 * @param source 被点击的事件源
	 */
	public abstract void customizeMenuItem(Object source);
	
	/**
	 * 设置事件源
	 * @param source
	 */
	public void setSource(Object source) {
		this.source = source;
		customizeMenuItem(source);
	}
	
	
	public void disableMenuItem(String itemName) {
		 Iterator<String> iter = itemMap.keySet().iterator();
		 while (iter.hasNext()) {
			String key = (String) iter.next();
			if (key.equals(itemName)) {
				itemMap.get(key).setEnabled(false);
			}
		}
	}
	
	
	public void enableMenuItem(String itemName) {
		Iterator<String> iter = itemMap.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			if (key.equals(itemName)) {
				itemMap.get(key).setEnabled(true);
			}
		}
	}

}
