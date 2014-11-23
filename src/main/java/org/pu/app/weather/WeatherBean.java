package org.pu.app.weather;

public class WeatherBean {
	private String city;// 城市名
	private String date;// 日期：yyyy年MM月d日
	private String lunarDate;// 农历日期/当日有
	private String week;// 星期
	private String fcTime;// 预报时间：24制小时数/当日有
	private String temperature;// 当日气温
	private String weather;// 天气
	private String wind;// 风力

	public void setCity(String city) {
		this.city = city;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setLunarDate(String lunarDate) {
		this.lunarDate = lunarDate;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public void setFcTime(String fcTime) {
		this.fcTime = fcTime;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	// 省略了getter和setter方法
	@Override
	public String toString() {
		return "Weather [city=" + city + ", date=" + date + ", fcTime=" + fcTime + ", lunarDate="
				+ lunarDate + ", temperature=" + temperature + ", weather=" + weather + ", week=" + week
				+ ", wind=" + wind + "]";
	}
}
