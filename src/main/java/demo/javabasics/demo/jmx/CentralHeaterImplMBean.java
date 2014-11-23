package demo.javabasics.demo.jmx;

/**
 * @author carlwu
 */

public interface CentralHeaterImplMBean {

	/**
	 * return the heater provider
	 * 
	 * @return
	 */
	public String getHeaterProvider();

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
	 * Print the current temperature of the heater
	 * 
	 * @return the string of current temperature
	 */
	public String printCurrentTemperature();

}
