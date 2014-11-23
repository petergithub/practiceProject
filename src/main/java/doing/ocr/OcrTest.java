package doing.ocr;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.pu.utils.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @version Date: Feb 22, 2013 11:20:45 AM
 * @author Shang Pu
 */
public class OcrTest {
	private static final Logger log = LoggerFactory.getLogger(OcrTest.class);

	@Test
	public void testOcrGifAbsolutePath() {
		String imagePath = "C:\\cache\\haijia.gif";
		try {
			Ocr.tesseract(new File(imagePath));
		} catch (Exception e) {
			log.error("Exception in testOcrGifAbsolutePath", e);
		}
	}

	@Test
	public void testOcrGifRelativePath() {
		String imagePath = "haijia.gif";
		try {
			Ocr.tesseract(new File(imagePath));
		} catch (Exception e) {
			log.error("Exception in testOcrGifRelativePath", e);
		}
	}

	public void testRecognizeText() {
		List<String> results = new ArrayList<String>();
		String folderPath = "C:\\cache\\haijia\\rand";
		final String suffix = "gif";
		File[] files = new File(folderPath).listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().endsWith(suffix)) return true;
				return false;
			}
		});
		for (File gif : files) {
			try {
				results.add(Ocr.tesseract(gif).replaceFirst("\r\n", ""));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.info("results = {}", results);
	}

	int i = 0;

	public void testGetHaiJiaRandImg() {
		String imgCodeUrl = "http://114.242.121.99/tools/CreateCode.ashx?key=ImgCode&random=0.9671970325095187";
		DefaultHttpClient httpClient = new DefaultHttpClient();
		for (; i < 30; i++) {
			HttpGet get = new HttpGet(imgCodeUrl);
			try {
				HttpResponse response = httpClient.execute(get);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String picName = "c://cache//rand//" + i + ".gif";
					OutputStream outStream = new FileOutputStream(picName);
					InputStream inputStream = entity.getContent();
					IoUtils.copy(inputStream, outStream);
					outStream.close();
				}
			} catch (IOException e) {
				log.error("Exception in testGetRandImg", e);
				testGetHaiJiaRandImg();
			}
		}
	}
}
