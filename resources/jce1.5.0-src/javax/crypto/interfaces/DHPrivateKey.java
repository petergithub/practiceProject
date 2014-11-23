/*
 * @(#)DHPrivateKey.java	1.12 04/01/14
 * 
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.crypto.interfaces;

import java.math.BigInteger;

/**
 * The interface to a Diffie-Hellman private key.
 *
 * @author Jan Luehe
 *
 * @version 1.12, 01/14/04
 *
 * @see DHKey
 * @see DHPublicKey
 * @since 1.4
 */
public interface DHPrivateKey extends DHKey, java.security.PrivateKey {

    /** 
     * The class fingerprint that is set to indicate serialization
     * compatibility since J2SE 1.4. 
     */
    static final long serialVersionUID = 2211791113380396553L;
    
    /**
     * Returns the private value, <code>x</code>.
     *
     * @return the private value, <code>x</code>
     */
    BigInteger getX();
}
