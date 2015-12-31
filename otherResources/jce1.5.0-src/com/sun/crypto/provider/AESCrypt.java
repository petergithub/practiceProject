/*
 * @(#)AESCrypt.java	1.6 04/01/06
 * 
 * Portions Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/* $Id: Rijndael.java,v 1.6 2000/02/10 01:31:41 gelderen Exp $
 *
 * Copyright (C) 1995-2000 The Cryptix Foundation Limited.
 * All rights reserved.
 *
 * Use, modification, copying and distribution of this softwareas is subject
 * the terms and conditions of the Cryptix General Licence. You should have
 * received a copy of the Cryptix General Licence along with this library;
 * if not, you can download a copy from http://www.cryptix.org/ .
 */

package com.sun.crypto.provider;

import java.security.InvalidKeyException;

/**
 * Rijndael --pronounced Reindaal-- is a symmetric cipher with a 128-bit
 * block size and variable key-size (128-, 192- and 256-bit).
 * <p>
 * Rijndael was designed by <a href="mailto:rijmen@esat.kuleuven.ac.be">Vincent
 * Rijmen</a> and <a href="mailto:Joan.Daemen@village.uunet.be">Joan Daemen</a>.
 * @version 1.6, 01/06/04
 */
final class AESCrypt extends SymmetricCipher implements AESConstants 
{
    private boolean ROUNDS_12 = false;
    private boolean ROUNDS_14 = false;

    /** Session and Sub keys */
    private Object[] sessionK = null;
    private int[] K = null;

    /** (ROUNDS-1) * 4 */
    private int limit = 0;
    
    AESCrypt() {
	// empty
    }

    /**
     * Returns this cipher's block size.
     *
     * @return this cipher's block size
     */
    int getBlockSize() {
	return AES_BLOCK_SIZE;
    }

    void init(boolean decrypting, String algorithm, byte[] key)
	    throws InvalidKeyException {
	if (!algorithm.equalsIgnoreCase("AES")
		    && !algorithm.equalsIgnoreCase("Rijndael")) {
	    throw new InvalidKeyException
	    	("Wrong algorithm: AES or Rijndael required");
	}
        int len = key.length ;
        if (!isKeySizeValid(len*8)) {
            throw new InvalidKeyException("Invalid AES key length: " + len*8);
	} 

	// generate session key and reset sub key.
        sessionK = makeKey(key);
	setSubKey(decrypting);
    }
    
    private void setSubKey(boolean decrypting) {
        int[][] Kd = (int[][]) sessionK[decrypting ? 1 : 0];
        int rounds = Kd.length;
        this.K = new int[rounds*4];
        for(int i=0; i<rounds; i++) {
            for(int j=0; j<4; j++) {
                K[i*4 + j] = Kd[i][j];
	    }
	}

        if (decrypting) {
            int j0 = K[K.length-4];
            int j1 = K[K.length-3];
            int j2 = K[K.length-2];
            int j3 = K[K.length-1];

            for (int i=this.K.length-1; i>3; i--) {
                this.K[i] = this.K[i-4];
	    }
            K[0] = j0;
            K[1] = j1;
            K[2] = j2;
            K[3] = j3;
        }

        ROUNDS_12 = (rounds>=13);
        ROUNDS_14 = (rounds==15);

        rounds--;
        limit=rounds*4;
    }

    private static int[]
        alog = new int[256],
        log  = new int[256];

    private static final byte[]
        S  = new byte[256],
        Si = new byte[256];

    private static final int[]
        T1 = new int[256],
        T2 = new int[256],
        T3 = new int[256],
        T4 = new int[256],
        T5 = new int[256],
        T6 = new int[256],
        T7 = new int[256],
        T8 = new int[256];

    private static final int[]
        U1 = new int[256],
        U2 = new int[256],
        U3 = new int[256],
        U4 = new int[256];

    private static final byte[] rcon = new byte[30];


