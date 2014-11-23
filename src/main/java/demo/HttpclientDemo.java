package demo;

import static org.pu.utils.Utils.UTF8;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.pu.utils.HtmlUtils;
import org.pu.utils.HttpUtils;
import org.pu.utils.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Shang Pu
 * @version Date: Nov 11, 2012 10:50:22 AM
 */
public class HttpclientDemo {
	private static final Logger log = LoggerFactory.getLogger(HttpclientDemo.class);
	HttpClient httpClient = new DefaultHttpClient();
	private String path = "";

	public void testGetGoogle() throws Exception {
		// 初始化，此处构造函数就与3.1中不同
		HttpClient httpclient = new DefaultHttpClient();

		HttpHost targetHost = new HttpHost("www.google.cn");
		HttpGet httpget = new HttpGet("/");

		// 查看默认request头部信息
		System.out.println("Accept-Charset:" + httpget.getFirstHeader("Accept-Charset"));
		// 以下这条如果不加会发现无论你设置Accept-Charset为gbk还是utf-8，他都会默认返回gb2312（本例针对google.cn来说）
		httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.2)");
		// 用逗号分隔显示可以同时接受多种编码
		httpget.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
		httpget.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
		// 验证头部信息设置生效
		System.out.println("Accept-Charset:" + httpget.getFirstHeader("Accept-Charset").getValue());

		// Execute HTTP request
		System.out.println("executing request " + httpget.getURI());

		HttpResponse response = httpclient.execute(targetHost, httpget);
		// HttpResponse response = httpclient.execute(httpget);

		System.out.println("----------------------------------------");
		System.out.println("Location: " + response.getLastHeader("Location"));
		System.out.println(response.getStatusLine().getStatusCode());
		System.out.println(response.getLastHeader("Content-Type"));
		System.out.println(response.getLastHeader("Content-Length"));

		System.out.println("----------------------------------------");

