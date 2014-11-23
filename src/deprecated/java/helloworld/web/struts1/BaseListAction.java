package com.helloworld.web.struts1;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.helloworld.web.pageutil.ISearchCondition;
import com.helloworld.web.pageutil.ResultPageInfo;
import com.helloworld.web.webutils.WebConstants;

/**
 * BaseListAction is a template action encapsulating common functionality for a list page. Use when the list
 * page need to suport function of next previous page.
 */
public abstract class BaseListAction extends BaseLookupDispatchAction {
	public ActionForward defaultMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return list(mapping, form, request, response);
	}

	/**
	 * This function should init the page setting and show the page according to the page setting
	 * 
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ISearchCondition condition = prepareSearchCondition(form, request);
		int count = getTotalCount(request, condition);
		initPageConfig(request, count);
		return show(mapping, form, request, response);
	}

	/**
	 * This function should be implemented to show the page according to the page setting
	 * 
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ISearchCondition condition = getSearchConditionForShow(request);
		List<?> searchResult = getSearchResult(request, condition);
		if (searchResult == null) {
			searchResult = new ArrayList<Object>();
		}
		super.setRequestAttribute(request, WebConstants.REQ_SEARCHRESULT_LIST, searchResult);
		return mapping.findForward("searchResult");
	}

	/**
	 * Refresh The Page
	 * 
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ISearchCondition condition = getSearchConditionForRefresh(request);
		int count = getTotalCount(request, condition);
		refreshPageConfig(request, count);
		return show(mapping, form, request, response);
	}

	/**
	 * Go to next page
	 * 
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward next(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		handleNextAndPreviousEvent(mapping, form, request, response, 1);
		return show(mapping, form, request, response);
	}

	/**
	 * Go to previous page
	 * 
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward previous(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		handleNextAndPreviousEvent(mapping, form, request, response, -1);
		return show(mapping, form, request, response);
	}

	/**
	 * Jump to page No
	 * 
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward gotoPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int pageNo = 0;
		try {
			pageNo = Integer.parseInt(request.getParameter(WebConstants.PARAM_PAGE_NO));
		} catch (Exception e) {
			pageNo = 1;
		}
		ResultPageInfo page = (ResultPageInfo) super.getSessionAttribute(request,
				WebConstants.SES_SEARCH_RESULT_PAGE_INFO);
		page.gotoPage(pageNo);
		super.setSessionAttribute(request, WebConstants.SES_SEARCH_RESULT_PAGE_INFO, page);
		return show(mapping, form, request, response);

	}

	/**
	 * Change the page config value when next/previous pressed
	 * 
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param step int
	 * @throws Exception
	 */
	private void handleNextAndPreviousEvent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, int step) throws Exception {

		ResultPageInfo page = (ResultPageInfo) super.getSessionAttribute(request,
				WebConstants.SES_SEARCH_RESULT_PAGE_INFO);
		if (step == 1) {
			page.next();
		} else if (step == -1) {
			page.previous();
		}
		super.setSessionAttribute(request, WebConstants.SES_SEARCH_RESULT_PAGE_INFO, page);
	}

	/**
	 * Init the page config, it will be called by the list function
	 * 
	 * @param request HttpServletRequest
	 * @param totalRecords int
	 */
	public void initPageConfig(HttpServletRequest request, int totalRecords) {
		// Remove the previous page setting
		removeSessionAttribute(request, WebConstants.SES_SEARCH_RESULT_PAGE_INFO);
		String pageSize_Str = request.getSession(true).getServletContext()
				.getInitParameter(WebConstants.PAGESIZE);
		ResultPageInfo page = new ResultPageInfo(pageSize_Str, totalRecords);

		// Set the page setting
		setSessionAttribute(request, WebConstants.SES_SEARCH_RESULT_PAGE_INFO, page);
	}

	/**
	 * Refresh the page config
	 * 
	 * @param request HttpServletRequest
	 * @param totalRecords int
	 */
	public void refreshPageConfig(HttpServletRequest request, int totalRecords) {
		ResultPageInfo page = (ResultPageInfo) getSessionAttribute(request,
				WebConstants.SES_SEARCH_RESULT_PAGE_INFO);
		page.setTotalRecords(totalRecords);
		page.refresh();
		// Set the page setting
		setSessionAttribute(request, WebConstants.SES_SEARCH_RESULT_PAGE_INFO, page);
	}

	/**
	 * When list the page for the first time or the search button pressed Prepare the search condition
	 * 
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @return ISearchCondition
	 * @throws Exception
	 */
	public abstract ISearchCondition prepareSearchCondition(ActionForm form, HttpServletRequest request)
			throws Exception;

	public abstract ISearchCondition getSearchConditionForRefresh(HttpServletRequest request)
			throws Exception;

	public abstract ISearchCondition getSearchConditionForShow(HttpServletRequest request) throws Exception;

	public abstract int getTotalCount(HttpServletRequest request, ISearchCondition condition)
			throws Exception;

	public abstract List<?> getSearchResult(HttpServletRequest request, ISearchCondition condition)
			throws Exception;

}
