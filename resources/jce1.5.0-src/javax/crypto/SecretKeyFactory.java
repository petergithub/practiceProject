/*
 * @(#)SecretKeyFactory.java	1.37 04/01/06
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
 * This class represents a factory for secret keys.
 *
 * <P> Key factories are used to convert <I>keys</I> (opaque
 * cryptographic keys of type <code>Key</code>) into <I>key specifications</I>
 * (transparent representations of the underlying key material), and vice
 * versa.
 * Secret key factories operate only on secret (symmetric) keys.
 *
 * <P> Key factories are bi-directional, i.e., they allow to build an opaque
 * key object from a given key specification (key material), or to retrieve
 * the underlying key material of a key object in a suitable format.
 *
 * <P> Application developers should refer to their provider's documentation
 * to find out which key specifications are supported by the
 * {@link #generateSecret(java.security.spec.KeySpec) generateSecret} and
 * {@link #getKeySpec(javax.crypto.SecretKey, java.lang.Class) getKeySpec} 
 * methods.
 * For example, the DES secret-key factory supplied by the "SunJCE" provider
 * supports <code>DESKeySpec</code> as a transparent representation of DES
 * keys, and that provider's secret-key factory for Triple DES keys supports
 * <code>DESedeKeySpec</code> as a transparent representation of Triple DES
 * keys.
 *
 * @author Jan Luehe
 *
 * @version 1.37, 01/06/04
 *
 * @see SecretKey
 * @see javax.crypto.spec.DESKeySpec
 * @see javax.crypto.spec.DESedeKeySpec
 * @see javax.crypto.spec.PBEKeySpec
 * @since 1.4
 */

public class SecretKeyFactory {

    // The provider
    private Provider provider;

    // The algorithm associated with this factory
    private final String algorithm;

    // The provider implementation (delegate)
    private volatile SecretKeyFactorySpi spi;

    // lock for mutex during provider selection
    private final Object lock = new Object();

    // remaining services to try in provider selection
    // null once provider is selected
    private Iterator serviceIterator;
    
    /**
     * Creates a SecretKeyFactory object.
     *
     * @param keyFacSpi the delegate
     * @param provider the provider
     * @param algorithm the secret-key algorithm
     */
    protected SecretKeyFactory(SecretKeyFactorySpi keyFacSpi,
			       Provider provider, String algorithm) {
	this.spi = keyFacSpi;
	this.provider = provider;
	this.algorithm = algorithm;
    }
    
    private SecretKeyFactory(String algorithm) throws NoSuchAlgorithmException {
	this.algorithm = algorithm;
	List list = GetInstance.getServices("SecretKeyFactory", algorithm);
	serviceIterator = list.iterator();
	// fetch and instantiate initial spi
	if (nextSpi(null) == null) {
	    throw new NoSuchAlgorithmException
	    	(algorithm + " SecretKeyFactory not available");
	}
    }

    /**
     * Generates a <code>SecretKeyFactory</code> object for the
     * specified secret-key algorithm.
     * If the default provider package provides an implementation of the
     * requested factory, an instance of <code>SecretKeyFactory</code>
     * containing that implementation is returned.
     * If the requested factory is not available in the default provider
     * package, other provider packages are searched.
     *
     * @param algorithm the standard name of the requested secret-key
     * algorithm. 
     * See Appendix A in the
     * <a href="../../../guide/security/jce/JCERefGuide.html#AppA">
     * Java Cryptography Extension Reference Guide</a> 
     * for information about standard algorithm names.
     *
     * @return a <code>SecretKeyFactory</code> object for the specified
     * secret-key algorithm.
     *
     * @exception NullPointerException if the specified algorithm 
     * is null.
     * @exception NoSuchAlgorithmException if a secret-key factory for
     * the specified algorithm is not available in the default provider
     * package or any of the other provider packages that were searched.  
     */
    public static final SecretKeyFactory getInstance(String algorithm) 
	    throws NoSuchAlgorithmException {
	Instance instance = JceSecurity.getInstance("SecretKeyFactory", 
		SecretKeyFactorySpi.class, algorithm);
	return new SecretKeyFactory((SecretKeyFactorySpi)instance.impl, 
		instance.provider, algorithm);
    }

