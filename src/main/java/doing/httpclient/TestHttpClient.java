package doing.httpclient;

import java.awt.MediaTracker;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pu.test.base.TestBase;
import org.pu.utils.HttpUtils;
import org.pu.utils.IoUtils;
import org.pu.utils.Utils;
import org.pu.utils.HttpUtils.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author Shang Pu
 * @version Date: Sep 7, 2012 5:27:26 PM
 */
public class TestHttpClient extends TestBase {
	private final static Logger log = LoggerFactory.getLogger(TestHttpClient.class);
	protected static DefaultHttpClient httpClient = new DefaultHttpClient(
			HttpUtils.createHttpParams());
	protected static HttpGet httpGet;
	protected static HttpPost httpPost;

	String webapp = "http://localhost:8080/webapp/";
	String url = "http://localhost:8080/webapp/shop/signonForm.do";
	protected String path = "c:/cache/test.html";
	protected String path2 = "c:/cache/test2.html";
	protected String path3 = "c:/cache/test3.html";
	protected static String username = "130629198603030438";
	protected static String pwd = "pass";

	@Test
	public void testDj() {
		loginDj("15110148227", "password");
	}

	public void loginDj(String username, String pwd) {
		try {
			// String randCode = getRandCode(httpClient, imgCodeUrl, 5);
			String randCode = HttpUtils.getRandCodeOcr(httpClient,
					"http://dj.192171.org/admin/rand.jsp?rd0.6710833205961946", new Validation() {

						@Override
						public boolean isRandomCodeValid(String code) {
							return isCodeValid(code);
						}

						@Override
						public int getTimes() {
							return 5;
						}

					});
			HttpPost httpPost = HttpUtils.setUpHttpPost("http://dj.192171.org/userLogin.action");
			// 登录表单的信息
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("__VIEWSTATE",
					"/wEPDwUKMTg0NDI4MDE5OGRkecATcwUaw2gw+8bwppLpsBEl2Scfh8acL+X+ssZCx+s="));
			params
					.add(new BasicNameValuePair("__EVENTVALIDATION",
							"/wEWBgKIhaiLCwKl1bKzCQK1qbSRCwLoyMm8DwLi44eGDAKAv7D9CmpaYpLSv4GbTlgEhWMl7ZXk+dc5ORysip3DOr8h9FQP"));
			params.add(new BasicNameValuePair("userName", username));
			params.add(new BasicNameValuePair("password", pwd));
			params.add(new BasicNameValuePair("checkCode", randCode));
			httpPost.setEntity(new UrlEncodedFormEntity(params, UTF8));

			HttpContext localContext = new BasicHttpContext();
			HttpResponse response = httpClient.execute(httpPost, localContext);
			log.info("getCookies() = {}", httpClient.getCookieStore().getCookies());

			// response = handleRedirection(response);
			int statusCode = response.getStatusLine().getStatusCode();
			if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
					|| (statusCode == HttpStatus.SC_MOVED_TEMPORARILY)
					|| (statusCode == HttpStatus.SC_SEE_OTHER)
					|| (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
				String newUri = response.getLastHeader("Location").getValue();
				// newUri = "http://114.242.121.99" + newUri;
				log.info("newUri = {}", newUri);
				newUri = "http://dj.192171.org/" + newUri;
				if (response.getEntity() != null) {
					EntityUtils.consume(response.getEntity());
				}
				HttpGet httpGet = HttpUtils.setUpHttpGet(newUri);
				response = httpClient.execute(httpGet);
			}

			String content = EntityUtils.toString(response.getEntity(), UTF8);
			log.info("content = {}", content);
			// {"result":1,"msg":"用户名密码校验失败，请重新输入！"}
			JSONObject jsonObject = new JSONObject(content);
			String msg = jsonObject.getString("msg");
			log.info("msg = {}", msg);
		} catch (Exception e) {
			log.error("Exception in HaiJia.loginYueChe()", e);
			// Utils.displayMsg(e.getMessage() + "; Please login again", false);
			log.info(e.getMessage() + "; Please login again");
		}
	}

	/**
	 * check if there are non alpha or non number, get randcode image again, return false
	 */
	private static boolean isCodeValid(String randomCode) {
		log.debug("randomCode = {}", randomCode);
		// return true;
		boolean isValid = randomCode.matches("[0-9]{4}");
		log.debug("Exit isRandomCodeValid() isValid = {}", isValid);
		return isValid;
	}

	public void testSCImgCode() {
		String url = "http://61.139.124.135:8635/indexBitmap.aspx?flagPassword=0.2563418115010797";
		getRandCode(httpClient, url, 5);
	}

	public static String getRandCode(DefaultHttpClient httpClient, String url, int frequent) {
		log.debug("Enter getRandCode({})", frequent);
		byte[] image = HttpUtils.getImageByte(httpClient, url);
		ImageIcon icon = new ImageIcon(image);
		if (MediaTracker.ERRORED == icon.getImageLoadStatus()) {
			log.info("Image is broken, reload again.");
			Utils.sleep(1000 / frequent);
			return getRandCode(httpClient, url, frequent);
		}
		JLabel label = new JLabel("label", JLabel.CENTER);
		label.setIcon(icon);
		label.setText(" Click Cancel to exit");
		log.info("Please input random code.");
		String input = JOptionPane.showInputDialog(null, label, "Please input random code.",
				JOptionPane.DEFAULT_OPTION);
		log.info("input = {}", input);
		if (input.equals("")) {
			log.info("input is empty");
			return getRandCode(httpClient, url, frequent);
		}
		log.debug("Exit getRandCode()");
		return input;
	}

	public void testGetTargetHost() throws IOException {
		HttpContext localContext = new BasicHttpContext();
		httpClient.execute(httpPost, localContext);
		HttpHost target = (HttpHost) localContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
		log.info("target = {}", target);
	}

	public String getCookieValue(String cookieName) {
		List<Cookie> cookies = httpClient.getCookieStore().getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(cookieName)) return cookie.getValue();
		}
		return "";
	}

	public void setCookie(DefaultHttpClient httpclient, List<Cookie> cookies) {
		log.debug("Enter setCookie()");
		if (cookies.isEmpty()) {
			log.info("Cookie is empty.");
			return;
		} else {
			for (int i = 0; i < cookies.size(); i++) {
				System.out.println((i + 1) + " - " + cookies.get(i).toString());
				httpclient.getCookieStore().addCookie(cookies.get(i));
			}
			System.out.println();
		}
		log.debug("Exit setCookie()");
	}

	public static String getContent(HttpEntity entity, String charset) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), charset));
		return IOUtils.toString(br);
	}

	/**
	 * 获取指定的POST页面,，因为post方法要封装参数，因此在函数外部封装好传参
	 */
	public String getHttpPostContent(HttpClient client, HttpPost post) throws IOException {
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseContent = "";
		try {
			responseContent = client.execute(post, responseHandler);
		} finally {
			post.abort();
		}
		return responseContent;
	}

	// 用post方法向服务器请求 并获得响应，因为post方法要封装参数，因此在函数外部封装好传参
	public HttpResponse postMethod(HttpClient client, HttpPost post) {
		HttpResponse resp = null;
		try {
			resp = client.execute(post);
		} catch (IOException e) {
			log.error("Exception in TestHttpclient.postMethod()", e);
		} finally {
			post.abort();
		}
		return resp;
	}

	// 用get方法向服务器请求并获得响应
	public HttpResponse getHttpGetResponse(HttpClient client, String url) {
		HttpGet get = new HttpGet(url);
		HttpResponse resp = null;
		try {
			resp = client.execute(get);
		} catch (IOException e) {
			log.error("Exception in TestHttpclient.getHttpGetResponse()", e);
		} finally {
			get.abort();
		}
		return resp;
	}

	@Before
	public void setUp() throws IOException {

	}

	@After
	public void tearDown() {
		if (httpPost != null) httpPost.reset();
		if (httpGet != null) httpGet.reset();
		// When HttpClient instance is no longer needed,
		// shut down the connection manager to ensure
		// immediate deallocation of all system resources
		if (httpClient != null) httpClient.getConnectionManager().shutdown();
	}

	protected void writeHtml(String path, String content) throws IOException {
		if (content == null) return;
		IoUtils.write(path, content.replace("<head>",
				"<head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>"));
	}
}
