package doing.ocr.problc;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 最近看了看验证码的识别，先从最简单的做起吧(固定大小，固定位置，固定字体) 验证码识别基本分四步，图片预处理，分割，训练，识别 这是一个德克萨斯扑克的注册页面的验证码 1。图像的预处理
 * 这种直接根据亮度设个阈值处理就可以了 2。分割 这个验证码居然是固定位置的，分割相当简单，直接截取相应位置就可以了 3。训练
 * 直接拿几张图片，包含0-9，每个数字一个样本就可以了，将文件名对应相应的数字 4。识别 因为是固定大小，固定位置，识别也很简单。
 * 直接拿分割的图片跟这个十个图片一个像素一个像素的比，相同的点最多的就是结果。比如如果跟5.jpg最相似，那么识别的结果就是5。
 * http://blog.csdn.net/problc/article/details/5794460
 */
public class ImagePreProcess {
	private static final Logger log = LoggerFactory.getLogger(ImagePreProcess.class);

	@Test
	public void readImg() throws IOException {
		String text = getAllOcr("c://cache//5.jpg", "c://cache//");
		log.info(".jpg text = {}", text);
	}

	public void testRemoveBackground() {
		String picFile = "C:\\cache\\img\\0.jpg";
		try {
			BufferedImage img = removeBackgroud(picFile);
			ImageIO.write(img, "JPG", new File("c://cache//result.jpg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int isWhite(int colorInt) {
		Color color = new Color(colorInt);
		if (color.getRed() + color.getGreen() + color.getBlue() > 100) {
			return 1;
		}
		return 0;
	}

	public static int isBlack(int colorInt) {
		Color color = new Color(colorInt);
		if (color.getRed() + color.getGreen() + color.getBlue() <= 100) {
			return 1;
		}
		return 0;
	}

	public static BufferedImage removeBackgroud(String picFile) throws IOException {
		BufferedImage img = ImageIO.read(new File(picFile));
		int width = img.getWidth();
		int height = img.getHeight();
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				if (isWhite(img.getRGB(x, y)) == 1) {
					img.setRGB(x, y, Color.WHITE.getRGB());
				} else {
					img.setRGB(x, y, Color.BLACK.getRGB());
				}
			}
		}
		return img;
	}

	public static List<BufferedImage> splitImage(BufferedImage img) {
		List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
		subImgs.add(img.getSubimage(10, 6, 8, 10));
		subImgs.add(img.getSubimage(19, 6, 8, 10));
		subImgs.add(img.getSubimage(28, 6, 8, 10));
		subImgs.add(img.getSubimage(37, 6, 8, 10));
		return subImgs;
	}

	public static Map<BufferedImage, String> loadTrainData() throws IOException {
		Map<BufferedImage, String> map = new HashMap<BufferedImage, String>();
		File dir = new File("C:\\cache\\train");
		File[] files = dir.listFiles();
		for (File file : files) {
			map.put(ImageIO.read(file), file.getName().charAt(0) + "");
		}
		return map;
	}

	public static String getSingleCharOcr(BufferedImage img, Map<BufferedImage, String> map) {
		String result = "";
		int width = img.getWidth();
		int height = img.getHeight();
		int min = width * height;
		for (BufferedImage bi : map.keySet()) {
			int count = 0;
			Label1: for (int x = 0; x < width; ++x) {
				for (int y = 0; y < height; ++y) {
					if (isWhite(img.getRGB(x, y)) != isWhite(bi.getRGB(x, y))) {
						count++;
						if (count >= min) break Label1;
					}
				}
			}
			if (count < min) {
				min = count;
				result = map.get(bi);
			}
		}
		return result;
	}

	public static String getAllOcr(String file, String outPutPath) throws IOException {
		BufferedImage img = removeBackgroud(file);
		List<BufferedImage> listImg = splitImage(img);
		Map<BufferedImage, String> map = loadTrainData();
		String result = "";
		for (BufferedImage bi : listImg) {
			String singleCharOcr = getSingleCharOcr(bi, map);
			result += singleCharOcr;
			// ImageIO.write(bi, "JPG", new File(outPutPath + "singleChar//" +
			// singleCharOcr + result +".jpg"));
		}
		ImageIO.write(img, "JPG", new File(outPutPath + result + ".jpg"));
		return result;
	}

	public static void downloadImage() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getMethod = new HttpGet("http://www.puke888.com/authimg.php");
		for (int i = 0; i < 30; i++) {
			try {
				// 执行getMethod
				HttpResponse response = httpClient.execute(getMethod);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					System.err.println("Method failed: " + statusLine);
				}
				// 读取内容
				// String picName = "img//" + i + ".jpg";
				String picName = "c://cache//img//" + i + ".jpg";
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					OutputStream outStream = new FileOutputStream(picName);
					InputStream inputStream = entity.getContent();
					IOUtils.copy(inputStream, outStream);
					outStream.close();
				}
				System.out.println("OK!" + i);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 释放连接
				getMethod.releaseConnection();
			}
		}
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// downloadImage();
		for (int i = 0; i < 30; ++i) {
			String text = getAllOcr("c://cache//img//" + i + ".jpg", "c://cache//result//");
			System.out.println(i + ".jpg = " + text);
		}
	}
}
