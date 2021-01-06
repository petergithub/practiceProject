package org.pu.utils;

import junit.framework.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DigitUtilTest {

    @Test
    public void parseDigits() {
        assertEquals(100, DigitUtil.parseDigits("一百").intValue());
        assertEquals(3500, DigitUtil.parseDigits("三千五").intValue());
        assertEquals(23530, DigitUtil.parseDigits("二万三千五百三").intValue());
        assertEquals(3400, DigitUtil.parseDigits("3千4百").intValue());
        assertEquals(DigitUtil.parseDigits("零").intValue(), 0);
        assertEquals(DigitUtil.parseDigits("一").intValue(), 1);
        assertEquals(DigitUtil.parseDigits("十四").intValue(), 14);
        assertEquals(DigitUtil.parseDigits("二十二").intValue(), 22);
        assertEquals(DigitUtil.parseDigits("一百零一").intValue(), 101);
        assertEquals(DigitUtil.parseDigits("一千零一十").intValue(), 1010);
        assertEquals(DigitUtil.parseDigits("三万九千八百五十七").intValue(), 39857);
        assertEquals(DigitUtil.parseDigits("二十万").intValue(), 200000);
        assertEquals(DigitUtil.parseDigits("九千七百零六万五百零一").intValue(), 97060501);
    }

    @Test
    public void isDigits() {
    }
}