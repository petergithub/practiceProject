/*
 * @(#)BadPaddingException.java	1.11 04/03/15
 * 
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.crypto;

import java.security.GeneralSecurityException;

/**
 * This exception is thrown when a particular padding mechanism is
 * expected for the input data but the data is not padded properly.
 *
 * @author Gigi Ankney
 *
 * @version 1.11, 03/15/04
 * @since 1.4
 */

public class BadPaddingException extends GeneralSecurityException {

    private static final long serialVersionUID = -5315033893984728443L;

    /** 
     * Constructs a BadPaddingException with no detail
     * message. A detail message is a String that describes this
     * particular exception.
     */
    public BadPaddingException() {
	super();
    }

    /**
     * Constructs a BadPaddingException with the specified
     * detail message. 
     *
     * @param msg the detail message.  
     */
    public BadPaddingException(String msg) {
	super(msg);
    }
}
