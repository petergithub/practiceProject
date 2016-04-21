package doing.rss;

import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class RSSReader {
//	private static final Logger log = LoggerFactory.getLogger(RSSReader.class);
	private final Logger log = LoggerFactory.getLogger(getClass());
	private static RSSReader instance = null;
	private URL rssURL;

	public void test() {
		StringBuffer sb = new StringBuffer();
		String fileInfo = "";
		String[] content = new String[2];
		try {
			String path = "http://hostname:9000/ontent.txt";
			String _path = URLDecoder.decode(path, "utf-8");
			URL url = new URL(_path);
			Scanner scanner = new Scanner(url.openStream());
			while (scanner.hasNext()) {
				String strLine = scanner.next();
				sb.append(strLine + " ");
			}
			scanner.close();
			System.out.println(sb.toString());
			fileInfo = sb.toString();
			content = fileInfo.split("<stop/>");
			log.debug("content.length = {}", content.length);
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	@Test
	public void testRss() {
		try {
			List<String> content = new ArrayList<String>();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			String url = "url";
			InputStream openStream = new URL(url).openStream();
			String html = IOUtils.toString(openStream);
			log.info("html = {}", html);
			Document doc = builder.parse(openStream);
			NodeList items = doc.getElementsByTagName("item");
			for (int i = 0; i < items.getLength(); i++) {
				Element item = (Element) items.item(i);
				String title = getValue(item, "title");
				String description = getValue(item, "description");
				String link = getValue(item, "link");
				StringBuffer result = new StringBuffer("<a href=\"").append(link).append("\">")
						.append(title).append("</a>")
						.append("<br/><p>").append(getBody(description)).append("</p>");
				content.add(result.toString());
			}
			log.info("content[0] = {}", content.get(0));
			log.info("content[1] = {}", content.get(1));
		} catch (Exception e) {
			log.error("Exception in RSSReader.testRss()", e);
		}
	}
	public String getBody(String description) {
		String result = removeHtmlTag(description);
		result = result.replaceAll("Body: ", "");
		if (result.length()> 600) {
			result = result.substring(0, 600);
			result += "...";
		}
		log.info("result = {}", result);
		return result;
	}
	
	/**
	 * 移除html标签
	 */
	public static String removeHtmlTag(String content) {

		String STYLE = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
		// 定义HTML标签的正则表达式
		String HTML_TAG = "<[^>]+>";

		if (content == null || content.isEmpty()) {
			return "";
		}
		String temp = content;
		// 去除style
		temp = temp.replaceAll(STYLE, "");
		// 去除html
		temp = temp.replaceAll(HTML_TAG, "");
		// 合并空格
		temp = temp.replaceAll("\\s+", " ");
		return temp.trim();
	}
	public static void main(String[] args) {
		try {
			RSSReader reader = RSSReader.getInstance();
			// reader.setURL(new URL("http://www.tullyrankin.com/feed/rss"));
			reader.setURL(new URL("http://feeds2.feedburner.com/programthink"));
			reader
					.setURL(new URL("url"));
			reader.writeFeed();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static RSSReader getInstance() {
		if (instance == null) instance = new RSSReader();
		return instance;
	}

	public void setURL(URL url) {
		rssURL = url;
	}

	public void writeFeed() {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(rssURL.openStream());
			
			NodeList items = doc.getElementsByTagName("item");
			for (int i = 0; i < items.getLength(); i++) {
				Element item = (Element) items.item(i);
				System.out.println(getValue(item, "title"));
				System.out.println(getValue(item, "description"));
				System.out.println(getValue(item, "link"));
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getValue(Element parent, String nodeName) {
		return parent.getElementsByTagName(nodeName).item(0).getFirstChild().getNodeValue();
	}
}