		// 判断页面返回状态判断是否进行转向抓取新链接
		int statusCode = response.getStatusLine().getStatusCode();
		if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statusCode == HttpStatus.SC_MOVED_TEMPORARILY)
				|| (statusCode == HttpStatus.SC_SEE_OTHER)
				|| (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
			// 此处重定向处理 此处还未验证
			String newUri = response.getLastHeader("Location").getValue();
			httpclient = new DefaultHttpClient();
			httpget = new HttpGet(newUri);
			response = httpclient.execute(httpget);
		}

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
			if (charSet == "") {
				String regEx = "(?=<meta).*?(?<=charset=[\\'|\\\"]?)([[a-z]|[A-Z]|[0-9]|-]*)";
				Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(new String(bytes)); // 默认编码转成字符串，因为我们的匹配中无中文，所以串中可能的乱码对我们没有影响
				boolean result = m.find();
				log.info("result = {}", result);
				if (m.groupCount() == 1) {
					charSet = m.group(1);
				} else {
					charSet = "";
				}
			}
			System.out.println("Last get: " + charSet);
			// 至此，我们可以将原byte数组按照正常编码专成字符串输出（如果找到了编码的话）
			System.out.println("Encoding string is: " + new String(bytes, charSet));
		}

		httpclient.getConnectionManager().shutdown();
	}

	public void springMvcShowcaseJsonPost() throws IOException, JSONException {
		String url = "http://localhost:8080/webapp/messageconverters/json";
		HttpPost httpPost = new HttpPost(url);
		JSONObject json = new JSONObject();
		json.put("foo", "bar");
		json.put("fruit", "apple");
		// { "foo": "bar", "fruit": "apple" }
		httpPost.setEntity(new StringEntity(json.toString()));
		httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
		HttpResponse response = httpClient.execute(httpPost);
		log.info("response.getStatusLine() = {}", response.getStatusLine());
		String content = HttpUtils.getResponseContent(response);
		Assert.assertEquals("Read from JSON: JavaBean {foo=[bar], fruit=[apple]}", content);
		writeHtml(path, content);
		JSONObject jsonObject = new JSONObject(content.replace("Read from JSON: JavaBean ", ""));
		String foo = jsonObject.getString("foo");
		Assert.assertEquals("[\"bar\"]", foo);
		String fruit = jsonObject.getString("fruit");
		Assert.assertEquals("[\"apple\"]", fruit);
	}

	public void springMvcShowcaseJsonGet() throws IOException, JSONException {
		String url = "http://localhost:8080/webapp/messageconverters/json";
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
		HttpResponse response = httpClient.execute(httpGet);
		String content = HttpUtils.getResponseContent(response);
		log.info("content = {}", content);
		// <?xml version="1.0" encoding="UTF-8"
		// standalone="yes"?><javaBean><foo>bar</foo><fruit>fruit</fruit></javaBean>
		writeHtml(path, content);
		// TODO: ResolveXML
	}

	public void springMvcShowcasePost() throws IOException {
		String url = "http://localhost:8080/webapp/data/entity";
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity = new StringEntity("foo");
		httpPost.setEntity(entity);
		HttpResponse response = httpClient.execute(httpPost);
		String content = HttpUtils.getResponseContent(response);
		log.debug("content = " + content);
	}

	public void springMvcShowcaseRssPost() throws IOException, JSONException {
		String url = "http://localhost:8080/webapp/messageconverters/rss";
		HttpPost httpPost = new HttpPost(url);
		// file content : <?xml version="1.0" encoding="UTF-8"?> <rss
		// version="2.0"><channel><title>My RSS feed</title></channel></rss>
		File file = new File("c:/cache/input.xml");
		StringEntity entity = new StringEntity(FileUtils.readFileToString(file));
		httpPost.setEntity(entity);
		httpPost.setHeader("Content-Type", "application/rss+xml;charset=UTF-8");
		HttpResponse response = httpClient.execute(httpPost);
		log.info("response.getStatusLine() = {}", response.getStatusLine());
		String content = HttpUtils.getResponseContent(response);
		Assert.assertEquals("Read My RSS feed", content);
	}

	public void springMvcShowcaseRssGet() throws IOException {
		String url = "http://localhost:8080/webapp/messageconverters/rss";
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Accept", "application/rss+xml;charset=UTF-8");
		HttpResponse response = httpClient.execute(httpGet);
		String content = HttpUtils.getResponseContent(response);
		log.info("content = {}", content);
		// {"language":null,"description":"Description","link":"http://localhost:8080/mvc-showcase/rss","categories":[],"modules":[],"title":"My RSS feed","generator":null,"copyright":null,"uri":null,"image":null,"items":[],"textInput":null,"rating":null,"pubDate":null,"lastBuildDate":null,"docs":null,"managingEditor":null,"webMaster":null,"skipHours":[],"skipDays":[],"cloud":null,"ttl":-1,"encoding":null,"feedType":"rss_2.0","foreignMarkup":[]}
		// <?xml version="1.0" encoding="UTF-8"?>
		// <rss version="2.0">
		// <channel>
		// <title>My RSS feed</title>
		// <link>http://localhost:8080/mvc-showcase/rss</link>
		// <description>Description</description>
		// </channel>
		// </rss>
		writeHtml(path, content);
	}

	public void springMvcShowcaseXmlPost() throws IOException, JSONException {
		String url = "http://localhost:8080/webapp/messageconverters/xml";
		HttpPost httpPost = new HttpPost(url);
		// file content : <?xml version="1.0" encoding="UTF-8"
		// standalone="yes"?><javaBean><foo>bar</foo><fruit>fruit</fruit></javaBean>
		File file = new File("c:/cache/input.xml");
		StringEntity entity = new StringEntity(FileUtils.readFileToString(file));
		httpPost.setEntity(entity);
		httpPost.setHeader("Content-Type", "application/xml;charset=UTF-8");
		HttpResponse response = httpClient.execute(httpPost);
		log.info("response.getStatusLine() = {}", response.getStatusLine());
		String content = HttpUtils.getResponseContent(response);
		Assert.assertEquals("Read from XML: JavaBean {foo=[bar], fruit=[apple]}", content);
		writeHtml(path, content);
		JSONObject jsonObject = new JSONObject(content.replace("Read from XML: JavaBean ", ""));
		String foo = jsonObject.getString("foo");
		log.info("foo = {}", foo);
		String fruit = jsonObject.getString("fruit");
		log.info("fruit = {}", fruit);
	}

	public void springMvcShowcaseXmlGet() throws IOException {
		String url = "http://localhost:8080/webapp/messageconverters/xml";
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = httpClient.execute(httpGet);
		String content = HttpUtils.getResponseContent(response);
		log.info("content = {}", content);
		// <?xml version="1.0" encoding="UTF-8"
		// standalone="yes"?><javaBean><foo>bar</foo><fruit>fruit</fruit></javaBean>
		writeHtml(path, content);
	}

	public void quickStartSearch() throws IOException {
		quickStartLogin("user");
		// http://localhost:8080/quickstart/task?search_LIKE_title=s#
		String searchUrl = "http://localhost:8080/quickstart/task?search_LIKE_title={0}#";
		String parameter = "a";
		searchUrl = MessageFormat.format(searchUrl, parameter);
		HttpGet httpGet = new HttpGet(searchUrl);
		HttpResponse response = httpClient.execute(httpGet);
		String content = HttpUtils.getResponseContent(response);
		log.debug("content = " + content);
		writeHtml(path, content);
	}

	public void quickStartUpdateUser() throws IOException {
		quickStartLogin("admin");
		String updateUserUrl = "http://localhost:8080/quickstart/admin/user/update";
		HttpPost httpPost = new HttpPost(updateUserUrl);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", "2"));
		params.add(new BasicNameValuePair("name", "Calvina"));
		params.add(new BasicNameValuePair("plainPassword", "user"));
		params.add(new BasicNameValuePair("confirmPassword", "user"));
		httpPost.setEntity(new UrlEncodedFormEntity(params, (Charset) null));
		HttpResponse response = httpClient.execute(httpPost);
		String content = HttpUtils.getResponseContent(response);
		log.debug("content = " + content);
	}

	public void quickStartCreateTask() throws IOException {
		quickStartLogin("admin");
		String createTaskUrl = "http://localhost:8080/quickstart/task/create";
		HttpPost httpPost = new HttpPost(createTaskUrl);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("description", "ba"));
		params.add(new BasicNameValuePair("title", "bbba"));
		params.add(new BasicNameValuePair("id", ""));
		httpPost.setEntity(new UrlEncodedFormEntity(params, UTF8));
		HttpResponse response = httpClient.execute(httpPost);
		httpPost.reset();
		response = HttpUtils.handleRedirection(httpClient, response, "");
		String content = HttpUtils.getResponseContent(response);
		log.debug("content = " + content);
	}

	public void quickStartLogin(String user) throws IOException {
		String loginUrl = "http://localhost:8080/quickstart/login";
		HttpPost httpPost = new HttpPost(loginUrl);
		// 登录表单的信息
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", user));
		params.add(new BasicNameValuePair("password", user));
		httpPost.setEntity(new UrlEncodedFormEntity(params, UTF8));
		HttpResponse response = httpClient.execute(httpPost);
		httpPost.reset();
		response = HttpUtils.handleRedirection(httpClient, response, "");
		String content = HttpUtils.getResponseContent(response);
		if (content.contains("记住我")) {
			log.error("=========Login failed=========");
			throw new RuntimeException("Login failed");
		}
	}

	public void testLoginCaixin() throws IOException {
		String loginUrl = "http://user.caixin.com/usermanage/login";
		String afterLoginUrl = "http://user.caixin.com/";

		HttpClient client = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(loginUrl);
		// 登录表单的信息
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", "sam.ukoom.com@gmail.com"));
		params.add(new BasicNameValuePair("url", afterLoginUrl));
		params.add(new BasicNameValuePair("password", "password"));
		httppost.setEntity(new UrlEncodedFormEntity(params, UTF8));
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = client.execute(httppost, responseHandler);

		log.info("responseBody = {}", HtmlUtils.removeHtmlTag(responseBody));
		String content = HttpUtils.getHttpGetContent(client, afterLoginUrl, UTF8);
		IoUtils.write(path, content);
		log.info("content = {}", HtmlUtils.removeHtmlTag(content));
	}

	public void loginDict() throws IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://dict.cn/login.php");
		post.setHeader("User-Agent", HttpUtils.userAgent);

		// 登录表单的信息
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", "samukoom"));
		params.add(new BasicNameValuePair("password", "password"));
		params.add(new BasicNameValuePair("url", "http://my.dict.cn/space-uid-1750387.html"));
		params.add(new BasicNameValuePair("loginforever", "1"));
		post.setEntity(new UrlEncodedFormEntity(params, "GBK"));

		// Execute the request
		HttpResponse response = client.execute(post);

		// Examine the response status
		log.info("response.getStatusLine() = {}", response.getStatusLine());

		// Get hold of the response entity
		HttpEntity entity = response.getEntity();

		// 打印页面
		String content = EntityUtils.toString(entity);
		content = HttpUtils.getHttpGetContent(client, "http://my.dict.cn/space-uid-1750387.html", "");

		IoUtils.write(path, content);
		log.info("content = {}", content);
	}

	public void getMobilePhone() throws URISyntaxException, IOException {
		String num = "13936313761";
		HttpGet httpget = HttpUtils.setUpHttpGet("http://haoma.imobile.com.cn/index.php?mob=" + num);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		HttpClient httpclient = new DefaultHttpClient();
		String content = httpclient.execute(httpget, responseHandler); // 打印服务器返回的状态
		String con = content.substring(content.indexOf("<table>"), content.indexOf("</table>"));
		System.out.println(con);
		System.out.println(HtmlUtils.removeHtmlTag(con));
	}

	/**
	 * httpclient4 如何模拟表单提交文件
	 * 
	 * <pre>
	 * 		<form action="127.0.0.1/up.php" method="post" enctype="multipart/form-data">
	 * 		<input type="text" name="filename">
	 * 		<input type="file" name="file">
	 * 		<input type="submit" name="t" value="提交">
	 * 		</form>
	 * </pre>
	 */
	public void PostFile() throws IOException {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

		HttpPost httppost = new HttpPost("http://localhost:9001/upload.php");
		File file = new File("c:/TRASH/zaba_1.jpg");

		MultipartEntity mpEntity = new MultipartEntity();
		ContentBody cbFile = new FileBody(file, "image/jpeg");
		mpEntity.addPart("userfile", cbFile);

		httppost.setEntity(mpEntity);
		System.out.println("executing request " + httppost.getRequestLine());
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity resEntity = response.getEntity();

		System.out.println(response.getStatusLine());
		if (resEntity != null) {
			System.out.println(EntityUtils.toString(resEntity));
			EntityUtils.consume(resEntity);
		}
		httpclient.getConnectionManager().shutdown();
	}

	private void writeHtml(String path, String content) throws IOException {
		if (content == null) return;
		IoUtils.write(path, content.replace("<head>",
				"<head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>"));
	}

	static class OpenId {
		private HttpClient httpClient;

		public OpenId(String loginURL, String userName, String password) {
			httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(loginURL);
			// 登录表单的信息, 将要POST的数据封包
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", userName));
			params.add(new BasicNameValuePair("password", password));

			try {
				httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
				HttpUtils.postMethod(httpClient, httpPost);
			} catch (IOException e) {
				log.error("Exception in OpenId.OpenId()", e);
			}
		}

		public static void main(String[] args) throws IOException {
			String url = "http://www.openid.org.cn/login";
			OpenId openId = new OpenId(url, "samopenid", "password");

			String content = HttpUtils.getHttpGetContent(openId.httpClient,
					"http://www.openid.org.cn/sites");
			log.info("content = {}", HtmlUtils.removeHtmlTag(content));
		}
	}
}
