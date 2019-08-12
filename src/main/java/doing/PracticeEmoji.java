package doing;

import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;

/**
 * 
 * Unicode code converter https://r12a.github.io/apps/conversion/ 在线编码解码
 * http://bianma.911cha.com/ Emoji Unicode Tables
 * https://apps.timwhitlock.info/emoji/tables/unicode
 * 
 * @version Date: Jan 14, 2014 6:45:48 PM
 * @author Shang Pu
 */
public class PracticeEmoji extends TestBase {
    private static final Logger log = LoggerFactory.getLogger(PracticeEmoji.class);

    @Test
    public void testEmoji() throws Exception {
        final String SMILE = "😁"; // \uD83D\uDE01  \xF0\x9F\x98\x81
        String content = "test \uD83D\uDE01 test😁test"; // 一个 emoji 表情
        String emojiContent = "test 😁 test😁test";
        Assert.assertEquals(emojiContent, content);

        String filterContent = emojiFilter(content);
        Assert.assertEquals("test [[EMOJI:%F0%9F%98%81]] test[[EMOJI:%F0%9F%98%81]]test", filterContent);
        Assert.assertEquals(emojiContent, emojiRecovery(filterContent));
        
        byte[] bytes = new byte[] { (byte) 0xf0, (byte) 0x9f, (byte) 0x98, (byte) 0x81 };
        Assert.assertEquals(SMILE, new String(bytes, StandardCharsets.UTF_8.name()));
    }

    @Test
    public void emojiConvert() throws UnsupportedEncodingException {
        final String MAPLE_LEAF = "🍁"; // \uD83C\uDF41 U+1F341 \xF0\x9F\x8D\x81
        byte[] bytes = new byte[] { (byte) 0xf0, (byte) 0x9f, (byte) 0x8d, (byte) 0x81 };
        Assert.assertEquals(MAPLE_LEAF, new String(bytes, StandardCharsets.UTF_8.name()));
        Assert.assertEquals(MAPLE_LEAF, "\uD83C\uDF41");
        Assert.assertEquals(MAPLE_LEAF, new StringBuilder().appendCodePoint(0x1F341).toString());
        log.info("bytes: {}", MAPLE_LEAF.getBytes()); // [-16, -97, -115, -127]
        printBytes(MAPLE_LEAF.getBytes(), MAPLE_LEAF); // \xF0\x9F\x8D\x81

        final String MICROPHONE = "🎤"; /* \uD83C\uDFA4 U+1F3A4 F0 9F 8E A4 \xF0 \x9F \x8E \xA4 */
        Assert.assertEquals(MICROPHONE, "\uD83C\uDFA4");
        Assert.assertEquals(MICROPHONE, new StringBuilder().appendCodePoint(0x1f3a4).toString());

        final String HEADPHONE = "🎧"; /* \uD83C\uDFA7 U+1F3A7 */
        Assert.assertEquals(HEADPHONE, "\uD83C\uDFA7");
        Assert.assertEquals(HEADPHONE, new StringBuilder().appendCodePoint(0x1f3a7).toString());

        byte[] bytes2 = new byte[] { 0x6c, 0x69, 'b', '/', 0x62, 0x2f, 0x6d, 0x69, 'n', 'd', '/', 'm', 0x61, 'x', 0x2e,
                0x70, 'h', 0x70 };
        String str2 = new String(bytes2, StandardCharsets.UTF_8.name());
        log.info("str2: {}", str2);
    }

    public static void printBytes(byte[] array, String name) {
        for (int k = 0; k < array.length; k++) {
            log.info(name + "[" + k + "] = " + "0x" + UnicodeFormatter.byteToHex(array[k]));
        }
    }

    private static String emojiFilter(String str) {
        String patternString = "([\\x{10000}-\\x{10ffff}\ud800-\udfff])";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            try {
                matcher.appendReplacement(sb, "[[EMOJI:" + URLEncoder.encode(matcher.group(1), StandardCharsets.UTF_8.name()) + "]]");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    private static String emojiRecovery(String str) {
        String patternString = "\\[\\[EMOJI:(.*?)\\]\\]";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(str);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            try {
                matcher.appendReplacement(sb, URLDecoder.decode(matcher.group(1), StandardCharsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    /**
     * https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/i18n/text/examples/UnicodeFormatter.java
     */
    static class UnicodeFormatter {
        static public String byteToHex(byte b) {
            // Returns hex String representation of byte b
            char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
            char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
            return new String(array);
        }

        static public String charToHex(char c) {
            // Returns hex String representation of char c
            byte hi = (byte) (c >>> 8);
            byte lo = (byte) (c & 0xff);
            return byteToHex(hi) + byteToHex(lo);
        }

    }
}
