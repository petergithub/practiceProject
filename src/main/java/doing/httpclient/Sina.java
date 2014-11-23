package doing.httpclient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

/**
 * http://blog.csdn.net/wolfphantasms/article/details/7398260
 */
public class Sina {

	private static String username;
	private static String password;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.cookie-policy",
				CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 5000);
		try {
			HttpPost post = new HttpPost(
					"http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.3.19)");
			String data = getServerTime();
			String nonce = makeNonce(6);

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("entry", "weibo"));
			nvps.add(new BasicNameValuePair("gateway", "1"));
			nvps.add(new BasicNameValuePair("from", ""));
			nvps.add(new BasicNameValuePair("savestate", "7"));
			nvps.add(new BasicNameValuePair("useticket", "1"));
			nvps.add(new BasicNameValuePair("ssosimplelogin", "1"));
			nvps.add(new BasicNameValuePair("su", encodeAccount(username)));
			nvps.add(new BasicNameValuePair("service", "miniblog"));
			nvps.add(new BasicNameValuePair("servertime", data));
			nvps.add(new BasicNameValuePair("nonce", nonce));
			nvps.add(new BasicNameValuePair("pwencode", "wsse"));
			nvps.add(new BasicNameValuePair("sp", new SinaSSOEncoder().encode(password, data, nonce)));

			nvps.add(new BasicNameValuePair("url",
					"http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack"));
			nvps.add(new BasicNameValuePair("returntype", "META"));
			nvps.add(new BasicNameValuePair("encoding", "UTF-8"));
			nvps.add(new BasicNameValuePair("vsnval", ""));

			post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));

			HttpResponse response = client.execute(post);
			String entity = EntityUtils.toString(response.getEntity());

			String url = entity.substring(entity.indexOf("http://weibo.com/ajaxlogin.php?"),
					entity.indexOf("code=0") + 6);

			// 获取到实际url进行连接
			HttpGet getMethod = new HttpGet(url);

			response = client.execute(getMethod);
			entity = EntityUtils.toString(response.getEntity());
			entity = entity.substring(entity.indexOf("userdomain") + 13, entity.lastIndexOf("\""));
			System.out.println(entity);

			getMethod = new HttpGet("http://weibo.com/u/2745482124?topnav=1");
			response = client.execute(getMethod);
			entity = EntityUtils.toString(response.getEntity());
			// Document doc =
			// Jsoup.parse(EntityUtils.toString(response.getEntity()));
			System.out.println(entity);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private static String encodeAccount(String account) {
		String userName = "";
		try {
			userName = Base64.encodeBase64String(URLEncoder.encode(account, "UTF-8").getBytes());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userName;
	}

	private static String makeNonce(int len) {
		String x = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String str = "";
		for (int i = 0; i < len; i++) {
			str += x.charAt((int) (Math.ceil(Math.random() * 1000000) % x.length()));
		}
		return str;
	}

	private static String getServerTime() {
		long servertime = new Date().getTime() / 1000;
		return String.valueOf(servertime);
	}
}
