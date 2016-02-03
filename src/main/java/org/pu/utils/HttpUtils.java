package org.pu.utils;

import static org.pu.utils.Constants.EOL;

import java.awt.MediaTracker;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.ImageIcon;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import doing.ocr.Ocr;

/**
 * @version Date: Dec 25, 2012 8:21:04 AM
 * @author Shang Pu
 */
// Host: 114.242.121.99
// User-Agent: Mozilla/5.0 (Windows NT 5.1; rv:16.0) Gecko/20100101 Firefox/16.0
// Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
// Accept-Language: en-us,zh-cn;q=0.7,en;q=0.3
// Accept-Encoding: gzip, deflate
// Connection: keep-alive
// Cookie: ASP.NET_SessionId=qpvbabdhwwik0ybub3lg221c
// String userAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)";
// <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
public class HttpUtils {
	private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);
	public static final String userAgent = "Mozilla/5.0 (Windows NT 5.1; rv:16.0) Gecko/20100101 Firefox/16.0";
	public static final String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
	public static final String acceptLanguage = "en-us,zh-cn;q=0.7,en;q=0.3";
	public static final String acceptEncoding = "gzip, deflate";
	public static final String acceptCharset = "GB2312,utf-8;q=0.7,*;q=0.7";
	public static final String connection = "keep-alive";
	public static final int TIMEOUT = 60;
	public static final String FAIL = "FAIL";
	public static final String SUCCESS = "Success";

	public static String getResponseContent(HttpResponse response) throws IOException {
		return EntityUtils.toString(response.getEntity(), (Charset) null);
	}

	/**
	 * 301 Moved Permanently. HttpStatus.SC_MOVED_PERMANENTLY 302 Moved Temporarily.
	 * HttpStatus.SC_MOVED_TEMPORARILY 303 See Other. HttpStatus.SC_SEE_OTHER 307 Temporary Redirect.
	 * HttpStatus.SC_TEMPORARY_REDIRECT
	 * 
	 * @param httpClient
	 * @param response
	 * @param url
	 */
	public static HttpResponse handleRedirection(HttpClient httpClient, HttpResponse response,
			String url) throws IOException {
		int statusCode = response.getStatusLine().getStatusCode();
		log.debug("Enter handleRedirection() statusCode = {}", statusCode);
		if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statusCode == HttpStatus.SC_MOVED_TEMPORARILY)
				|| (statusCode == HttpStatus.SC_SEE_OTHER)
				|| (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
			String location = response.getLastHeader("Location").getValue();
			location = toRedirectURL(location, url);
			log.info("newUrl location = {}", location);
			if (response.getEntity() != null) {
				EntityUtils.consume(response.getEntity());
			}
			HttpGet httpGet = HttpUtils.setUpHttpGet(location);
			response = httpClient.execute(httpGet);
		}
		log.debug("Exit handleRedirection()");
		return response;
	}

	private static String toRedirectURL(String location, String homeUrl) {
		if (location == null || location.trim().length() == 0) {
			location = "/";
		}
		String tmp = location.toLowerCase();
		if (!tmp.startsWith("http://") && !tmp.startsWith("https://")) {
			if (homeUrl == null) {
				throw new IllegalArgumentException(
						"homeUrl is null, can not find relative protocol and host name");
			}
			if (!homeUrl.endsWith("/")) homeUrl += "/";
			return homeUrl + location;
		}
		return location;
	}

	public static HttpPost setUpHttpPost(String url) {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("User-Agent", userAgent);
		httpPost.setHeader("Accept-Language", acceptLanguage);
		httpPost.setHeader("Accept-Encoding", acceptEncoding);
		httpPost.setHeader("Connection", connection);
		return httpPost;
	}

	public static HttpGet setUpHttpGet(String url) {
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("User-Agent", userAgent);
		// 用逗号分隔显示可以同时接受多种编码
		httpGet.setHeader("Accept-Language", acceptLanguage);
		// 以下这条如果不加会发现无论你设置Accept-Charset为gbk还是utf-8，他都会默认返回gb2312（本例针对google.cn来说）
		httpGet.setHeader("Accept-Charset", acceptCharset);
		return httpGet;
	}

	public static final HttpParams createHttpParams() {
		final HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);
		HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192 * 5);
		return params;
	}

	/**
	 * 获取指定的GET页面
	 */
	public static String getHttpGetContent(HttpClient httpClient, String url) throws IOException {
		return getHttpGetContent(httpClient, url, "");
	}

	/**
	 * 获取指定的GET页面
	 */
	public static String getHttpGetContent(HttpClient httpClient, String url, String charsetName)
			throws IOException {
		Charset charset;
		if (charsetName == null || charsetName.isEmpty()) {
			charset = null;
		} else {
			charset = Charset.forName(charsetName);
		}
		String content = "";
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse resp = httpClient.execute(get);
			content = EntityUtils.toString(resp.getEntity(), charset);
		} finally {
			get.abort();
		}
		return content;
	}

	// 用post方法向服务器请求 并获得响应，因为post方法要封装参数，因此在函数外部封装好传参
	public static HttpResponse postMethod(HttpClient httpClient, HttpPost post) throws IOException {
		HttpResponse resp = null;
		try {
			resp = httpClient.execute(post);
		} finally {
			post.abort();
		}
		return resp;
	}

	/**
	 * get指定的POST页面,，因为post方法要封装参数，因此在函数外部封装好传参
	 * 
	 * @param post
	 * @return String txt
	 * @throws IOException
	 */
	public static String getPostPage(HttpClient httpClient, HttpPost post) throws IOException {
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String content = null;
		try {
			content = httpClient.execute(post, responseHandler);
		} finally {
			post.abort();
		}
		return content;
	}

	/**
	 * return random code
	 */
	public static String getRandCodeOcr(DefaultHttpClient httpClient, String url,
			Validation validation) {
		log.debug("Enter getRandCodeOcr({})");
		File image = getImage(httpClient, url);
		ImageIcon icon = new ImageIcon(image.getAbsolutePath());
		if (MediaTracker.ERRORED == icon.getImageLoadStatus()) {
			log.info("Image is broken, reload again.");
			try {
				Thread.sleep(1000 / validation.getTimes());
			} catch (InterruptedException e) {
				log.error("InterruptedException in HttpUtils.getRandCodeOcr()", e);
			}
			return getRandCodeOcr(httpClient, url, validation);
		}
		String result = "";
		try {
			result = Ocr.tesseract(image).replace(" ", "").replace(EOL, "");
			if (!validation.isRandomCodeValid(result)) {
				return getRandCodeOcr(httpClient, url, validation);
			}
		} catch (IOException e) {
			log.error("Exception in HttpUtils.getRandCodeOcr()", e);
		} finally {
			// delete temp image
			image.delete();
		}
		log.debug("Exit getRandCodeOcr()");
		return result;
	}

	/**
	 * 获取指定url的图片字节信息
	 */
	public static byte[] getImageByte(DefaultHttpClient httpClient, String url) {
		log.debug("Enter getImageByte({})", url);
		HttpGet get = new HttpGet(url);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		HttpEntity entity = null;
		try {
			HttpResponse response = httpClient.execute(get);
			entity = response.getEntity();
			if (entity != null) {
				InputStream is = entity.getContent();
				byte[] buf = new byte[1024];
				int len = -1;
				while ((len = is.read(buf)) > -1) {
					baos.write(buf, 0, len);
				}
			}
		} catch (IOException e) {
			log.error("IOException in HttpUtils.getImageByte()", e);
		} finally {
			try {
				EntityUtils.consume(entity);
			} catch (IOException e) {
			}
		}
		log.debug("Exit getImageByte()");
		return baos.toByteArray();
	}

	/**
	 * 获取指定url图片
	 */
	public static File getImage(HttpClient httpClient, String url) {
		log.debug("Enter getImage({})", url);
		FileOutputStream out = null;
		File image = null;
		HttpGet get = new HttpGet(url);
		try {
			image = File.createTempFile("tempImageFile", null);
			out = new FileOutputStream(image);
			HttpResponse response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream is = entity.getContent();
				byte[] buf = new byte[1024];
				int len = -1;
				while ((len = is.read(buf)) > -1) {
					out.write(buf, 0, len);
				}
			}
		} catch (IOException e) {
			log.error("IOException in HttpUtils.getImage()", e);
		} finally {
			IoUtils.close(out);
			log.debug("image path = {}", image.getAbsolutePath());
		}
		log.debug("Exit getImage()");
		return image;
	}

	/**
	 * invode function in jsFile
	 * 
	 * @param jsFile javascript file
	 * @param function function name in js file
	 */
	public static String invokeJsFunction(String code, String jsFile, String function) {
		log.debug("Enter invokeJsFunction({},{},{})", new Object[] { code, jsFile, function });
		String result = "";
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		FileReader reader = null;// 读取js文件
		try {
			try {
				reader = new FileReader(IoUtils.getFile(jsFile));
				engine.eval(reader);
			} catch (IllegalArgumentException e) {
				log.debug("read file inside the jar");
				// read file inside jar "/ych2.js" 返回读取指定资源的输入流
				String script = IoUtils.getContentInJar(jsFile);
				log.debug("script = {}", script);
				engine.eval(script);
			}

			if (engine instanceof Invocable) {
				Invocable invoke = (Invocable) engine;
				// 调用function，并传入参数
				result = (String) invoke.invokeFunction(function, code);
			}
		} catch (Exception e) {
			log.error("Exception in GetCars.md5ImgCode()", e);
		} finally {
			IoUtils.close(reader);
		}
		log.debug("Exit invokeJsFunction() result = {}", result);
		return result;
	}

	public interface Validation {
		/**
		 * run it ? times every minute
		 */
		public int getTimes();

		public boolean isRandomCodeValid(String code);

	}
}
