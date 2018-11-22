package demo.web.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <pre>
 * web.xml 中配置
 *  <!--判断是否登录 filter -->
 *  <filter>
 *  <filter-name>sessionFilter</filter-name>
 *  <filter-class>com.cisp.bss.util.web.SessionFilter</filter-class>
 *  <init-param>
 *  <param-name>ignore</param-name>
 *  <param-value>false</param-value>
 *  </init-param>
 *  </filter>
 * </pre>
 */
public class SessionFilter implements Filter {
	protected FilterConfig filterConfig = null;
	protected boolean ignore;
	public final static String CURRENT_USER = "current_user";

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession(false);

		if ((!ignore) && session.getAttribute(CURRENT_USER) == null) {
			response.sendRedirect(request.getContextPath());
		}
		chain.doFilter(req, resp);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		String value = filterConfig.getInitParameter("ignore");
		if (value == null)
			this.ignore = false;
		else if (value.equalsIgnoreCase("false"))
			this.ignore = false;
		else if (value.equalsIgnoreCase("no"))
			this.ignore = false;
		else
			this.ignore = true;
	}

}
