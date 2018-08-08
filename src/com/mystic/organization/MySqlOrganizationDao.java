package com.mystic.organization;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.mystic.common.MySqlCommonDao;
import com.mystic.util.Pagination;

@Component
public class MySqlOrganizationDao extends MySqlCommonDao implements
		OrganizationDao {
	private Log logger = LogFactory.getLog(MySqlOrganizationDao.class);

	@Override
	public int organizationAdd(String organizationName, int creatorid) {
		int res = -1;
		StringBuffer sql = new StringBuffer(
				"insert into tborganization(organizationname,creatorid,createtime) values(?,?,?)");
		logger.debug(sql);
		res = jt.update(sql.toString(), new Object[] { organizationName,
				creatorid, new Timestamp(System.currentTimeMillis()) });
		return res;
	}

	@Override
	public void organizationListPagination(Pagination pagination,
			Map<String, Object> param) {
		String organizationSearchSql = "";
		if (param.get("organizationSearch") != null) {
			organizationSearchSql = " and organizationname like '%"
					+ param.get("organizationSearch") + "%'";
		}

		String sql = "select * from tborganization where 1 "
				+ organizationSearchSql + " order by organizationid asc";
		logger.debug(sql);
		pagination.setQuerySql(sql);
		pagination.setJdbcTemplate(jt);
		pagination.queryForList();

	}

	@Override
	public Organization organizationGetById(final int organizationid) {
		String sql = "select * from tborganization where organizationid = ? ";
		logger.debug(sql);
		return jt.query(sql, new Object[] { organizationid },
				new ResultSetExtractor<Organization>() {
					@Override
					public Organization extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						Organization organization = new Organization();
						organization.setOrganizationId(organizationid);
						if (rs.next()) {
							organization.setOrganizationName(rs
									.getString("organizationname"));
						}
						return organization;
					}
				});
	}

	@Override
	public int organizationEdit(Organization organization) {
		int res = -1;
		StringBuffer sql = new StringBuffer(
				"update tborganization set organizationname=?,creatorid=?,createtime=? where organizationid=?");
		logger.debug(sql);
		res = jt.update(
				sql.toString(),
				new Object[] { organization.getOrganizationName(),
						organization.getCreatorid(),
						organization.getCreatetime(),
						organization.getOrganizationId() });
		return res;
	}

	@Override
	public int organizationDelById(int organizationid) {
		int res = -1;
		StringBuffer sql = new StringBuffer(
				"delete from tborganization where organizationid=?");
		res = jt.update(sql.toString(), new Object[] { organizationid });
		logger.debug(sql);
		return res;
	}

	@Override
	public List<Organization> organizationListAll() {
		String sql = "select * from tborganization ";
		logger.debug(sql);
		return jt.query(sql, new BeanPropertyRowMapper<Organization>(
				Organization.class));
	}
}
