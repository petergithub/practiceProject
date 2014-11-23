package org.pu.utils;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.pu.utils.ImageIoHelper;

/**
 * @version Date: Feb 28, 2013 9:40:39 PM
 * @author Shang Pu
 */
public class ImageIoHelperTest {
	@Test
	public void testCreateTempTifImage() {
		File image = new File("haijia.gif");
		File temp = ImageIoHelper.createTempTifImage(image);
		Assert.assertTrue(temp.exists());
	}
}
