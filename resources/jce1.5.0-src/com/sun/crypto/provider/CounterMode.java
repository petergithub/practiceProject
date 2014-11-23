/*
 * @(#)CounterMode.java	1.3 04/02/03
 * 
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.crypto.provider;

import java.security.InvalidKeyException;

/**
 * This class represents ciphers in counter (CTR) mode.
 *
 * <p>This mode is implemented independently of a particular cipher.
 * Ciphers to which this mode should apply (e.g., DES) must be
 * <i>plugged-in</i> using the constructor.
 *
 * <p>NOTE: This class does not deal with buffering or padding.
 *  
 * @author Andreas Sterbenz
 * @since 1.4.2
 * @version 1.3, 02/03/04
 */
final class CounterMode extends FeedbackCipher {
    
    // current counter value
    private final byte[] counter;
    
    // encrypted bytes of the previous counter value
    private final byte[] encryptedCounter;
    
    // number of bytes in encryptedCounter already used up
    private int used;

    // variables for save/restore calls
    private byte[] counterSave = null;
    private byte[] encryptedCounterSave = null;
    private int usedSave = 0;

    CounterMode(SymmetricCipher embeddedCipher) {
	super(embeddedCipher);
	counter = new byte[blockSize];
	encryptedCounter = new byte[blockSize];
    }

    /**
     * Gets the name of the feedback mechanism
     *
     * @return the name of the feedback mechanism
     */
    String getFeedback() {
	return "CTR";
    }
    
    /**
     * Resets the iv to its original value.
     * This is used when doFinal is called in the Cipher class, so that the
     * cipher can be reused (with its original iv).
     */
    void reset() {
	System.arraycopy(iv, 0, counter, 0, blockSize);
	used = blockSize;
    }
    
    /**
     * Save the current content of this cipher.
     */
    void save() {
	if (counterSave == null) {
	    counterSave = new byte[blockSize];
	    encryptedCounterSave = new byte[blockSize];
	}
	System.arraycopy(counter, 0, counterSave, 0, blockSize);
        System.arraycopy(encryptedCounter, 0, encryptedCounterSave, 0, 
	    blockSize);
	usedSave = used;
    }

    /**
     * Restores the content of this cipher to the previous saved one.
     */
    void restore() {
	System.arraycopy(counterSave, 0, counter, 0, blockSize);
	System.arraycopy(encryptedCounterSave, 0, encryptedCounter, 0, 
	    blockSize);
	used = usedSave;
    }

    /**
     * Initializes the cipher in the specified mode with the given key
     * and iv.
     *
     * @param decrypting flag indicating encryption or decryption
     * @param algorithm the algorithm name
     * @param key the key
     * @param iv the iv
     *
     * @exception InvalidKeyException if the given key is inappropriate for
     * initializing this cipher
     */
    void init(boolean decrypting, String algorithm, byte[] key, byte[] iv)
	    throws InvalidKeyException {
	if ((key == null) || (iv == null) || (iv.length != blockSize)) {
	    throw new InvalidKeyException("Internal error");
	}
	this.iv = iv;
	reset();
	// always encrypt mode for embedded cipher
	embeddedCipher.init(false, algorithm, key);
    }

    /**
     * Performs encryption operation.
     * 
     * <p>The input plain text <code>plain</code>, starting at
     * <code>plainOffset</code> and ending at
     * <code>(plainOffset + len - 1)</code>, is encrypted.
     * The result is stored in <code>cipher</code>, starting at
     * <code>cipherOffset</code>.
     *
     * <p>It is the application's responsibility to make sure that
     * <code>plainLen</code> is a multiple of the embedded cipher's block size,
     * as any excess bytes are ignored.
     *
     * <p>It is also the application's responsibility to make sure that
     * <code>init</code> has been called before this method is called.
     * (This check is omitted here, to avoid double checking.)
     *
     * @param in the buffer with the input data to be encrypted
     * @param inOffset the offset in <code>plain</code>
     * @param len the length of the input data
     * @param out the buffer for the result
     * @param outOff the offset in <code>cipher</code>
     */
    void encrypt(byte[] in, int inOff, int len, byte[] out, int outOff) {
	crypt(in, inOff, len, out, outOff);
    }

    /**
     * Performs decryption operation.
     * 
     * <p>The input cipher text <code>cipher</code>, starting at
     * <code>cipherOffset</code> and ending at
     * <code>(cipherOffset + len - 1)</code>, is decrypted.
     * The result is stored in <code>plain</code>, starting at
     * <code>plainOffset</code>.
     *
     * <p>It is the application's responsibility to make sure that
     * <code>cipherLen</code> is a multiple of the embedded cipher's block
     * size, as any excess bytes are ignored.
     *
     * <p>It is also the application's responsibility to make sure that
     * <code>init</code> has been called before this method is called.
     * (This check is omitted here, to avoid double checking.)
     *
     * @param in the buffer with the input data to be decrypted
     * @param inOff the offset in <code>cipherOffset</code>
     * @param len the length of the input data
     * @param out the buffer for the result
     * @param outOff the offset in <code>plain</code>
     */
    void decrypt(byte[] in, int inOff, int len, byte[] out, int outOff) {
	crypt(in, inOff, len, out, outOff);
    }
    
    /**
     * Increment the counter value.
     */
    private static void increment(byte[] b) {
	int n = b.length - 1;
	while ((n >= 0) && (++b[n] == 0)) {
	    n--;
	}
    }

    /**
     * Do the actual encryption/decryption operation.
     * Essentially we XOR the input plaintext/ciphertext stream with a
     * keystream generated by encrypting the counter values. Counter values
     * are encrypted on demand.
     */
    private void crypt(byte[] in, int inOff, int len, byte[] out, int outOff) {
	while (len-- > 0) {
	    if (used >= blockSize) {
		embeddedCipher.encryptBlock(counter, 0, encryptedCounter, 0);
		increment(counter);
		used = 0;
	    }
	    out[outOff++] = (byte)(in[inOff++] ^ encryptedCounter[used++]);
	}
    }
}
