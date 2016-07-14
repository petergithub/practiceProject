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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); May be the legacy
 * system does not use padding. Try "AES/CBC/NoPadding". 如果你不指定填充及加密模式的话，将会采用
 * ECB 模式和 PKCS5Padding 填充进行处理。 AES 是块加密，块的长度是 16 个字节，如果原文不到 16 个字节，则需要填充至 16
 * 个字节后再进行处理。 AES 密文长度 = (原文长度 / 16) * 16 + 16 这里的“/”表示整除
 * 
 * 对称加密是指加解密的密钥相同； AES加密，如果输入是16*n字节，没有填充的情况下，输出和输入相同；有填充的情况下，输出是16*（n+1）。
 * 如果输入不是16字节整数倍，而是大于16*n小于16*（n+1），没有填充的情况下（只能是CFB和OFB模式），输出和输入长度相同；有填充情况下，
 * 输出长度是16*（n+1）；
 * 
 * @author Shang Pu
 * @version Date：Jul 14, 2016 1:42:25 PM
 */
public class PracticeEncrypt {
	private static final Logger log = LoggerFactory.getLogger(PracticeEncrypt.class);

	@Test
	public void testKeyLength() throws UnsupportedEncodingException {
		logLanguageLength("中文");
		logLanguageLength("en");
	}

	public void logLanguageLength(String key) throws UnsupportedEncodingException {
		log.debug("Enter logLanguageLength(" + key + ")");
		log.info("key.length() = " + key.length() + " in method getSecretKey()");
		logLength(key, "UTF-8");
		logLength(key, "ISO-8859-1");
		log.debug("Exit logLanguageLength()");
	}

	public void logLength(String key, String charset) throws UnsupportedEncodingException {
		log.debug("Enter logLength(" + key + ", " + charset + ")");
		byte[] keyBytes = key.getBytes(charset);
		log.info("keyBytes.length = " + keyBytes.length + " in method getSecretKey()");
		SecretKey secretKey = new SecretKeySpec(keyBytes, "AES/CBC/PKCS7Padding");

		byte[] encoded = secretKey.getEncoded();
		int keyLength = encoded.length * 8;
		log.info("keyLength = " + keyLength + " in method encryptOrDecrypt()");
		log.debug("Exit logLength()");
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
		if (key == null || (key.length != 16 && key.length != 24 && key.length != 32)) {
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
				cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
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
		if (key == null || (key.length != 16 && key.length != 24 && key.length != 32)) {
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
				cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
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
	public static KeyPair generateRsaKeyPair(int keySize, BigInteger publicExponent) {
		KeyPair keys = null;
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(keySize, publicExponent);
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
	public static PublicKey generateRsaPublicKey(BigInteger modulus, BigInteger publicExponent) {
		try {
			return KeyFactory.getInstance("RSA")
					.generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
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
	public static PrivateKey generateRsaPrivateKey(BigInteger modulus, BigInteger privateExponent) {
		try {
			return KeyFactory.getInstance("RSA")
					.generatePrivate(new RSAPrivateKeySpec(modulus, privateExponent));
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
