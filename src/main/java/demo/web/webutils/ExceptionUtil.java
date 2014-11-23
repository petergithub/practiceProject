package demo.web.webutils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;

public class ExceptionUtil {
	protected static final Logger logger = Logger
			.getLogger(ExceptionUtil.class);

	/**
	 * constructor: forbidden to use
	 */
	protected ExceptionUtil() {
		super();
	}

	/**
	 * @param throwable throwable object like exception
	 * @return Returns the stack trace text of the throwable object
	 */
	public static String getStackTraceText(Throwable throwable) {
		StringWriter strWriter = null;
		PrintWriter prtWriter = null;
		try {
			strWriter = new StringWriter();
			prtWriter = new PrintWriter(strWriter);
			throwable.printStackTrace(prtWriter);
			prtWriter.flush();
			return strWriter.toString();
		} catch (Exception ex2) {
			return throwable.getMessage();
		} finally {
			if (strWriter != null) try {
				strWriter.close();
			} catch (Exception ex) {
				logger.error("Exception in getStackTraceText()", ex);
			}
			if (prtWriter != null) try {
				prtWriter.close();
			} catch (Exception ex) {
				logger.error("Exception in getStackTraceText()", ex);
			}
		}
	}

}
