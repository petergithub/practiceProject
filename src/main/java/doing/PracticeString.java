package doing;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.pu.test.base.TestBase;
import org.pu.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version Date: Jan 14, 2014 6:45:48 PM
 * @author Shang Pu
 */
public class PracticeString extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(PracticeString.class);

	/**
	 * trim方法一般用来去除空格，但是根据JDK API的说明，该方法并不仅仅是去除空格，它能够去除从编码’\u0000′ 至 ‘ ′ 的所有字符。
	 * 回车换行也在这20个字符之中
	 */
	@Test
	public void trimString() {
		Character[] chars = new Character[20];
		chars[0] = '\u0000';
		chars[1] = '\u0001';
		chars[2] = '\u0002';
		chars[3] = '\u0003';
		chars[4] = '\u0004';
		chars[5] = '\u0005';
		chars[6] = '\u0006';
		chars[7] = '\u0007';
		chars[8] = '\u0008';
		chars[9] = '\u0009';
		chars[10] = '\u0010';
		chars[11] = '\u0012';
		chars[12] = '\u0013';
		chars[13] = '\u0014';
		chars[14] = '\u0015';
		chars[15] = '\u0016';
		chars[16] = '\u0017';
		chars[17] = '\u0018';
		chars[18] = '\u0019';
		chars[19] = '\u0020';
		List<Character> charList = Arrays.asList(chars);
		log.info("charList[{}]", charList);

		for (int i = 0; i < chars.length; i++) {
			System.out.print("(" + i + ")" + chars[i] + "    ");
			if (i != 0 && i % 5 == 0) {
				System.out.println();
			}
		}

		char[] charsTrim = { 'a', 'b', 'c', '\r', '\n' };
		System.out.println(charsTrim.length);
		String str = new String(charsTrim);
		System.out.println(str.length());
		String newStr = str.trim();
		System.out.println(newStr.length());
	}

	public void testNullString() {
		String nullStr = null;
		Assert.assertNull(nullStr);
		log.info("null = {}", nullStr);
		nullStr = "null";
		assertEquals("null", nullStr);
		log.info("nullStr = {}", nullStr);

		try {
			byte[] bytes = null;
			nullStr = new String(bytes);
		} catch (NullPointerException e) {
			log.info("Expected NullPointerException");
		}
	}

	public void testListString() {
		List<String> gpseqnum = Arrays.asList("a", "c", "b");
		log.info("gpseqnum = {}", gpseqnum);
		if (gpseqnum.size() == 0)
			return;
		String gpseqnumQuery = "'";
		for (String s : gpseqnum) {
			gpseqnumQuery = gpseqnumQuery + s.trim() + "','";
		}
		gpseqnumQuery = gpseqnumQuery.substring(0, gpseqnumQuery.lastIndexOf(",'"));
		log.info("gpseqnumQuery = {}", gpseqnumQuery);
	}

	public void testSplitLengthString() {
		String[] array = ", ".split(",");
		Assert.assertEquals(2, array.length);
		Assert.assertEquals("", array[0]);
		Assert.assertEquals(" ", array[1]);

		String[] array2 = ",".split(",");
		Assert.assertEquals(0, array2.length);
	}

	public void testSplitEscapeString() {
		String str = "a|cdef";
		String[] splitWithEscape = str.split("\\|");
		printArray(splitWithEscape);
		Assert.assertEquals("a", splitWithEscape[0]);
		Assert.assertEquals("cdef", splitWithEscape[1]);

		// split to char by char
		String[] splitWithoutEscape = str.split("|");
		printArray(splitWithoutEscape);
		Assert.assertEquals("", splitWithoutEscape[0]);
		Assert.assertEquals("a", splitWithoutEscape[1]);
		Assert.assertEquals("|", splitWithoutEscape[2]);
		Assert.assertEquals("c", splitWithoutEscape[3]);
	}

	public void testSubstring() {
		String str = null;
		log.info("str = {}", str);// print str = null
		String message = "<h2>HTTP ERROR: 500</h2><pre>Could not find link neurontin-03.jpg</pre>";
		String start = "<pre>";
		message = message.substring(message.indexOf(start) + start.length(),
				message.indexOf("</pre>"));
		log.info("message = {}", message);
	}

	public void testSubstring2() {
		String dql = "select gpseqnum, gpseqnum, compound, generic_name, trade_name from dm_dbo.pfe_t_compound_data where (lower(trade_name) like '%${filter}%' or lower(compound) like '%${filter}%' or lower(generic_name) like '%${filter}%') and active_flag='T' order by compound, trade_name, generic_name";
		String orderByPart = "";
		int orderByIndex = dql.toLowerCase().indexOf(" order by");
		if (orderByIndex > -1) {
			orderByPart = dql.substring(orderByIndex);
			dql = dql.substring(0, orderByIndex);
		}
		log.info("orderByPart = {}", orderByPart);
	}

	public void testSplit() {
		String attrValues = "5 mg�10 mg�20 mg�40 mg";
		String appendVal = "";
		while (attrValues != null) {
			String attrValue;
			if (attrValues.indexOf(Constants.VALUE_DELIMITER) >= 0) {
				attrValue = attrValues.substring(0, attrValues.indexOf(Constants.VALUE_DELIMITER));
				attrValues = attrValues
						.substring(attrValues.indexOf(Constants.VALUE_DELIMITER) + 1);
			} else {
				attrValue = attrValues;
				attrValues = null;
			}
			if (!((attrValue.equalsIgnoreCase("null")) || (attrValue.equalsIgnoreCase("nulldate")))) {
				appendVal = "append,id,attrName," + attrValue;
			}
			log.info("appendVal = {}", appendVal);
		}
	}

	public void testStringa() {
		String src = "http://gdms5dev.pfizer.com/gdms/index.jsp";
		String target = "http://gdms5dev.pfizer.com/gdms/drl/objectId/";
		String str = src.substring(0, src.indexOf("index.jsp")) + "drl/objectId/";
		log.info("str = {}", str);
		Assert.assertEquals(target, str);
		String msg = "[DM_USER_E_CANT_FIND_USER]error:  \"Cannot find user Watters, John.\"";
		String pre = "[DM_USER_E_CANT_FIND_USER]error:  \"Cannot find user ";
		String user = msg.substring(msg.indexOf(pre) + pre.length());

		user = user.substring(0, user.indexOf(".\""));
		log.info("user = {}", user);
	}

	public void testString2() {
		String docType = "pfe_sop";

		String r_object_id = "10101";
		String dql = new StringBuilder("select r_object_id from ")
				.append(docType)
				.append(" (all) where r_object_id='")
				.append(r_object_id)
				.append("' and pfe_xm_p_effective_date is not nulldate and Date(now)>=pfe_xm_p_effective_date and r_lock_owner is nullstring and xm_status = 'Approved' and r_policy_id = (select r_object_id from dm_policy where object_name = 'pfe_l_lifecycle_1')")
				.toString();
		log.info("dql = {}", dql);

		docType = "gtc_xm_prescribing_info";
		String dqlLc4 = new StringBuilder("select r_object_id from ")
				.append(docType)
				.append(" (all) where r_object_id='")
				.append(r_object_id)
				.append("' and pfe_xm_p_effective_date is not nulldate and Date(now)>=pfe_xm_p_effective_date and r_lock_owner is nullstring and r_policy_id = (select r_object_id from dm_policy where object_name = 'pfe_l_lifecycle_4') and xm_status='Internally Approved' and (ha_submission_status in ('No Notification/Submission Required','Not Subject to Regulation','HA Notify') or (ha_submission_status = 'HA Submission' and ha_response = 'HA Approved'))")
				.toString();

		log.info("dqlLc4 = {}", dqlLc4);
	}

	public void testString() {
		String lifecycle1_id = "4601d7f980005eaf";
		String lifecycle4_id = "4601d7f980005eb7";
		List<String> collection = Arrays.asList("pfe_sop", "gtc_xm_prescribing_info");
		for (Iterator<String> iterator = collection.iterator(); iterator.hasNext();) {
			String docType = (String) iterator.next();
			String queryStr = new StringBuilder("select r_object_id from ")
					.append(docType)
					.append(" (all) where Date(now)>=pfe_xm_p_effective_date and pfe_xm_p_effective_date is not nulldate and r_lock_owner is nullstring and ((r_policy_id = '")
					.append(lifecycle1_id)
					.append("' and xm_status='Approved') or (r_policy_id = '")

					// c. make effective:
					// i. Submission Status is “No Notification/Submission
					// Required”
					// ii. Submission Status is “Not Subject to Regulation”
					// iii. Submission Status is “HA Notify”
					// iv. Submission Status is “HA Submission” and HA Response
					// is “HA Approved”
					.append(lifecycle4_id)
					.append("' and xm_status='Internally Approved' and (ha_submission_status in ('No Notification/Submission Required','Not Subject to Regulation','HA Notify') or (ha_submission_status = 'HA Submission' and ha_response = 'HA Approved'))))")
					.toString();
			log.info("queryStr = {}", queryStr);

		}
	}
}
