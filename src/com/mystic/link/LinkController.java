package com.mystic.link;

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
import com.mystic.user.User;
import com.mystic.util.LoggerUtils;
import com.mystic.util.Pagination;
import com.mystic.util.ServletUtils;

@Controller
public class LinkController {
	@SuppressWarnings("unused")
	private Log logger = LogFactory.getLog(LinkController.class);

	@Autowired
	public LinkManager linkManager;

	@Autowired
	private LoggerUtils loggerUtils;

	// 分页类
	public Pagination getPagination(int rows, int page) {
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(page);
		pagination.setNumPerPage(rows);
		return pagination;
	}

	// 链接分页
	@RequestMapping(value = "link/linkListPagination.htm")
	public @ResponseBody
	Map<String, Object> linkListPagination(HttpServletRequest request,
			@RequestParam int page, @RequestParam int rows, String search) {
		Object obj = WebUtils.getSessionAttribute(request,
				Constants.USER_SESSION);
		User user = (User) obj;
		loggerUtils
				.log2DB("链接分页:link/linkListPagination.htm", user.getUserid());
		Map<String, Object> param = new HashMap<String, Object>();
		String searchKey = (String) ServletUtils.formatStringToNull(search);
		param.put("linkSearch", searchKey);

		Pagination pagination = getPagination(rows, page);

		linkManager.linkListPagination(pagination, param);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", pagination.getTotalRows());
		map.put("rows", pagination.getResultList());
		map.put("page", pagination.getCurrentPage());
		return map;
	}

	// 链接添加
	@RequestMapping(value = "link/linkAdd.htm")
	public @ResponseBody
	ResultInfo linkAdd(String linkname, String linktext, int parentid,
			int creatorid) {
		loggerUtils.log2DB("链接添加:link/linkAdd.htm", creatorid);
		ResultInfo result = new ResultInfo();
		Link link = new Link();
		link.setLinkname(linkname);
		link.setLinktext(linktext);
		link.setParentid(Integer.valueOf(parentid));
		link.setCreatorid(creatorid);
		link.setCreatetime(new Timestamp(System.currentTimeMillis()));
		try {
			int code = linkManager.linkAdd(link);
			result.setCode(code);
		} catch (Exception e) {
			LoggerUtils.error(e);
			result.setCode(-1);
		}
		return result;
	}

	// 链接删除
	@RequestMapping(value = "link/linkDelete.htm")
	public @ResponseBody
	ResultInfo linkDelete(String ids, int creatorid) {
		loggerUtils.log2DB("链接删除:link/linkDelete.htm", creatorid);
		ResultInfo result = new ResultInfo();
		try {
			String[] arrStr = ids.split(",");
			for (int i = 0; i < arrStr.length; i++) {
				linkManager.linkDelById(Integer.parseInt(arrStr[i].toString()));
			}
			result.setCode(1);
		} catch (Exception e) {
			LoggerUtils.error(e);
			result.setCode(-1);
		}
		return result;
	}

	// 链接获取,根据链接编号
	@RequestMapping(value = { "link/linkGet.htm" })
	public String linkGet(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, int linkid)
			throws IOException {
		Object obj = WebUtils.getSessionAttribute(request,
				Constants.USER_SESSION);
		User user = (User) obj;
		loggerUtils.log2DB("链接获取:link/linkGet.htm", user.getUserid());
		Link link = linkManager.linkGetById(linkid);
		model.put("linkid", link.getLinkid());
		model.put("linkname", link.getLinkname());
		model.put("linktext", link.getLinktext());
		model.put("parentid", link.getParentid());
		return "linkEdit.jsp";
	}

	// 链接编辑
	@RequestMapping(value = "link/linkEdit.htm")
	public @ResponseBody
	ResultInfo userEdit(HttpServletRequest request, HttpServletResponse response) {
		Object obj = WebUtils.getSessionAttribute(request,
				Constants.USER_SESSION);
		User user = (User) obj;
		loggerUtils.log2DB("链接编辑:link/linkEdit.htm", user.getUserid());
		String linkname = request.getParameter("linkname");
		String linkid = request.getParameter("linkid");
		String parentid = request.getParameter("parentid");
		String linktext = request.getParameter("linktext");

		ResultInfo result = new ResultInfo();
		Link link = linkManager.linkGetById(Integer.valueOf(linkid));
		link.setLinkname(linkname);
		link.setLinkid(Integer.valueOf(linkid));
		link.setLinktext(linktext);
		link.setParentid(Integer.valueOf(parentid));
		link.setCreatorid(user.getUserid());
		link.setCreatetime(new Timestamp(System.currentTimeMillis()));
		try {
			int code = linkManager.linkEdit(link);
			result.setCode(code);
		} catch (Exception e) {
			LoggerUtils.error(e);
			result.setCode(-1);
		}
		return result;
	}

	// 链接获取所有
	@RequestMapping(value = "link/linkListAll.htm")
	public @ResponseBody
	List<Link> linkListAll(int creatorid) {
		loggerUtils.log2DB("链接获取所有:link/linkListAll.htm", creatorid);
		List<Link> links = linkManager.linkAllList();
		return links;
	}

}
