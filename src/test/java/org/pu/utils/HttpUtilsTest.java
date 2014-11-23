package org.pu.utils;

import junit.framework.Assert;

import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.pu.utils.HttpUtils;
import org.pu.utils.HttpUtils.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @version Date: Feb 28, 2013 5:37:35 PM
 * @author Shang Pu
 */
public class HttpUtilsTest {
	private static final Logger log = LoggerFactory.getLogger(HttpUtilsTest.class);

	public void testJsFunction() {
		// md5Code
		String md5Code = HttpUtils.invokeJsFunction("abc", "ych2.js", "hex_md5");
		Assert.assertEquals(md5Code, "900150983cd24fb0d6963f7d28e17f72");
	}

	@Test
	public void testGetRandCodeOcr() {
		String urlImgCode = "http://114.242.121.99/tools/CreateCode.ashx?key=ImgCode&random=0.9671970325095187";
		String code = HttpUtils.getRandCodeOcr(new DefaultHttpClient(), urlImgCode, new Validation() {
			@Override
			public int getTimes() {
				return 5;
			}

			@Override
			public boolean isRandomCodeValid(String code) {
				return true;
			}
		});
		log.info("code = {}", code);
	}
}
