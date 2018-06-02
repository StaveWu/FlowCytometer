package dao;

import dao.implementions.DirTreeDAOProxy;
import dao.implementions.MenuDAOProxy;
import dao.implementions.ParamSettingsDAOProxy;
import dao.implementions.PlotsDAOProxy;
import dao.implementions.TableDAOProxy;
import dao.interfaces.IDirTreeDAO;
import dao.interfaces.IMenuDAO;
import dao.interfaces.IParamSettingsDAO;
import dao.interfaces.IPlotsDAO;
import dao.interfaces.ITableDAO;

public class DAOFactory {
	
	public static IMenuDAO newIMenuDAOInstance() throws Exception{
		return new MenuDAOProxy();
	}
	
	public static ITableDAO getITableDAOInstance() {
		return new TableDAOProxy();
	}
	
	public static IParamSettingsDAO getIParamSettingsDAOInstance(String path) {
		return new ParamSettingsDAOProxy(path);
	}
	
	public static IDirTreeDAO getIDirTreeDAOInstance() {
		return new DirTreeDAOProxy();
	}
	
	public static IPlotsDAO getIPlotsDAOInstance(String path) {
		return new PlotsDAOProxy(path);
	}

}
