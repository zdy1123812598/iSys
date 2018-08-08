package com.mystic.util;

import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.util.WebUtils;

import com.mystic.common.Constants;

public class ServletUtils {
	/**
	 * 输出JSON到客户端
	 * 
	 * @param response
	 * @param sMsg
	 */
	public static void writeJsonToResponse(ServletResponse response,
			Object object) {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			writer = response.getWriter();
			mapper.writeValue(writer, object);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}

	public static String getLoginPage(HttpServletRequest request,
			String defaultPage) {
		return WebUtils.getSessionAttribute(request, Constants.LOGIN_SESSION) == null ? defaultPage
				: WebUtils
						.getSessionAttribute(request, Constants.LOGIN_SESSION)
						.toString();
	}

	public static int invalidateSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Enumeration<String> enumeration = session.getAttributeNames();
		while (enumeration.hasMoreElements()) {
			String sessionName = (String) enumeration.nextElement();
			LoggerUtils.debug("logout:" + sessionName + " removed");
			session.removeAttribute(sessionName);
		}
		request.getSession().invalidate();
		return 0;
	}

	public static Object formatStringToNull(String str) {
		return str == null || "".equals(str.trim()) ? null : str;
	}
}
