package doing;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import junit.framework.Assert;

import org.junit.Test;
import org.pu.test.base.TestBase;
import org.pu.utils.Constants;
import org.pu.utils.EscapeChars;
import org.pu.utils.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shang Pu
 * @version Date: Apr 15, 2012 11:08:53 AM
 */
public class TestClass extends TestBase {
	private final Logger log = LoggerFactory.getLogger(TestClass.class);
	String code;

	// TODO: take a look PropertyDescriptor
//	@Test(expected = NullPointerException.class)
	@SuppressWarnings("null")
	public void testNullList() {
		List<String> list = null;
		for (String s : list) {
			log.info("s = {}", s);
		}
	}
	
	@Test
	public void testlog() {
		log.error("ACL {} doesn't exist", "aclName" );
		String path = getClass().getResource("/").getPath();
		///C:/sp/Dropbox/base/testProject/target/test-classes/
		log.info("path = {}", getClass().getResource("/").getPath());
		log.info("path = {}", getClass().getResource("/").getPath());
		
		getClass().getResourceAsStream("");
		
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
	
	
	public void testEscape() {
		String str = "the data might contain & or ! or % or ' or # etc";
		String escapedXml3 = org.apache.commons.lang3.StringEscapeUtils.escapeXml(str);
		String escapedXml = org.apache.commons.lang.StringEscapeUtils.escapeXml(str);
		log.info("escapedXml3 = {}", escapedXml3);
		log.info("escapedXml = {}", escapedXml);
		log.info("EscapeChars.forHTML = {}", EscapeChars.forHTML(str));
	}
	
	public void testSplit() {
		String attrValues = "5 mg�10 mg�20 mg�40 mg";
		String appendVal = "";
		while (attrValues != null) {
      String attrValue;
			if (attrValues.indexOf(Constants.VALUE_DELIMITER) >= 0) {
          attrValue = attrValues.substring(0, attrValues.indexOf(Constants.VALUE_DELIMITER));
          attrValues = attrValues.substring(attrValues.indexOf(Constants.VALUE_DELIMITER) + 1);
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
	
	public void testInteger() {
		int num= 6553800;
		log.info("int = {}", num);
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
	
	public void testSubstring() {
		String str = null;
		log.info("str = {}", str);//print str = null
		String message = "<h2>HTTP ERROR: 500</h2><pre>Could not find link neurontin-03.jpg</pre>";
		String start = "<pre>";
		message = message.substring(message.indexOf(start) + start.length(), message.indexOf("</pre>"));
		log.info("message = {}", message);
	}
	
	public void testTempFolder() throws IOException{
		File temp = File.createTempFile("test","");
		temp.delete();
		temp.mkdir();
		log.info("temp.getAbsolutePath() = {}", temp.getAbsolutePath());
	}
	
	public void testString2() {
		String docType = "pfe_sop";
				
		String r_object_id  = "10101";
		String dql = new StringBuilder("select r_object_id from ")
		.append(docType).append(" (all) where r_object_id='").append(r_object_id)
		.append("' and pfe_xm_p_effective_date is not nulldate and Date(now)>=pfe_xm_p_effective_date and r_lock_owner is nullstring and xm_status = 'Approved' and r_policy_id = (select r_object_id from dm_policy where object_name = 'pfe_l_lifecycle_1')")
		.toString();
		log.info("dql = {}", dql);
		
		docType = "gtc_xm_prescribing_info";
		String dqlLc4 = new StringBuilder("select r_object_id from ")
		.append(docType).append(" (all) where r_object_id='").append(r_object_id)
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
					.append(
							" (all) where Date(now)>=pfe_xm_p_effective_date and pfe_xm_p_effective_date is not nulldate and r_lock_owner is nullstring and ((r_policy_id = '")
					.append(lifecycle1_id)
					.append("' and xm_status='Approved') or (r_policy_id = '")

					// c. make effective:
					// i. Submission Status is “No Notification/Submission Required”
					// ii. Submission Status is “Not Subject to Regulation”
					// iii. Submission Status is “HA Notify”
					// iv. Submission Status is “HA Submission” and HA Response is “HA Approved”
					.append(lifecycle4_id)
					.append(
							"' and xm_status='Internally Approved' and (ha_submission_status in ('No Notification/Submission Required','Not Subject to Regulation','HA Notify') or (ha_submission_status = 'HA Submission' and ha_response = 'HA Approved'))))")
					.toString();
			log.info("queryStr = {}", queryStr);

		}
	}

	public void testAudio() throws IOException, LineUnavailableException,
			UnsupportedAudioFileException {
		URL url = new URL("http://pscode.org/media/leftright.wav");
		Clip clip = AudioSystem.getClip();
		// getAudioInputStream() also accepts a File or InputStream
		AudioInputStream ais = AudioSystem.getAudioInputStream(url);
		clip.open(ais);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// A GUI element to prevent the Clip's daemon Thread
				// from terminating at the end of the main()
				JOptionPane.showMessageDialog(null, "Close to exit!");
			}
		});
	}

	public void testUrlOpenStream() {
		URL url;
		try {
			// url = new URL("http://www.baidu.com");
			url = new URL("http://feeds2.feedburner.com/programthink");
			// url = new URL("http://www.tullyrankin.com/feed");
			InputStream openStream = url.openStream();
			String s = IoUtils.getString(openStream);
			s = s.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&");
			log.info("s = {}", s);
		} catch (Exception e) {
			log.error("Exception in TestClass.testUrl()", e);
		}
	}

	public void testEncode() {
		String code = System.getProperty("sun.io.unicode.encoding");
		log.info("code = {}", code);
	}

	public void testRename() {
		List<String> names = Arrays.asList("a", "a", "b", "c", "a", "d", "b", "b");
		log.info("names = {}", names);
		log.info("renames = {}", rename(names));
	}

	private List<String> rename(List<String> names) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < names.size(); i++) {
			String name = names.get(i);
			if (!map.containsKey(name)) {
				map.put(name, 1);
				String newName = name + "_" + 1;
				names.set(i, newName);
			} else {
				int count = map.get(name);
				int value = count + 1;
				map.put(name, value);
				String newName = name + "_" + value;
				names.set(i, newName);
			}
		}
		return names;
	}

	public void testFilenameFilter() throws Exception {
		File file = new File("a");
		String[] names = file.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".java");
			}
		});
		log.info("names = " + names);
	}

}
