package demo.security;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.pu.utils.IoUtils;


/**
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
 * Encrypt or Decrypt String and file with DES, DES3, AES
 * <p>
 * refer to <a href=
 * 'http://www.avajava.com/tutorials/lessons/how-do-i-encrypt-and-decrypt-files-using-des.html'>
 * H o w do I encrypt and decrypt files using DES?</a>
 * <p>
 * For further information about cryptography in JavaSW, you can go to
 * http://docs.oracle.com/javase/1.5.0/docs/guide/security/CryptoSpec.html and
 * http://docs.oracle.com/javase/1.5.0/docs/guide/security/jce/JCERefGuide.html.
 */
public class EncryptUtil {
	protected static final Logger logger = Logger.getLogger(EncryptUtil.class);

	public void testEncryptDecryptFile() throws Exception {
		// AlgorithmEnum algorithm = AlgorithmEnum.DES;
		// String key = "squirrel123"; // needs to be at least 8 characters for
		// DES

		String algorithm = Algorithm.DESede;
		String key = "012345678901234567890123456789"; // for DESede

		// AlgorithmEnum algorithm = AlgorithmEnum.AES;
		// String key = "0123456789012345"; // 16 characters for AES

		FileInputStream fis = new FileInputStream("original.txt");
		FileOutputStream fos = new FileOutputStream("encrypted.txt");
		encrypt(algorithm, key, fis, fos);

		FileInputStream fis2 = new FileInputStream("encrypted.txt");
		FileOutputStream fos2 = new FileOutputStream("decrypted.txt");
		decrypt(algorithm, key, fis2, fos2);
	}

	public void testEncryptDecryptWithAES() throws Exception {
		String password = "0123456789";
		String salt = "0123456789012345"; // 16 character
		encryptDecryptWithAES(password.toCharArray(), salt.getBytes());
	}

	@Test
	public void testAES() throws Exception {
		String str = "pfizer07";
		String key = "pfizer0123456789"; // 16 character
		String aesenc = encrypt(Algorithm.AES, str, key);
		logger.info("str = " + str + " after encrypt with AES is: " + aesenc);
		String decrypt = decrypt(Algorithm.AES, aesenc, key);
		logger.info("str = " + aesenc + " after decrypt with AES is: "
				+ decrypt);
		Assert.assertEquals(decrypt, str);
	}

	public void testAESLongKey() throws Exception {
		String str = "0123456789";
		logger.trace("the length of key must be 16/24/32. according the to the length(128/192/256) of SecretKeySpec in com.sun.crypto.provider.AESCipher.engineGetKeySize(Key)");
		String key = "0123456789012345"; // 16 character
		// *******************************************************
		// about key longer than 16
		// throw exception java.security.InvalidKeyException: Illegal key size
		// or default parameters

		// see
		// http://stackoverflow.com/questions/992019/java-256-bit-aes-password-based-encryption
		// and
		// http://stackoverflow.com/questions/6481627/java-security-illegal-key-size-or-default-parameters
		// A java.security.InvalidKeyException with the message
		// "Illegal key size or default parameters" means that the cryptography
		// strength is limited;
		// the unlimited strength jurisdiction policy files are not in the
		// correct location. In a
		// JDK, they should be placed under ${jdk}/jre/lib/security

		// you may need to download and install the Java Cryptography Extension
		// (JCE) Unlimited
		// Strength Jurisdiction Policy Files so that Java applications such as
		// the GSI-SSHTerm can
		// read this file. This is a matter of U.S. policy and U.S. export
		// controls (not due to
		// technical reasons).
		// Without these files, you may encounter an "Illegal Key Size" Error
		// when accessing PKCS#12
		// files
		// download at
		// http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-java-plat-419418.html#jce_policy-1.5.0-oth-JPR
		// http://www.oracle.com/technetwork/java/javase/downloads/jce-6-download-429243.html
		// *******************************************************
		// String key = "一二三四五六七八九十一二三四五六七八九十一二三四";// 24 character
		String aesenc = encrypt(Algorithm.AES, str, key);
		logger.info("str = " + str + " after encrypt with AES is: " + aesenc);
		String decrypt = decrypt(Algorithm.AES, aesenc, key);
		logger.info("str = " + aesenc + " after decrypt with AES is: "
				+ decrypt);
		Assert.assertEquals(decrypt, str);
	}

