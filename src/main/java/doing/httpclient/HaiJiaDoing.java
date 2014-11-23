package doing.httpclient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pu.test.base.TestBase;
import org.pu.utils.HtmlUtils;
import org.pu.utils.HttpUtils;
import org.pu.utils.HttpUtils.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @version Date: Dec 6, 2012 2:49:53 PM
 * @author Shang Pu
 */
public class HaiJiaDoing extends TestBase {
	private static final String contentTypeJson = "application/json;charset=UTF-8";
	private static final Logger log = LoggerFactory.getLogger(HaiJiaDoing.class);

	protected static DefaultHttpClient httpClient = new DefaultHttpClient();
	private String pathYueCheList = "c:/cache/yuecheList.html";
	private String pathYuekao2 = "c:/cache/yuekao2.html";

	private static String urlLoginYueChe = "http://114.242.121.99";
	private static String urlGetCars = "http://114.242.121.99/Han/ServiceBooking.asmx/GetCars";
	private static String urlBookingCar = "http://114.242.121.99/Han/ServiceBooking.asmx/BookingCar";
	private static String urlImgCode = "http://114.242.121.99/tools/CreateCode.ashx?key=ImgCode&random=0.9671970325095187";
	private static String urlImgCodeBooking = "http://114.242.121.99/tools/CreateCode.ashx?key=BookingCode&random=0.6569643824735577";
	private static String urlYueKao3 = "http://114.242.121.99/yk3.aspx";// 科目三考试

	private static String username;
	private static String pwd;
	int i = 1;

	public void testLogin() {
		loginYueChe(username, pwd);
	}

	public void testHtmlParse() throws IOException {
		File input = new File(pathYuekao2);
		Document doc = Jsoup.parse(input, "UTF-8");
		Elements table = doc.body().getElementsByAttributeValue("id", "tblMain");
		Elements trs = table.get(0).getElementsByTag("tr");
		for (Element tr : trs) {
			Elements tds = tr.getElementsByTag("td");
			if (tds.size() > 0) {
				String count = tds.get(2).text();
				if (!count.equals("0")) {
					String date = tds.get(0).text() + tds.get(1).text();
					log.info("date = {}", date);
					String js = tds.get(3).child(0).attr("href");
					String link = js.replace("javascript:__doPostBack('", "").replace("','')", "");
					log.info("link = {}", link);
				}
			}
		}
	}

	public void selectLivingCar() throws IOException {
		File input = new File(pathYueCheList);
		Document doc = Jsoup.parse(input, "UTF-8");
		Elements el = doc.getElementsByAttributeValue("title", "预约");
		Elements e2 = doc.select("#divCarsList");
		log.info("e2 = {}", e2);
		for (Element e : el) {
			String value = e.text();
			String yyrq = e.attr("yyrq");
			log.info("yyrq = {}, carNum = {}", yyrq, value);
		}
	}

	public void testYueKao3() throws IOException {
		loginYueChe(username, pwd);
		HttpPost httpPost = new HttpPost(urlYueKao3);
		String target = "RepeaterKM3Exam$ctl00$LinkBooking";
		// 表单信息
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params
				.add(new BasicNameValuePair(
						"__VIEWSTATE",
						"/wEPDwULLTE2OTg2ODQxNTAPZBYCAgEPZBYCAgEPFgIeC18hSXRlbUNvdW50AgMWBmYPZBYEZg8VAwoyMDEyLTEyLTA4BuS4i+WNiAEwZAIBDw8WBB4EVGV4dAUM6aKE57qm6ICD6K+VHg9Db21tYW5kQXJndW1lbnQFFDIwMTItMTItOCAwOjAwOjAwLDEyZGQCAQ9kFgRmDxUDCjIwMTItMTItMTIG5LiK5Y2IATBkAgEPDxYEHwEFDOmihOe6puiAg+ivlR8CBRUyMDEyLTEyLTEyIDA6MDA6MDAsMTFkZAICD2QWBGYPFQMKMjAxMi0xMi0xOQbkuIrljYgCMTFkAgEPDxYEHwEFDOmihOe6puiAg+ivlR8CBRUyMDEyLTEyLTE5IDA6MDA6MDAsMTFkZGS2wei13+kV/gmweKOh7pbzusGwBY2xsHKDirAHf1wLnw=="));
		params.add(new BasicNameValuePair("__EVENTVALIDATION",
				"/wEWBAKOlc3YAQL39sz6BQLans/6BQK1xcj6BYpjCr85EhlPe7EercMLssXihTD019qGNGVZawZ1egZC"));
		params.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		params.add(new BasicNameValuePair("__EVENTTARGET", target));
		httpPost.setEntity(new UrlEncodedFormEntity(params, UTF8));

		HttpResponse response = httpClient.execute(httpPost);
		String htmlContent = HttpUtils.getResponseContent(response);
		String resultMsg = getAlertMsg(htmlContent);
		log.info("resultMsg = {}", resultMsg);

		String content = HtmlUtils.removeHtmlTag(htmlContent);
		log.info("content = {}", content);
	}

