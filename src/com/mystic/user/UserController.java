package com.mystic.user;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.mystic.common.Constants;
import com.mystic.common.ResultInfo;
import com.mystic.common.TreeNodeTemp;
import com.mystic.util.LoggerUtils;
import com.mystic.util.Pagination;
import com.mystic.util.ServletUtils;

@Controller
public class UserController {
	@SuppressWarnings("unused")
	private Log logger = LogFactory.getLog(UserController.class);

	@Autowired
	public UserManager userManager;

	@Autowired
	private LoggerUtils loggerUtils;

	// 分页类
	public Pagination getPagination(int rows, int page) {
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(page);
		pagination.setNumPerPage(rows);
		return pagination;
	}

	// 用户分页查询
	@RequestMapping(value = "user/userListPagination.htm")
	public @ResponseBody
	Map<String, Object> userListPagination(HttpServletRequest request,
			@RequestParam int page, @RequestParam int rows, String search) {
		Object obj = WebUtils.getSessionAttribute(request,
				Constants.USER_SESSION);
		User user = (User) obj;
		loggerUtils
				.log2DB("用户分页:user/userListPagination.htm", user.getUserid());
		Map<String, Object> param = new HashMap<String, Object>();
		String searchKey = (String) ServletUtils.formatStringToNull(search);
		param.put("userSearch", searchKey);

		Pagination pagination = getPagination(rows, page);

		userManager.userListPagination(pagination, param);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", pagination.getTotalRows());
		map.put("rows", pagination.getResultList());
		map.put("page", pagination.getCurrentPage());
		return map;
	}

	// 用户添加
	@RequestMapping(value = "user/userAdd.htm")
	public @ResponseBody
	ResultInfo userListPagination(String username, String logonname,
			int gender, String telephone, String password, int status,
			String organizationid, int creatorid) {
		loggerUtils.log2DB("用户添加:user/userAdd.htm", creatorid);
		ResultInfo result = new ResultInfo();
		User user = new User();
		user.setUsername(username);
		user.setLogonname(logonname);
		user.setGender(Integer.valueOf(gender));
		user.setPassword(password);
		user.setTelephone(telephone);
		user.setStatus(Integer.valueOf(status));
		user.setOrganizationid(Integer.valueOf(organizationid));
		user.setCreatorid(creatorid);
		user.setCreatetime(new Timestamp(System.currentTimeMillis()));
		try {
			int code = userManager.userAdd(user);
			result.setCode(code);
		} catch (Exception e) {
			LoggerUtils.error(e);
			result.setCode(-1);
		}
		return result;
	}

	// 用户删除
	@RequestMapping(value = "user/userDelete.htm")
	public @ResponseBody
	ResultInfo userDelete(String ids, int creatorid) {
		loggerUtils.log2DB("用户删除:user/userDelete.htm", creatorid);
		ResultInfo result = new ResultInfo();
		try {
			String[] arrStr = ids.split(",");
			for (int i = 0; i < arrStr.length; i++) {
				userManager.userDelById(Integer.parseInt(arrStr[i].toString()));
			}
			result.setCode(1);
		} catch (Exception e) {
			LoggerUtils.error(e);
			result.setCode(-1);
		}
		return result;
	}

	// 用户获取,根据编号
	@RequestMapping(value = { "user/userGet.htm" })
	public String userGet(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, int userid)
			throws IOException {
		loggerUtils.log2DB("用户获取:user/userGet.htm", userid);
		User user = userManager.userGetById(userid);
		model.put("userid", user.getUserid());
		model.put("username", user.getUsername().toString());
		model.put("logonname", user.getLogonname().toString());
		model.put("gender", user.getGender());
		model.put("telephone", user.getTelephone().toString());
		model.put("status", user.getStatus());
		model.put("organizationid", user.getOrganizationid());
		return "userEdit.jsp";
	}

	// 用户编辑
	@RequestMapping(value = "user/userEdit.htm")
	public @ResponseBody
	ResultInfo userEdit(int userid, String username, String logonname,
			int gender, String telephone, int status, String organizationid,
			int creatorid) {
		loggerUtils.log2DB("用户编辑:user/userEdit.htm", creatorid);
		ResultInfo result = new ResultInfo();
		User user = userManager.userGetById(userid);
		user.setUsername(username);
		user.setLogonname(logonname);
		user.setGender(Integer.valueOf(gender));
		user.setTelephone(telephone);
		user.setStatus(Integer.valueOf(status));
		user.setOrganizationid(Integer.valueOf(organizationid));
		user.setCreatorid(creatorid);
		user.setCreatetime(new Timestamp(System.currentTimeMillis()));
		try {
			int code = userManager.userEdit(user);
			result.setCode(code);
		} catch (Exception e) {
			LoggerUtils.error(e);
			result.setCode(-1);
		}
		return result;
	}

	// 用户树
	@RequestMapping(value = "/user/userTreeGet.htm")
	public @ResponseBody
	List<TreeNodeTemp> getHumanTree(int roleid, int creatorid) {
		loggerUtils.log2DB("用户树:/user/userTreeGet.htm", creatorid);
		return userManager.userTreeGet(roleid);
	}

	// 密码修改
	@RequestMapping(value = "main/configForm.htm")
	public @ResponseBody
	ResultInfo passwordEdit(HttpServletRequest request,
			HttpServletResponse response) {

		ResultInfo result = new ResultInfo();
		String userid = request.getParameter("userid");
		String newpassword = request.getParameter("newpassword");
		String oldpassword = request.getParameter("oldpassword");
		User user = userManager.userGetById(Integer.valueOf(userid));
		loggerUtils.log2DB("密码修改:main/configForm.htm", Integer.valueOf(userid));
		if (!user.getPassword().equals(oldpassword)) {
			result.setCode(-2);
			return result;
		}
		user.setPassword(newpassword);
		try {
			int code = userManager.passwordEdit(user);
			result.setCode(code);
		} catch (Exception e) {
			LoggerUtils.error(e);
			result.setCode(-1);
		}
		return result;
	}

	//用户名校验
		@RequestMapping(value = "user/userReqired.htm")
		public @ResponseBody
		ResultInfo userReqired(HttpServletRequest request,
				HttpServletResponse response) {

			ResultInfo result = new ResultInfo();
			String logonname = request.getParameter("logonname");
			User user = userManager.userGetByName(logonname);
			if (user.getUserid()!=0) {
				result.setCode(100);
			}else{
				result.setCode(200);
			}
			return result;
		}

	
}
