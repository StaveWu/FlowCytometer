package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableTest {
	
	public static void main(String[] args) 
			throws FileNotFoundException, IOException, ClassNotFoundException {
//		Bean b = new Bean(2, 3, new SubBean("sss"));
//		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("bean.out"));
//		out.writeObject(b);
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("bean.out"));
		Bean b = (Bean) in.readObject();
		System.out.println(b.getX() + "," + b.getY() + ",");
	}

}

class Bean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int x;
	int y;
//	SubBean sb;
	
	public Bean(int x, int y, SubBean sb) {
		this.x = x;
		this.y = y;
//		this.sb = sb;
		System.out.println(sb.getS());
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
//	public SubBean getSb() {
//		return sb;
//	}
	
}


class SubBean {
	/**
	 * 序列化ID要一致
	 */
//	private static final long serialVersionUID = 1L;
	String s;
	public SubBean(String s) {
		this.s = s;
	}
	public String getS() {
		return s;
	}
	@Override
	public String toString() {
		return s;
	}
}