	@Test
	public void testGetCarList() throws IOException, JSONException {
		loginYueChe(username, pwd);

		HttpPost httpPost = HttpUtils.setUpHttpPost(urlGetCars);
		String jsonRequestStr = buildGetCarRequestStr("20121214", "58");
		httpPost.setEntity(new StringEntity(jsonRequestStr));
		httpPost.setHeader("Content-Type", contentTypeJson);

		HttpResponse response = httpClient.execute(httpPost);
		log.info("getCookies() = {}", httpClient.getCookieStore().getCookies());
		response = HttpUtils.handleRedirection(httpClient, response, urlLoginYueChe);

		String content = HttpUtils.getResponseContent(response);
		log.info("content = {}", content);
		if (content.contains("_0")) {
			log.debug("Exit getCarList() no car is left");
			return;
		}
		content = content.replace("_1", "").replace("\"[", "[").replace("]\"", "]").replace('\"', '"');
		log.info("content2 = {}", content);
		JSONObject jsonObject = new JSONObject(content);
		JSONArray array = jsonObject.getJSONArray("d");
		List<String> carList = new ArrayList<String>();
		String YYRQ = "";
		String XNSD = "";
		for (int i = 0; i < array.length(); i++) {
			JSONObject json = array.getJSONObject(i);
			YYRQ = json.getString("YYRQ");
			XNSD = json.getString("XNSD");
			String CNBH = json.getString("CNBH");
			carList.add(CNBH);
		}
		log.info("YYRQ = {}, XNSD = {}, CNBH = {}", new Object[] { YYRQ, XNSD, carList });
	}

