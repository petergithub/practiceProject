package demo.web;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Downloading Multiple Files using "multipart/x-mixed-replace".
 *
 * @author JavaDigest
 */
public class DownloadMultipleFilesForFirefoxOnly extends HttpServlet {

  private static final long serialVersionUID = 3305561818342965462L;

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // Set the response type and specify the boundary string
    response.setContentType("multipart/x-mixed-replace;boundary=END");

    // Set the content type based on the file type you need to download
    String contentType = "Content-type: text/rtf";

    // List of files to be downloaded
    List<File> files = new ArrayList<File>();
    files.add(new File("C:/first.txt"));
    files.add(new File("C:/second.txt"));
    files.add(new File("C:/third.txt"));

    ServletOutputStream out = response.getOutputStream();

    // Print the boundary string
    out.println();
    out.println("--END");

    for (File file : files) {

      // Get the file
      FileInputStream fis = null;
      try {
        fis = new FileInputStream(file);

      } catch (FileNotFoundException fnfe) {
        // If the file does not exists, continue with the next file
        System.out.println("Could not find file " + file.getAbsolutePath());
        continue;
      }

      BufferedInputStream fif = new BufferedInputStream(fis);

      // Print the content type
      out.println(contentType);
      out.println("Content-Disposition: attachment; filename=" + file.getName());
      out.println();

      System.out.println("Sending file " + file.getName());

      // Write the contents of the file
      int data = 0;
      while ((data = fif.read()) != -1) {
        out.write(data);
      }
      fif.close();

      // Print the boundary string
      out.println();
      out.println("--END");
      out.flush();
      System.out.println("Finished sending file " + file.getName());
    }

    // Print the ending boundary string
    out.println("--END--");
    out.flush();
    out.close();
  }

}