package helloworld.web.struts1;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class WebAppUtils {
	private static Logger logger = Logger
			.getLogger(WebAppUtils.class.getName());

	/**
	 * Get full context path of URL, i.e. http://localhost:port
	 * 
	 * @param request
	 * @return String
	 */
	public static String getFullContextPath(HttpServletRequest request) {
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		String context = request.getContextPath();
		String fullContext = "http://" + serverName + ":" + serverPort
				+ context;
		logger.debug("Context Path: " + fullContext);
		return fullContext;
	}

	/**
	 * Get servlet name from given mapping.
	 * 
	 * @param mapping
	 * @param name
	 * @return String
	 */
	public static String getFullContext(HttpServletRequest request,
			ActionMapping mapping, String name) {
		ActionForward forward = mapping.findForward(name);
		String path = forward.getPath();
		return getFullContextPath(request)
				+ path.substring(path.indexOf("/") + 1);
	}

	/**
	 * Get servlet name from given mapping.
	 * 
	 * @param mapping
	 * @param name
	 * @return String
	 */
	public static String getServletName(ActionMapping mapping, String name) {
		ActionForward forward = mapping.findForward(name);
		String path = forward.getPath();
		return path.substring(path.indexOf("/") + 1);
	}

	/**
	 * Retrieves an object from HttpSession.
	 * 
	 * @param req The HTTP request we are processing
	 * @param name The name of the attribute to be retrieved.
	 */
	public static Object getSessionAttribute(HttpServletRequest req, String name) {
		Object obj = null;
		HttpSession session = req.getSession(false);
		if (session != null) {
			obj = session.getAttribute(name);
		}
		return obj;
	}

	/**
	 * Retrieves an object from HttpSession.
	 * 
	 * @param req The HTTP request we are processing
	 * @param name The name of the attribute to be retrieved.
	 */
	public static Object getApplicationAttribute(HttpServletRequest req,
			String name) {
		Object obj = null;
		HttpSession session = req.getSession(false);
		if (session != null) {
			ServletContext app = session.getServletContext();
			if (app != null) {
				obj = app.getAttribute(name);
			}
		}
		return obj;
	}

	/**
	 * Retrieves an object from HttpServletRequest.
	 * 
	 * @param req The HTTP request we are processing
	 * @param name The name of the attribute to be retrieved.
	 */
	public static Object getRequestAttribute(HttpServletRequest req, String name) {
		logger.debug("Getting " + name + " from request.");
		Object obj = null;
		if (req != null) {
			obj = req.getAttribute(name);
		}
		return obj;
	}

	/**
	 * Judy created for compose a request string Given
	 * "listService.do, action, list" Return "listService.do?action=list" Given
	 * "listService.do?action=list, type, all" Return
	 * "listService.do?action=list&type=all"
	 * 
	 * @param url String
	 * @param key String
	 * @param value String
	 * @return String
	 */
	public static String composeRequest(String url, String key, String value) {
		if (url == null) {
			return "";
		}
		if (url.lastIndexOf("?") > 0) {
			url += "&" + key + "=" + value;
		} else {
			url += "?" + key + "=" + value;
		}
		return url;
	}
}
