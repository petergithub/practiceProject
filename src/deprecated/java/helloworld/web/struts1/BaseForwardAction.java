package com.helloworld.web.struts1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

import com.helloworld.web.common.ErrorBean;
import com.helloworld.web.common.SessionTimeoutException;
import com.helloworld.web.webutils.WebAppUtils;
import com.helloworld.web.webutils.WebConstants;

/**
 * Base servlet encapsulating common servlet functionality.
 */
public class BaseForwardAction extends ForwardAction {

	/**
	 * Process the specified HTTP request, and create the corresponding HTTP response (or forward to another
	 * web component that will create it). Return an <code>ActionForward</code> instance describing where and
	 * how control should be forwarded. <br>
	 * Encapsulates common action functionality including error handling.
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
			forward = super.execute(mapping, form, request, response);
		} catch (Exception e) {
			ErrorBean error = new ErrorBean(e);
			request.setAttribute(WebConstants.ERROR_KEY, error);
			return mapping.findForward("error");
		}
		return forward;
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

	protected Object getSessionAttribute(HttpServletRequest req, String name) {
		return WebAppUtils.getSessionAttribute(req, name);
	}

	public String getCurrentModule(HttpServletRequest request) {
		return (String) getSessionAttribute(request, WebConstants.SES_CURRENT_MODULE);
	}

}
