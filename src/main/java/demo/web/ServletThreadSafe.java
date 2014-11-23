package demo.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletThreadSafe extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger log = LoggerFactory.getLogger(ServletThreadSafe.class);
	private static Object thisIsNotThreadsafe;
	private Object thisIsAlsoNotThreadsafe;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Object thisIsThreadsafe;

		thisIsNotThreadsafe = request.getParameter("foo"); // BAD! Shared among all requests.
		thisIsAlsoNotThreadsafe = request.getParameter("foo"); // BAD! Shared among all requests.
		thisIsThreadsafe = request.getParameter("foo"); // Good.
		log.debug("thisIsNotThreadsafe = {} thisIsAlsoNotThreadsafe = {} thisIsThreadsafe = {}",
				new Object[] { thisIsNotThreadsafe, thisIsAlsoNotThreadsafe, thisIsThreadsafe });
	}
}
