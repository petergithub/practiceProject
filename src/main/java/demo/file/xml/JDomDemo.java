package demo.file.xml;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class JDomDemo {
	/**
	 * JDOM 生成与解析XML文档 *
	 */
	public void createXml(String fileName) {
		Element root = new Element("employees");
		Document document = new Document(root);
		Element employee = new Element("employee");
		root.addContent(employee);
		Element name = new Element("name");
		name.setText("ddvip");
		employee.addContent(name);
		Element sex = new Element("sex");
		sex.setText("m");
		employee.addContent(sex);
		Element age = new Element("age");
		age.setText("23");
		employee.addContent(age);
		XMLOutputter XMLOut = new XMLOutputter();
		try {
			XMLOut.output(document, new FileOutputStream(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void parserXml(String fileName) {
		SAXBuilder builder = new SAXBuilder(false);
		try {
			Document document = builder.build(fileName);
			Element employees = document.getRootElement();
			List<Element> employeeList = employees.getChildren("employee");
			for (int i = 0; i < employeeList.size(); i++) {
				Element employee = employeeList.get(i);
				List<Element> employeeInfo = employee.getChildren();
				int size = employeeInfo.size();
				for (int j = 0; j < size; j++) {
					System.out.println(((Element) employeeInfo.get(j))
							.getName()
							+ ":"
							+ ((Element) employeeInfo.get(j)).getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
