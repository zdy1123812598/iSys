package com.mystic.organization;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
public class OrganizationController {
	@SuppressWarnings("unused")
	private Log logger = LogFactory.getLog(OrganizationController.class);

	@Autowired
	public OrganizationManager organizationManager;

	@Autowired
	private LoggerUtils loggerUtils;

	// 分页类
	public Pagination getPagination(int rows, int page) {
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(page);
		pagination.setNumPerPage(rows);
		return pagination;
	}

	// 组织添加
	@RequestMapping(value = "organization/organizationAdd.htm")
	public @ResponseBody
	ResultInfo organizationAdd(String organizationName, int creatorid) {
		loggerUtils.log2DB("组织添加:organization/organizationAdd.htm", creatorid);
		ResultInfo result = new ResultInfo();
		try {
			int code = organizationManager.organizationAdd(organizationName,
					creatorid);
			result.setCode(code);
		} catch (Exception e) {
			LoggerUtils.error(e);
			result.setCode(-1);
		}
		return result;
	}

	// 组织分页查询
	@RequestMapping(value = "organization/organizationListPagination.htm")
	public @ResponseBody
	Map<String, Object> organizationListPagination(HttpServletRequest request,
			@RequestParam int page, @RequestParam int rows, String search) {
		Object obj = WebUtils.getSessionAttribute(request,
				Constants.USER_SESSION);
		User user = (User) obj;
		loggerUtils.log2DB("组织分页:organization/organizationListPagination.htm",
				user.getUserid());
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("organizationSearch", ServletUtils.formatStringToNull(search));

		Pagination pagination = getPagination(rows, page);

		organizationManager.organizationListPagination(pagination, param);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", pagination.getTotalRows());
		map.put("rows", pagination.getResultList());
		map.put("page", pagination.getCurrentPage());
		return map;
	}

	// 组织获取,根据组织编号
	@RequestMapping(value = { "organization/organizationGet.htm" })
	public String organizationGet(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, int organizationid)
			throws IOException {
		Object obj = WebUtils.getSessionAttribute(request,
				Constants.USER_SESSION);
		User user = (User) obj;
		loggerUtils.log2DB("组织获取,根据组织编号:organization/organizationGet.htm",
				user.getUserid());
		Organization organization = organizationManager
				.organizationGetById(organizationid);
		model.put("organizationid", organizationid);
		model.put("organizationname", organization.getOrganizationName()
				.toString());
		return "organizationEdit.jsp";
	}

	// 组织编辑
	@RequestMapping(value = "organization/organizationEdit.htm")
	public @ResponseBody
	ResultInfo organizationEdit(int organizationid, String organizationName,
			int creatorid) {
		loggerUtils.log2DB("组织编辑:organization/organizationEdit.htm", creatorid);
		ResultInfo result = new ResultInfo();
		try {
			Organization organization = new Organization();
			organization.setOrganizationId(organizationid);
			organization.setOrganizationName(organizationName);
			organization.setCreatorid(creatorid);
			organization
					.setCreatetime(new Timestamp(System.currentTimeMillis()));
			int res = organizationManager.organizationEdit(organization);
			result.setCode(res);
		} catch (Exception e) {
			LoggerUtils.error(e);
			result.setCode(-1);
		}
		return result;
	}

	// 组织删除
	@RequestMapping(value = "organization/organizationDelete.htm")
	public @ResponseBody
	ResultInfo organizationDelete(String ids, int creatorid) {
		loggerUtils.log2DB("组织删除:organization/organizationDelete.htm",
				creatorid);
		ResultInfo result = new ResultInfo();
		try {
			String[] arrStr = ids.split(",");
			for (int i = 0; i < arrStr.length; i++) {
				organizationManager.organizationDelById(Integer
						.parseInt(arrStr[i].toString()));
			}
			// int res =
			// organizationManager.organizationDelById(organizationid);
			result.setCode(1);
		} catch (Exception e) {
			LoggerUtils.error(e);
			result.setCode(-1);
		}
		return result;
	}

	// 组织获取所有
	@RequestMapping(value = "organization/organizationListAll.htm")
	public @ResponseBody
	List<Organization> organizationListAll(int creatorid) {
		loggerUtils.log2DB("组织获取所有:organization/organizationListAll.htm",
				creatorid);
		List<Organization> organzations = organizationManager
				.organizationListAll();
		return organzations;
	}

	// 组织导出所有
	@RequestMapping(value = "/organization/organizationExportExcel.htm")
	public @ResponseBody
	void organizationExportExcel(HttpServletRequest request,
			HttpServletResponse response) {
		Object obj = WebUtils.getSessionAttribute(request,
				Constants.USER_SESSION);
		User user = (User) obj;
		loggerUtils.log2DB("组织导出所有:/organization/organizationExportExcel.htm",
				user.getUserid());
		OutputStream output = null;
		List<Organization> organzations = organizationManager
				.organizationListAll();
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet1 = wb.createSheet("sheet1");
			HSSFRow row = sheet1.createRow(0);
			row.createCell(0).setCellValue("组织编号");
			row.createCell(1).setCellValue("组织名称");
			for (int i = 0; i < organzations.size(); i++) {// 循环行
				row = sheet1.createRow(i + 1);
				int organzationid = organzations.get(i).getOrganizationId();
				String organzationname = organzations.get(i)
						.getOrganizationName();

				row.createCell(0).setCellValue(organzationid);
				row.createCell(1).setCellValue(organzationname);

			}
			response.reset();
			response.setContentType("application/msexcel");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ URLEncoder.encode("组织信息列表.xls", "UTF-8"));
			output = response.getOutputStream();
			wb.write(output);

		} catch (Exception e) {
			LoggerUtils.error(e);
		} finally {
			if (output != null) {
				try {
					output.flush();
					output.close();
				} catch (Exception ex) {
				}
			}
		}

	}

}
