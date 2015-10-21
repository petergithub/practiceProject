package doing;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shang Pu
 * @version Date: Sep 23, 2015 10:59:39 AM
 */

public class PracticeList extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(PracticeList.class);

	@Test
	public void testProcessListParameter() {
		List<String> list = new ArrayList<String>(Arrays.asList("1", "2", "3"));
		processListParameter(list);
		log.info("list = {}", list);
	}

	private void processListParameter(List<String> list) {
		list.add("added in processListParameter");
		log.debug("Exit. list[{}]", list);
	}

	public void testListCombineKeepOrder() {
		List<String> first = Arrays.asList("1", "2", "3");
		List<String> second = Arrays.asList("4", "5", "6");

		List<String> expected = Arrays.asList("1", "2", "3", "4", "5", "6");

		List<String> result = new ArrayList<String>();
		result.addAll(first);
		result.addAll(second);
		assertThat(result, is(expected));
	}

	public void testList() {
		// test list in order
		List<String> actual = Arrays.asList("1", "2", "3");
		List<String> expected = Arrays.asList("1", "2", "3");
		assertEquals(expected, actual);
		assertThat(actual, is(expected));

		// test list without order
		expected = Arrays.asList("1", "3", "2");
		assertThat(actual, is(not(expected)));
		assertThat("List equality without order", actual,
				IsIterableContainingInAnyOrder.containsInAnyOrder(expected.toArray()));

		List<Integer> yourList = Arrays.asList(1, 2, 3, 4);
		// Notice: make hamcrest-all.jar before junit in classpath
		assertThat(yourList, Matchers.hasItems(1, 2, 3));
		assertThat(yourList, not(Matchers.hasItems(1, 2, 3, 4, 5)));
	}

	public void testEmptyList() {
		List<String> emptyList = new ArrayList<>();
		log.info("emptyList = {}", emptyList);
		String str = emptyList.get(0);
		log.info("str = {}", str);
		System.out.println(str);
		Assert.assertNotNull(str);
	}

	// @Test(expected = NullPointerException.class)
	@SuppressWarnings("null")
	public void testNullList() {
		List<String> list = null;
		for (String s : list) {
			log.info("s = {}", s);
		}

		List<String> emptyList = new ArrayList<>();
		String str = emptyList.get(0);
		Assert.assertNotNull(str);
	}
}
