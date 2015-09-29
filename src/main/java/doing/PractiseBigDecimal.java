package doing;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import junit.framework.Assert;

import org.junit.Test;
import org.pu.test.base.TestBase;

/**
 * <pre>
 *     addition, summation: (augend) + (addend) = (summand) + (summand) = (sum, total)
 *     subtraction: (minuend) − (subtrahend) = (difference)
 *     multiplication: (multiplier) × (multiplicand) = (factor) × (factor) = (product)
 *     division: (dividend) ÷ (divisor) = (quotient), remainder left over if divisor does not 
 *     		divide dividend
 * </pre>
 * 
 * 
 * @author Shang Pu
 * @version Date: Sep 23, 2015 10:59:39 AM
 */

public class PractiseBigDecimal extends TestBase {

	public void testMathContext() {
		System.out.println(new BigDecimal("123.4", new MathContext(4, RoundingMode.HALF_UP)));// 123.4
		System.out.println(new BigDecimal("123.4", new MathContext(2, RoundingMode.HALF_UP)));// 1.2E+2
		System.out.println(new BigDecimal("123.4", new MathContext(2, RoundingMode.CEILING)));// 1.3E+2
		System.out.println(new BigDecimal("123.4", new MathContext(1, RoundingMode.CEILING)));// 2E+2

		MathContext mc = new MathContext(3, RoundingMode.HALF_UP);
		BigDecimal d1 = new BigDecimal(1234.4, mc);
		System.out.println(d1);// 1.23E+3

		BigDecimal d2 = new BigDecimal(0.000000454770054, mc);
		System.out.println(d2);// 4.55E-7

		BigDecimal d3 = new BigDecimal(0.001000045477, mc);
		System.out.println(d3); // 0.00100

		BigDecimal d4 = new BigDecimal(0.200000477, mc);
		System.out.println(d4); // 0.200

		BigDecimal d5 = new BigDecimal(0.000000004, mc);
		System.out.println(d5); // 4.00E-9

		mc = new MathContext(4, RoundingMode.HALF_UP);
		BigDecimal d6 = new BigDecimal("1", mc);
		System.out.println(d6); // 1
	}

	public void testMutiply() {
		BigDecimal multiplier = new BigDecimal(0.25);
		BigDecimal multiplicand = new BigDecimal(0.25);
		MathContext mc = new MathContext(4, RoundingMode.HALF_UP);
		BigDecimal multiplication = multiplier.multiply(multiplicand, mc);
		Assert.assertEquals(new BigDecimal(0.0625), multiplication);

		mc = new MathContext(3, RoundingMode.HALF_UP);
		BigDecimal multiplication2 = multiplier.multiply(multiplicand, mc);
		Assert.assertFalse(new BigDecimal("0.063").equals(multiplication2));
		BigDecimal scaled = multiplication2.setScale(3, BigDecimal.ROUND_HALF_UP);
		Assert.assertEquals(new BigDecimal("0.063"), scaled);

		scaled = multiplication2.setScale(2, BigDecimal.ROUND_HALF_UP);
		Assert.assertEquals(new BigDecimal("0.06"), scaled);

		scaled = multiplication2.setScale(1, BigDecimal.ROUND_HALF_UP);
		Assert.assertEquals(new BigDecimal("0.1"), scaled);
	}

	public void testRoundUp() {
		BigDecimal original = new BigDecimal("1.235");
		int scale = 2;
		BigDecimal scaled = original.setScale(scale, BigDecimal.ROUND_HALF_UP);
		String valStr = "1.24";
		double valDouble = 1.24;

		Assert.assertEquals(new BigDecimal(valStr), scaled);
		Assert.assertTrue(new BigDecimal(valStr).equals(scaled));
		Assert.assertFalse(new BigDecimal(valDouble).equals(scaled));

		scaled = original.setScale(1, BigDecimal.ROUND_HALF_UP);
		Assert.assertEquals(new BigDecimal("1.2"), scaled);
	}

	@Test
	public void testEquals() {
		BigDecimal expected = new BigDecimal("1.2");
		BigDecimal original = expected;
		Assert.assertEquals(expected, original);
		System.out.println(original);
		Assert.assertEquals(new BigDecimal(1.2), original);
	}

	/**
	 * A BigDecimal representing the number 0
	 */
	public static final BigDecimal DECIMAL_ZERO = new BigDecimal(BigInteger.ZERO);
	public static final RoundingMode DecimalFormat_RoundingMode = RoundingMode.HALF_UP;
	public static final MathContext mathContext = new MathContext(4, DecimalFormat_RoundingMode);

	public static BigDecimal roundFinalResult(BigDecimal original) {
		BigDecimal scaled = original.setScale(2, DecimalFormat_RoundingMode);
		return scaled;
	}

}
