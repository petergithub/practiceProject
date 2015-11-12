package org.pu.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shang Pu
 * @version Date: Aug 23, 2013 11:10:24 AM
 */
public class XmlUtils extends IoUtils {
	private final static Logger log = LoggerFactory.getLogger(XmlUtils.class);

	/**
	 * write element to xml file
	 * 
	 * @param element
	 * @param filePath
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void writeElementToXml(Element element, String filePath)
			throws FileNotFoundException, IOException {
		writeDocumentToXml(new Document(element), filePath);
	}

	/**
	 * write document to xml file
	 * 
	 * @param doc
	 * @param filePath
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void writeDocumentToXml(Document doc, String filePath)
			throws FileNotFoundException, IOException {
		log.debug("Enter writeDocumentToXml()");
		XMLOutputter out = new XMLOutputter();
		out.setFormat(org.jdom.output.Format.getPrettyFormat());
		// out.getFormat().setEncoding("UTF-8");
		log.info(out.outputString(doc));
		out.output(doc, new FileOutputStream(filePath));
		log.debug("Exit writeDocumentToXml()");
	}

	public static boolean isExist(Element root, String xpath) throws JDOMException {
		Element element = (Element) XPath.selectSingleNode(root, xpath);
		if (element == null) {
			log.debug("there is not match value with the xpath {}", xpath);
			return false;
		}
		return true;
	}

	/**
	 * create XML element
	 * 
	 * @param name name of the XML element
	 * @param value content value of the XML element
	 * @param useCDATA if use CDATA format
	 * @return the XML element created
	 */
	public final static Element createElement(String name, String value, boolean useCDATA) {
		if (name == null) return null;
		Element element = new Element(name);
		if (value != null) {
			if (useCDATA) {
				element.addContent(new CDATA(value));
			} else {
				element.addContent(value);
			}
		}
		return element;
	}

	/**
	 * Create XML element
	 * 
	 * @param name name of the XML element
	 * @param value integer content value
	 * @return the XML element created
	 */
	public final static Element createElement(String name, long value) {
		return createElement(name, String.valueOf(value), false);
	}

	/**
	 * Create XML element
	 * 
	 * @param name name of the XML element
	 * @param value integer content value
	 * @return the XML element created
	 */
	public final static Element createElement(String name, Integer value) {
		return createElement(name, String.valueOf(value), false);
	}

	/**
	 * create XML element
	 * 
	 * @param name name of the XML element
	 * @param value boolean content value
	 * @return the XML element created
	 */
	public final static Element createElement(String name, boolean value) {
		return createElement(name, value ? String.valueOf(1) : String.valueOf(0), false);
	}

	/**
	 * @see #toString(Element,String)
	 */
	public final static String toString(Element element) {
		return toString(element, null);
	}

	/**
	 * export XML element to string
	 * 
	 * @param element XML element to export
	 * @param encoding char encoding; optional-default null.
	 * @return string in XML format
	 */
	public final static String toString(Element element, String encoding) {
		if (element == null) return null;
		return getXmlOutputter(encoding).outputString(element);
	}

	/**
	 * export XML element to string
	 * 
	 * @param xmlDoc XML document to export
	 * @param encoding char encoding; optional default null.
	 * @return string in XML format
	 */
	public final static String toString(Document xmlDoc, String encoding) {
		if (xmlDoc == null) return null;
		return getXmlOutputter(encoding).outputString(xmlDoc);
	}

	public static void writeXML(Document doc, String filePath) throws IOException {
		mkdirs(filePath.substring(0, filePath.lastIndexOf(File.separator)));
		FileWriter writer = null;
		try {
			writer = new FileWriter(filePath);
			getXmlOutputter(null).output(doc, writer);
		} finally {
			close(writer);
		}
	}

	private static XMLOutputter getXmlOutputter(String encoding) {
		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());
		outputter.getFormat().setIndent("\t");
		if (encoding != null) {
			outputter.getFormat().setEncoding(encoding);
		}
		return outputter;
	}

	/**
	 * @return all library names in .classpath file with eclipse style
	 */
	public static List<String> getClasspathFileLibs(String filePath, String xpath) {
		final List<String> libs = new ArrayList<String>();
		XmlUtils.readXml(filePath, xpath, new XmlUtils.Handler() {
			@Override
			public void handle(Element e) {
				String path = e.getAttribute("path").getValue();
				String lib = path.substring(path.lastIndexOf("/") + 1);
				libs.add(lib);
			}
		});
		log.info("libs = {}", libs);
		return libs;
	}

	/**
	 * resolve XML file
	 * 
	 * @param filePath
	 * @param xpath
	 * @param handler the interface to handle the result
	 */
	public static void readXml(String filePath, String xpath, Handler handler) {
		log.debug("Enter readXml({},{})", filePath, xpath);
		try {
			Element root = getRootElement(filePath);
			if (XmlUtils.isExist(root, xpath)) {
				@SuppressWarnings("unchecked")
				List<Element> list = (List<Element>) XPath.selectNodes(root, xpath);
				for (Element e : list) {
					handler.handle(e);
				}
			}
		} catch (JDOMException e) {
			log.error("Exception in XmlUtils.getClasspathFileLibs()", e);
		} catch (IOException e) {
			log.error("Exception in XmlUtils.getClasspathFileLibs()", e);
		}
	}

	public static Element getRootElement(String filePath) throws JDOMException, IOException {
		File file = new File(filePath);
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(file);
		Element root = document.getRootElement();
		return root;
	}

	public interface Handler {
		public void handle(Element e);
	}
}
