package doing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.aspectj.util.FileUtil;
import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.core.vcard.VCard;
import net.glxn.qrgen.javase.QRCode;

/**
 * @author Shang Pu
 * @version Date: Jul 2, 2016 11:02:00 AM
 */

public class PracticeQrCode extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(PracticeQrCode.class);

	@Test
	public void testQrGenFile() throws IOException {
		String url = "testScanString";
	    String qrName = "/tmp/testqr.jpg";
		File file = createQrGenFile(url); 
		
		File qrFile = new File(qrName);
		FileUtil.copyFile(file, qrFile);
	}
	
	public void testQrGen() throws IOException {
		String url = "http://testQrUrl.com";
	    String qrName = "/tmp/testqr.jpg";
		
	    ByteArrayOutputStream qrOut = createQrGen(url);
	    File qrFile = new File(qrName);
		OutputStream os = new FileOutputStream(qrFile);
	    os.write(qrOut.toByteArray());
	    os.flush();
	    os.close();
		log.info("qrFile[{}]", qrFile);
	}
	
	public static File createQrGenFile(String url) throws IOException {
		// 如果有中文，可使用withCharset("UTF-8")方法

		// 设置二维码url链接，图片宽度250*250，JPG类型
		return QRCode.from(url).withSize(250, 250).to(ImageType.JPG).file();
	}

	public static ByteArrayOutputStream createQrGen(String url) throws IOException {
		// 如果有中文，可使用withCharset("UTF-8")方法

		// 设置二维码url链接，图片宽度250*250，JPG类型
		return QRCode.from(url).withSize(250, 250).to(ImageType.JPG).stream();
	}

	public void testGithub() {

		// get QR file from text using defaults
		File file = QRCode.from("Hello World").file();

		// get QR stream from text using defaults
		ByteArrayOutputStream stream = QRCode.from("Hello World").stream();

		// override the image type to be JPG
		QRCode.from("Hello World").to(ImageType.JPG).file();
		QRCode.from("Hello World").to(ImageType.JPG).stream();

		// override image size to be 250x250
		QRCode.from("Hello World").withSize(250, 250).file();
		QRCode.from("Hello World").withSize(250, 250).stream();

		// override size and image type
		QRCode.from("Hello World").to(ImageType.GIF).withSize(250, 250).file();
		QRCode.from("Hello World").to(ImageType.GIF).withSize(250, 250).stream();

		// override default colors (black on white)
		// notice that the color format is "0x(alpha: 1 byte)(RGB: 3 bytes)"
		// so in the example below it's red for foreground and yellowish for
		// background, both 100% alpha (FF).
//		QRCode.from("Hello World").withColor(0xFFFF0000, 0xFFFFFFAA).file();

		// supply own outputstream
//		QRCode.from("Hello World").to(ImageType.PNG).writeTo(outputStream);

		// supply own file name
		QRCode.from("Hello World").file("QRCode");

		// supply charset hint to ZXING
		QRCode.from("Hello World").withCharset("UTF-8");

		// supply error correction level hint to ZXING
		QRCode.from("Hello World").withErrorCorrection(ErrorCorrectionLevel.L);

		// supply any hint to ZXING
		QRCode.from("Hello World").withHint(EncodeHintType.CHARACTER_SET, "UTF-8");

		// encode contact data as vcard using defaults
		VCard johnDoe = new VCard("John Doe").setEmail("john.doe@example.org")
				.setAddress("John Doe Street 1, 5678 Doestown").setTitle("Mister")
				.setCompany("John Doe Inc.").setPhoneNumber("1234").setWebsite("www.example.org");
		QRCode.from(johnDoe).file();

		// if using special characters don't forget to supply the encoding
		VCard johnSpecial = new VCard("Jöhn Dɵe").setAddress("ëåäöƞ Sträät 1, 1234 Döestüwn");
		QRCode.from(johnSpecial).withCharset("UTF-8").file();
	}

}
