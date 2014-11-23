package demo.security.crypto;

public interface Cryptographical {
	String encrypt(String plaintext);

	String decrypt(String ciphertext);
}
