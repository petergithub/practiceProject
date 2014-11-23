package demo.web.common;

/*
 * SessionTimeoutException: exception when session is timeout
 */
public class SessionTimeoutException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * constructor: default
	 */
	public SessionTimeoutException() {
		super();
	}

	/**
	 * constructor
	 * 
	 * @param _message exception message
	 */
	public SessionTimeoutException(String _message) {
		super(_message);
	}

	/**
	 * constructor
	 * 
	 * @param _cause exception cause
	 */
	public SessionTimeoutException(Throwable _cause) {
		super(_cause);
	}

	/**
	 * constructor
	 * 
	 * @param _message exception message
	 * @param _cause exception cause
	 */
	public SessionTimeoutException(String _message, Throwable _cause) {
		super(_message, _cause);
	}
}
