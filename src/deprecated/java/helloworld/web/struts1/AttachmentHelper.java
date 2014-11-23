package helloworld.web.struts1;

import helloworld.web.webutils.WebConstants;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;

import utils.StringUtils;

public class AttachmentHelper {
	private static Logger logger = Logger.getLogger(AttachmentHelper.class);

	public static String saveAttachment(HttpServletRequest request,
			FormFile frmFile) throws IOException {
		String fileName = frmFile.getFileName();
		if (StringUtils.isEmpty(fileName)) {
			return null;
		}

		String rootDir = getRootDir(request);

		String path;
		path = rootDir + "/";
		// create the output directory if not exists
		File filePath = new File(path);
		if (!filePath.exists() && !filePath.mkdirs()) {
			logger.debug("failed to created upload directory");
			throw new IOException("Could not create the upload folder");
		}

		fileName = URLEncoder.encode(fileName, "GBK");
		// Construct the output file name

		String uniqueFileName = getUniqueFileName(fileName);
		String filePathName = path + "/" + uniqueFileName;
		String relativePathName = uniqueFileName;
		logger.debug("file saved to " + filePathName);
		InputStream stream = frmFile.getInputStream();
		// write the file to the file specified
		OutputStream bos = new FileOutputStream(filePathName);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
			bos.write(buffer, 0, bytesRead);
		}
		bos.close();
		// close the stream
		stream.close();

		// delete the temporary file
		frmFile.destroy();
		return relativePathName;
	}

	public static void removeAttachment(HttpServletRequest request,
			String fullFilePath) throws IOException {

		File filePath = new File(fullFilePath);
		filePath.delete();
	}

	private static String getUniqueFileName(String origFileName) {
		// remove all the blank spaces
		origFileName = origFileName.trim();
		origFileName = origFileName.replaceAll(" ", "");

		// append time stamp
		long timeStamp = GregorianCalendar.getInstance().getTimeInMillis();
		String sTimeStamp = Long.toString(timeStamp);
		String outputFileName = sTimeStamp + "." + origFileName;
		return outputFileName;
	}

	public static String getRootDir(HttpServletRequest request) {
		String rootDir = request.getSession().getServletContext()
				.getInitParameter(WebConstants.UPLOAD_DIR_KEY);
		return rootDir;
	}

	public static String getPathForDownload(HttpServletRequest request,
			String relativeFilePathName) {
		String strFullURL = getRootDir(request) + "/" + relativeFilePathName;
		logger.debug(strFullURL);
		return (strFullURL);
	}

	/*
	 * public static String getHttpURLForDownload(HttpServletRequest request,
	 * String relativeFilePathName) { String serverName =
	 * request.getServerName(); int serverPort = request.getServerPort(); String
	 * server = "http://" + serverName + ":" + serverPort; String context =
	 * request.getContextPath(); String uploadDir = (String)
	 * request.getSession()
	 * .getServletContext().getInitParameter(WebConstants.UPLOAD_DIR_KEY);
	 * String strFullURL = server + context + "/" + uploadDir + "/" +
	 * relativeFilePathName; return (strFullURL); }
	 */

	public static String getFileURL(HttpServletRequest request, String fileName) {
		try {
			fileName = java.net.URLEncoder.encode(fileName, "GBK");
		} catch (Exception e) {
			logger.error("error when encoding attachment uri");
		}
		StringBuffer sb = new StringBuffer();
		sb.append(request.getScheme()).append("://");
		sb.append(request.getServerName());
		if (80 != request.getServerPort()) {
			sb.append(':').append(request.getServerPort());
		}
		sb.append('/').append(
				request.getSession().getServletContext()
						.getServletContextName());
		String relPath = request.getSession().getServletContext()
				.getInitParameter(WebConstants.RELATIVE_UPLOAD_PATH);
		if (relPath != null && relPath.length() > 0) {
			sb.append('/').append(relPath);
		}
		sb.append('/').append(fileName);

		return sb.toString();
	}

	public static void downLoad(String filePath, HttpServletResponse response,
			boolean isOnLine) throws Exception {
		File f = new File(filePath);
		if (!f.exists()) {
			response.sendError(404, "File not found!");
			return;
		}
		BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
		byte[] buf = new byte[1024];
		int len = 0;

		response.reset();
		if (isOnLine) {
			URL u = new URL("file:///" + filePath);
			response.setContentType(u.openConnection().getContentType());
			response.setHeader("Content-Disposition",
					"inline; filename=" + f.getName());
		} else {
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ f.getName());
		}
		OutputStream out = response.getOutputStream();
		while ((len = br.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		br.close();
		out.close();
	}

	public static void downLoad(String filePath, String realName,
			HttpServletResponse response, boolean isOnLine) throws Exception {
		File f = new File(filePath);
		if (!f.exists()) {
			response.sendError(404, "File not found!");
			return;
		}
		BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
		byte[] buf = new byte[1024];
		int len = 0;

		response.reset();
		if (isOnLine) {
			URL u = new URL("file:///" + filePath);
			response.setContentType(u.openConnection().getContentType());
			response.setHeader("Content-Disposition", "inline; filename="
					+ realName);
		} else {
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ realName);
		}
		OutputStream out = response.getOutputStream();
		while ((len = br.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		br.close();
		out.close();
	}
}
