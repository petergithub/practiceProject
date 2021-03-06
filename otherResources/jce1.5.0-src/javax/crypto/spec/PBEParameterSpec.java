/*
 * @(#)PBEParameterSpec.java	1.16 04/06/03
 * 
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.crypto.spec;

import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;

/**
 * This class specifies the set of parameters used with password-based
 * encryption (PBE), as defined in the
 * <a href="http://www.ietf.org/rfc/rfc2898.txt">PKCS #5</a>
 * standard.
 * 
 * @author Jan Luehe
 *
 * @version 1.16, 06/03/04
 * @since 1.4
 */
public class PBEParameterSpec implements AlgorithmParameterSpec {

    private byte[] salt;
    private int iterationCount;

    /**
     * Constructs a parameter set for password-based encryption as defined in
     * the PKCS #5 standard.
     *
     * @param salt the salt. The contents of <code>salt</code> are copied 
     * to protect against subsequent modification.
     * @param iterationCount the iteration count.
     * @exception NullPointerException if <code>salt</code> is null.
     */
    public PBEParameterSpec(byte[] salt, int iterationCount) {
	this.salt = (byte[])salt.clone();
	this.iterationCount = iterationCount;
    }

    /**
     * Returns the salt.
     *
     * @return the salt. Returns a new array
     * each time this method is called.
     */
    public byte[] getSalt() {
	return (byte[])this.salt.clone();
    }

    /**
     * Returns the iteration count.
     *
     * @return the iteration count
     */
    public int getIterationCount() {
	return this.iterationCount;
    }
}
