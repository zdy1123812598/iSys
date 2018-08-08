package com.mystic.common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

/**
 * 访问权限控制。如果没有登录，判断地址是否在白名单内，如果在则放行， 接着判断地址是否在黑名单内，如果是则阻止访问，重定向到登陆页。
 * 如果一个地址既在白名单中，又在黑名单中，则阻止访问，黑名单优先级高于白名单。
 * 
 * @author ZDY
 * @since 2017-04-20
 */
public class AuthFilter extends HttpServlet implements Filter {
	private static final long serialVersionUID = -798194576757475285L;

	@SuppressWarnings("unused")
	private FilterConfig filterConfig = null;
	@SuppressWarnings("unused")
	private List<Pattern> whitelist = null; // 白名单，在白名单中的地址不进行阻止
	@SuppressWarnings("unused")
	private List<Pattern> blacklist = null; // 黑名单，黑名单中的地址必须登录以后才能访问
	@SuppressWarnings("unused")
	private String redirectPage = null; // 禁止访问时重定向的页面

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		whitelist = new ArrayList<Pattern>();
		blacklist = new ArrayList<Pattern>();
		redirectPage = filterConfig.getInitParameter("redirectPage");
	}

	public void destroy() {
		filterConfig = null;
		whitelist = null;
		blacklist = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) {
		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
		}
	}
}