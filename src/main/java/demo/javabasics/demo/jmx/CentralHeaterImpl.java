package demo.javabasics.demo.jmx;

public class CentralHeaterImpl implements CentralHeaterInf,
		CentralHeaterImplMBean {

	int currentTemperature;

	public int getCurrentTemperature() {
		return currentTemperature;
	}

	public void setCurrentTemperature(int newTemperature) {
		currentTemperature = newTemperature;
	}

	public void turnOff() {
		System.out.println("The heater is off. ");
	}

	public void turnOn() {
		System.out.println("The heater is on. ");

	}

	public String getHeaterProvider() {
		return HEATER_PROVIDER;

	}

	public String printCurrentTemperature() {

		String printMsg = "Current temperature is:" + currentTemperature;
		System.out.println(printMsg);
		return printMsg;
	}

}
