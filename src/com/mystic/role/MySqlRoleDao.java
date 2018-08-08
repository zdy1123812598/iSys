package com.mystic.role;

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
public class MySqlRoleDao extends MySqlCommonDao implements RoleDao {
	private Log logger = LogFactory.getLog(MySqlRoleDao.class);

	@Override
	public void roleListPagination(Pagination pagination,
			Map<String, Object> param) {
		String roleSearchSql = "";
		if (param.get("roleSearch") != null) {
			roleSearchSql = " and rolename like '%" + param.get("roleSearch")
					+ "%'";
		}

		String sql = "select * from tbrole where 1 " + roleSearchSql
				+ " order by roleid asc";
		logger.debug(sql);
		pagination.setQuerySql(sql);
		pagination.setJdbcTemplate(jt);
		pagination.queryForList();
	}

	@Override
	public int roleAdd(String roleName, int creatorid) {
		int res = -1;
		StringBuffer sql = new StringBuffer(
				"insert into tbrole(rolename,creatorid,createtime) values(?,?,?);");
		logger.debug(sql);
		res = jt.update(sql.toString(), new Object[] { roleName, creatorid,
				new Timestamp(System.currentTimeMillis()) });
		return res;
	}

	@Override
	public int roleGetLastID() {
		int res = -1;
		res = jt.queryForInt("SELECT max(roleid) from tbrole");
		return res;
	}

	@Override
	public Role roleGetById(final int roleid) {
		String sql = "select * from tbrole where roleid = ? ";
		logger.debug(sql);
		return jt.query(sql, new Object[] { roleid },
				new ResultSetExtractor<Role>() {
					@Override
					public Role extractData(ResultSet rs) throws SQLException,
							DataAccessException {
						Role role = new Role();
						role.setRoleId(roleid);
						if (rs.next()) {
							role.setRoleName(rs.getString("rolename"));
						}
						return role;
					}
				});
	}

	@Override
	public int roleEdit(Role role) {
		int res = -1;
		StringBuffer sql = new StringBuffer(
				"update tbrole set rolename=?,creatorid=?,createtime=? where roleid=?");
		logger.debug(sql);
		res = jt.update(
				sql.toString(),
				new Object[] { role.getRoleName(), role.getCreatorid(),
						role.getCreatetime(), role.getRoleId() });
		return res;
	}

	@Override
	public int roleDelById(int roleid) {
		int res = -1;
		StringBuffer sql = new StringBuffer("delete from tbrole where roleid=?");
		logger.debug(sql);
		res = jt.update(sql.toString(), new Object[] { roleid });
		return res;
	}

	@Override
	public int roleUserDelete(int roleid) {
		int res = -1;
		String sql = "delete from tbroleuser where roleid=?";
		logger.debug(sql);
		res = jt.update(sql.toString(), new Object[] { roleid });
		return res;
	}

	@Override
	public int roleUserInsert(int roleid, int userid) {
		int res = -1;
		String sql = "insert into tbroleuser(roleid,userid) values(?,?) ";
		logger.debug(sql);
		res = jt.update(sql, new Object[] { roleid, userid });
		return res;
	}

	@Override
	public List<Role> roleListByUserId(int userid) {
		String sql = "select a.* from tbrole a,tbroleuser b where a.roleid = b.roleid and b.userid=?";
		logger.debug(sql);
		return jt.query(sql, new Object[] { userid },
				new BeanPropertyRowMapper<Role>(Role.class));
	}

	@Override
	public List<Map<String,Object>> getPremissionByRoleId(int roleid) {
		String sql = "select * from tbrolelink where roleid = ?";
		logger.debug(sql);
		return jt.queryForList(sql, new Object[]{roleid});
	}

	@Override
	public int addPremission(int roleid, int linkid) {
		int res = -1;
		StringBuffer sql = new StringBuffer(
				"insert into tbrolelink(roleid,linkid) values(?,?);");
		logger.debug(sql);
		res = jt.update(sql.toString(), new Object[] { roleid, linkid });
		return res;
	}

	@Override
	public int delPremission(int roleid) {
		int res = -1;
		StringBuffer sql = new StringBuffer(
				"delete from tbrolelink where roleid=?");
		logger.debug(sql);
		res = jt.update(sql.toString(), new Object[] { roleid });
		return res;
	}

}
