/*
 * Created on 2004-7-18 17:35:57
 *
 * @author Why
 * @description
 *      ErrorBean: bean for error handle
 * @update logs
 *      [1]
 */
package demo.web.common;

import demo.web.webutils.ExceptionUtil;

public class ErrorBean {
	private Throwable error;
	private String message;
	// Added By HZX, 07/22/2004
	private String link;

	/**
	 * construction
	 * 
	 * @param _throwable throwable object
	 */
	public ErrorBean(Throwable _error) {
		this.setError(_error);
	}

	/**
	 * construction
	 * 
	 * @param _throwable throwable object
	 * @param _link URL Link to go after user viewing the error information
	 */
	public ErrorBean(Throwable _error, String _link) {
		this.setError(_error);
		this.setLink(_link);
	}

	/**
	 * @param _error The error to set.
	 */
	public void setError(Throwable _error) {
		this.error = _error;
	}

	/**
	 * @return Returns the error.
	 */
	public Throwable getError() {
		return error;
	}

	/**
	 * @param _message The message to set.
	 */
	public void setMessage(String _message) {
		this.message = _message;
	}

	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message != null ? message : this.error.getMessage();
	}

	/**
	 * @param _link The the URL Link to set.
	 */
	public void setLink(String _link) {
		this.link = _link;
	}

	/**
	 * @return Returns the URL Link.
	 */
	public String getLink() {
		return link != null ? link : "";
	}

	/**
	 * @return Returns the detail message of the error
	 */
	public String getDetail() {
		return ExceptionUtil.getStackTraceText(this.error);
	}
}
