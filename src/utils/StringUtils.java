package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	
	/**
	 * ��·���е�������ĸ��������ȡ��������ַ���
	 * @param path
	 * @return
	 */
	public static String specialCharFilter(String path) {
		String res = "";
		String regex = "[a-zA-Z]+|\\d+|[\\u4E00-\\u9FA5]+";
		Matcher m = Pattern.compile(regex).matcher(path);
		while (m.find()) {
			res += m.group();
		}
		return res;
	}
	
	
	public static String getNumFrom(String str) {
		if (str == null || str.equals("")) {
			return null;
		}
		String res = "";
		String regex = "\\d+";
		Matcher m = Pattern.compile(regex).matcher(str);
		while (m.find()) {
			res += m.group();
		}
		return res;
	}
	
	public static String getTail(String path) {
		if (path == null) {
			return null;
		}
		String[] s1 = path.split("/");
		String[] s2 = s1[s1.length - 1].split("\\\\");
		return s2[s2.length - 1];
	}
	
	public static String getParentPath(String path) {
		if (path == null) {
			return null;
		}
		return path.substring(0, path.indexOf(getTail(path)) - 1);
	}
	
	public static String getExtension(String pathname) {
		if (pathname == null) {
			return null;
		}
		String[] s = pathname.split("\\.");
		if (s.length < 2) {
			return null;
		}
		return s[s.length - 1];
	}
	
	public static boolean isNumber(String str) {
		try {
			Double.parseDouble(str);
			return true;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}

}
