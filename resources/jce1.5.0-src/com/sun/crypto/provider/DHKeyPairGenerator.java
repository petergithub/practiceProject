/*
 * @(#)DHKeyPairGenerator.java	1.23 04/01/06
 * 
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.crypto.provider;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHGenParameterSpec;

import sun.security.provider.ParameterCache;

/**
 * This class represents the key pair generator for Diffie-Hellman key pairs.
 *
 * <p>This key pair generator may be initialized in two different ways:
 *
 * <ul>
 * <li>By providing the size in bits of the prime modulus -
 * This will be used to create a prime modulus and base generator, which will
 * then be used to create the Diffie-Hellman key pair. The default size of the
 * prime modulus is 1024 bits.
 * <li>By providing a prime modulus and base generator
 * </ul>
 *
 * @author Jan Luehe
 *
 * @version 1.23, 01/06/04
 *
 * @see java.security.KeyPairGenerator
 */
public final class DHKeyPairGenerator extends KeyPairGeneratorSpi {
    
    // parameters to use or null if not specified
    private DHParameterSpec params;

    // The size in bits of the prime modulus
    private int pSize;

    // The size in bits of the random exponent (private value)
    private int lSize;

    // The source of randomness
    private SecureRandom random;
    
    public DHKeyPairGenerator() {
	super();
	initialize(1024, null);
    }

    /**
     * Initializes this key pair generator for a certain keysize and source of
     * randomness.
     * The keysize is specified as the size in bits of the prime modulus.
     *
     * @param keysize the keysize (size of prime modulus) in bits
     * @param random the source of randomness
     */
    public void initialize(int keysize, SecureRandom random) {
	if ((keysize < 512) || (keysize > 1024) || (keysize % 64 != 0)) {
	    throw new InvalidParameterException("Keysize must be multiple "
						+ "of 64, and can only range "
						+ "from 512 to 1024 "
						+ "(inclusive)");
	}
	this.pSize = keysize;
	this.lSize = 0;
	this.random = random;
	this.params = null;
    }

    /**
     * Initializes this key pair generator for the specified parameter
     * set and source of randomness.
     *
     * <p>The given parameter set contains the prime modulus, the base
     * generator, and optionally the requested size in bits of the random
     * exponent (private value).
     *
     * @param params the parameter set used to generate the key pair
     * @param random the source of randomness
     *
     * @exception InvalidAlgorithmParameterException if the given parameters
     * are inappropriate for this key pair generator
     */
    public void initialize(AlgorithmParameterSpec algParams,
	    SecureRandom random) throws InvalidAlgorithmParameterException {
	if (!(algParams instanceof DHParameterSpec)){
	    throw new InvalidAlgorithmParameterException
		("Inappropriate parameter type");
	}
	
	params = (DHParameterSpec)algParams;
	pSize = params.getP().bitLength();
	if ((pSize < 512) || (pSize > 1024) ||
	    (pSize % 64 != 0)) {
	    throw new InvalidAlgorithmParameterException
		("Prime size must be multiple of 64, and can only range "
		 + "from 512 to 1024 (inclusive)");
	}
	
	// exponent size is optional, could be 0
	lSize = params.getL();
	
	// Require exponentSize < primeSize
	if ((lSize != 0) && (lSize > pSize)) {
	    throw new InvalidAlgorithmParameterException
		("Exponent value must be less than (modulus value -1)");
	}
	this.random = random;
    }

    /**
     * Generates a key pair.
     *
     * @return the new key pair
     */
    public KeyPair generateKeyPair() {
	if (random == null) {
	    random = SunJCE.RANDOM;
	}

	BigInteger x;
	if (lSize <= 0) {
	    /* 
             * We can choose the size of the random exponent (private value) 
             * ourselves. For simplicity, pick a private value with a shorter
	     * size than the modulus's.
             */ 
	    x = new BigInteger(pSize - 1, random).add(BigInteger.ONE);
	    lSize = x.bitLength();
	} else {
	    x = new BigInteger(lSize, random).setBit(lSize - 1);
	}

        if (lSize == pSize) {
	    // if L is specified, params are always available
	    BigInteger p = params.getP();
            // make sure x < p-1
            BigInteger pMinus1 = p.subtract(BigInteger.ONE);
            while (x.compareTo(pMinus1) != -1) {
                x = new BigInteger(lSize, random).setBit(lSize - 1);
            }
	}
	
	if (params == null) {
	    try {
		params = ParameterCache.getDHParameterSpec(pSize, random);
	    } catch (GeneralSecurityException e) {
		// should never happen
		throw new ProviderException(e);
	    }
	}
	
	BigInteger p = params.getP();
	BigInteger g = params.getG();
	
	// calculate public value y
	BigInteger y = g.modPow(x, p);
	
	DHPublicKey pubKey = new DHPublicKey(y, p, g, lSize);
	DHPrivateKey privKey = new DHPrivateKey(x, p, g, lSize);
	return new KeyPair(pubKey, privKey);
    }
}

