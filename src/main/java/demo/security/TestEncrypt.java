package demo.security;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.junit.Test;

public class TestEncrypt {
	protected static final Logger logger = Logger.getLogger(TestEncrypt.class);

	@Test
	public void testKeyLength() throws UnsupportedEncodingException {
		logLanguageLength("中文");
		logLanguageLength("en");
	}

	public void logLanguageLength(String key)
			throws UnsupportedEncodingException {
		logger.debug("Enter logLanguageLength(" + key + ")");
		logger.info("key.length() = " + key.length()
				+ " in method getSecretKey()");
		logLength(key, "UTF-8");
		logLength(key, "ISO-8859-1");
		logger.debug("Exit logLanguageLength()");
	}

	public void logLength(String key, String charset)
			throws UnsupportedEncodingException {
		logger.debug("Enter logLength(" + key + ", " + charset + ")");
		byte[] keyBytes = key.getBytes(charset);
		logger.info("keyBytes.length = " + keyBytes.length
				+ " in method getSecretKey()");
		SecretKey secretKey = new SecretKeySpec(keyBytes,
				"AES/CBC/PKCS7Padding");

		byte[] encoded = secretKey.getEncoded();
		int keyLength = encoded.length * 8;
		logger.info("keyLength = " + keyLength
				+ " in method encryptOrDecrypt()");
		logger.debug("Exit logLength()");
	}

	/**
	 * AES encrypt function
	 * 
	 * @param original
	 * @param key 16, 24, 32 bytes available
	 * @param iv initial vector (16 bytes) - if null: ECB mode, otherwise: CBC
	 *            mode
	 * @return
	 */
	public static byte[] aesEncrypt(byte[] original, byte[] key, byte[] iv) {
		if (key == null
				|| (key.length != 16 && key.length != 24 && key.length != 32)) {
			// Logger.e("key's bit length is not 128/192/256");
			return null;
		}
		if (iv != null && iv.length != 16) {
			// Logger.e("iv's bit length is not 16");
			return null;
		}

		try {
			SecretKeySpec keySpec = null;
			Cipher cipher = null;
			if (iv != null) {
				keySpec = new SecretKeySpec(key, "AES/CBC/PKCS7Padding");
				cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
				cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(
						iv));
			} else // if(iv == null)
			{
				keySpec = new SecretKeySpec(key, "AES/ECB/PKCS7Padding");
				cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
				cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			}

			return cipher.doFinal(original);
		} catch (Exception e) {
			// Logger.e(e.toString());
		}
		return null;
	}

	/**
	 * AES decrypt function
	 * 
	 * @param encrypted
	 * @param key 16, 24, 32 bytes available
	 * @param iv initial vector (16 bytes) - if null: ECB mode, otherwise: CBC
	 *            mode
	 * @return
	 */
	public static byte[] aesDecrypt(byte[] encrypted, byte[] key, byte[] iv) {
		if (key == null
				|| (key.length != 16 && key.length != 24 && key.length != 32)) {
			// Logger.e("key's bit length is not 128/192/256");
			return null;
		}
		if (iv != null && iv.length != 16) {
			// Logger.e("iv's bit length is not 16");
			return null;
		}

		try {
			SecretKeySpec keySpec = null;
			Cipher cipher = null;
			if (iv != null) {
				keySpec = new SecretKeySpec(key, "AES/CBC/PKCS7Padding");
				cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
				cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(
						iv));
			} else // if(iv == null)
			{
				keySpec = new SecretKeySpec(key, "AES/ECB/PKCS7Padding");
				cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
				cipher.init(Cipher.DECRYPT_MODE, keySpec);
			}

			return cipher.doFinal(encrypted);
		} catch (Exception e) {
			// Logger.e(e.toString());
		}
		return null;
	}

	/**
	 * generates RSA key pair
	 * 
	 * @param keySize
	 * @param publicExponent public exponent value (can be
	 *            RSAKeyGenParameterSpec.F0 or F4)
	 * @return
	 */
	public static KeyPair generateRsaKeyPair(int keySize,
			BigInteger publicExponent) {
		KeyPair keys = null;
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(keySize,
					publicExponent);
			keyGen.initialize(spec);
			keys = keyGen.generateKeyPair();
		} catch (Exception e) {
			// Logger.e(e.toString());
		}
		return keys;
	}

	/**
	 * generates a RSA public key with given modulus and public exponent
	 * 
	 * @param modulus (must be positive? don't know exactly)
	 * @param publicExponent
	 * @return
	 */
	public static PublicKey generateRsaPublicKey(BigInteger modulus,
			BigInteger publicExponent) {
		try {
			return KeyFactory.getInstance("RSA").generatePublic(
					new RSAPublicKeySpec(modulus, publicExponent));
		} catch (Exception e) {
			// Logger.e(e.toString());
		}
		return null;
	}

	/**
	 * generates a RSA private key with given modulus and private exponent
	 * 
	 * @param modulus (must be positive? don't know exactly)
	 * @param privateExponent
	 * @return
	 */
	public static PrivateKey generateRsaPrivateKey(BigInteger modulus,
			BigInteger privateExponent) {
		try {
			return KeyFactory.getInstance("RSA").generatePrivate(
					new RSAPrivateKeySpec(modulus, privateExponent));
		} catch (Exception e) {
			// Logger.e(e.toString());
		}
		return null;
	}

	/**
	 * RSA encrypt function (RSA / ECB / PKCS1-Padding)
	 * 
	 * @param original
	 * @param key
	 * @return
	 */
	public static byte[] rsaEncrypt(byte[] original, PublicKey key) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(original);
		} catch (Exception e) {
			// Logger.e(e.toString());
		}
		return null;
	}

	/**
	 * RSA decrypt function (RSA / ECB / PKCS1-Padding)
	 * 
	 * @param encrypted
	 * @param key
	 * @return
	 */
	public static byte[] rsaDecrypt(byte[] encrypted, PrivateKey key) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(encrypted);
		} catch (Exception e) {
			// Logger.e(e.toString());
		}
		return null;
	}

}
