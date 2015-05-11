/*
 * @(#)KeyGenerator.java	1.49 04/01/06
 * 
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
 
package javax.crypto;

import java.util.*;

import java.security.*;
import java.security.Provider.Service;
import java.security.spec.*;

import sun.security.jca.*;
import sun.security.jca.GetInstance.Instance;

/**
 * This class provides the functionality of a (symmetric) key generator.
 *
 * <p>Key generators are constructed using one of the <code>getInstance</code>
 * class methods of this class.
 *
 * <p>KeyGenerator objects are reusable, i.e., after a key has been
 * generated, the same KeyGenerator object can be re-used to generate further
 * keys.
 *
 * <p>There are two ways to generate a key: in an algorithm-independent
 * manner, and in an algorithm-specific manner.
 * The only difference between the two is the initialization of the object:
 * 
 * <ul>
 * <li><b>Algorithm-Independent Initialization</b>
 * <p>All key generators share the concepts of a <i>keysize</i> and a
 * <i>source of randomness</i>.
 * There is an 
 * {@link #init(int, java.security.SecureRandom) init} 
 * method in this KeyGenerator class that takes these two universally
 * shared types of arguments. There is also one that takes just a
 * <code>keysize</code> argument, and uses the SecureRandom implementation
 * of the highest-priority installed provider as the source of randomness
 * (or a system-provided source of randomness if none of the installed
 * providers supply a SecureRandom implementation), and one that takes just a
 * source of randomness.
 * 
 * <p>Since no other parameters are specified when you call the above
 * algorithm-independent <code>init</code> methods, it is up to the
 * provider what to do about the algorithm-specific parameters (if any) to be
 * associated with each of the keys.
 * <p>
 *
 * <li><b>Algorithm-Specific Initialization</b>
 * <p>For situations where a set of algorithm-specific parameters already
 * exists, there are two
 * {@link #init(java.security.spec.AlgorithmParameterSpec) init}
 * methods that have an <code>AlgorithmParameterSpec</code>
 * argument. One also has a <code>SecureRandom</code> argument, while the
 * other uses the SecureRandom implementation
 * of the highest-priority installed provider as the source of randomness
 * (or a system-provided source of randomness if none of the installed
 * providers supply a SecureRandom implementation).
 * </ul>
 *
 * <p>In case the client does not explicitly initialize the KeyGenerator
 * (via a call to an <code>init</code> method), each provider must
 * supply (and document) a default initialization.
 *
 * @author Jan Luehe
 *
 * @version 1.49, 01/06/04
 *
 * @see SecretKey
 * @since 1.4
 */

public class KeyGenerator {

    // see java.security.KeyPairGenerator for failover notes
    
    private final static int I_NONE   = 1;
    private final static int I_RANDOM = 2;
    private final static int I_PARAMS = 3;
    private final static int I_SIZE   = 4;
    
    // The provider
    private Provider provider;

    // The provider implementation (delegate)
    private volatile KeyGeneratorSpi spi;

    // The algorithm
    private final String algorithm;

    private final Object lock = new Object();
    
    private Iterator serviceIterator;
    
    private int initType;
    private int initKeySize;
    private AlgorithmParameterSpec initParams;
    private SecureRandom initRandom;

    /**
     * Creates a KeyGenerator object.
     *
     * @param keyGenSpi the delegate
     * @param provider the provider
     * @param algorithm the algorithm
     */
    protected KeyGenerator(KeyGeneratorSpi keyGenSpi, Provider provider,
			   String algorithm) {
	this.spi = keyGenSpi;
	this.provider = provider;
	this.algorithm = algorithm;
    }

    private KeyGenerator(String algorithm) throws NoSuchAlgorithmException {
	this.algorithm = algorithm;
	List list = GetInstance.getServices("KeyGenerator", algorithm);
	serviceIterator = list.iterator();
	initType = I_NONE;
	// fetch and instantiate initial spi
	if (nextSpi(null, false) == null) {
	    throw new NoSuchAlgorithmException
	    	(algorithm + " KeyGenerator not available");
	}
    }

    /**
     * Returns the algorithm name of this <code>KeyGenerator</code> object.
     *
     * <p>This is the same name that was specified in one of the
     * <code>getInstance</code> calls that created this
     * <code>KeyGenerator</code> object.
     * 
     * @return the algorithm name of this <code>KeyGenerator</code> object. 
     */
    public final String getAlgorithm() {
	return this.algorithm;
    }