    // Static code - to intialise S-boxes and T-boxes
    static 
    {
        int ROOT = 0x11B;
        int i, j = 0;

        //
        // produce log and alog tables, needed for multiplying in the
        // field GF(2^m) (generator = 3)
        //
        alog[0] = 1;
        for (i = 1; i < 256; i++) 
        {
            j = (alog[i-1] << 1) ^ alog[i-1];
            if ((j & 0x100) != 0) {
		j ^= ROOT;
            }
	    alog[i] = j;
        }
        for (i = 1; i < 255; i++) {
	    log[alog[i]] = i;
        }
	byte[][] A = new byte[][] 
        {
            {1, 1, 1, 1, 1, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 0, 0},
	    {0, 0, 1, 1, 1, 1, 1, 0},
	    {0, 0, 0, 1, 1, 1, 1, 1},
	    {1, 0, 0, 0, 1, 1, 1, 1},
	    {1, 1, 0, 0, 0, 1, 1, 1},
	    {1, 1, 1, 0, 0, 0, 1, 1},
	    {1, 1, 1, 1, 0, 0, 0, 1}
	};
        byte[] B = new byte[] { 0, 1, 1, 0, 0, 0, 1, 1};

        //
        // substitution box based on F^{-1}(x)
        //
        int t;
        byte[][] box = new byte[256][8];
        box[1][7] = 1;
        for (i = 2; i < 256; i++) {
            j = alog[255 - log[i]];
            for (t = 0; t < 8; t++) {
                box[i][t] = (byte)((j >>> (7 - t)) & 0x01);
	    }
        }
        //
        // affine transform:  box[i] <- B + A*box[i]
        //
        byte[][] cox = new byte[256][8];
        for (i = 0; i < 256; i++) {
            for (t = 0; t < 8; t++) {
                cox[i][t] = B[t];
                for (j = 0; j < 8; j++) {
                    cox[i][t] ^= A[t][j] * box[i][j];
		}
            }
	}
        //
        // S-boxes and inverse S-boxes
        //
        for (i = 0; i < 256; i++) {
            S[i] = (byte)(cox[i][0] << 7);
	    for (t = 1; t < 8; t++) {
	            S[i] ^= cox[i][t] << (7-t);
            }
	    Si[S[i] & 0xFF] = (byte) i;
        }
        //
        // T-boxes
        //
        byte[][] G = new byte[][] {
            {2, 1, 1, 3},
            {3, 2, 1, 1},
            {1, 3, 2, 1},
            {1, 1, 3, 2}
        };
        byte[][] AA = new byte[4][8];
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) AA[i][j] = G[i][j];
            AA[i][i+4] = 1;
        }
        byte pivot, tmp;
        byte[][] iG = new byte[4][4];
        for (i = 0; i < 4; i++) {
            pivot = AA[i][i];
            if (pivot == 0) {
                t = i + 1;
                while ((AA[t][i] == 0) && (t < 4)) {
                    t++;
                }
		if (t == 4) {
                    throw new RuntimeException("G matrix is not invertible");
                }
                else {
                    for (j = 0; j < 8; j++) {
                        tmp = AA[i][j];
                        AA[i][j] = AA[t][j];
                        AA[t][j] = (byte) tmp;
                    }
                    pivot = AA[i][i];
                }
            }
            for (j = 0; j < 8; j++) {
                if (AA[i][j] != 0) {
                    AA[i][j] = (byte)
                        alog[(255 + log[AA[i][j] & 0xFF] - log[pivot & 0xFF]) % 255];
		}
	    }
            for (t = 0; t < 4; t++) {
                if (i != t) {
                    for (j = i+1; j < 8; j++) {
                        AA[t][j] ^= mul(AA[i][j], AA[t][i]);
                    }
		    AA[t][i] = 0;
                }
	    }
        }
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
		iG[i][j] = AA[i][j + 4];
	    }
	}

        int s;
        for (t = 0; t < 256; t++) {
            s = S[t];
            T1[t] = mul4(s, G[0]);
            T2[t] = mul4(s, G[1]);
            T3[t] = mul4(s, G[2]);
            T4[t] = mul4(s, G[3]);

            s = Si[t];
            T5[t] = mul4(s, iG[0]);
            T6[t] = mul4(s, iG[1]);
            T7[t] = mul4(s, iG[2]);
            T8[t] = mul4(s, iG[3]);

            U1[t] = mul4(t, iG[0]);
            U2[t] = mul4(t, iG[1]);
            U3[t] = mul4(t, iG[2]);
            U4[t] = mul4(t, iG[3]);
        }
        //
        // round constants
        //
        rcon[0] = 1;
        int r = 1;
        for (t = 1; t < 30; t++) {
	    r = mul(2, r);
	    rcon[t] = (byte) r;
	}
        log = null;
        alog = null;
    }

    // multiply two elements of GF(2^m)
    private static final int mul (int a, int b) {
        return (a != 0 && b != 0) ?
            alog[(log[a & 0xFF] + log[b & 0xFF]) % 255] :
            0;
    }

    // convenience method used in generating Transposition boxes
    private static final int mul4 (int a, byte[] b) {
        if (a == 0) return 0;
        a = log[a & 0xFF];
        int a0 = (b[0] != 0) ? alog[(a + log[b[0] & 0xFF]) % 255] & 0xFF : 0;
        int a1 = (b[1] != 0) ? alog[(a + log[b[1] & 0xFF]) % 255] & 0xFF : 0;
        int a2 = (b[2] != 0) ? alog[(a + log[b[2] & 0xFF]) % 255] & 0xFF : 0;
        int a3 = (b[3] != 0) ? alog[(a + log[b[3] & 0xFF]) % 255] & 0xFF : 0;
        return a0 << 24 | a1 << 16 | a2 << 8 | a3;
    }

    static final boolean isKeySizeValid(int len) {
        boolean result = false;
        if ((len == 128) || (len == 192) || (len == 256)) {
            result = true;
        }
        return result;
    }

    /**
     * Encrypt exactly one block of plaintext.
     */
    void encryptBlock(byte[] in, int inOffset, 
                              byte[] out, int outOffset) 
    {
        int keyOffset = 0;
        int t0   = ((in[inOffset++]       ) << 24 |
                    (in[inOffset++] & 0xFF) << 16 |
                    (in[inOffset++] & 0xFF) <<  8 |
                    (in[inOffset++] & 0xFF)        ) ^ K[keyOffset++];
        int t1   = ((in[inOffset++]       ) << 24 |
                    (in[inOffset++] & 0xFF) << 16 |
                    (in[inOffset++] & 0xFF) <<  8 |
                    (in[inOffset++] & 0xFF)        ) ^ K[keyOffset++];
        int t2   = ((in[inOffset++]       ) << 24 |
                    (in[inOffset++] & 0xFF) << 16 |
                    (in[inOffset++] & 0xFF) <<  8 |
                    (in[inOffset++] & 0xFF)        ) ^ K[keyOffset++];
        int t3   = ((in[inOffset++]       ) << 24 |
                    (in[inOffset++] & 0xFF) << 16 |
                    (in[inOffset++] & 0xFF) <<  8 |
                    (in[inOffset++] & 0xFF)        ) ^ K[keyOffset++];

        // apply round transforms
        while( keyOffset < limit ) 
        {
            int a0, a1, a2;
            a0 = T1[(t0 >>> 24)       ] ^
                 T2[(t1 >>> 16) & 0xFF] ^
                 T3[(t2 >>>  8) & 0xFF] ^
                 T4[(t3       ) & 0xFF] ^ K[keyOffset++];
            a1 = T1[(t1 >>> 24)       ] ^
                 T2[(t2 >>> 16) & 0xFF] ^
                 T3[(t3 >>>  8) & 0xFF] ^
                 T4[(t0       ) & 0xFF] ^ K[keyOffset++];
            a2 = T1[(t2 >>> 24)       ] ^
                 T2[(t3 >>> 16) & 0xFF] ^
                 T3[(t0 >>>  8) & 0xFF] ^
                 T4[(t1       ) & 0xFF] ^ K[keyOffset++];
            t3 = T1[(t3 >>> 24)       ] ^
                 T2[(t0 >>> 16) & 0xFF] ^
                 T3[(t1 >>>  8) & 0xFF] ^
                 T4[(t2       ) & 0xFF] ^ K[keyOffset++];
            t0 = a0; t1 = a1; t2 = a2;
        }

        // last round is special
        int tt = K[keyOffset++];
        out[outOffset++] = (byte)(S[(t0 >>> 24)       ] ^ (tt >>> 24));
        out[outOffset++] = (byte)(S[(t1 >>> 16) & 0xFF] ^ (tt >>> 16));
        out[outOffset++] = (byte)(S[(t2 >>>  8) & 0xFF] ^ (tt >>>  8));
        out[outOffset++] = (byte)(S[(t3       ) & 0xFF] ^ (tt       ));
        tt = K[keyOffset++];
        out[outOffset++] = (byte)(S[(t1 >>> 24)       ] ^ (tt >>> 24));
        out[outOffset++] = (byte)(S[(t2 >>> 16) & 0xFF] ^ (tt >>> 16));
        out[outOffset++] = (byte)(S[(t3 >>>  8) & 0xFF] ^ (tt >>>  8));
        out[outOffset++] = (byte)(S[(t0       ) & 0xFF] ^ (tt       ));
        tt = K[keyOffset++];
        out[outOffset++] = (byte)(S[(t2 >>> 24)       ] ^ (tt >>> 24));
        out[outOffset++] = (byte)(S[(t3 >>> 16) & 0xFF] ^ (tt >>> 16));
        out[outOffset++] = (byte)(S[(t0 >>>  8) & 0xFF] ^ (tt >>>  8));
        out[outOffset++] = (byte)(S[(t1       ) & 0xFF] ^ (tt       ));
        tt = K[keyOffset++];
        out[outOffset++] = (byte)(S[(t3 >>> 24)       ] ^ (tt >>> 24));
        out[outOffset++] = (byte)(S[(t0 >>> 16) & 0xFF] ^ (tt >>> 16));
        out[outOffset++] = (byte)(S[(t1 >>>  8) & 0xFF] ^ (tt >>>  8));
        out[outOffset  ] = (byte)(S[(t2       ) & 0xFF] ^ (tt       ));
    }


    /**
     * Decrypt exactly one block of plaintext.
     */
    void decryptBlock(byte[] in, int inOffset, 
			      byte[] out, int outOffset)
    {
        int keyOffset = 4;
        int t0 = ((in[inOffset++]       ) << 24 |
		  (in[inOffset++] & 0xFF) << 16 |
		  (in[inOffset++] & 0xFF) <<  8 |
		  (in[inOffset++] & 0xFF)        ) ^ K[keyOffset++];
        int t1 = ((in[inOffset++]       ) << 24 |
		  (in[inOffset++] & 0xFF) << 16 |
		  (in[inOffset++] & 0xFF) <<  8 |
		  (in[inOffset++] & 0xFF)        ) ^ K[keyOffset++];
        int t2 = ((in[inOffset++]       ) << 24 |
		  (in[inOffset++] & 0xFF) << 16 |
		  (in[inOffset++] & 0xFF) <<  8 |
		  (in[inOffset++] & 0xFF)        ) ^ K[keyOffset++];
        int t3 = ((in[inOffset++]       ) << 24 |
		  (in[inOffset++] & 0xFF) << 16 |
		  (in[inOffset++] & 0xFF) <<  8 |
		  (in[inOffset  ] & 0xFF)        ) ^ K[keyOffset++];

	int a0, a1, a2;
        if(ROUNDS_12) 
        {
            a0 = T5[(t0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
                 T7[(t2>>> 8)&0xFF] ^ T8[(t1     )&0xFF] ^ K[keyOffset++];
            a1 = T5[(t1>>>24)     ] ^ T6[(t0>>>16)&0xFF] ^
                 T7[(t3>>> 8)&0xFF] ^ T8[(t2     )&0xFF] ^ K[keyOffset++];
            a2 = T5[(t2>>>24)     ] ^ T6[(t1>>>16)&0xFF] ^
                 T7[(t0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ K[keyOffset++];
            t3 = T5[(t3>>>24)     ] ^ T6[(t2>>>16)&0xFF] ^
                 T7[(t1>>> 8)&0xFF] ^ T8[(t0     )&0xFF] ^ K[keyOffset++];
            t0 = T5[(a0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
                 T7[(a2>>> 8)&0xFF] ^ T8[(a1     )&0xFF] ^ K[keyOffset++];
            t1 = T5[(a1>>>24)     ] ^ T6[(a0>>>16)&0xFF] ^
                 T7[(t3>>> 8)&0xFF] ^ T8[(a2     )&0xFF] ^ K[keyOffset++];
            t2 = T5[(a2>>>24)     ] ^ T6[(a1>>>16)&0xFF] ^
                 T7[(a0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ K[keyOffset++];
            t3 = T5[(t3>>>24)     ] ^ T6[(a2>>>16)&0xFF] ^
                 T7[(a1>>> 8)&0xFF] ^ T8[(a0     )&0xFF] ^ K[keyOffset++];

            if(ROUNDS_14) 
            {
                a0 = T5[(t0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
                     T7[(t2>>> 8)&0xFF] ^ T8[(t1     )&0xFF] ^ K[keyOffset++];
                a1 = T5[(t1>>>24)     ] ^ T6[(t0>>>16)&0xFF] ^
                     T7[(t3>>> 8)&0xFF] ^ T8[(t2     )&0xFF] ^ K[keyOffset++];
                a2 = T5[(t2>>>24)     ] ^ T6[(t1>>>16)&0xFF] ^
                     T7[(t0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ K[keyOffset++];
                t3 = T5[(t3>>>24)     ] ^ T6[(t2>>>16)&0xFF] ^
                     T7[(t1>>> 8)&0xFF] ^ T8[(t0     )&0xFF] ^ K[keyOffset++];
                t0 = T5[(a0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
                     T7[(a2>>> 8)&0xFF] ^ T8[(a1     )&0xFF] ^ K[keyOffset++];
                t1 = T5[(a1>>>24)     ] ^ T6[(a0>>>16)&0xFF] ^
                     T7[(t3>>> 8)&0xFF] ^ T8[(a2     )&0xFF] ^ K[keyOffset++];
                t2 = T5[(a2>>>24)     ] ^ T6[(a1>>>16)&0xFF] ^
                     T7[(a0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ K[keyOffset++];
                t3 = T5[(t3>>>24)     ] ^ T6[(a2>>>16)&0xFF] ^
                     T7[(a1>>> 8)&0xFF] ^ T8[(a0     )&0xFF] ^ K[keyOffset++];
            }
        }
        a0 = T5[(t0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
             T7[(t2>>> 8)&0xFF] ^ T8[(t1     )&0xFF] ^ K[keyOffset++];
        a1 = T5[(t1>>>24)     ] ^ T6[(t0>>>16)&0xFF] ^
             T7[(t3>>> 8)&0xFF] ^ T8[(t2     )&0xFF] ^ K[keyOffset++];
        a2 = T5[(t2>>>24)     ] ^ T6[(t1>>>16)&0xFF] ^
             T7[(t0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ K[keyOffset++];
        t3 = T5[(t3>>>24)     ] ^ T6[(t2>>>16)&0xFF] ^
             T7[(t1>>> 8)&0xFF] ^ T8[(t0     )&0xFF] ^ K[keyOffset++];
        t0 = T5[(a0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
             T7[(a2>>> 8)&0xFF] ^ T8[(a1     )&0xFF] ^ K[keyOffset++];
        t1 = T5[(a1>>>24)     ] ^ T6[(a0>>>16)&0xFF] ^
             T7[(t3>>> 8)&0xFF] ^ T8[(a2     )&0xFF] ^ K[keyOffset++];
        t2 = T5[(a2>>>24)     ] ^ T6[(a1>>>16)&0xFF] ^
             T7[(a0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ K[keyOffset++];
        t3 = T5[(t3>>>24)     ] ^ T6[(a2>>>16)&0xFF] ^
             T7[(a1>>> 8)&0xFF] ^ T8[(a0     )&0xFF] ^ K[keyOffset++];
        a0 = T5[(t0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
             T7[(t2>>> 8)&0xFF] ^ T8[(t1     )&0xFF] ^ K[keyOffset++];
        a1 = T5[(t1>>>24)     ] ^ T6[(t0>>>16)&0xFF] ^
             T7[(t3>>> 8)&0xFF] ^ T8[(t2     )&0xFF] ^ K[keyOffset++];
        a2 = T5[(t2>>>24)     ] ^ T6[(t1>>>16)&0xFF] ^
             T7[(t0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ K[keyOffset++];
        t3 = T5[(t3>>>24)     ] ^ T6[(t2>>>16)&0xFF] ^
             T7[(t1>>> 8)&0xFF] ^ T8[(t0     )&0xFF] ^ K[keyOffset++];
        t0 = T5[(a0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
             T7[(a2>>> 8)&0xFF] ^ T8[(a1     )&0xFF] ^ K[keyOffset++];
        t1 = T5[(a1>>>24)     ] ^ T6[(a0>>>16)&0xFF] ^
             T7[(t3>>> 8)&0xFF] ^ T8[(a2     )&0xFF] ^ K[keyOffset++];
        t2 = T5[(a2>>>24)     ] ^ T6[(a1>>>16)&0xFF] ^
             T7[(a0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ K[keyOffset++];
        t3 = T5[(t3>>>24)     ] ^ T6[(a2>>>16)&0xFF] ^
             T7[(a1>>> 8)&0xFF] ^ T8[(a0     )&0xFF] ^ K[keyOffset++];
        a0 = T5[(t0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
             T7[(t2>>> 8)&0xFF] ^ T8[(t1     )&0xFF] ^ K[keyOffset++];
        a1 = T5[(t1>>>24)     ] ^ T6[(t0>>>16)&0xFF] ^
             T7[(t3>>> 8)&0xFF] ^ T8[(t2     )&0xFF] ^ K[keyOffset++];
        a2 = T5[(t2>>>24)     ] ^ T6[(t1>>>16)&0xFF] ^
             T7[(t0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ K[keyOffset++];
        t3 = T5[(t3>>>24)     ] ^ T6[(t2>>>16)&0xFF] ^
             T7[(t1>>> 8)&0xFF] ^ T8[(t0     )&0xFF] ^ K[keyOffset++];
        t0 = T5[(a0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
             T7[(a2>>> 8)&0xFF] ^ T8[(a1     )&0xFF] ^ K[keyOffset++];
        t1 = T5[(a1>>>24)     ] ^ T6[(a0>>>16)&0xFF] ^
             T7[(t3>>> 8)&0xFF] ^ T8[(a2     )&0xFF] ^ K[keyOffset++];
        t2 = T5[(a2>>>24)     ] ^ T6[(a1>>>16)&0xFF] ^
             T7[(a0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ K[keyOffset++];
        t3 = T5[(t3>>>24)     ] ^ T6[(a2>>>16)&0xFF] ^
             T7[(a1>>> 8)&0xFF] ^ T8[(a0     )&0xFF] ^ K[keyOffset++];
        a0 = T5[(t0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
             T7[(t2>>> 8)&0xFF] ^ T8[(t1     )&0xFF] ^ K[keyOffset++];
        a1 = T5[(t1>>>24)     ] ^ T6[(t0>>>16)&0xFF] ^
             T7[(t3>>> 8)&0xFF] ^ T8[(t2     )&0xFF] ^ K[keyOffset++];
        a2 = T5[(t2>>>24)     ] ^ T6[(t1>>>16)&0xFF] ^
             T7[(t0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ K[keyOffset++];
        t3 = T5[(t3>>>24)     ] ^ T6[(t2>>>16)&0xFF] ^
             T7[(t1>>> 8)&0xFF] ^ T8[(t0     )&0xFF] ^ K[keyOffset++];
        t0 = T5[(a0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
             T7[(a2>>> 8)&0xFF] ^ T8[(a1     )&0xFF] ^ K[keyOffset++];
        t1 = T5[(a1>>>24)     ] ^ T6[(a0>>>16)&0xFF] ^
             T7[(t3>>> 8)&0xFF] ^ T8[(a2     )&0xFF] ^ K[keyOffset++];
        t2 = T5[(a2>>>24)     ] ^ T6[(a1>>>16)&0xFF] ^
             T7[(a0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ K[keyOffset++];
        t3 = T5[(t3>>>24)     ] ^ T6[(a2>>>16)&0xFF] ^
             T7[(a1>>> 8)&0xFF] ^ T8[(a0     )&0xFF] ^ K[keyOffset++];
        a0 = T5[(t0>>>24)     ] ^ T6[(t3>>>16)&0xFF] ^
             T7[(t2>>> 8)&0xFF] ^ T8[(t1     )&0xFF] ^ K[keyOffset++];
        a1 = T5[(t1>>>24)     ] ^ T6[(t0>>>16)&0xFF] ^
             T7[(t3>>> 8)&0xFF] ^ T8[(t2     )&0xFF] ^ K[keyOffset++];
        a2 = T5[(t2>>>24)     ] ^ T6[(t1>>>16)&0xFF] ^
             T7[(t0>>> 8)&0xFF] ^ T8[(t3     )&0xFF] ^ K[keyOffset++];
        t3 = T5[(t3>>>24)     ] ^ T6[(t2>>>16)&0xFF] ^
             T7[(t1>>> 8)&0xFF] ^ T8[(t0     )&0xFF] ^ K[keyOffset++];

        t1 = K[0];
        out[outOffset++] = (byte)(Si[(a0 >>> 24)       ] ^ (t1 >>> 24));
        out[outOffset++] = (byte)(Si[(t3 >>> 16) & 0xFF] ^ (t1 >>> 16));
        out[outOffset++] = (byte)(Si[(a2 >>>  8) & 0xFF] ^ (t1 >>>  8));
        out[outOffset++] = (byte)(Si[(a1       ) & 0xFF] ^ (t1       ));
        t1 = K[1];
        out[outOffset++] = (byte)(Si[(a1 >>> 24)       ] ^ (t1 >>> 24));
        out[outOffset++] = (byte)(Si[(a0 >>> 16) & 0xFF] ^ (t1 >>> 16));
        out[outOffset++] = (byte)(Si[(t3 >>>  8) & 0xFF] ^ (t1 >>>  8));
        out[outOffset++] = (byte)(Si[(a2       ) & 0xFF] ^ (t1       ));
        t1 = K[2];
        out[outOffset++] = (byte)(Si[(a2 >>> 24)       ] ^ (t1 >>> 24));
        out[outOffset++] = (byte)(Si[(a1 >>> 16) & 0xFF] ^ (t1 >>> 16));
        out[outOffset++] = (byte)(Si[(a0 >>>  8) & 0xFF] ^ (t1 >>>  8));
        out[outOffset++] = (byte)(Si[(t3       ) & 0xFF] ^ (t1       ));
        t1 = K[3];
        out[outOffset++] = (byte)(Si[(t3 >>> 24)       ] ^ (t1 >>> 24));
        out[outOffset++] = (byte)(Si[(a2 >>> 16) & 0xFF] ^ (t1 >>> 16));
        out[outOffset++] = (byte)(Si[(a1 >>>  8) & 0xFF] ^ (t1 >>>  8));
        out[outOffset  ] = (byte)(Si[(a0       ) & 0xFF] ^ (t1       ));
    }


    /**
     * Expand a user-supplied key material into a session key.
     *
     * @param key The 128/192/256-bit user-key to use.
     * @exception InvalidKeyException  If the key is invalid.
     */
    private static Object[] makeKey(byte[] k) throws InvalidKeyException {
        if (k == null) {
            throw new InvalidKeyException("Empty key");
	}
        if (!isKeySizeValid(k.length*8)) {
             throw new InvalidKeyException("Incorrect key length: " +
					   k.length*8);
	}
        int ROUNDS          = getRounds(k.length);
        int ROUND_KEY_COUNT = (ROUNDS + 1) * 4;

        int BC = 4;
        int[][] Ke = new int[ROUNDS + 1][4]; // encryption round keys
        int[][] Kd = new int[ROUNDS + 1][4]; // decryption round keys

        int KC = k.length/4; // keylen in 32-bit elements

        int[] tk = new int[KC];
        int i, j;

        // copy user material bytes into temporary ints
        for (i = 0, j = 0; i < KC; i++, j+=4) {
            tk[i] = (k[j]       ) << 24 |
                    (k[j+1] & 0xFF) << 16 |
                    (k[j+2] & 0xFF) <<  8 |
                    (k[j+3] & 0xFF);
	}

        // copy values into round key arrays
        int t = 0;
        for (j = 0; (j < KC) && (t < ROUND_KEY_COUNT); j++, t++) {
            Ke[t / 4][t % 4] = tk[j];
            Kd[ROUNDS - (t / 4)][t % 4] = tk[j];
        }
        int tt, rconpointer = 0;
        while (t < ROUND_KEY_COUNT) {
            // extrapolate using phi (the round key evolution function)
            tt = tk[KC - 1];
            tk[0] ^= (S[(tt >>> 16) & 0xFF]       ) << 24 ^
                     (S[(tt >>>  8) & 0xFF] & 0xFF) << 16 ^
                     (S[(tt       ) & 0xFF] & 0xFF) <<  8 ^
                     (S[(tt >>> 24)       ] & 0xFF)       ^
                     (rcon[rconpointer++]         ) << 24;
            if (KC != 8)
                for (i = 1, j = 0; i < KC; i++, j++) tk[i] ^= tk[j];
            else {
                for (i = 1, j = 0; i < KC / 2; i++, j++) tk[i] ^= tk[j];
                tt = tk[KC / 2 - 1];
                tk[KC / 2] ^= (S[(tt       ) & 0xFF] & 0xFF)       ^
                              (S[(tt >>>  8) & 0xFF] & 0xFF) <<  8 ^
                              (S[(tt >>> 16) & 0xFF] & 0xFF) << 16 ^
                              (S[(tt >>> 24)       ]       ) << 24;
                for (j = KC / 2, i = j + 1; i < KC; i++, j++) tk[i] ^= tk[j];
            }
            // copy values into round key arrays
            for (j = 0; (j < KC) && (t < ROUND_KEY_COUNT); j++, t++) {
                Ke[t / 4][t % 4] = tk[j];
                Kd[ROUNDS - (t / 4)][t % 4] = tk[j];
            }
        }
        for (int r = 1; r < ROUNDS; r++) {
	    // inverse MixColumn where needed
            for (j = 0; j < BC; j++) {
                tt = Kd[r][j];
                Kd[r][j] = U1[(tt >>> 24) & 0xFF] ^
                           U2[(tt >>> 16) & 0xFF] ^
                           U3[(tt >>>  8) & 0xFF] ^
                           U4[ tt         & 0xFF];
            }
	}
        // assemble the encryption (Ke) and decryption (Kd) round keys into
        // one sessionKey object
        Object[] result = new Object[] {Ke, Kd};
        return result;
    }


    /**
     * Return The number of rounds for a given Rijndael keysize.
     *
     * @param keySize  The size of the user key material in bytes.
     *                 MUST be one of (16, 24, 32).
     * @return         The number of rounds.
     */
    private static int getRounds(int keySize) {
        return (keySize >> 2) + 6;
    }
}