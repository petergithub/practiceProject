/*
 * @(#)ExemptionMechanismException.java	1.5 04/01/06
 * 
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.crypto;

import java.security.GeneralSecurityException;

/**
 * This is the generic ExemptionMechanism exception.
 *
 * @version 1.5, 01/06/04
 * @since 1.4
 */

public class ExemptionMechanismException extends GeneralSecurityException {

    private static final long serialVersionUID = 1572699429277957109L;

    /**
     * Constructs a ExemptionMechanismException with no detailed message.
     * (A detailed message is a String that describes this particular
     * exception.)
     */
    public ExemptionMechanismException() {
	super();
    }

    /**
     * Constructs a ExemptionMechanismException with the specified
     * detailed message. (A detailed message is a String that describes
     * this particular exception.)
     *
     * @param msg the detailed message.
     */
   public ExemptionMechanismException(String msg) {
       super(msg);
    }
}
