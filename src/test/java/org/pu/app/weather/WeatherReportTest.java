package org.pu.app.weather;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version Date: Feb 26, 2013 5:03:19 PM
 * @author Shang Pu
 */
public class WeatherReportTest {
	private static final Logger log = LoggerFactory.getLogger(WeatherReportTest.class);

	@Test
	public void testJsonWeatherBean2() {
		// WeatherReport report = new WeatherReport(WeatherReport.urlBeijing2);
		// String content = report.getJsonText();
		String content = "{\"weatherinfo\":{\"city\":\"北京\",\"cityid\":\"101010100\",\"temp\":\"6\",\"WD\":\"西南风\",\"WS\":\"2级\",\"SD\":\"47%\",\"WSE\":\"2\",\"time\":\"17:30\",\"isRadar\":\"1\",\"Radar\":\"JC_RADAR_AZ9010_JB\"}}";
		log.info("content = {}", content);
		try {
			JSONObject jsonObject = new JSONObject(content);
			String weatherinfo = jsonObject.getString("weatherinfo");
			log.info("weatherinfo = {}", weatherinfo);
			JSONObject json = new JSONObject(weatherinfo);

			WeatherBean2 weather = new WeatherBean2();
			weather.setCity(json.getString("city"));
			weather.setTime(json.getString("time"));

			weather.setTemperature(json.getString("temp"));
			weather.setHumidity(json.getString("SD"));
			// weather.setAtmosphericPressure(json.getString("AP"));

			weather.setWindDirection(json.getString("WD"));
			weather.setWindForce(json.getString("WS"));
			// weather.setWindSpeed(json.getString("sm") + "m/s");
			log.info("weather = {}", weather);
		} catch (JSONException e) {
			log.error("Exception in WeatherReportTest.testJson()", e);
		}
	}

	public void testJson20090211() {
		WeatherReport report = new WeatherReport(WeatherReport.urlBeijing);
		String content = report.getJsonText();
		log.info("content = {}", content);
		// String content =
		// "{\"weatherinfo\":{\"weather6\":\"多云转晴\",\"weather5\":\"晴\",\"weather4\":\"晴\",\"index_d\":\"天气冷，建议着棉服、羽绒服、皮夹克加羊毛衫等冬季服装。年老体弱者宜着厚棉衣、冬大衣或厚羽绒服。\",\"city\":\"大连\",\"img_single\":\"1\",\"index_cl\":\"较不宜\",\"img2\":\"0\",\"img1\":\"1\",\"index\":\"冷\",\"tempF1\":\"41℉~33.8℉\",\"img_title10\":\"晴\",\"img_title11\":\"多云\",\"img_title12\":\"晴\",\"index_xc\":\"较不宜\",\"index_co\":\"较不舒适\",\"img_title_single\":\"多云\",\"city_en\":\"dalian\",\"date_y\":\"2013年2月26日\",\"index48_d\":\"天气冷，建议着棉服、羽绒服、皮夹克加羊毛衫等冬季服装。年老体弱者宜着厚棉衣、冬大衣或厚羽绒服。\",\"img9\":\"0\",\"img7\":\"0\",\"img8\":\"99\",\"img5\":\"6\",\"img6\":\"99\",\"img3\":\"0\",\"img4\":\"1\",\"fx1\":\"西南风\",\"st5\":\"5\",\"st6\":\"-10\",\"st3\":\"5\",\"date\":\"\",\"st4\":\"1\",\"st1\":\"4\",\"st2\":\"0\",\"temp1\":\"5℃~1℃\",\"tempF6\":\"35.6℉~28.4℉\",\"temp2\":\"7℃~2℃\",\"temp3\":\"6℃~-6℃\",\"index48\":\"冷\",\"tempF4\":\"28.4℉~21.2℉\",\"tempF5\":\"32℉~24.8℉\",\"index_ls\":\"基本适宜\",\"tempF2\":\"44.6℉~35.6℉\",\"tempF3\":\"42.8℉~21.2℉\",\"index_tr\":\"一般\",\"index_ag\":\"极不易发\",\"index48_uv\":\"弱\",\"fl1\":\"4-5级\",\"fl5\":\"4-5级\",\"fl4\":\"5-6级\",\"temp6\":\"2℃~-2℃\",\"fl3\":\"4-5级转5-6级\",\"temp5\":\"0℃~-4℃\",\"fl2\":\"4-5级\",\"cityid\":\"101070201\",\"temp4\":\"-2℃~-6℃\",\"img12\":\"0\",\"img_title7\":\"晴\",\"img_title6\":\"雨夹雪\",\"fl6\":\"4-5级\",\"img_title5\":\"雨夹雪\",\"fchh\":\"11\",\"img_title4\":\"多云\",\"img10\":\"99\",\"img_title9\":\"晴\",\"img11\":\"1\",\"img_title8\":\"晴\",\"fx2\":\"西北风\",\"wind1\":\"西南风转西北风4-5级\",\"weather1\":\"多云转晴\",\"wind3\":\"西南风4-5级转西北风5-6级\",\"weather2\":\"晴转多云\",\"wind2\":\"南风转西南风4-5级\",\"weather3\":\"雨夹雪\",\"wind5\":\"北风4-5级\",\"img_title3\":\"晴\",\"index_uv\":\"最弱\",\"wind4\":\"北风5-6级\",\"img_title2\":\"晴\",\"img_title1\":\"多云\",\"wind6\":\"西南风转北风4-5级\",\"week\":\"星期二\"}}";
		try {
			JSONObject jsonObject = new JSONObject(content);
			String weatherinfo = jsonObject.getString("weatherinfo");
			log.info("weatherinfo = {}", weatherinfo);
			JSONObject weatherinfoJson = new JSONObject(weatherinfo);

			WeatherBean weather = new WeatherBean();
			weather.setCity(weatherinfoJson.getString("city"));
			weather.setDate(weatherinfoJson.getString("date_y"));
			weather.setLunarDate(weatherinfoJson.getString("date"));
			weather.setWeek(weatherinfoJson.getString("week"));
			weather.setFcTime(weatherinfoJson.getString("fchh"));
			weather.setTemperature(weatherinfoJson.getString("temp1"));
			weather.setWeather(weatherinfoJson.getString("weather1"));
			weather.setWind(weatherinfoJson.getString("wind1"));
			log.info("weather = {}", weather);
		} catch (JSONException e) {
			log.error("Exception in WeatherReportTest.testJson()", e);
		}
	}
}
