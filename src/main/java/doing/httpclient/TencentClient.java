package doing.httpclient;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

/**
 * http://blog.csdn.net/wolfphantasms/article/details/7416533
 */
public class TencentClient {

	private static String password;
	/**
	 * 用户名或QQ号
	 */
	private static String username;

	public static void main(String[] args) {

		HttpClient client = new DefaultHttpClient();

		client.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 5000);
		try {

			/********************* 获取验证码 ***********************/
			HttpGet get = new HttpGet("http://ptlogin2.qq.com/check?uin=" + username
					+ "&appid=46000101&r=0.37908964480776913");

			HttpResponse response = client.execute(get);
			String entity = EntityUtils.toString(response.getEntity());
			String[] checkNum = entity.substring(entity.indexOf("(") + 1, entity.lastIndexOf(")"))
					.replace("'", "").split(",");

			System.out.println(checkNum[1]);

			String pass = "";

			/******************** *加密密码 ***************************/
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("javascript");

			String jsFileName = "src/md5.js"; // 指定md5加密文件
			// 读取js文件
			FileReader reader;

			reader = new FileReader(jsFileName);
			engine.eval(reader);
			if (engine instanceof Invocable) {
				Invocable invoke = (Invocable) engine;
				// 调用preprocess方法，并传入两个参数密码和验证码
				pass = (String) invoke.invokeFunction("preprocess", password, checkNum[1]);
				System.out.println("c = " + pass);
			}
			reader.close();

			/************************* 登录 ****************************/
			get = new HttpGet(
					"http://ptlogin2.qq.com/login?ptlang=2052&u=用户名或QQ号&p="
							+ pass
							+ "&verifycode="
							+ checkNum[1]
							+ "&low_login_enable=1&low_login_hour=720&aid=46000101&u1=http%3A%2F%2Ft.qq.com&ptredirect=1&h=1&from_ui=1&dumy=&fp=loginerroralert&action=4-13-11101&dummy=");
			System.out.println(get.getURI());

			response = client.execute(get);
			entity = EntityUtils.toString(response.getEntity());
			System.out.println(entity);

			/************************* 分享 *************************/
			HttpPost post = new HttpPost("http://radio.t.qq.com/publish.php");
			// 这句很重要
			post.addHeader(
					"Referer",
					"http://radio.t.qq.com/share.php?title=%E4%B9%8C%E9%B2%81%E6%9C%A8%E9%BD%90%E5%B8%82%E5%A4%A7%E9%A3%8E%E8%87%B473%E4%BA%BA%E5%8F%97%E4%BC%A4%203%E4%BA%BA%E6%AD%BB%E4%BA%A1(%E5%9B%BE)&url=http://news.qq.com/a/20120331/000027.htm&pref=qqcom.dp.tmblog&source=qqnews");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("content",
					"乌鲁木齐市大风致73人受伤 3人死亡(图) http://news.qq.com/a/20120331/000027.htm (分享自 @腾讯新闻 )"));
			nvps.add(new BasicNameValuePair("countType", ""));
			nvps.add(new BasicNameValuePair("pic", ""));
			nvps.add(new BasicNameValuePair("source", "1000001"));
			nvps.add(new BasicNameValuePair("sourcepic",
					"http://img1.gtimg.com/news/pics/hv1/125/130/1010/65708525.jpg"));
			nvps.add(new BasicNameValuePair("viewModel", "0"));
			nvps.add(new BasicNameValuePair("wizardpref", "qqcom.dp.tmblog"));
			nvps.add(new BasicNameValuePair("wizardurl", "http://news.qq.com/a/20120331/000027.htm"));
			post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			response = client.execute(post);
			entity = EntityUtils.toString(response.getEntity());
			System.out.println(entity);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
