package demo.security.crypto;

public class CryptoException extends Exception {

	private static final long serialVersionUID = 1L;

	public CryptoException(String message) {
		super(message);
	}

	public CryptoException(Throwable t) {
		super(t);
	}

	public CryptoException(String message, Throwable t) {
		super(message, t);
	}
}
