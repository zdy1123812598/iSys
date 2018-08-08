package com.mystic.login;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.mystic.common.Constants;
import com.mystic.common.ResultInfo;
import com.mystic.link.Link;
import com.mystic.user.User;
import com.mystic.user.UserManager;
import com.mystic.util.LoggerUtils;
import com.mystic.util.ServletUtils;
import com.mystic.util.StringUtils;

@Controller
public class LoginController {
	@SuppressWarnings("unused")
	private Log logger = LogFactory.getLog(LoginController.class);

	@Autowired
	public LoginManager loginManager;

	@Autowired
	public UserManager userManager;

	@Autowired
	private LoggerUtils loggerUtils;

	// 登录验证
	@RequestMapping(value = "login/login.htm")
	public @ResponseBody
	ResultInfo login(String logonname, String password,
			HttpServletRequest request) throws IOException {
		loggerUtils.log2DB("登录验证:login/login.htm", 0);
		ResultInfo result = new ResultInfo();
		User user = new User();
		Map<String, Object> data = new HashMap<String, Object>();
		user = userManager.getUser(logonname, password);
		if (user.getUserid() > 0) {
			loggerUtils.log2DB("登录验证:login/login.htm", user.getUserid());
			if (user.getStatus() == 1) {
				WebUtils.setSessionAttribute(request, Constants.USER_SESSION,
						user);
				result.setData(data);
				data.put("userid", user.getUserid());
				data.put("username", user.getUsername());
				data.put("logonname", user.getLogonname());
				result.setMessage("用户登录!");
				result.setCode(1);
			} else {
				result.setCode(0);
				result.setMessage("用户没有登录权限!");
			}

		} else {
			result.setCode(0);
			result.setMessage("用户名或密码错误!");
		}
		return result;
	}

	// 登出
	@RequestMapping(value = "login/logout.htm")
	public @ResponseBody
	ResultInfo logout(HttpServletRequest request) {
		Object obj = WebUtils.getSessionAttribute(request,
				Constants.USER_SESSION);
		User user = (User) obj;
		loggerUtils.log2DB("登出:login/logout.htm", user.getUserid());
		ResultInfo result = new ResultInfo();
		try {
			result.setData(ServletUtils.getLoginPage(request, ""));
			result.setCode(ServletUtils.invalidateSession(request));
		} catch (Exception e) {
			LoggerUtils.error(e);
			result.setCode(-1);
		}
		return result;
	}

	// 登录跳转
	@SuppressWarnings("unused")
	@RequestMapping(value = { "login/main.htm" })
	public String login(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws IOException {
		Object obj = WebUtils.getSessionAttribute(request,
				Constants.USER_SESSION);
		User user = (User) obj;
		loggerUtils.log2DB("登录跳转:login/main.htm", user.getUserid());

		Map<String, Object> data = new HashMap<String, Object>();
		if (user.getUserid() > 0) {
			model.put("userid", user.getUserid());
			model.put("username", user.getUsername());
			model.put("logonname", user.getLogonname());
			// 根据人员获取角色,根据角色获取菜单项
			Set<Link> links = loginManager.linkListByid(user.getUserid());
			try {
				model.put("link", StringUtils.serializeToJson(links));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "../main/index.jsp?dt=" + new Date().getTime();

		} else {
			return "../login/login.jsp?dt=" + new Date().getTime();
		}
	}

}