    /**
     * Generates a <code>KeyGenerator</code> object for the specified
     * algorithm.
     * If the default provider package provides an implementation of the
     * requested key generator, an instance of
     * <code>KeyGenerator</code> containing that implementation is returned.
     * If the requested key generator is not available in the default provider
     * package, other provider packages are searched.
     *
     * @param algorithm the standard name of the requested key algorithm.
     * See Appendix A in the
     * <a href="../../../guide/security/jce/JCERefGuide.html#AppA">
     * Java Cryptography Extension Reference Guide</a> 
     * for information about standard algorithm names.
     *
     * @return the new <code>KeyGenerator</code> object
     *
     * @exception NullPointerException if the specified algorithm is null.
     * @exception NoSuchAlgorithmException if a key generator for the
     * specified algorithm is not available in the default provider
     * package or any of the other provider packages that were searched.
     */
    public static final KeyGenerator getInstance(String algorithm) 
	    throws NoSuchAlgorithmException {
	return new KeyGenerator(algorithm);
    }

    /** 
     * Generates a <code>KeyGenerator</code> object for the specified key
     * algorithm from the specified provider.
     *
     * @param algorithm the standard name of the requested key algorithm.
     * See Appendix A in the
     * <a href="../../../guide/security/jce/JCERefGuide.html#AppA">
     * Java Cryptography Extension Reference Guide</a> 
     * for information about standard algorithm names.
     * @param provider the name of the provider
     *
     * @return the new <code>KeyGenerator</code> object
     *
     * @exception NullPointerException if the specified algorithm is null.
     * @exception NoSuchAlgorithmException if a key generator for the
     * specified algorithm is not available from the specified provider.
     * @exception NoSuchProviderException if the specified provider has not
     * been configured.
     * @exception IllegalArgumentException if the <code>provider</code>
     * is null.
     */
    public static final KeyGenerator getInstance(String algorithm,
	    String provider) throws NoSuchAlgorithmException, 
	    NoSuchProviderException {	
	Instance instance = JceSecurity.getInstance("KeyGenerator", 
		KeyGeneratorSpi.class, algorithm, provider);
	return new KeyGenerator((KeyGeneratorSpi)instance.impl, 
		instance.provider, algorithm);
    }

    /** 
     * Generates a <code>KeyGenerator</code> object for the specified key
     * algorithm from the specified provider. Note: the <code>provider</code>
     * doesn't have to be registered.
     *
     * @param algorithm the standard name of the requested key algorithm.
     * See Appendix A in the
     * <a href="../../../guide/security/jce/JCERefGuide.html#AppA">
     * Java Cryptography Extension Reference Guide</a> 
     * for information about standard algorithm names.
     * @param provider the provider
     *
     * @return the new <code>KeyGenerator</code> object
     *
     * @exception NullPointerException if the specified algorithm is null.
     * @exception NoSuchAlgorithmException if a key generator for the
     * specified algorithm is not available from the specified provider.
     * @exception IllegalArgumentException if the <code>provider</code>
     * is null.
     */
    public static final KeyGenerator getInstance(String algorithm,
	    Provider provider) throws NoSuchAlgorithmException {	
	Instance instance = JceSecurity.getInstance("KeyGenerator", 
		KeyGeneratorSpi.class, algorithm, provider);
	return new KeyGenerator((KeyGeneratorSpi)instance.impl, 
		instance.provider, algorithm);
    }

    /** 
     * Returns the provider of this <code>KeyGenerator</code> object.
     * 
     * @return the provider of this <code>KeyGenerator</code> object
     */
    public final Provider getProvider() {
	synchronized (lock) {
	    disableFailover();
	    return provider;
	}
    }

    /**
     * Update the active spi of this class and return the next
     * implementation for failover. If no more implemenations are
     * available, this method returns null. However, the active spi of
     * this class is never set to null.
     */
    private KeyGeneratorSpi nextSpi(KeyGeneratorSpi oldSpi,
	    boolean reinit) {
	synchronized (lock) {
	    // somebody else did a failover concurrently
	    // try that spi now
	    if ((oldSpi != null) && (oldSpi != spi)) {
		return spi;
	    }
	    if (serviceIterator == null) {
		return null;
	    }
	    while (serviceIterator.hasNext()) {
		Service s = (Service)serviceIterator.next();
		if (JceSecurity.canUseProvider(s.getProvider()) == false) {
		    continue;
		}
		try {
		    Object inst = s.newInstance(null);
		    // ignore non-spis
		    if (inst instanceof KeyGeneratorSpi == false) {
			continue;
		    }
		    KeyGeneratorSpi spi = (KeyGeneratorSpi)inst;
		    if (reinit) {
			if (initType == I_SIZE) {
			    spi.engineInit(initKeySize, initRandom);
			} else if (initType == I_PARAMS) {
			    spi.engineInit(initParams, initRandom);
			} else if (initType == I_RANDOM) {
			    spi.engineInit(initRandom);
			} else if (initType != I_NONE) {
			    throw new AssertionError
				("KeyGenerator initType: " + initType);
			}
		    }
		    provider = s.getProvider();
		    this.spi = spi;
		    return spi;
		} catch (Exception e) {
		    // ignore
		}
	    }
	    disableFailover();
	    return null;
	}
    }