    /**
     * Generates a <code>SecretKeyFactory</code> object for the specified
     * secret-key algorithm from the specified provider.
     *
     * @param algorithm the standard name of the requested secret-key
     * algorithm. 
     * See Appendix A in the
     * <a href="../../../guide/security/jce/JCERefGuide.html#AppA">
     * Java Cryptography Extension Reference Guide</a> 
     * for information about standard algorithm names.
     * @param provider the name of the provider.
     *
     * @return a <code>SecretKeyFactory</code> object for the specified
     * secret-key algorithm.
     *
     * @exception NoSuchAlgorithmException if a secret-key factory for the
     * specified algorithm is not available from the specified provider.
     *
     * @exception NullPointerException if the specified algorithm 
     * is null.
     * @exception NoSuchProviderException if the specified provider has not
     * been configured.
     * @exception IllegalArgumentException if the <code>provider</code>
     * is null.
     */
    public static final SecretKeyFactory getInstance(String algorithm,
	    String provider) throws NoSuchAlgorithmException, 
	    NoSuchProviderException {	
	Instance instance = JceSecurity.getInstance("SecretKeyFactory", 
		SecretKeyFactorySpi.class, algorithm, provider);
	return new SecretKeyFactory((SecretKeyFactorySpi)instance.impl, 
		instance.provider, algorithm);
    }

    /**
     * Generates a <code>SecretKeyFactory</code> object for the specified
     * secret-key algorithm from the specified provider. Note: the 
     * <code>provider</code> doesn't have to be registered.
     *
     * @param algorithm the standard name of the requested secret-key
     * algorithm. 
     * See Appendix A in the
     * <a href="../../../guide/security/jce/JCERefGuide.html#AppA">
     * Java Cryptography Extension Reference Guide</a> 
     * for information about standard algorithm names.
     * @param provider the provider.
     *
     * @return a <code>SecretKeyFactory</code> object for the specified
     * secret-key algorithm.
     *
     * @exception NullPointerException if the specified algorithm 
     * is null.
     * @exception NoSuchAlgorithmException if a secret-key factory for the
     * specified algorithm is not available from the specified provider.
     * @exception IllegalArgumentException if the <code>provider</code>
     * is null.
     */
    public static final SecretKeyFactory getInstance(String algorithm,
	    Provider provider) throws NoSuchAlgorithmException {
	Instance instance = JceSecurity.getInstance("SecretKeyFactory", 
		SecretKeyFactorySpi.class, algorithm, provider);
	return new SecretKeyFactory((SecretKeyFactorySpi)instance.impl, 
		instance.provider, algorithm);
    }

    /** 
     * Returns the provider of this <code>SecretKeyFactory</code> object.
     * 
     * @return the provider of this <code>SecretKeyFactory</code> object
     */
    public final Provider getProvider() {
	synchronized (lock) {
	    // disable further failover after this call
	    serviceIterator = null;
	    return provider;
	}
    }

    /**
     * Returns the algorithm name of this <code>SecretKeyFactory</code> object.
     *
     * <p>This is the same name that was specified in one of the
     * <code>getInstance</code> calls that created this
     * <code>SecretKeyFactory</code> object.
     * 
     * @return the algorithm name of this <code>SecretKeyFactory</code>
     * object. 
     */
    public final String getAlgorithm() {
	return this.algorithm;
    }

