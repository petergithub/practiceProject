package demo.security.crypto;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStore.SecretKeyEntry;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.apache.log4j.Logger;
import org.junit.Assert;

import demo.security.BASE64Decoder;
import demo.security.BASE64Encoder;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

public class AESCryptoImpl implements Cryptographical {
	protected static final Logger logger = Logger
			.getLogger(AESCryptoImpl.class);

	// private Key key;
	private Cipher ecipher;
	private Cipher dcipher;

	@org.junit.Test
	public void testEncryptRandomKey() throws Exception {
		SecretKey key = KeyGenerator.getInstance("AES").generateKey();
		Cryptographical crypto = AESCryptoImpl
				.initialize(new AESCryptoKey(key));
		String enc = crypto.encrypt("Andy");
		Assert.assertEquals("Andy", crypto.decrypt(enc));

		SecretKey anotherKey = KeyGenerator.getInstance("AES").generateKey();
		Cryptographical anotherInst = AESCryptoImpl
				.initialize(new AESCryptoKey(anotherKey));
		String anotherEncrypt = anotherInst.encrypt("Andy");
		Assert.assertEquals("Andy", anotherInst.decrypt(anotherEncrypt));

		Assert.assertFalse(anotherEncrypt.equals(enc));
	}

	public void testEncrypt() throws Exception {
		SecretKey key = KeyGenerator.getInstance("AES").generateKey();

		KeyStore ks = KeyStore.getInstance("JCEKS");
		ks.load(null, null);
		KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(key);
		ks.setEntry("mykey", skEntry, new KeyStore.PasswordProtection(
				"mykeypassword".toCharArray()));
		FileOutputStream fos = new FileOutputStream("agb50.keystore");
		ks.store(fos, "somepassword".toCharArray());
		fos.close();

		Cryptographical crypto = AESCryptoImpl
				.initialize(new AESCryptoKey(key));
		String enc = crypto.encrypt("Andy");
		Assert.assertEquals("Andy", crypto.decrypt(enc));

		// alternatively, read the keystore file itself to obtain the key
		Cryptographical anotherInst = AESCryptoImpl
				.initialize(new AESCryptoKey(key));
		String anotherEncrypt = anotherInst.encrypt("Andy");
		Assert.assertEquals("Andy", anotherInst.decrypt(anotherEncrypt));
		Assert.assertTrue(anotherEncrypt.equals(enc));

		// read the keystore file itself to obtain the key
		FileInputStream fis = new FileInputStream("agb50.keystore");
		KeyStore ksFromFile = KeyStore.getInstance("JCEKS");
		ksFromFile.load(fis, "somepassword".toCharArray());
		KeyStore.SecretKeyEntry entry = (SecretKeyEntry) ksFromFile.getEntry(
				"mykey",
				new KeyStore.PasswordProtection("mykeypassword".toCharArray()));
		SecretKey keyFromFile = entry.getSecretKey();

		Cryptographical cryptoFromFile = AESCryptoImpl
				.initialize(new AESCryptoKey(keyFromFile));
		String encryptFromFile = cryptoFromFile.encrypt("Andy");
		Assert.assertEquals("Andy", cryptoFromFile.decrypt(encryptFromFile));
		Assert.assertTrue(encryptFromFile.equals(enc));

	}

	public String encrypt(String plaintext) {
		try {
			return new BASE64Encoder().encode(ecipher.doFinal(plaintext
					.getBytes("UTF8")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String decrypt(String ciphertext) {
		try {
			return new String(dcipher.doFinal(new BASE64Decoder()
					.decodeBuffer(ciphertext)), "UTF8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Cryptographical initialize(CryptoKeyable key)
			throws CryptoException {
		try {
			return new AESCryptoImpl(key.getKey());
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		} catch (NoSuchPaddingException e) {
			throw new CryptoException(e);
		} catch (InvalidKeyException e) {
			throw new CryptoException(e);
		}
	}

	private AESCryptoImpl(Key key) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException {
		// this.key = key;
		this.ecipher = Cipher.getInstance("AES");
		this.dcipher = Cipher.getInstance("AES");
		this.ecipher.init(Cipher.ENCRYPT_MODE, key);
		this.dcipher.init(Cipher.DECRYPT_MODE, key);
	}

	public AESCryptoImpl() {

	}
}
