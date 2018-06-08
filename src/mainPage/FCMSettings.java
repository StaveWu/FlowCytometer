package mainPage;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import device.CommDeviceType;

public class FCMSettings {
	
	private static String workSpacePath = null;
	private static final String XMLPATH = System.getProperty("user.dir") + "\\config\\FCM.xml";
	private static final Document DOC;
	
	static {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			DOC = db.parse(XMLPATH);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("加载工作目录失败！请检查xml文件是否存在！");
		}
	}
	
	public static String getWorkSpacePath() {
		if (workSpacePath == null) {
			NodeList nodeList = DOC.getElementsByTagName("workspace");
			if (nodeList.getLength() <= 0) {
				return null;
			}
			Node node = nodeList.item(0);
			workSpacePath = node.getTextContent();
		}
		return workSpacePath;
	} 
	
	public static boolean bootSpaceChooserBox() {
		NodeList nodeList = DOC.getElementsByTagName("bootChooserBox");
		if (nodeList.getLength() <= 0) {
			return false;
		}
		else {
			String res = nodeList.item(0).getTextContent();
			return res.equals("1");
		}
	}
	
	public static void setBootSpaceChooserBox(boolean bootOrNot) {
		NodeList nodeList = DOC.getElementsByTagName("bootChooserBox");
		if (nodeList.getLength() <= 0) {
			return;
		}
		if (bootOrNot) {
			nodeList.item(0).setTextContent("0");
		} else {
			nodeList.item(0).setTextContent("1");
		}
		
		save();
	}
	
	public static void setWorkSpacePath(String path) {
		setContent("workspace", path);
	}
	
	public static void save() {
		try {
			Source xmlSource = new DOMSource(DOC);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			Result res = new StreamResult(new File(XMLPATH));
			transformer.transform(xmlSource, res);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("保存xml文件失败！");
		}
	}
	
	public static String getSerialPortName() {
		return getContent("portname");
	}
	
	public static int getBaudRate() {
		String res = getContent("baudrate");
		if (res == null) {
			return -1;
		}
		return Integer.valueOf(res);
	}
	
	public static void setSerialPortName(String name) {
		setContent("portname", name);
	}
	
	public static void setBaudRate(int b) {
		setContent("baudrate", b);
	}
	
	public static CommDeviceType getCommDeviceType() {
		NodeList nodeList = DOC.getElementsByTagName("selected");
		if (nodeList.getLength() <= 0) {
			return null;
		}
		if (nodeList.item(0).getTextContent().equals(CommDeviceType.SERIALPORT.toString())) {
			return CommDeviceType.SERIALPORT;
		}
		else if (nodeList.item(0).getTextContent().equals(CommDeviceType.USB.toString())) {
			return CommDeviceType.USB;
		}
		throw new RuntimeException("选择的设备不存在");
	}
	
	public static void setCommDeviceType(CommDeviceType type) {
		setContent("selected", type);
	}
	
	public static void setSelectedProjectLid(String projectlid) {
		setContent("selectedProjectLid", projectlid);
	}
	
	public static String getSelectedProjectLid() {
		return getContent("selectedProjectLid");
	}
	
	private static String getContent(String tag) {
		NodeList nodeList = DOC.getElementsByTagName(tag);
		if (nodeList.getLength() <= 0) {
			return null;
		}
		return nodeList.item(0).getTextContent();
	}
	
	private static void setContent(String tag, Object value) {
		NodeList nodeList = DOC.getElementsByTagName(tag);
		if (nodeList.getLength() <= 0) {
			return;
		}
		nodeList.item(0).setTextContent(value.toString());
		save();
	}
}
