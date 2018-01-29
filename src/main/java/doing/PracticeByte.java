package doing;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;

/**
 * http://www.tutorialspoint.com/java/java_basic_operators.htm
 * Assume integer variable A holds 10 and variable B holds 20, then:
 * <pre>
 * SR.NO 	Operator and Description
 * 
 * 1 	& (bitwise and)
 * Binary AND Operator copies a bit to the result if it exists in both operands.
 * Example: (A & B) will give 12 which is 0000 1100
 * 
 * 2 	| (bitwise or)
 * Binary OR Operator copies a bit if it exists in either operand.
 * Example: (A | B) will give 61 which is 0011 1101
 * 
 * 3 	^ (bitwise XOR)
 * Binary XOR Operator copies the bit if it is set in one operand but not both.
 * Example: (A ^ B) will give 49 which is 0011 0001
 * 
 * 4 	~ (bitwise compliment)
 * Binary Ones Complement Operator is unary and has the effect of 'flipping' bits.
 * Example: (~A ) will give -61 which is 1100 0011 in 2's complement form due to a signed binary number.
 * 
 * 5 	<< (left shift)
 * Binary Left Shift Operator. The left operands value is moved left by the number of bits specified by the right operand
 * Example: A << 2 will give 240 which is 1111 0000
 * 
 * 6 	>> (right shift)
 * Binary Right Shift Operator. The left operands value is moved right by the number of bits specified by the right operand.
 * Example: A >> 2 will give 15 which is 1111
 * 
 * 7 	>>> (zero fill right shift)
 * Shift right zero fill operator. The left operands value is moved right by the number of bits specified by the right operand and shifted values are filled up with zeros.
 * Example: A >>>2 will give 15 which is 0000 1111
 * 
 * 
 * 
 * Category  	Operator  	Associativity 
 * Postfix  	() [] . (dot operator) 	Left toright 
 * Unary  	++ - - ! ~ 	Right to left 
 * Multiplicative   	* / %  	Left to right 
 * Additive   	+ -  	Left to right 
 * Shift   	>> >>> <<   	Left to right 
 * Relational   	> >= < <=   	Left to right 
 * Equality   	== !=  	Left to right 
 * Bitwise AND  	&  	Left to right 
 * Bitwise XOR  	^  	Left to right 
 * Bitwise OR  	|  	Left to right 
 * Logical AND  	&&  	Left to right 
 * Logical OR  	||  	Left to right 
 * Conditional  	?:  	Right to left 
 * Assignment  	= += -= *= /= %= >>= <<= &= ^= |=  	Right to left 
 * Comma  	,  	Left to right
 * </pre>
 * 
 * @author Shang Pu
 * @version Date: Dec 14, 2015 5:15:51 PM
 */

public class PracticeByte {
	private static final Logger log = LoggerFactory.getLogger(PracticeByte.class);

    /**
     * 按位与，两1得1，遇0得0
     * 
     * 按位或，两0得0，遇1得1
     * 
     * 按位异或，不同得1，相同得0
     * 
     * <a href=
     * "https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op3.html">Bitwise
     * and Bit Shift Operators</a>
     * 
     * The unary bitwise complement operator "~" inverts a bit pattern;
     * 
     * The bitwise & operator performs a bitwise AND operation.
     * 
     * The bitwise | operator performs a bitwise inclusive OR operation.
     * 
     * The bitwise ^ operator performs a bitwise exclusive OR operation.
     * 
     * 
     * https://www.cnblogs.com/dongpo888/archive/2011/07/13/2105001.html
     * 
     * m<<n 即在数字没有溢出的前提下，对于正数和负数，左移n位都相当于m乘以2的n次方.
     * 
     * m>>n 即相当于m除以2的n次方，得到的为整数时，即为结果。如果结果为小数，此时会出现两种情况：
     * 
     * (1)如果m为正数，得到的商会无条件 的舍弃小数位；
     * 
     * (2)如果m为负数，舍弃小数部分，然后把整数部分加+1得到位移后的值。
     */
    @Test
    public void bitwise() {
        /* 1 = 001 */
        /* 2 = 010 */
        /* 3 = 011 */
        /* 4 = 100 */
        /* 5 = 101 */
        /* 6 = 110 */
        /* 7 = 111 */
        Assert.assertEquals(5 | 3, 7);

        Assert.assertEquals(3 & 1, 1);
        Assert.assertEquals(3 & 2, 2);
        Assert.assertEquals(3 & 4, 0);

        Assert.assertEquals(5 & 1, 1);
        Assert.assertEquals(5 & 4, 4);
        Assert.assertEquals(5 & 2, 0);

        Assert.assertEquals(7 & 1, 1);
        Assert.assertEquals(7 & 2, 2);
        Assert.assertEquals(7 & 4, 4);

        /* 60 = 0011 1100 */
        /* 13 = 0000 1101 */
        Assert.assertEquals(60 & 13, 12); /* 12 = 0000 1100 */
        Assert.assertEquals(60 | 13, 61); /* 61 = 0011 1101 */
        Assert.assertEquals(60 ^ 13, 49); /* 49 = 0011 0001 */
        Assert.assertEquals(~60, -61); /*-61 = 1100 0011 */
        Assert.assertEquals(60 << 2, 240); /* 240 = 1111 0000 */
        Assert.assertEquals(60 >> 2, 15); /* 15 = 1111 */
        Assert.assertEquals(60 >>> 2, 15); /* 15 = 0000 1111 */
    }

	@Test
	public void testByteOR() {
		byte serializationId = (byte) (FLAG_REQUEST | ID);
		log.info("serializationId[{}]", serializationId);
		Assert.assertEquals(-126, serializationId);

		byte twoWay = (byte) (serializationId | FLAG_TWOWAY);
		log.info("twoWay[{}]", twoWay);
		Assert.assertEquals(-62, twoWay);

		byte event = (byte) (twoWay | FLAG_EVENT);
		log.info("event[{}]", event);
		Assert.assertEquals(-30, event);
	}

	@Test
	public void testByte() {
		byte[] header = new byte[HEADER_LENGTH];
		log.info("header[{}]", header);

		// set magic number.
		Bytes.short2bytes(MAGIC, header);
		log.info("header[{}]", header);

		// set request and serialization flag.
		header[2] = (byte) (FLAG_REQUEST | ID);
		log.info("header[{}]", header);

		header[2] |= FLAG_TWOWAY;
		log.info("header[{}]", header);
		header[2] |= FLAG_EVENT;
		log.info("header[{}]", header);
	}

	// header length.
	protected static final int HEADER_LENGTH = 16;

	// magic header.
	protected static final short MAGIC = (short) 0xdabb;

	protected static final byte MAGIC_HIGH = Bytes.short2bytes(MAGIC)[0];

	protected static final byte MAGIC_LOW = Bytes.short2bytes(MAGIC)[1];

	// message flag.
	protected static final byte FLAG_REQUEST = (byte) 0x80;

	protected static final byte FLAG_TWOWAY = (byte) 0x40;

	protected static final byte FLAG_EVENT = (byte) 0x20;

	protected static final int SERIALIZATION_MASK = 0x1f;
	public static final byte ID = 2;

}