    void disableFailover() {
	serviceIterator = null;
	initType = 0;
	initParams = null;
	initRandom = null;
    }

    /**
     * Initializes this key generator.
     * 
     * @param random the source of randomness for this generator
     */
    public final void init(SecureRandom random) {
	if (serviceIterator == null) {
	    spi.engineInit(random);
	    return;
	}
	RuntimeException failure = null;
	KeyGeneratorSpi mySpi = spi;
	do {
	    try {
		mySpi.engineInit(random);
		initType = I_RANDOM;
		initKeySize = 0;
		initParams = null;
		initRandom = random;
		return;
	    } catch (RuntimeException e) {
		if (failure == null) {
		    failure = e;
		}
		mySpi = nextSpi(mySpi, false);
	    }
	} while (mySpi != null);
	throw failure;
    }

    /**
     * Initializes this key generator with the specified parameter set.
     *
     * <p> If this key generator requires any random bytes, it will get them
     * using the
     * {@link SecureRandom <code>SecureRandom</code>}
     * implementation of the highest-priority installed
     * provider as the source of randomness.
     * (If none of the installed providers supply an implementation of
     * SecureRandom, a system-provided source of randomness will be used.)
     *
     * @param params the key generation parameters
     *
     * @exception InvalidAlgorithmParameterException if the given parameters
     * are inappropriate for this key generator
     */
    public final void init(AlgorithmParameterSpec params)
	throws InvalidAlgorithmParameterException
    {
	init(params, JceSecurity.RANDOM);
    }

    /**
     * Initializes this key generator with the specified parameter
     * set and a user-provided source of randomness.
     *
     * @param params the key generation parameters
     * @param random the source of randomness for this key generator
     *
     * @exception InvalidAlgorithmParameterException if <code>params</code> is
     * inappropriate for this key generator
     */
    public final void init(AlgorithmParameterSpec params, SecureRandom random)
	throws InvalidAlgorithmParameterException
    {
	if (serviceIterator == null) {
	    spi.engineInit(params, random);
	    return;
	}
	Exception failure = null;
	KeyGeneratorSpi mySpi = spi;
	do {
	    try {
		mySpi.engineInit(params, random);
		initType = I_PARAMS;
		initKeySize = 0;
		initParams = params;
		initRandom = random;
		return;
	    } catch (Exception e) {
		if (failure == null) {
		    failure = e;
		}
		mySpi = nextSpi(mySpi, false);
	    }
	} while (mySpi != null);
	if (failure instanceof InvalidAlgorithmParameterException) {
	    throw (InvalidAlgorithmParameterException)failure;
	}
	if (failure instanceof RuntimeException) {
	    throw (RuntimeException)failure;
	}
	throw new InvalidAlgorithmParameterException("init() failed", failure);
    }

    /**
     * Initializes this key generator for a certain keysize.
     *
     * <p> If this key generator requires any random bytes, it will get them
     * using the
     * {@link SecureRandom <code>SecureRandom</code>}
     * implementation of the highest-priority installed
     * provider as the source of randomness.
     * (If none of the installed providers supply an implementation of
     * SecureRandom, a system-provided source of randomness will be used.)
     *
     * @param keysize the keysize. This is an algorithm-specific metric,
     * specified in number of bits.
     *
     * @exception InvalidParameterException if the keysize is wrong or not
     * supported.
     */
    public final void init(int keysize) {
	init(keysize, JceSecurity.RANDOM);
    }

    /**
     * Initializes this key generator for a certain keysize, using a
     * user-provided source of randomness.
     *
     * @param keysize the keysize. This is an algorithm-specific metric,
     * specified in number of bits.
     * @param random the source of randomness for this key generator
     *
     * @exception InvalidParameterException if the keysize is wrong or not
     * supported.
     */
    public final void init(int keysize, SecureRandom random) {
	if (serviceIterator == null) {
	    spi.engineInit(keysize, random);
	    return;
	}
	RuntimeException failure = null;
	KeyGeneratorSpi mySpi = spi;
	do {
	    try {
		mySpi.engineInit(keysize, random);
		initType = I_SIZE;
		initKeySize = keysize;
		initParams = null;
		initRandom = random;
		return;
	    } catch (RuntimeException e) {
		if (failure == null) {
		    failure = e;
		}
		mySpi = nextSpi(mySpi, false);
	    }
	} while (mySpi != null);
	throw failure;
    }

    /**
     * Generates a secret key.
     *
     * @return the new key
     */
    public final SecretKey generateKey() {
	if (serviceIterator == null) {
	    return spi.engineGenerateKey();
	}
	RuntimeException failure = null;
	KeyGeneratorSpi mySpi = spi;
	do {
	    try {
		return mySpi.engineGenerateKey();
	    } catch (RuntimeException e) {
		if (failure == null) {
		    failure = e;
		}
		mySpi = nextSpi(mySpi, true);
	    }
	} while (mySpi != null);
	throw failure;
   }
}
