package com.mystic.link;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.mystic.util.Pagination;

@Component
public class LinkManager {
	@SuppressWarnings("unused")
	private final Log logger = LogFactory.getLog(LinkManager.class);

	@Autowired
	private JdbcTemplate jt = null;

	@Autowired
	public LinkDao linkDao;

	public void linkListPagination(Pagination pagination,
			Map<String, Object> param) {
		linkDao.linkListPagination(pagination, param);
	}

	public int linkAdd(Link link) {
		return linkDao.linkAdd(link);
	}

	public int linkDelById(int linkid) {
		return linkDao.linkDelById(linkid);
	}

	public Link linkGetById(int linkid) {
		return linkDao.linkGetById(linkid);
	}

	public int linkEdit(Link link) {
		return linkDao.linkEdit(link);
	}

	public List<Link> linkAllList() {
		return linkDao.linkAllList();
	}

	public List<Link> getLinkListByParentId(int parentid) {
		return linkDao.getLinkListByParentId(parentid);
	}

	public List<Link> linkListByRoleId(int roleid) {
		return linkDao.linkListByRoleId(roleid);
	}
}
