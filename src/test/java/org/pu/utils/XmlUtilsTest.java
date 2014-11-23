/**
 * @author Shang Pu
 * @create on 20120229
 */
package org.pu.utils;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Text;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class XmlUtilsTest extends TestBase {
	private final static Logger log = LoggerFactory.getLogger(XmlUtilsTest.class);
	String filePath = "C:\\sp\\doing\\.classpath";
//	<classpathentry kind="src" path="src/main/java"/>
	String xpath = "//classpathentry[@kind='var']|//classpathentry[@kind='var']";
//	 <staff id="1">
//   <firstname>yong</firstname>
//   </staff>
	String xpathStaff = "/staff[firstname='yong']";
	private static Element config;

	@BeforeClass
	public static void initConfig() throws Exception {
		testWriteElementToXml();
	}

	@Test
	public void checkExistElement() {
		Element notExsitChild = config.getChild("NotExsitChild");
		Assert.assertNull(notExsitChild);
		String notExsitChildText = config.getChildText("NotExsitChild");
		Assert.assertNull(notExsitChildText);
		String emptyContent = config.getChildText("ApplySignature");
		Assert.assertEquals("", emptyContent);
		Attribute notExistAttribute = config.getAttribute("notExistAuditEvent");
		Assert.assertNull(notExistAttribute);
		String notExistAttributeValue = config.getAttributeValue("notExistAuditEvent");
		Assert.assertNull(notExistAttributeValue);
		Attribute auditEvent = config.getChild("ApplySignature").getAttribute("AuditEvent");
		Assert.assertEquals("gtc_xm_approve_workflow_task_with_signature", auditEvent.getValue());
	}
	
	public void testNamespace() {
		// <value xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		// xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:boolean">true</value>

		Element e = new Element("value").addContent("true");
		// xmlns:xs="http://www.w3.org/2001/XMLSchema"
		Namespace xs = Namespace.getNamespace("xs", "http://www.w3.org/2001/XMLSchema");
		e.addNamespaceDeclaration(xs);
		
		// xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		// xsi:type="xs:boolean"
		Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		e.setAttribute("type", "xs:boolean", xsi);
		
		Element editableConditions = new Element("editableConditions").addContent(e);
		log.info("XmlUtils.toString(element) = {}", XmlUtils.toString(editableConditions));

		Element stylesheet = new Element("stylesheet", "xsl", "http://www.w3.org/1999/XSL/Transform");
		Namespace svg = Namespace.getNamespace("svg", "http://www.w3.org/2000/svg");
		stylesheet.addNamespaceDeclaration(svg);
		log.info("XmlUtils.toString(element) = {}", XmlUtils.toString(stylesheet));
	}

	public void testCompactCreateElement() {
		Element e = new Element("ApplicationProfile");
		e.addContent("xmlabeling");
		Element e2 = new Element("ApplicationProfile").addContent("xmlabeling");
		Assert.assertEquals("<ApplicationProfile>xmlabeling</ApplicationProfile>", XmlUtils.toString(e));
		Assert.assertEquals(XmlUtils.toString(e), XmlUtils.toString(e2));
	}
	
	public void testElement() {
		Element e = new Element("ApplicationProfile");
		e.addContent("xmlabeling");
		log.info("ApplicationProfile = {}", e);
		log.info("ApplicationProfile = {}", e.addContent("xmlabeling"));
		Text text = (Text) e.getContent().get(0);
		String content = text.getText();
		log.info("content = {}", content);
		e.setText("changeContent");
		text = (Text) e.getContent().get(0);
		content = text.getText();
		log.info("content = {}", content);
		e.setAttribute("name", "value");
		e.getAttribute("name").getValue();
	}
	
	public void testReadXml() {
		final List<String> libs = new ArrayList<String>();
		HandlerImpl handler = new HandlerImpl();
		XmlUtils.readXml(filePath, xpath, handler);
		libs.addAll(handler.libs);
		log.info("libs = {}", handler.libs);

		List<String> libs2 = XmlUtils.getClasspathFileLibs(filePath, xpath);
		log.info("libs2 = {}", libs2);
		Assert.assertEquals(libs, libs2);
	}

	class HandlerImpl implements XmlUtils.Handler {
		public List<String> libs = new ArrayList<String>();

		@Override
		public void handle(Element e) {
			String path = e.getAttribute("path").getValue();
			String lib = path.substring(path.lastIndexOf("/") + 1);
			libs.add(lib);
		}
	}

	// <?xml version="1.0" encoding="UTF-8"?>
	// <End>
	// <LifeCycle>
	// <Promote>
	// <State>Approved</State>
	// </Promote>
	// </LifeCycle>
	// <ApplySignature AuditEvent="gtc_xm_approve_workflow_task_with_signature"
	// AuditedObjectId="esig_object_id" DateTimeFormat="dd-MMM-yyyy HH:mm:ss" />
	// <DynamicDistribution>
	// <Distribution>
	// <eventName>Approval Completed</eventName>
	// <message>The Approval Workflow for the following document has been
	// completed:</message>
	// </Distribution>
	// <UserQuery>select user_name from dm_user where user_name in (select
	// supervisor_name from
	// dm_workflow
	// where r_object_id in (select distinct r_workflow_id from dmi_package
	// where any r_component_id
	// =
	// '$objectid'))</UserQuery>
	// </DynamicDistribution>
	// <Audit>
	// <Event>gtc_xm_end_workflow</Event>
	// </Audit>
	// <super />
	// </End>
	public static void testWriteElementToXml() throws Exception {
		Element end = new Element("End");
		Element applySignature = new Element("ApplySignature");
		applySignature.setAttribute("AuditEvent", "gtc_xm_approve_workflow_task_with_signature");
		applySignature.setAttribute("AuditedObjectId", "esig_object_id");
		applySignature.setAttribute("DateTimeFormat", "dd-MMM-yyyy HH:mm:ss");
//		applySignature.addContent("applySignature");

		Element lifeCycle = new Element("LifeCycle");
		Element promote = new Element("Promote");
		Element state = new Element("State");
		state.addContent("Approved");
		promote.addContent(state);
		lifeCycle.addContent(promote);

		Element dynamicDistribution = new Element("DynamicDistribution");
		Element userQuery = new Element("UserQuery");
		userQuery
				.addContent("select user_name from dm_user where user_name in (select supervisor_name from dm_workflow where r_object_id in (select distinct r_workflow_id from dmi_package where any r_component_id = '$objectid'))");
		Element distribution = new Element("Distribution");
		Element eventName = new Element("eventName");
		eventName.addContent("Approval Completed");
		Element message = new Element("message");
		message.addContent("The Approval Workflow for the following document has been completed:");
		distribution.addContent(eventName);
		distribution.addContent(message);
		dynamicDistribution.addContent(distribution);
		dynamicDistribution.addContent(userQuery);

		Element audit = new Element("Audit");
		Element event = new Element("Event");
		event.addContent("gtc_xm_end_workflow");
		audit.addContent(event);
		Element superTag = new Element("super");

		end.addContent(lifeCycle);
		end.addContent(applySignature);
		end.addContent(dynamicDistribution);
		end.addContent(audit);
		end.addContent(superTag);

		config = end;
		log.info("end = {}", XmlUtils.toString(end));
//		XmlUtils.writeElementToXml(end, "c:/cache/test.xml");
	}

}
