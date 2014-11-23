package doing.ocr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.pu.bean.OS;
import org.pu.utils.ImageIoHelper;
import org.pu.utils.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Ocr {
	private static final Logger log = LoggerFactory.getLogger(Ocr.class);
	private static final String tesseractPath = "resources/tesseract-ocr";

	// private static final String tesseractPath =
	// "C:/ProgramFiles/tesseract-ocr3.0.2";

	/**
	 * tesseract imagename outputbase [-l lang] [configfile s[[+|-]varfile]...]
	 * <p>
	 * return empty String if imageFile does not exits
	 */
	public static String tesseract(File imageFile) throws IOException {
		log.debug("Enter tesseract({})", imageFile);
		if (!imageFile.exists() || !new File(tesseractPath).exists()) {
			log.warn("Exit tesseract() {} or {} do not exist", imageFile, tesseractPath);
			return "";
		}
		String result = "";
		File tempImage = ImageIoHelper.createTempTifImage(imageFile);
		String output = tempImage.getParent() + File.separatorChar + "output";

		List<String> cmdArgs = new ArrayList<String>();
		if (OS.isLinux()) {
			cmdArgs.add("tesseract");
		} else {// XP or other
			cmdArgs.add(tesseractPath + File.separatorChar + "tesseract");
		}
		cmdArgs.add("");
		cmdArgs.add(output);
		cmdArgs.add("-l");
		cmdArgs.add("eng");
		cmdArgs.set(1, tempImage.getAbsolutePath());
		log.debug("cmdArgs = {}", cmdArgs);

		ProcessBuilder pb = new ProcessBuilder();
		pb.command(cmdArgs);
		pb.redirectErrorStream(true);
		Process process = pb.start();

		/*
		 * // 在Windows平台上，运行被调用程序的DOS窗口在程序执行完毕后往往并不会自动关闭， // 从而导致Java应用程序阻塞在waitfor()语句。导致该现象的一个可能的原因是，
		 * // 该可执行程序的标准输出比较多，而运行窗口的标准输出缓冲区不够大。 // 解决的办法是，利用Java中Process类提供的方法让Java虚拟机截获被调用程序的DOS运行窗口 //
		 * 的标准输出，在waitfor()命令之前读出窗口的标准输出缓冲区中的内容。 BufferedReader bufferedReader = new BufferedReader( new
		 * InputStreamReader(process.getInputStream())); while ((bufferedReader.readLine()) != null) ;
		 */

		int returnCode = -1;
		try {
			returnCode = process.waitFor();
		} catch (InterruptedException e) {
			log.error("InterruptedException in Ocr.tesseract()", e);
		}
		String outputTxt = output + ".txt";
		// delete outputTxt and tesseract log file
		new File(outputTxt).deleteOnExit();
		new File("tesseract.log").deleteOnExit();
		if (returnCode == 0) {
			result = IoUtils.getContent(outputTxt);
		} else {
			throw new IOException(getErrorMessage(returnCode));
		}

		log.debug("Exit tesseract() result={}", result);
		return result;
	}

	private static String getErrorMessage(int returnCode) {
		log.debug("Enter getErrorMessage({})", returnCode);
		String msg;
		switch (returnCode) {
			case 1:
				msg = "Errors accessing files. There may be spaces in your image's filename.";
				break;
			case 29:
				msg = "Cannot recognize the image or its selected region.";
				break;
			case 31:
				msg = "Unsupported image format.";
				break;
			default:
				msg = "Errors occurred.";
		}
		log.debug("Exit getErrorMessage() msg=", msg);
		return msg;
	}
}
