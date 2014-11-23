package demo.web.webutils;

public class WebConstants {
	public final static String ERROR_KEY = "errorBean";
	// For application.
	public final static String APP_LABEL_MAP = "appLabelMap";
	public final static String APP_ROLE_LIST = "app_RoleList";
	public final static String APP_ROLE_LIST_WITH_EMPTY = "app_RoleListWithEmpty";
	public final static String APP_ROLE_LIST_FOR_VENDOR = "app_RoleListForVendor";
	public final static String APP_ROLE_LIST_FOR_VENDOR_WITH_EMPTY = "app_RoleListForVendorWithEmpty";
	public final static String APP_USERNAME_LIST = "app_UserNameList";
	public final static String APP_FOLDERNAME_LIST = "app_folderNameList";
	public final static String APP_SORT_ITEM_LIST = "app_sortItemList";
	public final static String APP_DOC_ID_LIST_WITH_EMPTY = "app_docIdList";
	public final static String APP_DOC_NAME_LIST_WITH_EMPTY = "app_docNameList";
	public final static String APP_DOC_PROCESS_LIST_WITH_EMPTY = "app_docProcessList";
	public final static String APP_DOC_MODUEL_NAME_LIST_WITH_EMPTY = "app_docModuleNameList";
	public final static String APP_CATEGORY_LIST_WITH_EMPTY = "app_categoryList";
	public final static String APP_UPLOADBY_LIST_WITH_EMPTY = APP_USERNAME_LIST;

	public final static String APP_DOC_ID_LIST = APP_DOC_ID_LIST_WITH_EMPTY;
	public final static String APP_DOC_NAME_LIST = APP_DOC_NAME_LIST_WITH_EMPTY;
	public final static String APP_PROCESS_LIST = APP_DOC_PROCESS_LIST_WITH_EMPTY;
	public final static String APP_MODULE_NAME_LIST = APP_DOC_MODUEL_NAME_LIST_WITH_EMPTY;

	public final static String APP_NEW_DOC_ID_LIST = "app_newDodIdList";
	public final static String APP_NEW_DOC_NAME_LIST = "app_newDocNameList";
	public final static String APP_NEW_PROCESS_LIST = "app_newProcessList";
	public final static String APP_NEW_MODULE_NAME_LIST = "app_newModuleNameList";

	// For context init parameter
	public final static String PAGESIZE = "pageSize";
	public final static String UPLOAD_DIR_KEY = "uploadDirKey";
	public final static String RELATIVE_UPLOAD_PATH = "RelativePath";

	// Session key string.
	public final static String SES_CURRENT_USER = "ses_Current_User";
	public final static String SES_TAB_ID = "ses_Tab_Id";
	public final static String SES_CURRENT_MODULE = "ses_CurrentModule";
	public final static String SES_SEARCH_RESULT_PAGE_INFO = "ses_SearchResultPageInfo";
	public final static String SES_USER_FILTER = "UserSearchBean";
	public final static String SES_CURRENT_FOLDER = "ses_CurrentFolder";

	// Request parameter key string.
	public final static String PARAM_TAB_ID = "param_Tab_Name";
	public final static String PARAM_ID = "id";
	public final static String PARAM_NEW_PASSWORD = "param_NewPwd";
	public final static String PARAM_PAGE_NO = "param_PageNo";
	public final static String PARAM_FOLDER_ID = "param_FolderId";

	public final static String PARAM_HAVE_SAME_FOLDERNAME = "have_Same_Folder_Name";
	public final static String PARAM_FOLDER_NOT_EMPTY = "Folder_Is_Not_Empty";

	// Request Attribute key string.
	public final static String USER_BEAN = "UserBean";
	public final static String FOLDER_BEAN = "FolderBean";
	public final static String EDIT_CONTENT_BEAN = "EditContentBean";
	public final static String REQ_SEARCHRESULT_LIST = "req_SearchResultList";
	public final static String REQ_CONTENTS_LIST_XML = "req_ContentsListXML";
	public final static String REQ_FOLDERS_LIST_XML = "req_FoldersListXML";
	public final static String REQ_FOLDERNAME_LIST = "req_FolderNameList";
	public final static String REQ_USERNAME_LIST = "req_UserNameList";
	public final static String REQ_SEARCHRESULT_FLAG = "req_searchResultFlag";

	// ///////////////////////////////////////////////////////////////////////////
	public final static String ACTION = "action";

	// User role id, start from 200.
	public final static String ROLE_NOT_SPECIFIED = "";
	public final static String ROLE_CONTENT_PROVIDER = "ContentProvider";
	public final static String ROLE_PROJECT_MANAGER = "ProjectManager";
	public final static String ROLE_SYSTEM_ADMIN = "SystemAdmin";

	// Sort Item.
	public final static String SORT_EMPTY = "";
	public final static String SORT_ALL = "All";
	public final static String SORT_DOC_ID = "docId";
	public final static String SORT_DOC_NAME = "docName";
	public final static String SORT_PROCESS = "process";
	public final static String SORT_VERSION = "version";
	public final static String SORT_MODULE = "module";
	public final static String SORT_CATEGORY = "category";
	public final static String SORT_SUBJECT = "subject";
	public final static String SORT_FILENAME = "fileName";
	public final static String SORT_UPLOADBY = "uploadBy";
	public final static String SORT_UPLOADDATE = "uploadDate";

	// Tab id, start from 300.
	public final static int TABID_CONTENT = 300;
	public final static int TABID_USER = 301;
	public final static int TABID_SEARCH = 302;

	// Tab name.
	public final static String TABNAME_CONTENT = "Content";
	public final static String TABNAME_USER = "User";

}
