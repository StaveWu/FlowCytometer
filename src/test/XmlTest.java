package test;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlTest {
	
	public static void main(String[] args) throws Exception {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		org.w3c.dom.Document document = db.parse(System.getProperty("user.dir") + "\\config\\FCM.xml");
		NodeList nodeList = document.getElementsByTagName("workspace");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			System.out.println(node.getTextContent());
		}
//		System.out.println(System.getProperty("user.dir") + "\\config\\FCM.xml");
	}

}
