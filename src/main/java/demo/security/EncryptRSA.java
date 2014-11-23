package demo.security;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
 * 该算法于1977年由美国麻省理工学院MIT(Massachusetts Institute of Technology)的Ronal Rivest，Adi
 * Shamir和Len
 * Adleman三位年轻教授提出，并以三人的姓氏Rivest，Shamir和Adlernan命名为RSA算法，是一个支持变长密钥的公共密钥算法
 * ，需要加密的文件快的长度也是可变的! 所谓RSA加密算法，是世界上第一个非对称加密算法，也是数论的第一个实际应用。
 * RSA是目前最有影响力的公钥加密算法，它能够抵抗到目前为止已知的所有密码攻击，已被ISO推荐为公钥数据加密标准。RSA算法基于一个十分简单的数论事实
 * : 将两个大素数相乘十分容易，但那时想要对其乘积进行因式分解却极其困难，因此可以将乘积公开作为加密密钥。
 */
public class EncryptRSA {
	private static final Logger log = LoggerFactory
			.getLogger(EncryptRSA.class);
	/** 指定加密算法为DESede */
	private static String ALGORITHM = "RSA";
	/** 指定key的大小 */
	private static int KEYSIZE = 2048;
	/** 指定公钥存放文件 */
	private static String PUBLIC_KEY_FILE = "PublicKey.key";
	/** 指定私钥存放文件 */
	private static String PRIVATE_KEY_FILE = "PrivateKey.key";
	private static int priKeyLength;
	private static int pubKeyLength;

	/**
	 * 生成密钥对
	 */
	private static void generateKeyPair() throws Exception {
		/** RSA算法要求有一个可信任的随机数源 */
		SecureRandom sr = new SecureRandom();
		/** 为RSA算法创建一个KeyPairGenerator对象 */
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
		/** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
		kpg.initialize(KEYSIZE, sr);
		/** 生成密匙对 */
		KeyPair kp = kpg.generateKeyPair();

		/** 得到公钥 */
		Key publicKey = kp.getPublic();
		/** 得到私钥 */
		Key privateKey = kp.getPrivate();
		/** 用对象流将生成的密钥写入文件 */
		DataOutputStream oos1 = new DataOutputStream(new FileOutputStream(
				PUBLIC_KEY_FILE));
		DataOutputStream oos2 = new DataOutputStream(new FileOutputStream(
				PRIVATE_KEY_FILE));
		priKeyLength = privateKey.getEncoded().length;
		pubKeyLength = publicKey.getEncoded().length;
		log.info("priKeyLength = {}, pubKeyLength = {}", priKeyLength, pubKeyLength);
		oos1.write(publicKey.getEncoded());
		oos2.write(privateKey.getEncoded());
		/** 清空缓存，关闭文件输出流 */
		oos1.close();
		oos2.close();
	}

	/**
	 * 加密方法 
	 * @param source:源数据
	 * @return 密文
	 */
	public static String encrypt(String source) throws Exception {
		generateKeyPair();
		/** 将文件中的公钥对象读出 */
		DataInputStream ois = new DataInputStream(new FileInputStream(
				PUBLIC_KEY_FILE));

		byte[] pubKeyBytes = new byte[pubKeyLength];
		ois.readFully(pubKeyBytes);
		// Key key = (Key) ois.readObject();
		ois.close();

		X509EncodedKeySpec spec = new X509EncodedKeySpec(pubKeyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		Key key = kf.generatePublic(spec);

		/** 得到Cipher对象来实现对源数据的RSA加密 */
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] b = source.getBytes();
		/** 执行加密操作 */
		byte[] b1 = cipher.doFinal(b);
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(b1);
	}

	/**
	 * 解密算法 
	 * @param cryptograph:密文
	 * @return 明文
	 */
	public static String decrypt(String cryptograph) throws Exception {
		/** 将文件中的私钥对象读出 */
		DataInputStream ois = new DataInputStream(new FileInputStream(
				PRIVATE_KEY_FILE));

		byte[] priKeyBytes = new byte[priKeyLength];
		ois.readFully(priKeyBytes);
		log.info("priKeyBytes = {}", priKeyBytes);
		// Key key = (Key) ois.readObject();
		ois.close();
		KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(priKeyBytes);
		Key key = kf.generatePrivate(spec);

		/** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] b1 = decoder.decodeBuffer(cryptograph);
		/** 执行解密操作 */
		byte[] b = cipher.doFinal(b1);
		return new String(b);
	}

	public static void main(String[] args) throws Exception {
		String source = "Hello World!";// 要加密的字符串
		String cryptograph = encrypt(source);// 生成的密文
		log.info("cryptograph = {}", cryptograph);

		String target = decrypt(cryptograph);// 解密密文
		log.info("target = {}", target);
	}
}
