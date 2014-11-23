package doing;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenPdfServlet extends HttpServlet {
	private static final long serialVersionUID = -6385253102826694932L;
	private static final Logger log = LoggerFactory.getLogger(OpenPdfServlet.class);

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		log.debug("Enter doPost()");
		File file = null;
		this.request = req;
		this.response = resp;

		try {
			String filePath = "c:/cache/a.pdf";
			log.info("filePath = {}", filePath);
			file = new File(filePath);

			// obtains ServletContext
			ServletContext context = getServletContext();
			// gets MIME type of the file
			String mimeType = context.getMimeType(filePath);
			if (mimeType == null) {
				// set to binary type if MIME mapping not found
				mimeType = "application/pdf";
				mimeType = "application/octet-stream";
			}
			// modifies response
			response.setContentType(mimeType);
			response.setContentLength((int) file.length());

			String headerKey = "Content-Disposition";
			
			// forces download: attachment
//			String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
			// open it in browser: inline
			String headerValue = String.format("inline; filename=\"%s\"", file.getName());
			log.info("file.getName() = {}", file.getName());
			response.setHeader(headerKey, headerValue);

			exportFile(file);
		} catch (Exception e) {
			log.error("DfException in ControlledCopyServlet.doPost()", e);
		} finally {
//			if (file != null) {
//				file.delete();
//			}
		}

		log.debug("Exit doPost()");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		doPost(req, resp);
	}

	private void exportFile(File file) throws IOException {
		log.debug("Enter exportFile()");
		ServletOutputStream out = response.getOutputStream();
		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		int contentLength = (int) file.length();
		log.debug("Sending out content {}", contentLength);
		try {
			byte[] buffer = new byte[10240];
			int len;
			while ((len = inputStream.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
			}
		}
		log.debug("Exit exportFile()");
	}
}
