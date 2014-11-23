package org.pu.app.weather;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 天气预报服务，解析JSON
 */
public class WeatherReport {
	private static final Logger log = LoggerFactory.getLogger(WeatherReport.class);

	protected static final String urlDalian = "http://m.weather.com.cn/data/101070201.html";
	protected static final String urlBeijing = "http://m.weather.com.cn/data/101010100.html";
	protected static final String urlBeijing2 = "http://www.weather.com.cn/data/sk/101010100.html";
	private HttpClient client;
	private HttpGet getMethod;

	public static void main(String[] args) {
		WeatherReport report = new WeatherReport(urlBeijing);
		WeatherBean result = report.getWeather();
		log.info("result = {}", result);
	}

	public WeatherReport(String url) {
		client = new DefaultHttpClient();
		getMethod = new HttpGet(url);
	}

	/**
	 * convert json string to weather bean
	 */
	public WeatherBean getWeather() {
		// Weather对象
		WeatherBean weather = new WeatherBean();
		try {
			JsonFactory jsonFactory = new MappingJsonFactory();
			// Json解析器
			JsonParser jsonParser = jsonFactory.createJsonParser(getJsonText());
			// 跳到结果集的开始
			jsonParser.nextToken();
			// 接受结果的HashMap
			HashMap<String, String> map = new HashMap<String, String>();
			// while循环遍历Json结果
			while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
				// 跳转到Value
				jsonParser.nextToken();
				// 将Json中的值装入Map中
				map.put(jsonParser.getCurrentName(), jsonParser.getText());
			}
			// 将数据封装
			weather.setCity(map.get("city"));
			weather.setDate(map.get("date_y"));
			weather.setLunarDate(map.get("date"));
			weather.setWeek(map.get("week"));
			weather.setFcTime(map.get("fchh"));
			weather.setTemperature(map.get("temp1"));
			weather.setWeather(map.get("weather1"));
			weather.setWind(map.get("wind1"));
		} catch (IOException e) {
			log.error("IOException in WeatherReport.getWeather()", e);
		}
		return weather;
	}

	/**
	 * get wethter info as Json string
	 */
	protected String getJsonText() {
		log.debug("Enter getJsonText()");
		String jsonText = "";
		try {
			HttpResponse response = client.execute(getMethod);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				jsonText = EntityUtils.toString(response.getEntity());
			}
		} catch (IOException e) {
			log.error("IOException in WeatherReport.getJsonText()", e);
		}
		log.debug("Exit getJsonText()");
		return jsonText;
	}
}
