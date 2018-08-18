package demo.java8;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;

/**
 * @author http://www.importnew.com/11908.html
 *
 */
public class PracticeJdk8 extends TestBase {
    private static final Logger log = LoggerFactory.getLogger(PracticeJdk8.class);

    @Test
    public void testSet() {
        Set<String> strSet1 = Stream.of("A", "B", "C", "D")
                .collect(Collectors.toCollection(HashSet::new));
//        Using Java 8 (Unmodifiable Sets)
//        Using Collections.unmodifiableSet - We can use Collections.unmodifiableSet as:
        Set<String> strSet4 = Collections.unmodifiableSet(strSet1);
    }
    
    @Test
    public void testDate() {
        // Clock可以替换System.currentTimeMillis()与TimeZone.getDefault()。
        // Get the system clock as UTC offset
        final Clock clock = Clock.systemUTC();
        System.out.println(clock.instant()); // 2014-04-12T15:19:29.282Z
        System.out.println(clock.millis()); // 1397315969360

        // Get the local date and local time
        final LocalDate date = LocalDate.now(); // 2014-04-12
        final LocalDate dateFromClock = LocalDate.now(clock); // 2014-04-12
        System.out.println(date);
        System.out.println(dateFromClock);

        // Get the local date and local time
        final LocalTime time = LocalTime.now(); // 11:25:54.568
        final LocalTime timeFromClock = LocalTime.now(clock);// 15:25:54.568
        System.out.println(time);
        System.out.println(timeFromClock);

        // Get the local date/time
        final LocalDateTime datetime = LocalDateTime.now(); // 2014-04-12T11:37:52.309
        final LocalDateTime datetimeFromClock = LocalDateTime.now(clock); // 2014-04-12T15:37:52.309
        System.out.println(datetime);
        System.out.println(datetimeFromClock);

        // Get the zoned date/time
        final ZonedDateTime zonedDatetime = ZonedDateTime.now(); // 2014-04-12T11:47:01.017-04:00[America/New_York]
        final ZonedDateTime zonedDatetimeFromClock = ZonedDateTime.now(clock);// 2014-04-12T15:47:01.017Z
        final ZonedDateTime zonedDatetimeFromZone = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));// 2014-04-12T08:47:01.017-07:00[America/Los_Angeles]
        System.out.println(zonedDatetime);
        System.out.println(zonedDatetimeFromClock);
        System.out.println(zonedDatetimeFromZone);

        // Get duration between two dates
        final LocalDateTime from = LocalDateTime.of(2014, Month.APRIL, 16, 0, 0, 0);
        final LocalDateTime to = LocalDateTime.of(2015, Month.APRIL, 16, 23, 59, 59);

        final Duration duration = Duration.between(from, to);
        System.out.println("Duration in days: " + duration.toDays());// Duration in days: 365
        System.out.println("Duration in hours: " + duration.toHours()); // Duration in hours: 8783

        LocalDate today = LocalDate.parse("2014-02-27");
        log.info("today: {}", today);
        // or this method
        LocalDate bday = LocalDate.of(2014, 3, 18);
        log.info("bday: {}", bday);
        
    }

    @Test
    public void testDate_add_to_date() {
//        Adding To Dates
        LocalDate today = LocalDate.parse("2014-02-27");
        LocalDate oneMonthFromNow = today.plusDays(30);
        assertTrue(oneMonthFromNow.isEqual(LocalDate.parse("2014-03-29")));

        LocalDate nextMonth = today.plusMonths(1);
        assertTrue(nextMonth.isEqual(LocalDate.parse("2014-03-27")));

        LocalDate future = today.plus(4, ChronoUnit.WEEKS);
        assertTrue(future.isEqual(LocalDate.parse("2014-03-27")));
    }

    @Test
    public void testDate_subtract_from_date() {
        // Subtracting From Dates
        LocalDate today = LocalDate.parse("2014-02-27");
        assertThat(today.minusWeeks(1).toString(), is("2014-02-20"));
        assertThat(today.minusMonths(2).toString(), is("2013-12-27"));

        assertThat(today.minusYears(4).toString(), is("2010-02-27"));

        Period twoMonths = Period.ofMonths(2);
        assertThat(today.minus(twoMonths).toString(), is("2013-12-27"));
    }
    
    @Test
    public void testDate_get_days_between_dates() {
        LocalDate today = LocalDate.parse("2014-02-27");
        LocalDate vacationStart = LocalDate.parse("2014-07-04");
        Period timeUntilVacation = today.until(vacationStart);

        assertThat(timeUntilVacation.getMonths(), is(4));

        assertThat(timeUntilVacation.getDays(), is(7));

        assertThat(today.until(vacationStart, ChronoUnit.DAYS), is(127L));

        LocalDate libraryBookDue = LocalDate.parse("2000-03-18");

        assertThat(today.until(libraryBookDue).isNegative(), is(true));

        assertThat(today.until(libraryBookDue, ChronoUnit.DAYS), is(-5094L));

        LocalDate christmas = LocalDate.parse("2014-12-25");
        assertThat(today.until(christmas, ChronoUnit.DAYS), is(301L));

    }

    @Test
    public void testLambda() {
        // 一个lambda可以由用逗号分隔的参数列表、–>符号与函数体三部分表示。例如：
        // 请注意参数e的类型是由编译器推测出来的
        Arrays.asList("a", "b", "d").forEach(e -> System.out.println(e));

        // 同时，你也可以通过把参数类型与参数包括在括号中的形式直接给出参数的类型：
        Arrays.asList("a", "b", "d").forEach((String e) -> System.out.println(e));

        // 在某些情况下lambda的函数体会更加复杂，这时可以把函数体放到在一对花括号中，就像在Java中定义普通函数一样。例如：
        Arrays.asList("a", "b", "d").forEach(e -> {
            System.out.print(e);
            System.out.print(e);
        });

        // Lambda可以引用类的成员变量与局部变量（如果这些变量不是final的话，它们会被隐含的转为final，这样效率更高）。例如，下面两个代码片段是等价的：
        String separatorA = ",";
        Arrays.asList("a", "b", "d").forEach((String e) -> System.out.print(e + separatorA));

        final String separatorB = ",";
        Arrays.asList("a", "b", "d").forEach((String e) -> System.out.print(e + separatorB));
    }

    /**
     * Optional实际上是个容器：它可以保存类型T的值，或者仅仅保存null。Optional提供很多有用的方法，这样我们就不用显式进行空值检测。
     * 更多详情请参考官方文档。
     */
    @Test
    public void testOptional() {
        // 如果Optional类的实例为非空值的话，isPresent()返回true，否从返回false。
        // 为了防止Optional为空值，orElseGet()方法通过回调函数来产生一个默认值。
        // map()函数对当前Optional的值进行转化，然后返回一个新的Optional实例。
        // orElse()方法和orElseGet()方法类似，但是orElse接受一个默认值而不是一个回调函数。下面是这个程序的输出：
        Optional<String> fullName = Optional.ofNullable(null);
        boolean presentFalse = fullName.isPresent();
        String fullName1 = fullName.orElseGet(() -> "[none]");
        String fullName2 = fullName.map(s -> "Hey " + s + "!").orElse("Hey Stranger!");

        System.out.println("Full Name is set? " + presentFalse);
        Assert.assertFalse(presentFalse);

        System.out.println("Full Name: " + fullName1);
        Assert.assertEquals(fullName1, "[none]");

        System.out.println(fullName2);
        Assert.assertEquals(fullName2, "Hey Stranger!");

        Optional<String> firstName = Optional.of("Tom");
        boolean presentTrue = firstName.isPresent();
        String firstName1 = firstName.orElseGet(() -> "[none]");
        String firstName2 = firstName.map(s -> "Hey " + s + "!").orElse("Hey Stranger!");

        System.out.println("First Name is set? " + presentTrue);
        Assert.assertTrue(presentTrue);
        System.out.println("First Name: " + firstName1);
        Assert.assertEquals(firstName1, "Tom");
        System.out.println(firstName2);
        Assert.assertEquals(firstName2, "Hey Tom!");
    }
    
    /**
     * Nashorn，一个新的JavaScript引擎随着Java 8一起公诸于世，它允许在JVM上开发运行某些JavaScript应用。Nashorn就是javax.script.ScriptEngine的另一种实现，并且它们俩遵循相同的规则，允许Java与JavaScript相互调用
     * @throws ScriptException
     */
    @Test
    public void testJavaScript() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName( "JavaScript" );
                 
        assertEquals(engine.getClass().getName(), "jdk.nashorn.api.scripting.NashornScriptEngine");
        assertEquals(engine.eval( "function f() { return 1; }; f() + 1;" ), 2.0);
    }
    
    @Test
    public void testBase64() {
        final String text = "Base64 finally in Java 8!";
         
        final String encoded = Base64
            .getEncoder()
            .encodeToString( text.getBytes( StandardCharsets.UTF_8 ) );
        assertEquals(encoded, "QmFzZTY0IGZpbmFsbHkgaW4gSmF2YSA4IQ==");
         
        final String decoded = new String( 
            Base64.getDecoder().decode( encoded ),
            StandardCharsets.UTF_8 );
        assertEquals(decoded, text);
    }
}
