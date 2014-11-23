package demo.javabasics.demo.jmx;

/**
 * The interface of Central Heater
 * 
 * @author carlwu
 */
public interface CentralHeaterInf {

	/**
	 * The heater is provided by British Gas Company
	 */
	public final static String HEATER_PROVIDER = "British Gas Company";

	/**
	 * Get current temperature of heater
	 * 
	 * @return the temperature of the heater
	 */
	public int getCurrentTemperature();

	/**
	 * Set the new temperature
	 * 
	 * @param newTemperature
	 */
	public void setCurrentTemperature(int newTemperature);

	/**
	 * Turn on the heater
	 */
	public void turnOn();

	/**
	 * Turn off the heater
	 */
	public void turnOff();

}