	public void testBookCar() {
		String cnbh = "08020";
		loginYueChe(username, pwd);
		// String cnbh = "05070";
		try {
			HttpPost httpPost = new HttpPost(urlBookingCar);
			String yyrq = "20121219";
			String time = "58";
			String jsonRequestStr;
			jsonRequestStr = buildBookCarRequestStr(yyrq, time, cnbh);
			httpPost.setEntity(new StringEntity(jsonRequestStr));
			httpPost.setHeader("Content-Type", contentTypeJson);
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				log.info("StatusCode = {}", HttpStatus.SC_INTERNAL_SERVER_ERROR);
				return;
			}
			response = HttpUtils.handleRedirection(httpClient, response, urlLoginYueChe);

			String content = HttpUtils.getResponseContent(response);
			// {"d":"[\r\n  {\r\n    \"Result\": false,\r\n    \"OutMSG\": \"验证码错误！\"\r\n  }\r\n]"}
			log.info("content = {}", content);
			log.info("content2 = {}", content);
			JSONObject jsonObject = new JSONObject(content);
			String value = jsonObject.getString("d");
			JSONArray array = new JSONArray(value);
			for (int i = 0; i < array.length(); i++) {
				JSONObject json = array.getJSONObject(i);
				String result = json.getString("Result");
				String outMSG = json.getString("OutMSG");
				log.info("result = {}, outMSG = {}", result, outMSG);
				if (result.equals("true")) {
					log.info("booked successfully");
				}
			}
		} catch (Exception e) {
			log.error("Exception in HaiJiaDoing.testBookCar()", e);
		}
	}

	private String buildBookCarRequestStr(String yyrq, String time, String cnbh) throws Exception {
		JSONObject jsonRequest = new JSONObject();
		jsonRequest.put("yyrq", yyrq);
		jsonRequest.put("xnsd", time);
		jsonRequest.put("cnbh", cnbh);
		jsonRequest.put("KMID", "2");

		String code = getRandomCode(urlImgCodeBooking);
		// md5Code
		String imgCode = HttpUtils.invokeJsFunction(code, "ych2.js", "hex_md5");
		jsonRequest.put("imgCode", imgCode);
		String jsonRequestStr = jsonRequest.toString();
		log.info("jsonRequestStr = {}", jsonRequestStr);
		return jsonRequestStr;
	}

	private String buildGetCarRequestStr(String yyrq, String yysd) throws JSONException {
		JSONObject jsonRequest = new JSONObject();
		jsonRequest.put("yyrq", yyrq);
		jsonRequest.put("yysd", yysd);
		jsonRequest.put("xllxID", "2");
		jsonRequest.put("pageSize", "35");
		jsonRequest.put("pageNum", "1");
		String jsonRequestStr = jsonRequest.toString();
		log.info("jsonRequestStr = {}", jsonRequestStr);
		return jsonRequestStr;
	}

	/*public void testWebServices() {
		String result = getFromWebService("http://114.242.121.99/Han/ServiceBooking.asmx", "");
		log.info("result = {}", result);
	}*/

	// <GetCars xmlns="http://tempuri.org/">
	// <yyrq>string</yyrq>
	// <yysd>string</yysd>
	// <xllxID>string</xllxID>
	// <pageSize>int</pageSize>
	// <pageNum>int</pageNum>
	// </GetCars>
	/*public String getFromWebService(String endpoint, String xmlStr) {
		String newXmlStr = "";
		Service service = new Service();
		try {
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			String namespaceURI = "StuSite.Han";
			call.setOperationName(new QName(namespaceURI, "GetCars"));
			call.addParameter(new QName(namespaceURI, "yyrq"), Constants.XSD_STRING, ParameterMode.IN);
			call.addParameter(new QName(namespaceURI, "yysd"), Constants.XSD_STRING, ParameterMode.IN);
			call.addParameter(new QName(namespaceURI, "xllxID"), Constants.XSD_STRING, ParameterMode.IN);
			call.addParameter(new QName(namespaceURI, "pageSize"), Constants.XSD_INT, ParameterMode.IN);
			call.addParameter(new QName(namespaceURI, "pageNum"), Constants.XSD_INT, ParameterMode.IN);
			call.setReturnType(Constants.XSD_STRING);
			call.setUseSOAPAction(true);
			call.setSOAPActionURI("http://tempuri.org/GetCars");
			call.setUsername(username);
			call.setPassword(pwd);
			newXmlStr = (String) call.invoke(new Object[] { "20121122", "15", "2", "35", "1" });
		} catch (Exception e) {
			log.error("Exception in TestHttpclient.getFromWebService()", e);
		}
		return newXmlStr;
	}*/

	private boolean isLogin(String content) {
		return !content.contains("账号");
	}

	private boolean isContentValid(String content) {
		if (content.contains("Service Unavailable") || content.contains("运行时错误")) {
			return false;
		}
		return true;
	}

	public void loginYueChe(String username, String pwd) {
		try {
			// String randCode = getRandCode(httpClient, imgCodeUrl, 5);
			String code = getRandomCode(urlImgCode);
			String resultMsg = HttpUtils.FAIL;

			HttpPost httpPost = HttpUtils.setUpHttpPost(urlLoginYueChe);
			// 登录表单的信息
			List<NameValuePair> params = new ArrayList<NameValuePair>();
//			params.add(new BasicNameValuePair("__VIEWSTATE",
//					"/wEPDwUKMTg0NDI4MDE5OGRkecATcwUaw2gw+8bwppLpsBEl2Scfh8acL+X+ssZCx+s="));
//			params
//					.add(new BasicNameValuePair("__EVENTVALIDATION",
//							"/wEWBgKIhaiLCwKl1bKzCQK1qbSRCwLoyMm8DwLi44eGDAKAv7D9CmpaYpLSv4GbTlgEhWMl7ZXk+dc5ORysip3DOr8h9FQP"));
			params.add(new BasicNameValuePair("__VIEWSTATE",
					"/wEPDwUJLTE5MzE4NjU3D2QWAgIBD2QWAgINDxYCHglpbm5lcmh0bWwFAjk5ZGSJY6WW52dstnUjyuwcxwqksDuWSDLaFgmMK53lVzt5PQ=="));
			params
			.add(new BasicNameValuePair("__EVENTVALIDATION",
					"/wEWBgKU7PaxCgKl1bKzCQK1qbSRCwLoyMm8DwLi44eGDAKAv7D9CkWv8qjbZguciWb/US2al4x/yk1a6os+TZev6Kbjo+ma"));
			params.add(new BasicNameValuePair("txtUserName", username));
			params.add(new BasicNameValuePair("txtPassword", pwd));
			params.add(new BasicNameValuePair("txtIMGCode", code));
			params.add(new BasicNameValuePair("rcode", ""));
			params.add(new BasicNameValuePair("BtnLogin", "登 录"));
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
				newUri = urlLoginYueChe + newUri;
				log.info("newUri = {}", newUri);
				if (response.getEntity() != null) {
					EntityUtils.consume(response.getEntity());
				}
				HttpGet httpGet = HttpUtils.setUpHttpGet(newUri);
				response = httpClient.execute(httpGet);
			}

			String htmlContent = HttpUtils.getResponseContent(response);
			resultMsg = getAlertMsg(htmlContent);

			String content = HtmlUtils.removeHtmlTag(htmlContent);
			log.info("content = {}", content);
			if (isContentValid(content) && isLogin(content) && resultMsg.equals("")) {
				return;
			} else if (resultMsg.contains("系统服务时间每天从07:35-20:00")
					&& !(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null,
							"系统服务时间每天从07:35-20:00\r\nContinute to login?"))) {
				exit("Exit!!!");
			} else {
				// Utils.displayMsg(resultMsg + " Please login again", false);
				log.info(resultMsg + "; Please login again");
				loginYueChe(username, pwd);
			}
		} catch (IOException e) {
			log.error("Exception in HaiJia.loginYueChe()", e);
			// Utils.displayMsg(e.getMessage() + "; Please login again", false);
			log.info(e.getMessage() + "; Please login again");
			loginYueChe(username, pwd);
		}
	}

	private String getRandomCode(String imgCodeUrl) {
		String code = HttpUtils.getRandCodeOcr(httpClient, imgCodeUrl, new Validation() {

			/**
			 * check if there are non alpha or non number, get randcode image again, return false
			 */
			@Override
			public boolean isRandomCodeValid(String code) {
				log.debug("randomCode = {}", code);
				boolean isValid = code.matches("[0-9a-zA-Z]{4}");
				log.debug("Exit isRandomCodeValid() isValid = {}", isValid);
				return isValid;
			}

			@Override
			public int getTimes() {
				return 5;
			}
		});
		return code;
	}

	private void exit(String msg) {
		JOptionPane.showMessageDialog(null, msg);
		System.exit(0);
	}

	private String getAlertMsg(String content) {
		if (content.contains("alert")) {
			String begin = "<script>alert('";
			String msg = content.substring(content.indexOf(begin) + begin.length(),
					content.indexOf("');</script>"));
			log.info("alert message = {}", msg);
			return msg;
		}
		return "";
	}

	@BeforeClass
	public static void setUp() {
		// String[] args = new String[] { "130803198701050026", "0105" };
		// String[] args = new String[] { "410521199002100176", "0210" };
		// String[] args = new String[] { "152324199005130027", "0513" };
//		String[] args = new String[] { "511324198706050697", "0605" };
		String[] args = new String[] { "37232119890202132X", "0202" };
		// args = new String[]{"130633199004254738", "0425"};
		// args = new String[]{"11010819890725346X", "0725"};
		// args = new String[]{"511324198706050697", "0605"};
		// args = new String[]{"130604198312251217", "1225"};
		username = args[0];
		pwd = args[1];
	}
}
