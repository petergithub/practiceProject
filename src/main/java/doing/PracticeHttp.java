package doing;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.pu.test.base.TestBase;
import org.pu.utils.EscapeChars;
import org.pu.utils.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;

/**
 * @author Shang Pu
 * @version Date: Nov 12, 2015 10:33:59 AM
 */

public class PracticeHttp extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(PracticeHttp.class);


	@Test
	public void updateUserInfo() throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost("https://example.com/account/updateuserinfo");
		HttpParams params = new BasicHttpParams();
		params.setParameter("username", "pu.shang@tcl.com");
		params.setParameter("nickname", "测试昵称");
		params.setParameter("clientId", "51347980");
		params.setParameter("token", "69b6fb6fffac7df0b251d8a11fe21735");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post.setParams(params);

//		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse response = httpClient.execute(post);

		int statusCode = response.getStatusLine().getStatusCode();
		String result = "";
		if (statusCode == 200) {
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
		}
		log.debug("result[{}]", result);
	}

	@Test
	public void updateUserInfoStandard() throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost("https://example.com/account/updateuserinfo");
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("username", "pu.shang@tcl.com"));
		nvps.add(new BasicNameValuePair("nickname", "测试昵称updateUserInfoStandard"));
		nvps.add(new BasicNameValuePair("clientId", "51347980"));
		nvps.add(new BasicNameValuePair("token", "69b6fb6fffac7df0b251d8a11fe21735"));

		post.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8));

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(post);

		getResponseResult(response);
	}
	
	private void getResponseResult(HttpResponse response)
			throws IOException, UnsupportedEncodingException {
		// Get hold of the response entity
		HttpEntity entity = response.getEntity();
		// 查看所有返回头部信息
		Header headers[] = response.getAllHeaders();
		int ii = 0;
		while (ii < headers.length) {
			System.out.println(headers[ii].getName() + ": " + headers[ii].getValue());
			++ii;
		}

		// If the response does not enclose an entity, there is no need
		// to bother about connection release
		if (entity != null) {
			// 将源码流保存在一个byte数组当中，因为可能需要两次用到该流，
			byte[] bytes = EntityUtils.toByteArray(entity);
			String charSet = "";

			// 如果头部Content-Type中包含了编码信息，那么我们可以直接在此处获取
			// charSet = EntityUtils.getContentCharSet(entity);
			charSet = ContentType.getOrDefault(entity).getCharset().displayName();

			System.out.println("In header: " + charSet);
			// 如果头部中没有，那么我们需要 查看页面源码，这个方法虽然不能说完全正确，因为有些粗糙的网页编码者没有在页面中写头部编码信息
			System.out.println("Last get: " + charSet);
			// 至此，我们可以将原byte数组按照正常编码专成字符串输出（如果找到了编码的话）
			String result = new String(bytes, charSet);
			System.out.println("Encoding string is: " + result);
		}
	}

	@Test
	public void testEncodeDecode() throws UnsupportedEncodingException, URIException {
		String src = "DEMO测试";
		String target = "DEMO%E6%B5%8B%E8%AF%95";

		Assert.assertEquals(target, URLEncoder.encode(src, "UTF-8"));
		Assert.assertEquals(src, URLDecoder.decode(target, "UTF-8"));
		
		Assert.assertEquals(target, URIUtil.encode(src,new BitSet()));
		Assert.assertEquals(src, URIUtil.decode(target));
		Assert.assertEquals("测试账号123", URIUtil.decode("%E6%B5%8B%E8%AF%95%E8%B4%A6%E5%8F%B7123"));
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
