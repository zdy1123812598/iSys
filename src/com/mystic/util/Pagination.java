package com.mystic.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mystic.exception.PaginationException;

/**
 * 分页函数
 */
public class Pagination {
	private Log logger = LogFactory.getLog(Pagination.class);

	public static final int NUMBERS_PER_PAGE = 10;
	// 一页显示的记录数
	private int numPerPage = NUMBERS_PER_PAGE;
	// 记录总数
	private int totalRows = 0;
	// 总页数
	private int totalPages = 0;
	// 当前页码
	private int currentPage = 1;
	// 起始行数
	private int startIndex = 0;
	// 结束行数
	private int lastIndex = 0;

	private List<Map<String, Object>> resultList = null;

	private JdbcTemplate jdbcTemplate = null;
	// 查询sql语句
	private String querySql = null;

	private Object[] args = null;

	/**
	 * 缺省构造函数
	 */
	public Pagination() {
	}

	/**
	 * 分页构造函数
	 * 
	 * @param sql
	 *            根据传入的sql语句得到一些基本分页信息
	 * @param currentPage
	 *            当前页
	 * @param numPerPage
	 *            每页记录数
	 * @param jdbcTemplate
	 *            JdbcTemplate实例
	 */
	public Pagination(String sql, int currentPage, int numPerPage,
			JdbcTemplate jdbcTemplate) {
		this(sql, null, currentPage, numPerPage, jdbcTemplate);
	}

	public Pagination(String sql, Object[] args, int currentPage,
			int numPerPage, JdbcTemplate jdbcTemplate) {
		// 设置查询语句
		setQuerySql(sql);
		// 设置参数
		setArgs(args);
		// 设置每页显示记录数
		setNumPerPage(numPerPage);
		// 设置要显示的页数
		setCurrentPage(currentPage);
		// 给JdbcTemplate赋值
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 初始化
	 * 
	 * @param sql
	 *            根据传入的sql语句得到一些基本分页信息
	 * @param currentPage
	 *            当前页
	 * @param numPerPage
	 *            每页记录数
	 * @param jdbcTemplate
	 *            JdbcTemplate实例
	 */
	public void queryForList() {
		// 总记录数
		setTotalRows(jdbcTemplate.queryForInt("select count(*) from ("
				+ querySql + ")jtcount", args));
		// 计算总页数
		setTotalPages();
		// 计算起始行数
		setStartIndex();
		// 计算结束行数
		setLastIndex();
		// 构造mysql数据库的分页语句
		StringBuffer paginationSQL = new StringBuffer(" SELECT * FROM ( ");
		paginationSQL.append(" SELECT temp.* ,(@rowNum:=@rowNum+1) as num FROM ( ");
		paginationSQL.append(querySql);
		paginationSQL.append(") temp ,(Select (@rowNum :=0) ) numcou where  (@rowNum) < " + lastIndex);
		paginationSQL.append(" )tempp where tempp.num > " + startIndex);

		logger.debug("paginationSQL[" + paginationSQL + "]");

		// 装入结果集List
		setResultList(jdbcTemplate.queryForList(paginationSQL.toString(),
				args == null ? new Object[] {} : args));
	}

	public Pagination execute() {
		this.queryForList();
		return this;
	}

	/**
	 * 封装datagrid查询结构
	 * 
	 * @return
	 * @throws PaginationException
	 */
	public Map<String, Object> executeForMap() throws PaginationException {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			this.execute();
			result.put("total", this.totalRows);
			result.put("rows", this.getResultList());
		} catch (Exception ex) {
			throw new PaginationException(ex);
		}
		return result;
	}

	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

	public String getQuerySql() {
		return querySql;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

	public List<Map<String, Object>> getResultList() {
		return resultList;
	}

	public void setResultList(List<Map<String, Object>> resultList) {
		this.resultList = resultList;
	}

	public int getTotalPages() {
		return totalPages;
	}

	// 计算总页数
	public void setTotalPages() {
		if (totalRows % numPerPage == 0) {
			this.totalPages = totalRows / numPerPage;
		} else {
			this.totalPages = (totalRows / numPerPage) + 1;
		}
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex() {
		this.startIndex = (currentPage - 1) * numPerPage;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// 计算结束时候的索引
	public void setLastIndex() {
		if (totalRows < numPerPage) {
			this.lastIndex = totalRows;
		} else if ((totalRows % numPerPage == 0)
				|| (totalRows % numPerPage != 0 && currentPage < totalPages)) {
			this.lastIndex = currentPage * numPerPage;
		} else if (totalRows % numPerPage != 0 && currentPage == totalPages) {// 最后一页
			this.lastIndex = totalRows;
		}
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public <T> Map<String, Object> getPaginationList(int currentPage,
			int numPerPage, List<T> list) {
		setCurrentPage(currentPage);
		setNumPerPage(numPerPage);
		// 总记录数
		setTotalRows(list.size());
		// 计算总页数
		setTotalPages();
		// 计算起始行数
		setStartIndex();
		// 计算结束行数
		setLastIndex();
		Map<String, Object> result = new HashMap<String, Object>();
		List<T> resultList = list.subList(startIndex, lastIndex);
		result.put("total", this.totalRows);
		result.put("rows", resultList);
		return result;
	}
}