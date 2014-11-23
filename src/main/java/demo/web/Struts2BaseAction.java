package demo.web;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Sam The BaseAction is used for Struts 2.0
 */
public class Struts2BaseAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private static final String USERNAME_KEY = "key";

	// 取得当前登录的用户名
	protected String getLoginUsername() {
		return (String) ActionContext.getContext().getSession()
				.get(USERNAME_KEY);
	}

	// 判断当前用户是否超时
	protected boolean isTimeout() {
		return ActionContext.getContext().getSession().get(USERNAME_KEY) == null;
	}

	// 检查Session对象是否存在
	protected boolean isExistSession(String key) {
		return ActionContext.getContext().getSession().get(key) != null;
	}

	// 保存Session对象
	protected void setSession(String key, Object obj) {
		ActionContext.getContext().getSession().put(key, obj);
	}

	// 取得Session对象
	protected Object getSession(String key) {
		return ActionContext.getContext().getSession().get(key);
	}

	// 保存一条错误
	protected void saveActionError(String key) {
		super.addActionError(super.getText(key));
	}

	// 保存一个消息
	protected void saveActionMessage(String key) {
		super.addActionMessage(super.getText(key));
	}

	// 取得查询的URL
	protected String getRequestPath() {
		return ServletActionContext.getRequest().getServletPath();
	}
}
