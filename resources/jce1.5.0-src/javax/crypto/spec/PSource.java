/*
 * @(#)PSource.java	1.4 04/06/03
 * 
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.crypto.spec;

import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;

/**
 * This class specifies the source for encoding input P in OAEP Padding,
 * as defined in the
 * <a href="http://www.ietf.org/rfc/rfc3447.txt">PKCS #1</a>
 * standard.
 * <pre>
 * PKCS1PSourceAlgorithms    ALGORITHM-IDENTIFIER ::= {
 *   { OID id-pSpecified PARAMETERS OCTET STRING },
 *   ...  -- Allows for future expansion --
 * }
 * </pre>
 * @author Valerie Peng
 *
 * @version 1.4, 06/03/04
 * @since 1.5
 */
public class PSource {

    private String pSrcName;

    /**
     * Constructs a source of the encoding input P for OAEP 
     * padding as defined in the PKCS #1 standard using the 
     * specified PSource algorithm.
     * @param pSrcName the algorithm for the source of the
     * encoding input P.
     * @exception NullPointerException if <code>pSrcName</code>
     * is null.
     */
    protected PSource(String pSrcName) {
	if (pSrcName == null) {
	    throw new NullPointerException("pSource algorithm is null");
	}
	this.pSrcName = pSrcName;
    }
    /**
     * Returns the PSource algorithm name. 
     *
     * @return the PSource algorithm name. 
     */
    public String getAlgorithm() {
	return pSrcName;
    }

    /**
     * This class is used to explicitly specify the value for 
     * encoding input P in OAEP Padding.
     */
    public static final class PSpecified extends PSource {

	private byte[] p = new byte[0];

	/**
	 * The encoding input P whose value equals byte[0].
	 */
	public static final PSpecified DEFAULT = new PSpecified(new byte[0]);

	/**
	 * Constructs the source explicitly with the specified
	 * value <code>p</code> as the encoding input P. 
	 * Note: 
	 * @param p the value of the encoding input. The contents 
	 * of the array are copied to protect against subsequent 
	 * modification.
	 * @exception NullPointerException if <code>p</code> is null.
	 */
	public PSpecified(byte[] p) {
	    super("PSpecified");
	    this.p = (byte[]) p.clone();
	}
	/** 
	 * Returns the value of encoding input P.
	 * @return the value of encoding input P. A new array is 
	 * returned each time this method is called.
	 */
	public byte[] getValue() {
	    return (p.length==0? p: (byte[])p.clone());
	}
    }
}
