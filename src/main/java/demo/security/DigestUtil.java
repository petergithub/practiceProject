package demo.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.pu.utils.Base64Coder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
//import sun.misc.BASE64Encoder;
 * MD5 and SHA
 */
public class DigestUtil {
	private static final Logger log = LoggerFactory.getLogger(DigestUtil.class);

	@org.junit.Test
	public void testDigest() throws Exception {
		String str = "0123456789";
		log.info("str = " + str + " after digest with MD5 is: "
				+ digestWithMd5(str));
		log.info("str = " + str + " after digest with SHA is: "
				+ digestWithSha(str));
	}

	public static String digestWithMd5(String str) {
		try {
			return digest(str, Algorithm.MD5);
		} catch (NoSuchAlgorithmException e) {
			log.error("Exception in DigestUtil.digestWithMd5()", e);
		} catch (UnsupportedEncodingException e) {
			log.error("Exception in DigestUtil.digestWithMd5()", e);
		}
		return "";
	}

	public static String digestWithSha(String str) {
		try {
			return digest(str, Algorithm.SHA);
		} catch (NoSuchAlgorithmException e) {
			log.error("Exception in DigestUtil.digestWithMd5()", e);
		} catch (UnsupportedEncodingException e) {
			log.error("Exception in DigestUtil.digestWithMd5()", e);
		}
		return "";
	}

	/**
	 * get digest for InputStream
	 * <p>
	 * such as get digest for a file
	 * 
	 * <pre>
	 * getDigest(new InputStream(filePath))
	 * </pre>
	 */
	public static String getDigest(InputStream is, String algorithm,
			int byteArraySize) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance(algorithm);
		byte[] bytes = new byte[byteArraySize];
		int numBytes;
		while ((numBytes = is.read(bytes)) != -1) {
			md.update(bytes, 0, numBytes);
		}
		byte[] digest = md.digest();
		String result = new String(Hex.encodeHex(digest));
		log.info("algorithm = " + algorithm + "Digest is : " + result);
		return result;
	}

	/**
	 * digest str with algorithm
	 * 
	 * @param str which will be digest
	 * @return String after digest
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String digest(String str, String algorithm)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// 根据算法生成MessageDigest对象
		MessageDigest md = MessageDigest.getInstance(algorithm);
		byte[] strBytes = str.getBytes();
		// 使用strBytes更新摘要
		md.update(strBytes);
		// 完成哈希计算，得到result
		byte[] resultBytes = md.digest();
		BASE64Encoder encoder = new BASE64Encoder();
		// 加密后的字符串
		String digest = encoder.encode(resultBytes);
		return digest;
	}

	/**
	 * 判断用户密码是否正确
	 * 
	 * @param newpasswd 用户输入的密码
	 * @param oldpasswd 数据库中存储的密码－－用户密码的摘要
	 * @param algorithm
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public boolean checkpassword(String newpasswd, String oldpasswd,
			String algorithm) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		return digest(newpasswd, algorithm).equals(oldpasswd);
	}

	/**
	 * @param original
	 * @param key
	 * @throws Exception
	 */
	public static String hmacSha1Digest(String original, String key)
			throws Exception {
		return hmacSha1Digest(original.getBytes(), key.getBytes());
	}

	/**
	 * @param original
	 * @param key
	 * @throws Exception
	 */
	public static String hmacSha1Digest(byte[] original, byte[] key)
			throws Exception {
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(new SecretKeySpec(key, "HmacSHA1"));
		byte[] rawHmac = mac.doFinal(original);
		return new String(Base64Coder.encode(rawHmac));
	}

	/**
	 * @param original
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5sum(byte[] original)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(original, 0, original.length);
		StringBuffer md5sum = new StringBuffer(
				new BigInteger(1, md.digest()).toString(16));
		while (md5sum.length() < 32)
			md5sum.insert(0, "0");
		return md5sum.toString();
	}

	/**
	 * @param original
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5sum(String original)
			throws NoSuchAlgorithmException {
		return md5sum(original.getBytes());
	}

}
