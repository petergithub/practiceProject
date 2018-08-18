package demo.file.pdf;
//
//import java.awt.Color;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.MalformedURLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.lowagie.text.Cell;
//import com.lowagie.text.Chapter;
//import com.lowagie.text.Chunk;
//import com.lowagie.text.Document;
//import com.lowagie.text.DocumentException;
//import com.lowagie.text.Element;
//import com.lowagie.text.Font;
//import com.lowagie.text.FontFactory;
//import com.lowagie.text.Image;
//import com.lowagie.text.ListItem;
//import com.lowagie.text.PageSize;
//import com.lowagie.text.Paragraph;
//import com.lowagie.text.Section;
//import com.lowagie.text.Table;
//import com.lowagie.text.pdf.BaseFont;
//import com.lowagie.text.pdf.PdfContentByte;
//import com.lowagie.text.pdf.PdfEncryptor;
//import com.lowagie.text.pdf.PdfImportedPage;
//import com.lowagie.text.pdf.PdfReader;
//import com.lowagie.text.pdf.PdfStamper;
//import com.lowagie.text.pdf.PdfWriter;
//
///**
// * <itext.version>1.3</itext.version>
// */
public class TestPdf {
//	private static final Logger log = LoggerFactory.getLogger(TestPdf.class);
//
//	@Test
//	public void previewCustomize() {
//		String imagePath="c:\\sp\\doing\\watermark.jpg";
//			String splitPdfPath = "c:/sp/d.pdf";
//			splitPdf("c:\\sp\\design.pdf",splitPdfPath , 1, 3);
//
//			String securityPdfPath = "c:/sp/e.pdf";;
//			addRestrictPermission(splitPdfPath, securityPdfPath);
//			
//			String watermarkPdfPath = "c:/sp/f.pdf";;
//			addWatermark(securityPdfPath, watermarkPdfPath, imagePath );
//	}
//
//	public void testAddWatermark() {
//		String pdfPath="c:\\sp\\c.pdf";
//		String pdfWithWatermarkPath="c:\\sp\\New_PDF_With_Watermark_Image.pdf";
//		String imagePath="c:\\sp\\doing\\watermark_preview.jpg";
//		addWatermark(pdfPath, pdfWithWatermarkPath, imagePath);
//	}
//
//	
//	public void testPermission2() {
//		String USER_PASS = "a";
//		HashMap<?, ?> moreInfo = new HashMap<Object, Object>();
//		int permissions = 0;
//		try {
//			String OWNER_PASS = USER_PASS;
//			PdfReader reader = new PdfReader("c:\\sp\\c.pdf");
//			PdfEncryptor.encrypt(reader, new FileOutputStream("c:\\sp\\c.pdf"),
//					null, OWNER_PASS.getBytes(), permissions, true, moreInfo);
//		} catch (DocumentException e) {
//			log.error("DocumentException in TestPdf.testPermission2()", e);
//		} catch (IOException e) {
//			log.error("IOException in TestPdf.testPermission2()", e);
//		}
//	}
//
//	public void testMergePdf() {
//		try {
//			List<String> pdfs = new ArrayList<String>();
//			pdfs.add("c:\\1.pdf");
//			pdfs.add("c:\\2.pdf");
//			concatPdfs(pdfs, "c:\\merge.pdf", true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void testSplitPdf() {
//		splitPdf("C:\\sp\\d.pdf", "C:\\sp\\output1.pdf", 1, 3);
//	}
//
//	public void testPermission() {
//		String USER_PASS = "a";
//		String OWNER_PASS = USER_PASS;
//		int permissions = 0;
//		try {
//			File tempDir = new File(System.getProperty("java.io.tmpdir"),
//					"cara");
//			File tmpFile2 = File.createTempFile("docsecure2", ".pdf", tempDir);
//			PdfReader pdfReader = new PdfReader("c:\\sp\\a.pdf");
//			FileOutputStream os = new FileOutputStream(tmpFile2);
//			PdfStamper pdfStamper = new PdfStamper(pdfReader, os);
//			pdfStamper.setEncryption(null, OWNER_PASS.getBytes(), permissions,
//					PdfWriter.STRENGTH128BITS);
//			pdfStamper.close();
//			os.close();
//		} catch (DocumentException e) {
//		} catch (IOException e) {
//		}
//	}
//
//	public static void creatPdf() {
//		try {
//			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
//			@SuppressWarnings("unused")
//			PdfWriter writer = PdfWriter.getInstance(document,
//					new FileOutputStream("c:\\ITextTest.pdf"));
//			document.open();
//			document.add(new Paragraph("First page of the document."));
//			document
//					.add(new Paragraph(
//							"Some more text on the first page with different color and font type.",
//							FontFactory.getFont(FontFactory.COURIER, 14,
//									Font.BOLD, new Color(255, 150, 200))));
//
//			Paragraph title1 = new Paragraph("Chapter 1", FontFactory.getFont(
//					FontFactory.HELVETICA, 18, Font.BOLDITALIC, new Color(0, 0,
//							255)));
//			Chapter chapter1 = new Chapter(title1, 1);
//			chapter1.setNumberDepth(0);
//			Paragraph title11 = new Paragraph("This is Section 1 in Chapter 1",
//					FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD,
//							new Color(255, 0, 0)));
//			Section section1 = chapter1.addSection(title11);
//			Paragraph someSectionText = new Paragraph(
//					"This text comes as part of section 1 of chapter 1.");
//			section1.add(someSectionText);
//			someSectionText = new Paragraph("Following is a 3 X 2 table.");
//			section1.add(someSectionText);
//			Table t = new Table(3, 2);
//			t.setBorderColor(new Color(220, 255, 100));
//			t.setPadding(5);
//			t.setSpacing(5);
//			t.setBorderWidth(1);
//			Cell c1 = new Cell("header1");
//			t.addCell(c1);
//			c1 = new Cell("Header2");
//			t.addCell(c1);
//			c1 = new Cell("Header3");
//			t.addCell(c1);
//			t.addCell("1.1");
//			t.addCell("1.2");
//			t.addCell("1.3");
//			section1.add(t);
//
//			com.lowagie.text.List l = new com.lowagie.text.List(true, true, 10);
//			l.add(new ListItem("First item of list"));
//			l.add(new ListItem("Second item of list"));
//			section1.add(l);
//
//			document.add(chapter1);
//			document.close();
//		} catch (Exception e2) {
//			System.out.println(e2.getMessage());
//		}
//	}
//
//	/**
//	 * <itext.version>2.1.7</itext.version>
//	 */
//	/*public void disablePdfPrint() {
//		String USER_PASS = "a";
//		String OWNER_PASS = USER_PASS;
//		int permissions = 0;
//		try {
//			PdfReader reader = new PdfReader("c:\\sp\\a.pdf");
//			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
//					"c:\\sp\\b.pdf"));
//			 stamper.setEncryption(null, OWNER_PASS.getBytes(), permissions,
//			 PdfWriter.ENCRYPTION_AES_128);
//			stamper.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}*/
//
//	/**
//	 * <itext.version>2.1.7</itext.version>
//	 */
//	/*public void testPdfSecurity() {
//		String USER_PASS = "a";
//		String OWNER_PASS = "a";
//		int permissions = 0;
//		// int permissions = PdfWriter.ALLOW_PRINTING | PdfWriter.ALLOW_COPY
//		try {
//			OutputStream file = new FileOutputStream(new File("c:\\sp\\ch.pdf"));
//			Document document = new Document();
//			PdfWriter writer = PdfWriter.getInstance(document, file);
//			 writer.setEncryption(USER_PASS.getBytes(), OWNER_PASS.getBytes(),
//			 permissions,
//			 PdfWriter.ENCRYPTION_AES_128);
//
//			document.open();
//			document.add(new Paragraph("Hello World, iText"));
//			BaseFont bfChinese = BaseFont.createFont("STSongStd-Light",
//					"UniGB-UCS2-H", false);
//			Font fontChinese = new Font(bfChinese, 12, Font.NORMAL, Color.GREEN);
//			document.add(new Paragraph(
//					"测试中文Test iTextAsian Chinese 测试中文, iText", fontChinese));
//			document.add(new Paragraph(new Date().toString()));
//
//			document.close();
//			file.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}*/
//
//	/**
//	 * The iText project, located at http://www.lowagie.com/iText/, is a JavaSW
//	 * library that lets you generate PDF documents. It is very easy to use and
//	 * features a high degree of functionality. In addition, the documentation
//	 * is quite good and examples of iText on the web abound, such as <a href=
//	 * 'http://www.adobepress.com/articles/printerfriendly.asp?p=420686&rl=1'>To
//	 * o
//	 * l
//	 * s
//	 * of the Trade, Part 1: Creating PDF documents with iText<a/>.
//	 */
//	public void testITextWritePdfFile() {
//		try {
//			File file = new File("itext-test.pdf");
//			System.out.println(file.getAbsolutePath());
//
//			FileOutputStream fileout = new FileOutputStream(file);
//			Document document = new Document();
//			PdfWriter.getInstance(document, fileout);
//			document.addAuthor("Me");
//			document.addTitle("My iText Test");
//
//			document.open();
//
//			Chunk chunk = new Chunk("iText Test");
//			Font font = new Font(Font.COURIER);
//			font.setStyle(Font.UNDERLINE);
//			font.setStyle(Font.ITALIC);
//			chunk.setFont(font);
//			chunk.setBackground(Color.CYAN);
//			document.add(chunk);
//
//			Paragraph paragraph = new Paragraph();
//			paragraph.add("Hello World");
//			paragraph.setAlignment(Element.ALIGN_CENTER);
//			document.add(paragraph);
//
//			Image image;
//			try {
//				image = Image.getInstance("world.gif");
//				image.setAlignment(Image.MIDDLE);
//				document.add(image);
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//			com.lowagie.text.List list = new com.lowagie.text.List(true, 15);
//			list.add("ABC");
//			list.add("DEF");
//			document.add(list);
//
//			document.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void concatPdfs(List<String> inputPdfPaths,
//			String outputPath, boolean paginate) {
//		Document document = new Document();
//		try {
//			List<PdfReader> readers = new ArrayList<PdfReader>();
//			int totalPages = 0;
//			Iterator<String> iteratorPDFs = inputPdfPaths.iterator();
//
//			// Create Readers for the pdfs.
//			while (iteratorPDFs.hasNext()) {
//				InputStream pdf = new FileInputStream(iteratorPDFs.next());
//				PdfReader pdfReader = new PdfReader(pdf);
//				readers.add(pdfReader);
//				totalPages += pdfReader.getNumberOfPages();
//			}
//			// Create a writer for the outputstream
//			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));
//
//			document.open();
//			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
//					BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
//			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
//			// data
//
//			PdfImportedPage page;
//			int currentPageNumber = 0;
//			int pageOfCurrentReaderPDF = 0;
//			Iterator<PdfReader> iteratorPDFReader = readers.iterator();
//
//			// Loop through the PDF files and add to the output.
//			while (iteratorPDFReader.hasNext()) {
//				PdfReader pdfReader = iteratorPDFReader.next();
//
//				// Create a new page in the target for each source page.
//				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
//					document.newPage();
//					pageOfCurrentReaderPDF++;
//					currentPageNumber++;
//					page = writer.getImportedPage(pdfReader,
//							pageOfCurrentReaderPDF);
//					cb.addTemplate(page, 0, 0);
//
//					// Code for pagination.
//					if (paginate) {
//						cb.beginText();
//						cb.setFontAndSize(bf, 9);
//						cb.showTextAligned(PdfContentByte.ALIGN_CENTER, ""
//								+ currentPageNumber + " of " + totalPages, 520,
//								5, 0);
//						cb.endText();
//					}
//				}
//				pageOfCurrentReaderPDF = 0;
//			}
//			document.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (document.isOpen()) document.close();
//		}
//	}
//
//	public static void addRestrictPermission(String inputPath, String outputPath) {
//		log.info("Enter addRestrictPermission()");
//		String OWNER_PASS = "a";
//		try {
//			log.debug("Saving to temp file {}", outputPath);
//			PdfReader pdfReader = new PdfReader(inputPath);
//			FileOutputStream os = new FileOutputStream(outputPath);
//			PdfStamper pdfStamper = new PdfStamper(pdfReader,
//					os);
//			pdfStamper.setEncryption(null, OWNER_PASS.getBytes(), 0,
//					PdfWriter.STRENGTH128BITS);
//			pdfStamper.close();
//			os.close();
//		} catch (DocumentException e) {
//			log.error("DocumentException in TestPdf.addRestrictPermission()", e);
//		} catch (IOException e) {
//			log.error("IOException in TestPdf.addRestrictPermission()", e);
//		}
//		log.debug("Exit addRestrictPermission()");
//	}
//	
//	/**
//	 * @param inputPath Input PDF file path
//	 * @param outputPath Output PDF file path
//	 * @param fromPage start page from input PDF file
//	 * @param toPage end page from input PDF file
//	 */
//	public static void splitPdf(String inputPath, String outputPath, int fromPage, int toPage) {
//		OutputStream outputStream = null;
//		Document document = new Document();
//		try {
//			outputStream = new FileOutputStream(outputPath);
//			PdfReader inputPDF = new PdfReader(inputPath);
//			int totalPages = inputPDF.getNumberOfPages();
//
//			// make fromPage equals to toPage if it is greater
//			if (fromPage > toPage) {
//				fromPage = toPage;
//			}
//			if (toPage > totalPages) {
//				toPage = totalPages;
//			}
//
//			// Create a writer for the outputstream
//			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
//
//			document.open();
//			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF data
//			PdfImportedPage page;
//
//			while (fromPage <= toPage) {
//				document.newPage();
//				page = writer.getImportedPage(inputPDF, fromPage);
//				cb.addTemplate(page, 0, 0);
//				fromPage++;
//			}
//			outputStream.flush();
//			document.close();
//			outputStream.close();
//		} catch (IOException e) {
//			log.error("IOException in TestPdf.splitPdf()", e);
//		} catch (DocumentException e) {
//			log.error("DocumentException in TestPdf.splitPdf()", e);
//		} finally {
//			if (document.isOpen()) document.close();
//			try {
//				if (outputStream != null) outputStream.close();
//			} catch (IOException e) {
//			}
//		}
//	}
//
//	public static void addWatermark(String inputPath, String outputPath,
//			String imagePath) {
//		try {
//			PdfReader reader = new PdfReader(inputPath);
//			PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(
//					outputPath));
//			Image watermark_image = Image.getInstance(imagePath);
//			watermark_image.setAbsolutePosition(200, 400);
//			PdfContentByte add_watermark;
//			int number_of_pages = reader.getNumberOfPages();
//			int i = 0;
//			while (i < number_of_pages) {
//				i++;
//				add_watermark = stamp.getUnderContent(i);
//				add_watermark.addImage(watermark_image);
//			}
//			stamp.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
