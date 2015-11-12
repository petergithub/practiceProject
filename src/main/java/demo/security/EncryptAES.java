package demo.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;

/**
 * Created by peng.wu on 2015/6/4. desc: java 中 AES 加密 Cipher.getInstance("AES")
 * = Cipher.getInstance("AES/ECB/PKCS5Padding")
 *
 * 本算法采用能和大多数语言如PHP等互通的方案
 */
@SuppressWarnings("restriction")
public class EncryptAES {

	public static String PASSWORD = "AES7654321!#@tcl";

	private static String IV = "1234567812345678";

	private static String ALGORITHM = "AES/CBC/NoPadding";

	public static String encrypt(String data, String key) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			int blockSize = cipher.getBlockSize();

			byte[] dataBytes = data.getBytes();
			int plaintextLength = dataBytes.length;
			if (plaintextLength % blockSize != 0) {
				plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
			}

			byte[] plaintext = new byte[plaintextLength];
			System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());

			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
			byte[] encrypted = cipher.doFinal(plaintext);
			return new sun.misc.BASE64Encoder().encode(encrypted);

		} catch (Exception e) {
			return null;
		}
	}

	public static String decrypt(String data, String key) throws Exception {
		try {
			byte[] encrypted1 = new BASE64Decoder().decodeBuffer(data);

			Cipher cipher = Cipher.getInstance(ALGORITHM);
			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());

			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original);
			return originalString.trim();
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		String content = "lTDZXojfaXvis7Epoysdkha/VmA6UcTVVdSzq1IUFW6bjur5iq6TwtS6IO/Ax8EG/ZmAvcpYPmaOxQPvWBQbex1pd0yKrz8xunZAZ+d+qRZ1cEsWoA7thZ+LI1xBhDuomJaz9IZW5lBb6DGBTsSMAg==";
		String password = PASSWORD;
		// 加密
		/*
		 * System.out.println("加密前：" + content); String str = encrypt(content,
		 * password); System.out.println(str);
		 */
		// 解密

		String decryptResult = decrypt(content, password);
		System.out.println("解密后：" + decryptResult.trim());
	}
}
