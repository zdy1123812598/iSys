package com.mystic.role;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.mystic.user.User;
import com.mystic.util.LoggerUtils;
import com.mystic.util.Pagination;
import com.mystic.util.ServletUtils;

@Controller
public class RoleController {
	@SuppressWarnings("unused")
	private Log logger = LogFactory.getLog(RoleController.class);

	@Autowired
	public RoleManager roleManager;

	@Autowired
	private LoggerUtils loggerUtils;

	// 分页类
	public Pagination getPagination(int rows, int page) {
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(page);
		pagination.setNumPerPage(rows);
		return pagination;
	}

	// 角色添加
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "role/roleAdd.htm")
	public @ResponseBody
	ResultInfo roleAdd(String roleName, String userids, String linkids,
			int creatorid) {
		loggerUtils.log2DB("角色添加:role/roleAdd.htm", creatorid);
		ResultInfo result = new ResultInfo();
		String[] arrStr = !"".equals(userids) ? userids.split(",")
				: new String[0];
		String[] linkStr = !"".equals(linkids) ? linkids.split(",")
				: new String[0];
		try {
			int resAdd = roleManager.roleAdd(roleName, creatorid);
			if (resAdd > -1) {
				int roleid = roleManager.roleGetLastID();
				if (roleid > 0) {
					roleManager.roleUserDelete(roleid);
					for (int i = 0; i < arrStr.length; i++) {
						roleManager.roleUserInsert(roleid,
								Integer.parseInt(arrStr[i].toString()));
					}
					List<Map> liPre = new ArrayList<Map>();
					for (int i = 0; i < linkStr.length; i++) {
						Map pre = new HashMap<String, String>();
						pre.put("roleid", roleid);
						pre.put("linkid", linkStr[i].toString());
						liPre.add(pre);
					}
					roleManager.premissionEdit(roleid, liPre);
				}
			}

			result.setCode(1);
		} catch (Exception e) {
			LoggerUtils.error(e);
			result.setCode(-1);
		}
		return result;
	}

	// 角色分页
	@RequestMapping(value = "role/roleListPagination.htm")
	public @ResponseBody
	Map<String, Object> roleListPagination(HttpServletRequest request,
			@RequestParam int page, @RequestParam int rows, String search) {
		Object obj = WebUtils.getSessionAttribute(request,
				Constants.USER_SESSION);
		User user = (User) obj;
		loggerUtils
				.log2DB("角色分页:role/roleListPagination.htm", user.getUserid());
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("roleSearch", ServletUtils.formatStringToNull(search));
		Pagination pagination = getPagination(rows, page);
		roleManager.roleListPagination(pagination, param);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", pagination.getTotalRows());
		map.put("rows", pagination.getResultList());
		map.put("page", pagination.getCurrentPage());
		return map;
	}

	// 角色获取,根据编号
	@RequestMapping(value = { "role/roleGet.htm" })
	public String roleGet(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, int roleid)
			throws IOException {
		Object obj = WebUtils.getSessionAttribute(request,
				Constants.USER_SESSION);
		User user = (User) obj;
		loggerUtils.log2DB("角色获取:role/roleGet.htm", user.getUserid());
		Role role = roleManager.roleGetById(roleid);
		model.put("roleid", role.getRoleId());
		model.put("rolename", role.getRoleName().toString());
		return "roleEdit.jsp";
	}

	// 角色编辑
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "role/roleEdit.htm")
	public @ResponseBody
	ResultInfo roleEdit(int roleid, String roleName, String userids,
			String linkids, int creatorid) {
		loggerUtils.log2DB("角色编辑:role/roleEdit.htm", creatorid);
		ResultInfo result = new ResultInfo();
		String[] arrStr = !"".equals(userids) ? userids.split(",")
				: new String[0];
		String[] linkStr = !"".equals(linkids) ? linkids.split(",")
				: new String[0];
		List<Map> liPre = new ArrayList<Map>();
		for (int i = 0; i < linkStr.length; i++) {
			Map pre = new HashMap<String, String>();
			pre.put("roleid", roleid);
			pre.put("linkid", linkStr[i].toString());
			liPre.add(pre);
		}
		try {
			Role role = new Role();
			role.setRoleId(roleid);
			role.setRoleName(roleName);
			role.setRoleId(creatorid);
			role.setCreatetime(new Timestamp(System.currentTimeMillis()));
			int res = roleManager.roleEdit(role);
			if (res > -1) {
				roleManager.roleUserDelete(roleid);
				for (int i = 0; i < arrStr.length; i++) {
					roleManager.roleUserInsert(roleid,
							Integer.parseInt(arrStr[i].toString()));
				}
				res = roleManager.premissionEdit(roleid, liPre);
			}
			result.setCode(res);
		} catch (Exception e) {
			LoggerUtils.error(e);
			result.setCode(-1);
		}
		return result;
	}

	// 角色删除
	@RequestMapping(value = "role/roleDelete.htm")
	public @ResponseBody
	ResultInfo roleDelete(String ids, int creatorid) {
		loggerUtils.log2DB("角色删除:role/roleDelete.htm", creatorid);
		ResultInfo result = new ResultInfo();
		try {
			String[] arrStr = ids.split(",");
			for (int i = 0; i < arrStr.length; i++) {
				roleManager.roleDelById(Integer.parseInt(arrStr[i].toString()));
				roleManager.roleUserDelete(Integer.parseInt(arrStr[i]
						.toString()));
				roleManager
						.premissionDel(Integer.parseInt(arrStr[i].toString()));
			}
			result.setCode(1);
		} catch (Exception e) {
			LoggerUtils.error(e);
			result.setCode(-1);
		}
		return result;
	}

	// 链接树
	@RequestMapping(value = "/role/linkTreeGet.htm")
	public @ResponseBody
	List<TreeNodeTemp> getLinkTree(int roleid, int creatorid) {
		loggerUtils.log2DB("链接树:/role/linkTreeGet.htm", creatorid);
		return roleManager.premissionTree(roleid);
	}

}