    /**
     * Update the active spi of this class and return the next
     * implementation for failover. If no more implemenations are
     * available, this method returns null. However, the active spi of
     * this class is never set to null.
     */
    private SecretKeyFactorySpi nextSpi(SecretKeyFactorySpi oldSpi) {
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
		    Object obj = s.newInstance(null);
		    if (obj instanceof SecretKeyFactory == false) {
			continue;
		    }
		    SecretKeyFactorySpi spi = (SecretKeyFactorySpi)obj;
		    provider = s.getProvider();
		    this.spi = spi;
		    return spi;
		} catch (NoSuchAlgorithmException e) {
		    // ignore
		}
	    }
	    serviceIterator = null;
	    return null;
	}
    }

    /**
     * Generates a <code>SecretKey</code> object from the provided key
     * specification (key material).
     *
     * @param keySpec the specification (key material) of the secret key
     *
     * @return the secret key
     *
     * @exception InvalidKeySpecException if the given key specification
     * is inappropriate for this secret-key factory to produce a secret key.
     */
    public final SecretKey generateSecret(KeySpec keySpec)
	    throws InvalidKeySpecException {
	if (serviceIterator == null) {
	    return spi.engineGenerateSecret(keySpec);
	}
	Exception failure = null;
	SecretKeyFactorySpi mySpi = spi;
	do {
	    try {
		return mySpi.engineGenerateSecret(keySpec);
	    } catch (Exception e) {
		if (failure == null) {
		    failure = e;
		}
		mySpi = nextSpi(mySpi);
	    }
	} while (mySpi != null);
	if (failure instanceof InvalidKeySpecException) {
	    throw (InvalidKeySpecException)failure;
	}
	throw new InvalidKeySpecException
		("Could not generate secret key", failure);
    }

    /**
     * Returns a specification (key material) of the given key object
     * in the requested format.
     *
     * @param key the key 
     * @param keySpec the requested format in which the key material shall be
     * returned
     *
     * @return the underlying key specification (key material) in the
     * requested format
     *
     * @exception InvalidKeySpecException if the requested key specification is
     * inappropriate for the given key (e.g., the algorithms associated with
     * <code>key</code> and <code>keySpec</code> do not match, or
     * <code>key</code> references a key on a cryptographic hardware device
     * whereas <code>keySpec</code> is the specification of a software-based
     * key), or the given key cannot be dealt with
     * (e.g., the given key has an algorithm or format not supported by this
     * secret-key factory).
     */
    public final KeySpec getKeySpec(SecretKey key, Class keySpec)
	    throws InvalidKeySpecException {
	if (serviceIterator == null) {
	    return spi.engineGetKeySpec(key, keySpec);
	}
	Exception failure = null;
	SecretKeyFactorySpi mySpi = spi;
	do {
	    try {
		return mySpi.engineGetKeySpec(key, keySpec);
	    } catch (Exception e) {
		if (failure == null) {
		    failure = e;
		}
		mySpi = nextSpi(mySpi);
	    }
	} while (mySpi != null);
	if (failure instanceof InvalidKeySpecException) {
	    throw (InvalidKeySpecException)failure;
	}
	throw new InvalidKeySpecException
		("Could not get key spec", failure);
    }

    /**
     * Translates a key object, whose provider may be unknown or potentially
     * untrusted, into a corresponding key object of this secret-key factory.
     *
     * @param key the key whose provider is unknown or untrusted
     *
     * @return the translated key
     *
     * @exception InvalidKeyException if the given key cannot be processed
     * by this secret-key factory.
     */
    public final SecretKey translateKey(SecretKey key)
	    throws InvalidKeyException {
	if (serviceIterator == null) {
	    return spi.engineTranslateKey(key);
	}
	Exception failure = null;
	SecretKeyFactorySpi mySpi = spi;
	do {
	    try {
		return mySpi.engineTranslateKey(key);
	    } catch (Exception e) {
		if (failure == null) {
		    failure = e;
		}
		mySpi = nextSpi(mySpi);
	    }
	} while (mySpi != null);
	if (failure instanceof InvalidKeyException) {
	    throw (InvalidKeyException)failure;
	}
	throw new InvalidKeyException
		("Could not translate key", failure);
    }
}
