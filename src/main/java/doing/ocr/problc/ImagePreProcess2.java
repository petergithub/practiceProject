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

/**
 * http://blog.csdn.net/problc/article/details/5797507 换一个字体固定，大小固定，位置不固定的验证码 还是四步。 1。图像预处理
 * 这验证码还是很厚道的，都没有任何干扰。不用处理 2。分割 先纵向扫描，很容易分成四部分 再对每一部分横向扫描 3。训练就容易了 4。识别 因为固定大小，识别跟 验证码识别--1
 * 里面一样，像素比较就可以了。
 */
public class ImagePreProcess2 {

	private static Map<BufferedImage, String> trainMap = null;
	private static int index = 0;

	@Test
	public void readImg() throws IOException {
		String text = getAllOcr("c://cache//5.jpg");
		System.out.println(".jpg text = " + text);
	}

	public static int isBlack(int colorInt) {
		Color color = new Color(colorInt);
		if (color.getRed() + color.getGreen() + color.getBlue() <= 100) {
			return 1;
		}
		return 0;
	}

	public static int isWhite(int colorInt) {
		Color color = new Color(colorInt);
		if (color.getRed() + color.getGreen() + color.getBlue() > 100) {
			return 1;
		}
		return 0;
	}

	public static BufferedImage removeBackgroud(String picFile) throws IOException {
		BufferedImage img = ImageIO.read(new File(picFile));
		return img;
	}

	public static BufferedImage removeBlank(BufferedImage img) throws IOException {
		int width = img.getWidth();
		int height = img.getHeight();
		int start = 0;
		int end = 0;
		Label1: for (int y = 0; y < height; ++y) {
			int count = 0;
			for (int x = 0; x < width; ++x) {
				if (isWhite(img.getRGB(x, y)) == 1) {
					count++;
				}
				if (count >= 1) {
					start = y;
					break Label1;
				}
			}
		}
		Label2: for (int y = height - 1; y >= 0; --y) {
			int count = 0;
			for (int x = 0; x < width; ++x) {
				if (isWhite(img.getRGB(x, y)) == 1) {
					count++;
				}
				if (count >= 1) {
					end = y;
					break Label2;
				}
			}
		}
		return img.getSubimage(0, start, width, end - start + 1);
	}

	public static List<BufferedImage> splitImage(BufferedImage img) throws IOException {
		List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
		int width = img.getWidth();
		int height = img.getHeight();
		List<Integer> weightlist = new ArrayList<Integer>();
		for (int x = 0; x < width; ++x) {
			int count = 0;
			for (int y = 0; y < height; ++y) {
				if (isWhite(img.getRGB(x, y)) == 1) {
					count++;
				}
			}
			weightlist.add(count);
		}
		for (int i = 0; i < weightlist.size();) {
			int length = 0;
			while (weightlist.get(i++) > 1) {
				length++;
			}
			if (length > 12) {
				subImgs.add(removeBlank(img.getSubimage(i - length - 1, 0, length / 2, height)));
				subImgs.add(removeBlank(img.getSubimage(i - length / 2 - 1, 0, length / 2, height)));
			} else if (length > 3) {
				subImgs.add(removeBlank(img.getSubimage(i - length - 1, 0, length, height)));
			}
		}
		return subImgs;
	}

	public static Map<BufferedImage, String> loadTrainData() throws IOException {
		if (trainMap == null) {
			Map<BufferedImage, String> map = new HashMap<BufferedImage, String>();
			File dir = new File("c://cache//train2");
			File[] files = dir.listFiles();
			for (File file : files) {
				map.put(ImageIO.read(file), file.getName().charAt(0) + "");
			}
			trainMap = map;
		}
		return trainMap;
	}

	public static String getSingleCharOcr(BufferedImage img, Map<BufferedImage, String> map) {
		String result = "";
		int width = img.getWidth();
		int height = img.getHeight();
		int min = width * height;
		for (BufferedImage bi : map.keySet()) {
			int count = 0;
			int widthmin = width < bi.getWidth() ? width : bi.getWidth();
			int heightmin = height < bi.getHeight() ? height : bi.getHeight();
			Label1: for (int x = 0; x < widthmin; ++x) {
				for (int y = 0; y < heightmin; ++y) {
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

	public static String getAllOcr(String file) throws IOException {
		BufferedImage img = removeBackgroud(file);
		List<BufferedImage> listImg = splitImage(img);
		Map<BufferedImage, String> map = loadTrainData();
		String result = "";
		for (BufferedImage bi : listImg) {
			String singleCharOcr = getSingleCharOcr(bi, map);
			result += singleCharOcr;
			ImageIO.write(bi, "JPG", new File("c://cache//result2//singleChar//" + singleCharOcr + result
					+ ".jpg"));
		}
		ImageIO.write(img, "JPG", new File("c://cache//result2//" + result + ".jpg"));
		return result;
	}

	public static void downloadImage() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getMethod = null;
		for (int i = 0; i < 30; i++) {
			getMethod = new HttpGet("http://www.pkland.net/img.php?key=" + (2000 + i));
			try {
				// 执行getMethod
				HttpResponse response = httpClient.execute(getMethod);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					System.err.println("Method failed: " + statusCode);
				}
				// 读取内容
				String picName = "c://cache//img2//" + i + ".jpg";
				// String picName = "c://cache//img2//" + i + ".jpg";
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					OutputStream outStream = new FileOutputStream(picName);
					InputStream inputStream = entity.getContent();
					IOUtils.copy(inputStream, outStream);
					outStream.close();
				}
				System.out.println(i + "OK!");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 释放连接
				getMethod.releaseConnection();
			}
		}
	}

	public static void trainData() throws Exception {
		File dir = new File("temp");
		File[] files = dir.listFiles();
		for (File file : files) {
			BufferedImage img = removeBackgroud("c://cache//temp//" + file.getName());
			List<BufferedImage> listImg = splitImage(img);
			if (listImg.size() == 4) {
				for (int j = 0; j < listImg.size(); ++j) {
					ImageIO.write(listImg.get(j), "JPG", new File("c://cache//train2//"
							+ file.getName().charAt(j) + "-" + (index++) + ".jpg"));
				}
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
			String text = getAllOcr("c://cache//img2//" + i + ".jpg");
			System.out.println(i + ".jpg = " + text);
		}
	}
}
