package doing.ocr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.pu.test.base.TestBase;
import org.pu.utils.ImageIoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 图像过滤,增强OCR识别成功率 增强识别率的关键是要识别的验证码图片越干净越好,为此准备了六种图像过滤,
 * 用以滤干净图像,有:图像二值化,锐化,中值滤波,线性灰度变换,转黑白灰度图,放大(非平滑缩放).
 * 一般干扰不太严重的验证码,如支付宝使用图像二值化和线性灰度变换就可以做到100%识别,有噪点的才需要中值滤波. 使用这六种过滤的组合应该可以搞定大部分比较弱智的验证码 refer to
 * http://ykf.iteye.com/blog/212431
 * 
 * @author Shang Pu
 * @version Date: Feb 3, 2013 6:20:44 PM
 */
public class TestImageFilter extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(TestImageFilter.class);

	@Test
	public void test() {
		String picFile = "C:\\cache\\cat.jpg";
		// String picFile = "C:\\cache\\springFestival.jpg";
		// String picFile = "C:\\cache\\DSCN2505.JPG";
		try {
			BufferedImage inputImg = ImageIO.read(new File(picFile));
			BufferedImage img = median(inputImg);
			ImageIO.write(img, "JPG", new File("c://cache//cat_result.jpg"));
		} catch (IOException e) {
			log.error("Exception in TestImage.testRemoveBackground()", e);
		}
	}

	/**
	 * TODO: 浮雕效果 refer to http://www.icodelogic.com/?p=575
	 * 浮雕的算法相对复杂一些,用当前点的RGB值减去相邻点的RGB值并加上128作为新的RGB值. 由于图片中相邻点的颜色值是比较接近的,因此这样的算法处理之后,只有颜色的边沿区域,
	 * 也就是相邻颜色差异较大的部分的结果才会比较明显,而其他平滑区域则值都接近128左右, 也就是灰色,这样 就具有了浮雕效果. 在实际的效果中,这样处理后,有些区域可能还是会
	 * 有”彩色”的一些点或者条状痕迹,所以最好再对新的RGB值做一个灰度处理.
	 */
	public static BufferedImage sculptural(BufferedImage img) {
		Color prepreColor = new Color(0);
		Color preColor = new Color(0);
		int width = img.getWidth();
		int height = img.getHeight();
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				log.info("x = {}, y = {}", x, y);
				if (y == 0) {
					prepreColor = new Color(img.getRGB(x, y));
					// preColor = new Color(img.getRGB(x, y));
					continue;
				}
				Color color = new Color(img.getRGB(x, y));
				int red = color.getRed() - prepreColor.getRed() + 128;
				int green = color.getGreen() - prepreColor.getGreen() + 128;
				int blue = color.getBlue() - prepreColor.getBlue() + 128;
				log.info("red = {}, green = {}, blue = {}", new Object[] { red, green, blue });

				int grayInt = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
				img.setRGB(x, y, new Color(grayInt, grayInt, grayInt).getRGB());
				preColor = color;
				prepreColor = preColor;
			}
		}
		return img;
	}

	/**
	 * 底片效果 算法原理:将当前像素点的RGB值分别与255之差后的值作为当前点的RGB值,即 R = 255 – R;G = 255 – G;B = 255 – B;
	 */
	public static BufferedImage photocopy(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				Color color = new Color(img.getRGB(x, y));
				int red = 255 - color.getRed();
				int green = 255 - color.getGreen();
				int blue = 255 - color.getBlue();
				img.setRGB(x, y, new Color(red, green, blue).getRGB());
			}
		}
		return img;
	}

	/**
	 * 灰度处理一般有三种算法: 1 最大值法:即新的颜色值R=G=B=Max(R,G,B),这种方法处理后的图片看起来亮度值偏高. 2
	 * 平均值法:即新的颜色值R=G=B=(R+G+B)／3,这样处理的图片十分柔和 3
	 * 加权平均值法:即新的颜色值R=G=B=(R*Wr+G*Wg+B*Wb),一般由于人眼对不同颜色的敏感度不一样,所以三种颜色值的权重不一样, 一般来说绿色最高
	 * ,红色其次,蓝色最低,最合理的取值分别为Wr = 30%,Wg = 59%,Wb = 11%
	 */
	public static BufferedImage gray(BufferedImage img) {
		log.debug("Enter gray()");
		int width = img.getWidth();
		int height = img.getHeight();
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				Color color = new Color(img.getRGB(x, y));
				int grayInt = (int) (color.getRed() * 0.3 + color.getGreen() * 0.59 + color.getBlue() * 0.11);
				// int grayInt = (color.getRed(.) + color.getGreen() +
				// color.getBlue() )/3;
				img.setRGB(x, y, new Color(grayInt, grayInt, grayInt).getRGB());
			}
		}
		log.debug("Exit gray()");
		return img;
	}

	/** TODO: 图像二值化 */
	public BufferedImage gray2(BufferedImage image) {
		log.debug("Enter gray2()");
		int iw = image.getWidth();
		int ih = image.getHeight();
		int[] pixels = new int[iw * ih];
		PixelGrabber pg = new PixelGrabber(image.getSource(), 0, 0, iw, ih, pixels, 0, iw);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			log.error("InterruptedException in grayImage2", e);
		}
		// 设定二值化的域值，默认值为100
		int gray = 100;
		// 对图像进行二值化处理，Alpha值保持不变
		ColorModel cm = ColorModel.getRGBdefault();
		for (int i = 0; i < iw * ih; i++) {
			int red, green, blue;
			int alpha = cm.getAlpha(pixels[i]);
			if (cm.getRed(pixels[i]) > gray) {
				red = 255;
			} else {
				red = 0;
			}

			if (cm.getGreen(pixels[i]) > gray) {
				green = 255;
			} else {
				green = 0;
			}

			if (cm.getBlue(pixels[i]) > gray) {
				blue = 255;
			} else {
				blue = 0;
			}

			pixels[i] = alpha << 24 | red << 16 | green << 8 | blue;
		}
		// 将数组中的象素产生一个图像
		log.debug("Exit gray2()");
		return ImageIoHelper.imageProducerToBufferedImage(new MemoryImageSource(iw, ih, pixels, 0, iw));
	}

	/** 线性灰度变换 */
	public BufferedImage lineGray(BufferedImage img) {
		log.debug("Enter lineGray()");
		int iw = img.getWidth();
		int ih = img.getHeight();
		int[] pixels = new int[iw * ih];
		PixelGrabber pg = new PixelGrabber(img.getSource(), 0, 0, iw, ih, pixels, 0, iw);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			log.error("Exception in TestImage.lineGrey()", e);
		}
		// 对图像进行进行线性拉伸，Alpha值保持不变
		ColorModel cm = ColorModel.getRGBdefault();
		for (int i = 0; i < iw * ih; i++) {
			int alpha = cm.getAlpha(pixels[i]);
			int red = cm.getRed(pixels[i]);
			int green = cm.getGreen(pixels[i]);
			int blue = cm.getBlue(pixels[i]);

			// 增加了图像的亮度
			red = (int) (1.1 * red + 30);
			green = (int) (1.1 * green + 30);
			blue = (int) (1.1 * blue + 30);
			if (red >= 255) {
				red = 255;
			}
			if (green >= 255) {
				green = 255;
			}
			if (blue >= 255) {
				blue = 255;
			}
			pixels[i] = alpha << 24 | red << 16 | green << 8 | blue;
		}

		log.debug("Exit lineGray()");
		// 将数组中的象素产生一个图像
		return ImageIoHelper.imageProducerToBufferedImage(new MemoryImageSource(iw, ih, pixels, 0, iw));
	}

	/**
	 * 黑白图片的处理算法更简单: 求RGB平均值Avg=(R+G+B)/3,如果Avg>=100,则新的颜色值为R=G=B=255;
	 * 如果Avg<100,则新的颜色值为R=G=B=0;255就是白色,0就是黑色; 至于为什么用100作比较,这是一个经验值吧,设置为128也可以,可以根据效果来调整.
	 */
	public static BufferedImage whiteBlackImg(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				if (isWhite(img.getRGB(x, y))) {
					img.setRGB(x, y, Color.WHITE.getRGB());
				} else {
					img.setRGB(x, y, Color.BLACK.getRGB());
				}
			}
		}
		return img;
	}

	public static boolean isWhite(int colorInt) {
		Color color = new Color(colorInt);
		if ((color.getRed() + color.getGreen() + color.getBlue()) / 3 >= 100) {
			return true;
		}
		return false;
	}

	/** 转换为黑白灰度图 */
	public BufferedImage grayFilter(BufferedImage img) {
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		ColorConvertOp op = new ColorConvertOp(cs, null);
		return op.filter(img, null);
	}

	/** 平滑缩放, but the color is change to be pink */
	public BufferedImage scaling(BufferedImage img) {
		AffineTransform tx = new AffineTransform();
		double s = 10;
		tx.scale(s, s);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op.filter(img, null);
	}

	public BufferedImage scale(BufferedImage img) {
		int srcW = img.getWidth();
		int srcH = img.getHeight();
		float s = 10;
		int newW = Math.round(srcW * s);
		int newH = Math.round(srcH * s);
		// 先做水平方向上的伸缩变换
		BufferedImage tmp = new BufferedImage(newW, newH, img.getType());
		Graphics2D g = tmp.createGraphics();
		for (int x = 0; x < newW; x++) {
			g.setClip(x, 0, 1, srcH);
			// 按比例放缩
			g.drawImage(img, x - x * srcW / newW, 0, null);
		}

		// 再做垂直方向上的伸缩变换
		BufferedImage dst = new BufferedImage(newW, newH, img.getType());
		g = dst.createGraphics();
		for (int y = 0; y < newH; y++) {
			g.setClip(0, y, newW, 1);
			// 按比例放缩
			g.drawImage(tmp, 0, y - y * srcH / newH, null);
		}
		return dst;
	}

	/** 中值滤波 */
	public BufferedImage median(BufferedImage img) {
		log.debug("Enter median()");
		int iw = img.getWidth();
		int ih = img.getHeight();
		int[] pixels = new int[iw * ih];
		PixelGrabber pg = new PixelGrabber(img.getSource(), 0, 0, iw, ih, pixels, 0, iw);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			log.error("Exception in TestImage.median()", e);
		}
		// 对图像进行中值滤波，Alpha值保持不变
		ColorModel cm = ColorModel.getRGBdefault();
		for (int i = 1; i < ih - 1; i++) {
			for (int j = 1; j < iw - 1; j++) {
				int red, green, blue;
				int alpha = cm.getAlpha(pixels[i * iw + j]);

				// int red2 = cm.getRed(pixels[(i - 1) * iw + j]);
				int red4 = cm.getRed(pixels[i * iw + j - 1]);
				int red5 = cm.getRed(pixels[i * iw + j]);
				int red6 = cm.getRed(pixels[i * iw + j + 1]);
				// int red8 = cm.getRed(pixels[(i + 1) * iw + j]);

				// 水平方向进行中值滤波
				if (red4 >= red5) {
					if (red5 >= red6) {
						red = red5;
					} else {
						if (red4 >= red6) {
							red = red6;
						} else {
							red = red4;
						}
					}
				} else {
					if (red4 > red6) {
						red = red4;
					} else {
						if (red5 > red6) {
							red = red6;
						} else {
							red = red5;
						}
					}
				}

				// int green2 = cm.getGreen(pixels[(i - 1) * iw + j]);
				int green4 = cm.getGreen(pixels[i * iw + j - 1]);
				int green5 = cm.getGreen(pixels[i * iw + j]);
				int green6 = cm.getGreen(pixels[i * iw + j + 1]);
				// int green8 = cm.getGreen(pixels[(i + 1) * iw + j]);

				// 水平方向进行中值滤波
				if (green4 >= green5) {
					if (green5 >= green6) {
						green = green5;
					} else {
						if (green4 >= green6) {
							green = green6;
						} else {
							green = green4;
						}
					}
				} else {
					if (green4 > green6) {
						green = green4;
					} else {
						if (green5 > green6) {
							green = green6;
						} else {
							green = green5;
						}
					}
				}

				// int blue2 = cm.getBlue(pixels[(i - 1) * iw + j]);
				int blue4 = cm.getBlue(pixels[i * iw + j - 1]);
				int blue5 = cm.getBlue(pixels[i * iw + j]);
				int blue6 = cm.getBlue(pixels[i * iw + j + 1]);
				// int blue8 = cm.getBlue(pixels[(i + 1) * iw + j]);

				// 水平方向进行中值滤波
				if (blue4 >= blue5) {
					if (blue5 >= blue6) {
						blue = blue5;
					} else {
						if (blue4 >= blue6) {
							blue = blue6;
						} else {
							blue = blue4;
						}
					}
				} else {
					if (blue4 > blue6) {
						blue = blue4;
					} else {
						if (blue5 > blue6) {
							blue = blue6;
						} else {
							blue = blue5;
						}
					}
				}
				pixels[i * iw + j] = alpha << 24 | red << 16 | green << 8 | blue;
			}
		}

		// 将数组中的象素产生一个图像
		return ImageIoHelper.imageProducerToBufferedImage(new MemoryImageSource(iw, ih, pixels, 0, iw));
	}

	/** TODO: 提升清晰度,进行锐化 */
	public BufferedImage sharp(BufferedImage img) {
		log.debug("Enter sharp()");
		int iw = img.getWidth();
		int ih = img.getHeight();
		int[] pixels = new int[iw * ih];
		PixelGrabber pg = new PixelGrabber(img.getSource(), 0, 0, iw, ih, pixels, 0, iw);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			log.error("异常:", e);
		}

		// 象素的中间变量
		int tempPixels[] = new int[iw * ih];
		for (int i = 0; i < iw * ih; i++) {
			tempPixels[i] = pixels[i];
		}
		// 对图像进行尖锐化处理，Alpha值保持不变
		ColorModel cm = ColorModel.getRGBdefault();
		for (int i = 1; i < ih - 1; i++) {
			for (int j = 1; j < iw - 1; j++) {
				int alpha = cm.getAlpha(pixels[i * iw + j]);

				// 对图像进行尖锐化
				int red6 = cm.getRed(pixels[i * iw + j + 1]);
				int red5 = cm.getRed(pixels[i * iw + j]);
				int red8 = cm.getRed(pixels[(i + 1) * iw + j]);
				int sharpRed = Math.abs(red6 - red5) + Math.abs(red8 - red5);

				int green5 = cm.getGreen(pixels[i * iw + j]);
				int green6 = cm.getGreen(pixels[i * iw + j + 1]);
				int green8 = cm.getGreen(pixels[(i + 1) * iw + j]);
				int sharpGreen = Math.abs(green6 - green5) + Math.abs(green8 - green5);

				int blue5 = cm.getBlue(pixels[i * iw + j]);
				int blue6 = cm.getBlue(pixels[i * iw + j + 1]);
				int blue8 = cm.getBlue(pixels[(i + 1) * iw + j]);
				int sharpBlue = Math.abs(blue6 - blue5) + Math.abs(blue8 - blue5);

				if (sharpRed > 255) {
					sharpRed = 255;
				}
				if (sharpGreen > 255) {
					sharpGreen = 255;
				}
				if (sharpBlue > 255) {
					sharpBlue = 255;
				}

				tempPixels[i * iw + j] = alpha << 24 | sharpRed << 16 | sharpGreen << 8 | sharpBlue;
			}
		}

		log.debug("Exit sharp()");
		// 将数组中的象素产生一个图像
		return ImageIoHelper.imageProducerToBufferedImage(new MemoryImageSource(iw, ih, tempPixels, 0,
				iw));
	}
}
