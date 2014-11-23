package demo.security;

import java.io.IOException;

/**
 * @version Date: Jun 5, 2013 10:26:28 AM
 * @author Shang Pu
 */
public class CEStreamExhausted extends IOException {
	private static final long serialVersionUID = 1L;
	private String message;

	public CEStreamExhausted() {
	}

	public CEStreamExhausted(String message) {
		this.message = message;
	}

	public CEStreamExhausted(String message, Throwable throwable) {
		super(throwable);
		setMessage(message);
	}

	public CEStreamExhausted(Throwable throwable) {
		super(throwable);
		if (throwable != null) setMessage(throwable.getMessage());
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
