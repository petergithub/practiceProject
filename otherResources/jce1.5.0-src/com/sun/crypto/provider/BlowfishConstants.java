/*
 * @(#)BlowfishConstants.java	1.6 04/01/06
 * 
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
 
package com.sun.crypto.provider;

/**
 * This class defines the constants used by the Blowfish algorithm
 * implementation.
 * 
 * @author Jan Luehe
 * @version 1.6, 01/06/04
 *
 * @see BlowfishCipher
 * @see BlowfishCrypt
 */

interface BlowfishConstants {
    int BLOWFISH_BLOCK_SIZE = 8; // number of bytes
    int BLOWFISH_MAX_KEYSIZE = 56; // number of bytes
}
    
