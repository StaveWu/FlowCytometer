package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import dao.beans.DirTreeBean;
import dao.beans.MenuBean;
import dao.beans.ParamSettingsBean;
import dao.beans.TableBean;

public class SQLTextReader {
	
	public static List<MenuBean> readMenu(String path) {
		
		List<MenuBean> res = new ArrayList<MenuBean>();
		
		try {
			BufferedReader br1 = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(path))));
			String in1 = null;
			int i = 0;
			while((in1 = br1.readLine()) != null) {
				if(i == 0) {	//ÅÅ³ý×Ö¶ÎÐÐ
					i++;
					continue;
				}
				String s[] = in1.split("\\t");
				MenuBean mb = new MenuBean();
				mb.setLid(s[0]);
				mb.setName(s[1]);
				mb.setType(s[2]);
				mb.setLevel(Integer.valueOf(s[3]));
				
				if(s.length != 4 && s.length != 6) {
					throw new RuntimeException("Êý¾ÝÏî´íÎó£¡");
				}
				if(s.length != 6) {
					mb.setFather("");
					mb.setCommand("");
				}
				else {
					mb.setFather(s[4]);
					mb.setCommand(s[5]);
				}
				res.add(mb);
				i++;
			}
			br1.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	public static List<DirTreeBean> readTree(String path) {
		
		List<DirTreeBean> res = new ArrayList<DirTreeBean>();
		
		try {
			BufferedReader br1 = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(path))));
			String in1 = null;
			int i = 0;
			while((in1 = br1.readLine()) != null) {
				if(i == 0) {	//ÅÅ³ý×Ö¶ÎÐÐ
					i++;
					continue;
				}
				String s[] = in1.split("\\t");
				DirTreeBean mb = new DirTreeBean();
				mb.setLid(s[0]);
				mb.setName(s[1]);
				mb.setNodeType(s[2]);
				mb.setFileType(s[3]);
				mb.setLevel(Integer.valueOf(s[4]));
				
				if(s.length < 6) {
					mb.setFather("");
				}
				else {
					mb.setFather(s[5]);
				}
				res.add(mb);
				i++;
			}
			br1.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	public static List<TableBean> readTable(String path) {
		List<TableBean> res = new ArrayList<>();
		
		try {
			BufferedReader br1 = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(path))));
			String in1 = null;
			int i = 0;
			while((in1 = br1.readLine()) != null) {
				if(i == 0) {	//ÅÅ³ý×Ö¶ÎÐÐ
					i++;
					continue;
				}
				String s[] = in1.split("\\t");
				TableBean tb = new TableBean();
				tb.setColumnId(Integer.valueOf(s[0]));
				tb.setColumnName(s[1]);
				tb.setCommand(s[2]);
				
				res.add(tb);
				i++;
			}
			br1.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	public static List<ParamSettingsBean> readParamSettings(String path) {
		List<ParamSettingsBean> res = new ArrayList<>();
		try {
			BufferedReader br1 = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(path))));
			String in1 = null;
			int i = 0;
			while((in1 = br1.readLine()) != null) {
				if(i == 0) {	//ÅÅ³ý×Ö¶ÎÐÐ
					i++;
					continue;
				}
				String s[] = in1.split("\\t");
				ParamSettingsBean psb = new ParamSettingsBean();
				psb.setParamName(s[0]);
				psb.setVoltage(Integer.valueOf(s[1]));
				psb.setThreshold(Integer.valueOf(s[2]));
				psb.setA(Boolean.valueOf(s[3]));
				psb.setH(Boolean.valueOf(s[4]));
				psb.setW(Boolean.valueOf(s[5]));
				
				res.add(psb);
				i++;
			}
			br1.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}

}
