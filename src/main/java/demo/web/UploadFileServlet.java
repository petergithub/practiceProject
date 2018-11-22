package demo.web;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class UploadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String uploadPath = "D:\\upload\\";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		upload(request, uploadPath);
	}

	@SuppressWarnings("unused")
	private void upload(HttpServletRequest request, String upload) {
		try {
			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();
			// Create a new file upload handler
			ServletFileUpload uploadServletFile = new ServletFileUpload(factory);
			// Parse the request /**//* FileItem */
			List<?> items = uploadServletFile.parseRequest(request);
			// Process the uploaded items
			Iterator<?> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {
					// Process a regular form field
					String name = item.getFieldName();
					String value = item.getString();
				} else if (!item.isFormField()) {
					// Process a file upload
					String fieldName = item.getFieldName();
					// get the file name form the "item.getName()" which
					// contains the path and name.
					String fileName = item.getName();
					String contentType = item.getContentType();
					long sizeInBytes = item.getSize();
					File uploadedFile = new File(uploadPath + "fileName.txt");
					item.write(uploadedFile);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
