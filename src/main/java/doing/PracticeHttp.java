package doing;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.pu.test.base.TestBase;
import org.pu.utils.EscapeChars;
import org.pu.utils.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shang Pu
 * @version Date: Nov 12, 2015 10:33:59 AM
 */

public class PracticeHttp extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(PracticeHttp.class);

	public void teste() throws UnsupportedEncodingException, URIException {
		String str = "DEMO%E6%B5%8B%E8%AF%95";
		String decodeURL = URLDecoder.decode(str, "UTF-8");
		log.info("decodeURL = {}", decodeURL);
		log.info("URIUtil.decode = {}", URIUtil.decode(str));
	}

	public void testUrl() throws Exception {
		String str = "queryStatisticalRecord?condition={condition:{startDate:2015-09-21 00:00,endDate:2015-10-21 00:00}}";
		// encodeURL =
		// queryStatisticalRecord%3Fcondition%3D%7Bcondition%3A%7BstartDate%3A2015-09-21+00%3A00%2CendDate%3A2015-10-21+00%3A00%7D%7D
		String encodeURL = URLEncoder.encode(str, "UTF-8");
		log.info("encodeURL = {}", encodeURL);
		URL url = new URL("http", "testtclpay.tclclouds.com", "/settlement/" + encodeURL);
		// URL url = new URL("http", "testtclpay.tclclouds.com",
		// "dataDictionary/list");
		URI uri = url.toURI();
		log.info("url = {}", uri);
	}

	public void testHtmlEscape() {
		String str = "the data might contain < or & or ! or % or ' or # etc";
		String escapedXml3 = org.apache.commons.lang3.StringEscapeUtils.escapeXml(str);
		String escapedXml = org.apache.commons.lang.StringEscapeUtils.escapeXml(str);
		log.info("escapedXml3 = {}", escapedXml3);
		log.info("escapedXml = {}", escapedXml);
		log.info("EscapeChars.forHTML = {}", EscapeChars.forHTML(str));
	}

	public void testUrlOpenStream() {
		URL url;
		try {
			// url = new URL("http://www.baidu.com");
			url = new URL("http://feeds2.feedburner.com/programthink");
			// url = new URL("http://www.tullyrankin.com/feed");
			InputStream openStream = url.openStream();
			String s = IoUtils.getString(openStream);
			s = s.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&");
			log.info("s = {}", s);
		} catch (Exception e) {
			log.error("Exception in TestClass.testUrl()", e);
		}
	}

}
