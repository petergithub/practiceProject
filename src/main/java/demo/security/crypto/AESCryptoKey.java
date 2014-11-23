package demo.security.crypto;

import java.security.Key;

import javax.crypto.SecretKey;

public class AESCryptoKey implements CryptoKeyable {
	SecretKey key;

	public AESCryptoKey(SecretKey key) {
		this.key = key;
	}

	public Key getKey() {
		return key;
	}

}
