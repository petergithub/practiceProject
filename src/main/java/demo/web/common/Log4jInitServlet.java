package demo.web.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log4jInitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	protected static final Logger logger = Logger
			.getLogger(Log4jInitServlet.class);

	public void init() {
		int debug = 0;

		String value = getServletConfig().getInitParameter("debug");
		String prefix = getServletContext().getRealPath("/");
		String logFile = getInitParameter("log4j-init-file");

		try {
			debug = Integer.parseInt(value);
		} catch (Throwable t) {
			debug = 0;
		}

		if (debug >= 1) {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"MMM d, yyyy H:mm:ss a z");
			Date today = new Date();
			String output = "<" + formatter.format(today)
					+ "> <Debug> <MarshCM Monitor>";
			logger.info(output + " log4j property file: " + logFile);
		}

		if (logFile != null) {
			PropertyConfigurator.configure(prefix + logFile);
		}
	}

	public void init(ServletConfig config) throws ServletException {
		logger.info("Log4JInitServlet is initializing log4j");
		String log4jLocation = config
				.getInitParameter("log4j-properties-location");

		ServletContext sc = config.getServletContext();

		if (log4jLocation == null) {
			logger.error("*** No log4j-properties-location init param, so initializing log4j with BasicConfigurator");
			BasicConfigurator.configure();
		} else {
			String webAppPath = sc.getRealPath("/");
			String log4jProp = webAppPath + log4jLocation;
			File file = new File(log4jProp);
			if (file.exists()) {
				logger.info("Initializing log4j with: " + log4jProp);
				PropertyConfigurator.configure(log4jProp);
			} else {
				logger.error("*** "
						+ log4jProp
						+ " file not found, so initializing log4j with BasicConfigurator");
				BasicConfigurator.configure();
			}
		}
		super.init(config);
	}

}
