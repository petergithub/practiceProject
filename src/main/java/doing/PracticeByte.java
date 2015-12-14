package doing;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://www.tutorialspoint.com/java/java_basic_operators.htm
 * 
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

	public static void main(String args[]) {
		int a = 60; /* 60 = 0011 1100 */
		int b = 13; /* 13 = 0000 1101 */
		int c = 0;

		c = a & b; /* 12 = 0000 1100 */
		System.out.println("a & b = " + c);

		c = a | b; /* 61 = 0011 1101 */
		System.out.println("a | b = " + c);

		c = a ^ b; /* 49 = 0011 0001 */
		System.out.println("a ^ b = " + c);

		c = ~a; /*-61 = 1100 0011 */
		System.out.println("~a = " + c);

		c = a << 2; /* 240 = 1111 0000 */
		System.out.println("a << 2 = " + c);

		c = a >> 2; /* 215 = 1111 */
		System.out.println("a >> 2  = " + c);

		c = a >>> 2; /* 215 = 0000 1111 */
		System.out.println("a >>> 2 = " + c);
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
