package org.pu.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageProducer;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.media.imageio.plugins.tiff.TIFFImageWriteParam;

public class ImageIoHelper {
	private static final String TIF = ".tif";
	private static final String TEMP_IMAGE_FILE = "tempImageFile";
	private static final Logger log = LoggerFactory.getLogger(ImageIoHelper.class);

	/**
	 * @param image
	 * @return null if JAI Image I/O package is not installed, or if IOException is throw
	 */
	public static File createTempTifImage(File image) {
		log.debug("Enter createTempTifImage({})", image.getAbsolutePath());
		File tifImage = null;
		ImageReader reader = null;
		ImageInputStream iis = null;
		try {
			ImageInputStream input = new FileImageInputStream(image);
			Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
			reader = readers.next();
			if (reader == null) {
				log.error("Need to install JAI Image I/O package.\nhttps://jai-imageio.dev.java.net in ImageIOHelper.createTempTifImage()");
				return null;
			}
			iis = ImageIO.createImageInputStream(image);
			reader.setInput(iis);
			// Read the metadata
			IIOMetadata metadata = reader.getImageMetadata(0);
			BufferedImage bi = reader.read(0);
			tifImage = createTempTifImage(bi, metadata);
		} catch (IOException e) {
			log.error("IOException in ImageIOHelper.createTempTifImage()", e);
		} finally {
			if (reader != null) reader.dispose();
			close(iis);
		}
		log.debug("Exit createTempTifImage()");
		return tifImage;
	}

	public static File createTempTifImage(BufferedImage bi, IIOMetadata metadata) {
		log.debug("Enter createTempTifImage({}, {})", bi, metadata);
		File tifImage = null;
		ImageOutputStream ios = null;
		ImageWriter writer = null;
		try {
			tifImage = File.createTempFile(TEMP_IMAGE_FILE, TIF);
			tifImage.deleteOnExit();
			// Set up the writeParam
			TIFFImageWriteParam tiffWriteParam = new TIFFImageWriteParam(Locale.US);
			tiffWriteParam.setCompressionMode(ImageWriteParam.MODE_DISABLED);

			IIOImage iioImage = new IIOImage(bi, null, metadata);
			ios = ImageIO.createImageOutputStream(tifImage);

			// Get tif writer and set output to file
			Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("tiff");
			writer = writers.next();
			writer.setOutput(ios);
			writer.write(null, iioImage, tiffWriteParam);
		} catch (IOException e) {
			log.error("IOException in ImageIOHelper.createTempTifImage()", e);
		} finally {
			if (writer != null) writer.dispose();
			close(ios);
		}
		log.debug("Exit createTempTifImage() tifImage={}", tifImage);
		return tifImage;
	}

	/**
	 * @param image
	 */
	public static BufferedImage getBufferedImage(File image) {
		BufferedImage bufferedImage = null;
		try {
			// String name = imageFile.getName();
			// String format = name.substring(name.lastIndexOf('.') + 1);
			// Iterator<ImageReader> readers = ImageIO
			// .getImageReadersByFormatName(format);
			ImageInputStream input = new FileImageInputStream(image);
			Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
			ImageReader reader = readers.next();

			if (reader == null) {
				log.error("Need to install JAI Image I/O package.\nhttps://jai-imageio.dev.java.net in ImageIOHelper.getBufferedImage()");
				return null;
			}
			ImageInputStream iis = ImageIO.createImageInputStream(image);
			reader.setInput(iis);
			bufferedImage = reader.read(0);
			reader.dispose();
			close(iis);
		} catch (IOException e) {
			log.error("IOException in ImageIOHelper.getBufferedImage()", e);
		}
		return bufferedImage;
	}

	public static BufferedImage imageToBufferedImage(Image image) {
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bufferedImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		return bufferedImage;
	}

	public static BufferedImage imageProducerToBufferedImage(ImageProducer imageProducer) {
		return imageToBufferedImage(Toolkit.getDefaultToolkit().createImage(imageProducer));
	}

	public static byte[] image_byte_data(BufferedImage image) {
		WritableRaster raster = image.getRaster();
		DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
		return buffer.getData();
	}

	public static void close(ImageInputStream iis) {
		if (iis != null) {
			try {
				iis.close();
			} catch (IOException ignore) {
			}
		}
	}
}
