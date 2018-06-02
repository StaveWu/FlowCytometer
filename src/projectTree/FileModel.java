package projectTree;

import java.io.File;

public class FileModel {
	
	public boolean createFile(String pathName) throws Exception {
		File file = new File(pathName);
		if (!file.exists()) {
			return file.createNewFile();
		}
		return false;
	}

	public boolean createFolder(String pathName) throws Exception {
		File file = new File(pathName);
		if (!file.exists()) {
			return file.mkdir();
		}
		return false;
	}

	public boolean rename(String oldPathname, String newPathname) throws Exception {
		File file = new File(oldPathname);
		if (file.exists()) {
			File dest = new File(newPathname);
			return file.renameTo(dest);
		}
		return false;
	}

	public boolean delete(String pathName) throws Exception {
		File file = new File(pathName);
		if (file.exists()) {
			deleteRecusion(file);
		}
		return !file.exists();
	}
	
	private void deleteRecusion(File file) {
		// µÝ¹éÉ¾³ýÎÄ¼þ
		File[] children = file.listFiles();
		if (children != null) {
			for (File ele : children) {
				if (ele.isDirectory()) {
					deleteRecusion(ele);
				} else {
					ele.delete();
				}
			}
		}
		file.delete();
	}
	
	public boolean isExisting(String pathname) throws Exception {
		return new File(pathname).exists();
	}

}
