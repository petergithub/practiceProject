package demo.file.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class DomDemo {
	
	/**
	 * DOM生成与解析XML文档
	 */
	public void testDom() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			createXmlDom("domDemo.xml", document);
			parserXmlDom("test.xml");
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		}
	}

	public void createXmlDom(String fileName, Document document) {
		Element root = document.createElement("employees");

		document.appendChild(root);
		Element employee = document.createElement("employee");

		Element name = document.createElement("name");
		name.appendChild(document.createTextNode("丁宏亮"));
		employee.appendChild(name);

		Element sex = document.createElement("sex");
		sex.appendChild(document.createTextNode("m"));
		employee.appendChild(sex);

		Element age = document.createElement("age");
		age.appendChild(document.createTextNode("30"));
		employee.appendChild(age);

		root.appendChild(employee);
		TransformerFactory tf = TransformerFactory.newInstance();
		try {
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "gb2312");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
			System.out.println("生成XML文件成功!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void parserXmlDom(String fileName) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(fileName);
			NodeList employees = document.getChildNodes();
			for (int i = 0; i < employees.getLength(); i++) {
				Node employee = employees.item(i);
				NodeList employeeInfo = employee.getChildNodes();
				for (int j = 0; j < employeeInfo.getLength(); j++) {
					Node node = employeeInfo.item(j);
					NodeList employeeMeta = node.getChildNodes();
					for (int k = 0; k < employeeMeta.getLength(); k++) {
						System.out.println(employeeMeta.item(k).getNodeName()
								+ ":" + employeeMeta.item(k).getNodeValue());
					}
				}
			}
			System.out.println("解析完毕");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Dom4j 生成XML文档与解析XML文档
	 */
	public void testDom4jDemo() {
		createXmlDom4j("dom4jDemo.xml");
		parserXmlDom4j("test.xml");
	}

	public void createXmlDom4j(String fileName) {
		org.dom4j.Document document = DocumentHelper.createDocument();
		org.dom4j.Element employees = document.addElement("employees");
		org.dom4j.Element employee = employees.addElement("employee");
		org.dom4j.Element name = employee.addElement("name");
		name.setText("ddvip");
		org.dom4j.Element sex = employee.addElement("sex");
		sex.setText("m");
		org.dom4j.Element age = employee.addElement("age");
		age.setText("29");
		try {
			Writer fileWriter = new FileWriter(fileName);
			XMLWriter xmlWriter = new XMLWriter(fileWriter);
			xmlWriter.write(document);
			xmlWriter.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void parserXmlDom4j(String fileName) {
		File inputXml = new File(fileName);
		SAXReader saxReader = new SAXReader();
		try {
			org.dom4j.Document document = saxReader.read(inputXml);
			org.dom4j.Element employees = document.getRootElement();
			for (Iterator<org.dom4j.Element> i = employees.elementIterator(); i.hasNext();) {
				org.dom4j.Element employee = i.next();
				for (Iterator<org.dom4j.Element> j = employee.elementIterator(); j.hasNext();) {
					org.dom4j.Element node = j.next();
					System.out.println(node.getName() + ":" + node.getText());
				}
			}
		} catch (DocumentException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("dom4j parserXml");
	}
	
	public void testSaxDemo() {
		createXmlSax("saxDemo.xml");
		parserXmlSax("test.xml");
	}

	public void createXmlSax(String fileName) {
		System.out.println("<<" + fileName + ">>");
	}

	public void parserXmlSax(String fileName) {
		SAXParserFactory saxfac = SAXParserFactory.newInstance();
		try {
			SAXParser saxparser = saxfac.newSAXParser();
			InputStream is = new FileInputStream(fileName);
			saxparser.parse(is, new MySAXHandler());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class MySAXHandler extends org.xml.sax.helpers.DefaultHandler {
	boolean hasAttribute = false;
	Attributes attributes = null;

	public void startDocument() throws SAXException {
		System.out.println("文档开始打印了");
	}

	public void endDocument() throws SAXException {
		System.out.println("文档打印结束了");
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName.equals("employees")) {
			return;
		}
		if (qName.equals("employee")) {
			System.out.println(qName);
		}
		if (attributes.getLength() > 0) {
			this.attributes = attributes;
			this.hasAttribute = true;
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (hasAttribute && (attributes != null)) {
			for (int i = 0; i < attributes.getLength(); i++) {
				System.out.println(attributes.getQName(0)
						+ attributes.getValue(0));
			}
		}
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		System.out.println(new String(ch, start, length));
	}
}