	public void testDESede() throws Exception {
		String str = "0123456789";
		String key = "012345678901234567890123456789";
		String aesenc = encrypt(Algorithm.DESede, str, key);
		logger.info("str = " + str + " after encrypt with DESede is: " + aesenc);
		String decrypt = decrypt(Algorithm.DESede, aesenc, key);
		logger.info("str = " + aesenc + " after decrypt with DESede is: "
				+ decrypt);
		Assert.assertEquals(decrypt, str);
	}

	public void testDES() throws Exception {
		String str = "0123456789";
		String key = "0123456789";
		String aesenc = encrypt(Algorithm.DES, str, key);
		logger.info("str = " + str + " after encrypt with DES is: " + aesenc);
		String decrypt = decrypt(Algorithm.DES, aesenc, key);
		logger.info("str = " + aesenc + " after decrypt with DES is: "
				+ decrypt);
		Assert.assertEquals(decrypt, str);
	}

	public static void encryptDecryptWithAES(char[] password, byte[] salt)
			throws Exception {
		/* Derive the key, given password and salt. */
		SecretKeyFactory factory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");
		KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), Algorithm.AES);
		/* Encrypt the message. */
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secret);
		AlgorithmParameters params = cipher.getParameters();
		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
		byte[] ciphertext = cipher.doFinal("Hello, World!".getBytes("UTF-8"));

		// Now send the ciphertext and the iv to the recipient. The recipient
		// generates a SecretKey
		// in exactly the same way, using the same salt and password. Then
		// initialize the cipher
		// with the key and the initialization vector.
		/* Decrypt the message, given derived key and initialization vector. */
		// Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
		String plaintext = new String(cipher.doFinal(ciphertext), "UTF-8");
		logger.info("plaintext = " + plaintext
				+ " in method encryptDecryptWithAES()");
	}

	public static String encrypt(String algorithm, String str, String key)
			throws Exception {
		SecretKey secretKey = getSecretKey(algorithm, key);
		return encryptOrDecrypt(algorithm, str, secretKey, Cipher.ENCRYPT_MODE);
	}

	public static String decrypt(String algorithm, String str, String key)
			throws Exception {
		SecretKey secretKey = getSecretKey(algorithm, key);
		return encryptOrDecrypt(algorithm, str, secretKey, Cipher.DECRYPT_MODE);
	}

	public static void encrypt(String algorithm, String key, InputStream is,
			OutputStream os) throws Exception {
		SecretKey secretKey = getSecretKey(algorithm, key);
		encryptOrDecrypt(algorithm, secretKey, Cipher.ENCRYPT_MODE, is, os);
	}

	public static void decrypt(String algorithm, String key, InputStream is,
			OutputStream os) throws Exception {
		SecretKey secretKey = getSecretKey(algorithm, key);
		encryptOrDecrypt(algorithm, secretKey, Cipher.DECRYPT_MODE, is, os);
	}

	/**
	 * get SecretKey with specify algorithm and String key
	 * 
	 * @param algorithm
	 * @param key
	 * @return
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static SecretKey getSecretKey(String algorithm, String key)
			throws GeneralSecurityException, UnsupportedEncodingException {
		KeySpec keySpec;
		SecretKeyFactory secretKeyFactory;
		SecretKey secretKey;
		if (algorithm.equals(Algorithm.DES)) {
			keySpec = new DESKeySpec(key.getBytes());
			secretKeyFactory = SecretKeyFactory.getInstance(Algorithm.DES);
			secretKey = secretKeyFactory.generateSecret(keySpec);
		} else if (algorithm.equals(Algorithm.DESede)) {
			keySpec = new DESedeKeySpec(key.getBytes());
			secretKeyFactory = SecretKeyFactory.getInstance(Algorithm.DESede);
			secretKey = secretKeyFactory.generateSecret(keySpec);
		} else if (algorithm.equals(Algorithm.AES)) {
			logger.trace("NOTE: the length of key must be 16/24/32. Current key.length() = "
					+ key.length() + " in method encryptOrDecrypt()");
			secretKey = new SecretKeySpec(key.getBytes("ISO-8859-1"),
					Algorithm.AES);
		} else {
			throw new NoSuchAlgorithmException(algorithm
					+ ": There is no such algorithm Exception in getKey()");
		}
		return secretKey;
	}

	/**
	 * get SecretKey with specify algorithm and String key
	 * 
	 * @param algorithm
	 * @param key
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 */
	public static SecretKey getSecretKey(Algorithm algorithm, String key)
			throws UnsupportedEncodingException, GeneralSecurityException {
		KeySpec keySpec;
		SecretKeyFactory secretKeyFactory;
		SecretKey secretKey;
		if (Algorithm.DES.equals(algorithm)) {
			keySpec = new DESKeySpec(key.getBytes());
			secretKeyFactory = SecretKeyFactory.getInstance(Algorithm.DES);
			secretKey = secretKeyFactory.generateSecret(keySpec);
		} else if (Algorithm.DESede.equals(algorithm)) {
			keySpec = new DESedeKeySpec(key.getBytes());
			secretKeyFactory = SecretKeyFactory.getInstance(Algorithm.DESede);
			secretKey = secretKeyFactory.generateSecret(keySpec);
		} else if (Algorithm.AES.equals(algorithm)) {
			logger.trace("NOTE: the length of key must be 16/24/32. Current key.length() = "
					+ key.length() + " in method encryptOrDecrypt()");
			secretKey = new SecretKeySpec(key.getBytes("ISO-8859-1"),
					Algorithm.AES);
		} else {
			throw new NoSuchAlgorithmException(algorithm
					+ ": There is no such algorithm Exception in getKey()");
		}
		return secretKey;
	}

	/**
	 * get default key with KeyGenerator
	 * 
	 * @param algorithm
	 * @return SecretKey
	 * @throws NoSuchAlgorithmException
	 */
	public static SecretKey getSecretKey(String algorithm)
			throws NoSuchAlgorithmException {
		// KeyGenerator 提供对称密钥生成器的功能，支持各种算法
		KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
		// 生成密钥,SecretKey 负责保存对称密钥
		SecretKey secretKey = keyGenerator.generateKey();
		return secretKey;
	}

	/**
	 * encrypt str with given algorithm(DES, DESede, AES), return the encrypt
	 * string
	 * 
	 * @param str
	 * @param algorithm - DES, DESede, AES
	 * @param key - Key
	 * @param mode - Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE
	 */
	public static String encryptOrDecrypt(String algorithm, String str,
			Key secretKey, int mode) throws Exception {
		// Security.addProvider(new com.sun.crypto.provider.SunJCE());

		// 生成Cipher对象,指定算法,Cipher负责完成加密或解密工作
		Cipher cipher = Cipher.getInstance(algorithm);

		String result = "";
		if (mode == Cipher.ENCRYPT_MODE) {
			// 根据密钥，对Cipher对象进行初始化，ENCRYPT_MODE表示加密模式
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			// 加密，结果保存进cipherByte
			byte[] cipherByte = cipher.doFinal(str.getBytes());
			BASE64Encoder encoder = new BASE64Encoder();
			result = encoder.encode(cipherByte);
		} else if (mode == Cipher.DECRYPT_MODE) {
			// decrypt the String
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] buff = decoder.decodeBuffer(str);
			byte[] cipherByte = cipher.doFinal(buff);
			String.valueOf(cipherByte);
			result = new String(cipherByte);
		}
		return result;
	}

	/**
	 * encrypt inputStream with given algorithm(DES, DESede, AES)
	 * 
	 * @param algorithm - DES, DESede, AES
	 * @param key - Key
	 * @param mode - Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void encryptOrDecrypt(String algorithm, Key key, int mode,
			InputStream is, OutputStream os) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithm);

		if (mode == Cipher.ENCRYPT_MODE) {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			CipherInputStream cis = new CipherInputStream(is, cipher);
			IoUtils.copy(cis, os);
		} else if (mode == Cipher.DECRYPT_MODE) {
			cipher.init(Cipher.DECRYPT_MODE, key);
			CipherOutputStream cos = new CipherOutputStream(os, cipher);
			IoUtils.copy(is, cos);
		}
	}
}
