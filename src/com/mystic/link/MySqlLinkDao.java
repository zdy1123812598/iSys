package com.mystic.link;

import java.sql.ResultSet;
import java.sql.SQLException;
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
public class MySqlLinkDao extends MySqlCommonDao implements LinkDao {

	private Log logger = LogFactory.getLog(MySqlLinkDao.class);

	@Override
	public void linkListPagination(Pagination pagination,
			Map<String, Object> param) {

		String linkSearchSql = "";
		if (param.get("linkSearch") != null) {
			linkSearchSql = " and linkname like '%" + param.get("linkSearch")
					+ "%'";
		}

		String sql = "select * from tblink where 1 " + linkSearchSql
				+ " order by linkid asc";
		logger.debug(sql);
		pagination.setQuerySql(sql);
		pagination.setJdbcTemplate(jt);
		pagination.queryForList();

	}

	@Override
	public int linkAdd(Link link) {
		int res = -1;
		String sql = "insert into tblink(linkname,linktext,parentid,creatorid,createtime) values(?,?,?,?,?)";
		logger.debug(sql);
		try {
			res = jt.update(
					sql,
					new Object[] { link.getLinkname(), link.getLinktext(),
							link.getParentid(), link.getCreatorid(),
							link.getCreatetime() });
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	@Override
	public int linkDelById(int linkid) {
		int res = -1;
		StringBuffer sql = new StringBuffer("delete from tblink where linkid=?");
		logger.debug(sql);
		res = jt.update(sql.toString(), new Object[] { linkid });
		return res;
	}

	@Override
	public Link linkGetById(final int linkid) {
		String sql = "select * from tblink where linkid = ? ";
		logger.debug(sql);
		return jt.query(sql, new Object[] { linkid },
				new ResultSetExtractor<Link>() {
					@Override
					public Link extractData(ResultSet rs) throws SQLException,
							DataAccessException {
						Link link = new Link();
						link.setLinkid(linkid);
						if (rs.next()) {
							link.setLinkid(rs.getInt("linkid"));
							link.setLinkname(rs.getString("linkname"));
							link.setLinktext(rs.getString("linktext"));
							link.setParentid(rs.getInt("parentid"));
						}
						return link;
					}
				});
	}

	@Override
	public int linkEdit(Link link) {
		int res = -1;
		StringBuffer sql = new StringBuffer(
				"update tblink set linkname=?,linktext=?,parentid=?,creatorid=?,createtime=? where linkid=?");
		logger.debug(sql);
		res = jt.update(
				sql.toString(),
				new Object[] { link.getLinkname(), link.getLinktext(),
						link.getParentid(), link.getCreatorid(),
						link.getCreatetime(), link.getLinkid() });
		return res;
	}

	@Override
	public List<Link> linkAllList() {
		String sql = "select * from tblink where parentid=0";
		logger.debug(sql);
		return jt.query(sql, new BeanPropertyRowMapper<Link>(Link.class));
	}

	@Override
	public List<Link> getLinkListByParentId(int parentid) {
		String sql = "select * from tblink where parentid=?";
		logger.debug(sql);
		return jt.query(sql, new Object[] { parentid },
				new BeanPropertyRowMapper<Link>(Link.class));
	}

	@Override
	public List<Link> linkListByRoleId(int roleid) {
		String sql = "select a.* from tblink a,tbrolelink b where a.linkid=b.linkid and b.roleid=? order by linkid";
		logger.debug(sql);
		return jt.query(sql, new Object[] { roleid },
				new BeanPropertyRowMapper<Link>(Link.class));
	}
}
