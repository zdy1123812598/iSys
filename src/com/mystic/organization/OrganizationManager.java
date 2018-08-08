package com.mystic.organization;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.mystic.util.Pagination;

@Component
public class OrganizationManager {

	@SuppressWarnings("unused")
	private final Log logger = LogFactory.getLog(OrganizationManager.class);

	@Autowired
	private JdbcTemplate jt = null;

	@Autowired
	public OrganizationDao organizationDao;

	public int organizationAdd(String organizationName, int creatorid) {
		return organizationDao.organizationAdd(organizationName,creatorid);
	}

	public void organizationListPagination(Pagination pagination,
			Map<String, Object> param) {
		organizationDao.organizationListPagination(pagination, param);

	}

	public Organization organizationGetById(int organizationid) {
		return organizationDao.organizationGetById(organizationid);
	}

	public int organizationEdit(Organization organization) {
		return organizationDao.organizationEdit(organization);
	}

	public int organizationDelById(int organizationid) {
		return organizationDao.organizationDelById(organizationid);
	}

	public List<Organization> organizationListAll() {
		return organizationDao.organizationListAll();
	}

}
