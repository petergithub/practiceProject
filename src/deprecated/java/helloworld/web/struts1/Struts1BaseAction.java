package helloworld.web.struts1;

import helloworld.web.common.ErrorBean;
import helloworld.web.common.SessionTimeoutException;
import helloworld.web.webutils.WebConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Base servlet encapsulating common servlet functionality.
 * used for struts 1
 */
public abstract class Struts1BaseAction extends Action {
	private static Logger logger = Logger.getLogger(Struts1BaseAction.class);
	
	/**
	 * <p>
	 * Process the specified HTTP request, and create the corresponding HTTP response (or forward to another
	 * web component that will create it). Return an <code>ActionForward</code> instance describing where and
	 * how control should be forwarded. <br>
	 * All subclasses override this method with specific action functionality.
	 * </p>
	 * 
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 */
	public abstract ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception;

	/**
	 * <p>
	 * Process the specified HTTP request, and create the corresponding HTTP response (or forward to another
	 * web component that will create it). Return an <code>ActionForward</code> instance describing where and
	 * how control should be forwarded. <br>
	 * Encapsulates common action functionality including error handling.
	 * </p>
	 * 
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActionForward forward;
		try {
			checkSession(request, mapping);
		} catch (SessionTimeoutException e) {
			return mapping.findForward("session.timeout");
		}

		try {
			forward = executeAction(mapping, form, request, response);
		} catch (Exception e) {
			ErrorBean error = new ErrorBean(e);
			request.setAttribute(WebConstants.ERROR_KEY, error);
			return mapping.findForward("error");
		}
		return forward;
	}

	/**
	 * <p>
	 * Removes an attribute from HttpSession.
	 * </p>
	 * 
	 * @param req The HTTP request we are processing
	 * @param name The name of the attribute to be removed.
	 */
	protected void removeSessionAttribute(HttpServletRequest req, String name) {
		logger.debug("Removing " + name + " from session.");
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.removeAttribute(name);
		}
	}

	/**
	 * <p>
	 * Sets an object to the HttpSession
	 * </p>
	 * 
	 * @param req The HTTP request we are processing
	 * @param name The name key of object.
	 * @param obj The object to be set on HttpSession.
	 */
	protected void setSessionAttribute(HttpServletRequest req, String name, Object obj) {
		logger.debug("Setting " + name + " of type " + obj.getClass().getName() + " on session.");
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.setAttribute(name, obj);
		}
	}

	/**
	 * <p>
	 * Retrieves an object from HttpSession.
	 * </p>
	 * 
	 * @param req The HTTP request we are processing
	 * @param name The name of the attribute to be retrieved.
	 */
	protected Object getSessionAttribute(HttpServletRequest req, String name) {
		return WebAppUtils.getSessionAttribute(req, name);
	}

	/**
	 * <p>
	 * Retrieves an object from Application.
	 * </p>
	 * 
	 * @param req The HTTP request we are processing
	 * @param name The name of the attribute to be retrieved.
	 */
	protected Object getApplicationAttribute(HttpServletRequest req, String name) {
		return WebAppUtils.getApplicationAttribute(req, name);
	}

	/**
	 * <p>
	 * Removes an attribute from HttpServletRequest.
	 * </p>
	 * 
	 * @param req The HTTP request we are processing
	 * @param name The name of the attribute to be removed.
	 */
	protected void removeRequestAttribute(HttpServletRequest req, String name) {
		logger.debug("Removing " + name + " from request.");
		if (req != null) {
			req.removeAttribute(name);
		}
	}

	/**
	 * <p>
	 * Sets an object to the HttpServletRequest
	 * </p>
	 * 
	 * @param req The HTTP request we are processing
	 * @param name The name key of object.
	 * @param obj The object to be set on HttpServletRequest.
	 */
	protected void setRequestAttribute(HttpServletRequest req, String name, Object obj) {
		logger.debug("Setting " + name + " of type " + obj.getClass().getName() + " on request.");
		if (req != null) {
			req.setAttribute(name, obj);
		}
	}

	/**
	 * <p>
	 * Retrieves an object from HttpServletRequest.
	 * </p>
	 * 
	 * @param req The HTTP request we are processing
	 * @param name The name of the attribute to be retrieved.
	 */
	protected Object getRequestAttribute(HttpServletRequest req, String name) {
		return WebAppUtils.getRequestAttribute(req, name);
	}

	protected void checkSession(HttpServletRequest request, ActionMapping mapping)
			throws SessionTimeoutException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute(WebConstants.SES_CURRENT_USER) == null) {
			throw new SessionTimeoutException();
		}
	} 

//	public User getCurrentUser(HttpServletRequest request) {
//		User user = (User) getSessionAttribute(request, WebConstants.SES_CURRENT_USER);
//		return user;
//	}

	public String getCurrentModule(HttpServletRequest request) {
		return (String) getSessionAttribute(request, WebConstants.SES_CURRENT_MODULE);
	}

}
