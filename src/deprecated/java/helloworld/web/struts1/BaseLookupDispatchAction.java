package helloworld.web.struts1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

import com.helloworld.web.common.ErrorBean;
import com.helloworld.web.common.SessionTimeoutException;
import com.helloworld.web.webutils.WebAppUtils;
import com.helloworld.web.webutils.WebConstants;

/**
 * <p>
 * Base Action encapsulating common lookup dispatch functionality. Use when form page contains mulitple submit
 * buttons.
 * </p>
 */
public abstract class BaseLookupDispatchAction extends LookupDispatchAction {
	private static Logger logger = Logger.getLogger(BaseLookupDispatchAction.class.getName());

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
			// Added by Lin to check whether the current user is in a session
			checkSession(request, mapping);

		} catch (SessionTimeoutException e) {
			return mapping.findForward("session.timeout");
		}
		try {

			if (request.getParameter(mapping.getParameter()) == null) {
				logger.debug("Default method call.");
				forward = defaultMethod(mapping, form, request, response);
			} else {
				logger.debug("Executing LookupDispatchAction.");
				forward = super.execute(mapping, form, request, response);
			}
		} catch (Exception e) {
			ErrorBean error = new ErrorBean(e);
			request.setAttribute(WebConstants.ERROR_KEY, error);
			return mapping.findForward("error");
		}
		return forward;
	}

	/**
	 * <p>
	 * All sub-classes must implement this method where if not action is supplied the defaultMethod
	 * implementation will be executed.
	 * </p>
	 * 
	 * @param mapping The ActionMapping used to select this instance
	 * @param form The optional ActionForm bean for this request (if any)
	 * @param request The HTTP request we are processing
	 * @param response The HTTP response we are creating
	 */
	public abstract ActionForward defaultMethod(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception;

	/**
	 * <p>
	 * Removes an attribute from HttpSession.
	 * </p>
	 * 
	 * @param req The HTTP request we are processing
	 * @param name The name of the attribute to be removed.
	 */
	public void removeSessionAttribute(HttpServletRequest req, String name) {
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
	public void setSessionAttribute(HttpServletRequest req, String name, Object obj) {
		logger.debug("Setting " + name + " of type " + obj.getClass().getName() + " on session.");
		HttpSession session = req.getSession(false);

		if (session != null) {
			session.setAttribute(name, obj);
		}
	}

	/**
	 * <p>
	 * Retrieves an object from Application.
	 * </p>
	 * 
	 * @param req The HTTP request we are processing
	 * @param name The name of the attribute to be retrieved.
	 */
	public Object getApplicationAttribute(HttpServletRequest req, String name) {
		return WebAppUtils.getApplicationAttribute(req, name);
	}

	/**
	 * <p>
	 * Retrieves an object from HttpSession.
	 * </p>
	 * 
	 * @param req The HTTP request we are processing
	 * @param name The name of the attribute to be retrieved.
	 */
	public Object getSessionAttribute(HttpServletRequest req, String name) {
		return WebAppUtils.getSessionAttribute(req, name);
	}

	/**
	 * <p>
	 * Removes an attribute from HttpServletRequest.
	 * </p>
	 * 
	 * @param req The HTTP request we are processing
	 * @param name The name of the attribute to be removed.
	 */
	public void removeRequestAttribute(HttpServletRequest req, String name) {
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
	public void setRequestAttribute(HttpServletRequest req, String name, Object obj) {
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
	public Object getRequestAttribute(HttpServletRequest req, String name) {
		return WebAppUtils.getRequestAttribute(req, name);
	}

	public void checkSession(HttpServletRequest request, ActionMapping mapping)
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
