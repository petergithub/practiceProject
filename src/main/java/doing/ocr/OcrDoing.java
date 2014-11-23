package doing.ocr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version Date: Feb 28, 2013 4:17:36 PM
 * @author Shang Pu
 */
public class OcrDoing {
	private static final Logger log = LoggerFactory.getLogger(OcrDoing.class);

	public void testResolveRandImg() throws IOException {
		// String file = "C:\\cache\\haijia\\random.jpg";
		String file = "C:\\cache\\haijia\\12.jpg";
		String path = "C:\\cache\\haijia\\";
		getOcr(file, path);
	}

	public void getOcr(String file, String path) throws IOException {
		BufferedImage img = TestImageFilter.whiteBlackImg(ImageIO.read(new File(file)));
		ImageIO.write(img, "JPG", new File("c:/cache/haijia/removeBackground.jpg"));
		List<BufferedImage> listImg = splitImage(img);
		Map<BufferedImage, String> map = loadTrainData(path + "train");
		String result = "";
		for (BufferedImage bi : listImg) {
			String singleCharOcr = getSingleCharOcr(bi, map);
			result += singleCharOcr;
			ImageIO.write(bi, "JPG", new File("c:/cache/haijia/singleChar/" + new Random().nextInt()
					+ ".jpg"));
		}
		log.info("result = {}", result);
	}

	private String getSingleCharOcr(BufferedImage img, Map<BufferedImage, String> map) {
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
					if (TestImageFilter.isWhite(img.getRGB(x, y)) != TestImageFilter.isWhite(bi.getRGB(x, y))) {
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

	private Map<BufferedImage, String> loadTrainData(String trainPath) throws IOException {
		Map<BufferedImage, String> map = new HashMap<BufferedImage, String>();
		File dir = new File(trainPath);
		File[] files = dir.listFiles();
		for (File file : files) {
			map.put(ImageIO.read(file), file.getName().charAt(0) + "");
		}
		return map;
	}

	private List<BufferedImage> splitImage(BufferedImage img) {
		List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
		subImgs.add(img.getSubimage(5, 5, 17, 18));
		subImgs.add(img.getSubimage(21, 5, 17, 22));
		subImgs.add(img.getSubimage(37, 5, 14, 21));
		subImgs.add(img.getSubimage(48, 5, 14, 21));
		return subImgs;
	}

}
