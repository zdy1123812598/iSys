package com.mystic.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.mystic.common.MySqlCommonDao;
import com.mystic.organization.OrganizationManager;
import com.mystic.util.Pagination;

@Component
public class MySqlUserDao extends MySqlCommonDao implements UserDao {

	private Log logger = LogFactory.getLog(MySqlUserDao.class);

	@Autowired
	public OrganizationManager organizationManager;

	@Override
	public User getUser(String logonname, String password) {
		String sql = "select * from tbuser where logonname=? and password=? ";
		logger.debug(sql);
		return jt.query(sql, new Object[] { logonname, password },
				new ResultSetExtractor<User>() {
					@Override
					public User extractData(ResultSet rs) throws SQLException,
							DataAccessException {
						User user = new User();
						if (rs.next()) {
							user.setUserid(rs.getInt("userid"));
							user.setUsername(rs.getString("username"));
							user.setLogonname(rs.getString("logonname"));
							user.setGender(rs.getInt("gender"));
							user.setPassword(rs.getString("password"));
							user.setTelephone(rs.getString("telephone"));
							user.setStatus(rs.getInt("status"));
						}
						return user;
					}
				});
	}

	@Override
	public void userListPagination(Pagination pagination,
			Map<String, Object> param) {
		String userSearchSql = "";
		if (param.get("userSearch") != null) {
			userSearchSql = " and (username like '%" + param.get("userSearch")
					+ "%' or  logonname like '%" + param.get("userSearch")
					+ "%')";
		}

		String sql = "select * from tbuser where 1 " + userSearchSql
				+ " order by userid asc";
		logger.debug(sql);
		pagination.setQuerySql(sql);
		pagination.setJdbcTemplate(jt);
		pagination.queryForList();

	}

	@Override
	public int userAdd(User user) {
		int res = -1;
		String sql = "insert into tbuser(username,logonname,password,gender,telephone,status,organizationid,creatorid,createtime) values(?,?,?,?,?,?,?,?,?)";
		logger.debug(sql);
		try {
			res = jt.update(
					sql,
					new Object[] { user.getUsername(), user.getLogonname(),
							user.getPassword(), user.getGender(),
							user.getTelephone(), user.getStatus(),
							user.getOrganizationid(), user.getCreatorid(),
							user.getCreatetime() });
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	@Override
	public int userDelById(int userid) {
		int res = -1;
		StringBuffer sql = new StringBuffer("delete from tbuser where userid=?");
		logger.debug(sql);
		res = jt.update(sql.toString(), new Object[] { userid });
		return res;
	}

	@Override
	public User userGetById(final int userid) {
		String sql = "select * from tbuser where userid = ? ";
		logger.debug(sql);
		return jt.query(sql, new Object[] { userid },
				new ResultSetExtractor<User>() {
					@Override
					public User extractData(ResultSet rs) throws SQLException,
							DataAccessException {
						User user = new User();
						user.setUserid(userid);
						if (rs.next()) {
							user.setUserid(rs.getInt("userid"));
							user.setUsername(rs.getString("username"));
							user.setLogonname(rs.getString("logonname"));
							user.setGender(rs.getInt("gender"));
							user.setPassword(rs.getString("password"));
							user.setTelephone(rs.getString("telephone"));
							user.setStatus(rs.getInt("status"));
							user.setOrganizationid(rs.getInt("organizationid"));
						}
						return user;
					}
				});
	}

	@Override
	public int userEdit(User user) {
		int res = -1;
		StringBuffer sql = new StringBuffer(
				"update tbuser set username=?,logonname=?,gender=?,telephone=?,status=?,organizationid=?,creatorid=?,createtime=? where userid=?");
		logger.debug(sql);
		res = jt.update(
				sql.toString(),
				new Object[] { user.getUsername(), user.getLogonname(),
						user.getGender(), user.getTelephone(),
						user.getStatus(), user.getOrganizationid(),
						user.getCreatorid(), user.getCreatetime(),
						user.getUserid() });
		return res;
	}

	@Override
	public List<User> userListByOrganizationId(int organizationId) {
		String sql = "select * from tbuser where organizationid = ? ";
		logger.debug(sql);
		return jt.query(sql, new Object[] { organizationId },
				new BeanPropertyRowMapper<User>(User.class));
	}

	@Override
	public int passwordEdit(User user) {
		int res = -1;
		StringBuffer sql = new StringBuffer(
				"update tbuser set password=? where userid=?");
		logger.debug(sql);
		try {
			res = jt.update(sql.toString(), new Object[] { user.getPassword(),
					user.getUserid() });
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	@Override
	public List<User> UserGetByRoleId(int roleid) {
		String sql = "select b.* from tbroleuser a,tbuser b where a.userid = b.userid  and roleid = ?";
		logger.debug(sql);
		return jt.query(sql, new Object[] { roleid },
				new BeanPropertyRowMapper<User>(User.class));
	}

	@Override
	public User userGetByName(String logonname) {
		String sql = "select * from tbuser where logonname = ? ";
		logger.debug(sql);
		return jt.query(sql, new Object[] { logonname },
				new ResultSetExtractor<User>() {
					@Override
					public User extractData(ResultSet rs) throws SQLException,
							DataAccessException {
						User user = new User();
						if (rs.next()) {
							user.setUserid(rs.getInt("userid"));
							user.setUsername(rs.getString("username"));
							user.setLogonname(rs.getString("logonname"));
							user.setGender(rs.getInt("gender"));
							user.setPassword(rs.getString("password"));
							user.setTelephone(rs.getString("telephone"));
							user.setStatus(rs.getInt("status"));
							user.setOrganizationid(rs.getInt("organizationid"));
						}
						return user;
					}
				});
	}
}
