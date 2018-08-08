package com.mystic.common;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Component;

import com.mystic.exception.BatchUpdateException;

@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MySqlCommonDao implements CommonDao {

	private Log logger = LogFactory.getLog(MySqlCommonDao.class);

	@Autowired
	protected JdbcTemplate jt = null;

	/**
	 * @Autowired protected LobHandler lobHandler;
	 */

	protected DataManager dataManager;

	public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
		return new NamedParameterJdbcTemplate(jt);
	}

	public SimpleJdbcCall getSimpleJdbcCall() {
		return new SimpleJdbcCall(jt);
	}

	/**
	 * 执行查询(多条记录)
	 * 
	 * @param table
	 *            表名
	 * @param filedNames
	 *            作为查询条件的字段名
	 * @param clazz
	 *            结果数据封装
	 * @param value
	 *            查询的相关值
	 * @return
	 */
	@Override
	public List select(String table, String[] filedNames, Class clazz,
			Object[] value) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < filedNames.length; i++) {
			sb.append(" and ").append(filedNames[i]).append("=? ");
		}

		String sql = String.format("select * from %1$s where 1 = 1 ", table);
		return jt.query(sql + sb.toString(), value, new BeanPropertyRowMapper(
				clazz));
	}

	/**
	 * 执行查询(单条记录)
	 * 
	 * @param table
	 *            表名
	 * @param primaryKey
	 *            主键名
	 * @param clazz
	 *            对应的Bean的类型
	 * @param value
	 *            主键值
	 * @return 数据
	 */
	@Override
	public Object select(String table, String primaryKey, Class clazz,
			Object value) {
		String sql = String.format("select * from %1$s where %2$s = ?", table,
				primaryKey);
		List result = jt.query(sql, new Object[] { value },
				new BeanPropertyRowMapper(clazz));
		return result.size() > 0 ? result.get(0) : null;
	}

	/**
	 * 执行查询(多条记录)
	 * 
	 * @param table
	 *            表名
	 * @param primaryKey
	 *            主键名
	 * @param value
	 *            主键值
	 * @return 数据
	 */
	@Override
	public <T> List<T> select(String table, Map<String, String> aliasMap,
			Class<T> clazz, Map<String, ?> conds) throws Exception {
		String sql = buildSql(table, aliasMap, clazz, conds);
		Object[] values = new Object[conds.keySet().size()];
		Integer index = 0;
		for (Iterator<String> itr = conds.keySet().iterator(); itr.hasNext();) {
			String key = itr.next();
			values[index++] = conds.get(key);
		}
		logger.debug("sqlParameters:" + StringUtils.join(values, ","));
		return jt.query(sql, values, new BeanPropertyRowMapper<T>(clazz));
	}

	@Override
	public <T> List<T> select(String table, Map<String, String> aliasMap,
			Class<T> clazz) throws Exception {
		String sql = buildSql(table, aliasMap, clazz);
		return jt.query(sql, new BeanPropertyRowMapper<T>(clazz));
	}

	@Override
	public <T> List<T> select(String table, Class<T> clazz) throws Exception {
		String sql = buildSql(table, clazz);
		logger.debug(sql);
		return jt.query(sql, new BeanPropertyRowMapper<T>(clazz));
	}

	@Override
	public List<Map<String, Object>> select(String table, String primaryKey,
			Object value) {
		String sql = String.format("select * from %1$s where %2$s = ?", table,
				primaryKey);
		return jt.queryForList(sql, new Object[] { value });
	}

	@Override
	public List<Map<String, Object>> selectForList(String tableName,
			String[] primaryKeys, String[] fieldNames, Object[] values) {
		String sql = String.format("select %1$s from %2$s where %3$s",
				StringUtils.join(fieldNames, ","), tableName,
				StringUtils.join(primaryKeys, " = ? and ") + " = ?");
		logger.debug(sql);
		logger.debug("sqlparameters : >>> " + Arrays.toString(values));
		return jt.queryForList(sql, values);
	}

	@Override
	public List<Map<String, Object>> selectForList(String tableName,
			String[] primaryKeys, String[] fieldNames, Object[] values,
			String orderKey, boolean asc) {
		String sql = String.format(
				"select %1$s from %2$s where %3$s  order by %4$s",
				StringUtils.join(fieldNames, ","), tableName,
				StringUtils.join(primaryKeys, " = ? and ") + " = ?", orderKey
						+ " " + (asc ? "asc" : "desc"));
		logger.debug(sql);
		logger.debug("sqlparameters : >>> " + Arrays.toString(values));
		return jt.queryForList(sql, values);
	}

	@Override
	public Map<String, Object> selectForMap(String tableName,
			String[] primaryKeys, String fieldName, Object[] values) {
		String sql = String.format("select %1$s from %2$s where %3$s",
				fieldName, tableName,
				StringUtils.join(primaryKeys, " = ? and ") + " = ?");
		return jt.queryForMap(sql, values);
	}

	@Override
	public Map<String, Object> selectForMap(String tableName,
			String[] primaryKeys, String[] fieldNames, Object[] values) {
		String sql = String.format("select %1$s from %2$s where %3$s",
				StringUtils.join(fieldNames, ","), tableName,
				StringUtils.join(primaryKeys, " = ? and ") + " = ?");
		return jt.queryForMap(sql, values);
	}

	@Override
	public String selectForString(String tableName, String[] primaryKeys,
			String fieldName, Object[] values) {
		String sql = String.format("select %1$s from %2$s where %3$s",
				fieldName, tableName,
				StringUtils.join(primaryKeys, " = ? and ") + " = ?");
		logger.debug(sql);
		logger.debug("sqlParameters : >>> " + Arrays.toString(values));
		List<String> list = jt.query(sql, values,
				new SingleColumnRowMapper<String>(String.class));
		return list.size() == 0 ? "" : list.get(0);
	}

	@Override
	public Object selectForObject(String tableName, String[] primaryKeys,
			String fieldName, Object[] values) {
		String sql = String.format("select %1$s from %2$s where %3$s",
				fieldName, tableName,
				StringUtils.join(primaryKeys, " = ? and ") + " = ?");
		return jt.queryForObject(sql, values, Object.class);
	}

	@Override
	public List<Map<String, Object>> selectForList(String sql, Object[] params) {
		return jt.queryForList(sql, params);
	}

	@Override
	public List<Map<String, Object>> selectForList(String sql) {
		return jt.queryForList(sql);
	}

	@Override
	public Integer queryForInt(String fieldName, String table,
			String[] fieldNames, Object[] values) {
		String sql = String.format("select %1$s from %2$s where %3$s",
				fieldName, table, StringUtils.join(fieldNames, " = ? and ")
						+ " = ? ");
		return jt.queryForInt(sql, values);
	}

	@Override
	public boolean isTableExists(String tableName) {
		StoredProcedure func = new StoredProcedure(jt, " fnIsTableExist") {
		};
		func.setFunction(true);
		func.declareParameter(new SqlOutParameter("return", Types.INTEGER));
		func.declareParameter(new SqlParameter("TableName", Types.VARCHAR));
		func.compile();
		Map<String, Object> result = func.execute(tableName);
		return Integer.parseInt(result.get("return").toString()) != 0;
	}

	@Override
	public int update(String table, String[] fields, String[] primaryKeys,
			Object[] values) {
		String sql = String.format("update %1$s set %2$s where %3$s", table,
				StringUtils.join(fields, "=?,") + "=?",
				StringUtils.join(primaryKeys, " = ? and ") + " = ?");
		logger.debug("updateSQL-"
				+ sql
				+ String.format(
						",\nparams:"
								+ StringUtils.rightPad("%s",
										values.length * 3 - 1, ",%s"), values));
		return jt.update(sql, values);
	}

	@Override
	public int update(String table, String[] primaryKeys, String[] fields,
			Object bean) {
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		List<Object> values = new ArrayList<Object>();
		for (int i = 0, len = fields.length; i < len; i++) {
			values.add(bw.getPropertyValue(fields[i]));
		}
		for (int i = 0, len = primaryKeys.length; i < len; i++) {
			values.add(bw.getPropertyValue(primaryKeys[i]));
		}
		String sql = String.format("update %1$s set %2$s where %3$s", table,
				StringUtils.join(fields, " = ?,") + " = ?",
				StringUtils.join(primaryKeys, " = ? and ") + " = ?");
		logger.debug(sql);
		logger.debug("parameters : " + values.toArray());
		return jt.update(sql, values.toArray());
	}

	@Override
	public int insert(String table, Object[] data, String[] fields) {
		String sql = String.format("insert into %1$s (%2$s) values (%3$s)",
				table, StringUtils.join(fields, ','),
				StringUtils.rightPad("?", fields.length * 2 - 1, ",?"));
		return jt.update(sql, data);
	}

	@Override
	public List<Map<String, Object>> select(String table, Object[] data,
			String[] fields, String orderKey, boolean asc) {
		String sql = String.format(
				"select * from %1$s where %2$s order by %3$s", table,
				StringUtils.join(fields, " = ? and ") + " = ?", orderKey + " "
						+ (asc ? "asc" : "desc"));
		return jt.queryForList(sql, data);
	}

	/**
	 * 插入数据
	 * 
	 * @param table
	 *            表名
	 * @param clazz
	 *            类名
	 * @param bean
	 *            数据对象
	 * @return
	 */
	@Override
	public int insert(String table, Class clazz, Object bean) {
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		Field[] fields = clazz.getDeclaredFields();
		List<String> names = new ArrayList<String>();
		List<Object> values = new ArrayList<Object>();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			names.add(f.getName());
			values.add(bw.getPropertyValue(f.getName()));
		}
		String sql = String.format("insert into %1$s (%2$s) values (%3$s)",
				table, StringUtils.join(names, ','),
				StringUtils.rightPad("?", names.size() * 2 - 1, ",?"));
		logger.debug("sqlParameters:" + StringUtils.join(values, ","));
		logger.debug(sql);
		return jt.update(sql, values.toArray());
	}

	/**
	 * 插入数据
	 * 
	 * @param table
	 *            表名
	 * @param clazz
	 *            类名
	 * @param bean
	 *            数据对象
	 * @param fields
	 *            需要插入的字段列表，用逗号(,)分隔
	 * @return
	 */
	@Override
	public int insert(String table, Class clazz, Object bean, String fields) {
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		String[] names = StringUtils.split(fields, ",");
		List<Object> values = new ArrayList<Object>();
		for (int i = 0; i < names.length; i++) {
			values.add(bw.getPropertyValue(names[i]));
		}
		String sql = String.format("insert into %1$s (%2$s) values (%3$s)",
				table, StringUtils.join(names, ','),
				StringUtils.rightPad("?", names.length * 2 - 1, ",?"));
		logger.debug("sqlParameters:" + StringUtils.join(values, ","));
		logger.debug(sql);
		return jt.update(sql, values.toArray());
	}

	/**
	 * 插入数据
	 * 
	 * @param table
	 *            表名
	 * @param data
	 *            数据数组
	 * @param fields
	 *            需要插入的字段列表，用逗号(,)分隔
	 * @return
	 */
	@Override
	public int insert(String table, Object[] data, String fields) {
		String[] names = StringUtils.split(fields, ",");
		String sql = String.format("insert into %1$s (%2$s) values (%3$s)",
				table, StringUtils.join(names, ','),
				StringUtils.rightPad("?", names.length * 2 - 1, ",?"));
		logger.debug("sqlParameters:" + StringUtils.join(data, ","));
		logger.debug(sql);
		return jt.update(sql, data);
	}

	/**
	 * 插入数据
	 * 
	 * @param table
	 *            表名
	 * @param data
	 *            数据
	 * @param fields
	 *            字段列表
	 * @return
	 */
	@Override
	public int insert(String table, Map data, String fields) {
		String[] names = StringUtils.split(fields, ",");
		String sql = String.format("insert into %1$s (%2$s) values (%3$s)",
				table, StringUtils.join(names, ','),
				StringUtils.rightPad("?", names.length * 2 - 1, ",?"));
		List list = new ArrayList();
		for (String name : names) {
			list.add(data.get(name));
		}
		logger.debug("sqlParameters:" + StringUtils.join(list, ","));
		logger.debug(sql);
		return jt.update(sql, list.toArray());
	}

	/**
	 * 执行表更新
	 * 
	 * @param table
	 *            表名，例如 tbRole
	 * @param primaryKey
	 *            主键，例如 RoleID
	 * @param clazz
	 *            对应的Bean的类型
	 * @param bean
	 *            包含数据的Bean
	 * @return 更新的行数
	 */
	@Override
	public int update(String table, String primaryKey, Class clazz, Object bean) {
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		Field[] fields = clazz.getDeclaredFields();
		List<String> names = new ArrayList<String>();
		List<Object> values = new ArrayList<Object>();
		Object pkValue = null;
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			if (primaryKey.equalsIgnoreCase(f.getName())) {
				pkValue = bw.getPropertyValue(f.getName());
			} else {
				names.add(f.getName());
				values.add(bw.getPropertyValue(f.getName()));
			}
		}
		values.add(pkValue);
		String sql = String.format("update %1$s set %2$s where %3$s", table,
				StringUtils.join(names, "=?,") + "=?", primaryKey + " = ?");
		logger.debug("sqlParameters:" + StringUtils.join(values, ","));
		logger.debug(sql);
		return jt.update(sql, values.toArray());
	}

	/**
	 * 执行表更新
	 * 
	 * @param table
	 *            表名，例如 tbRole
	 * @param primaryKey
	 *            主键，例如 RoleID
	 * @param clazz
	 *            对应的Bean的类型
	 * @param bean
	 *            包含数据的Bean
	 * @param ignoreFields
	 *            不需要更新的字段，用逗号分隔
	 * @return 更新的行数
	 */
	@Override
	public int updateWithIgnoreFields(String table, String primaryKey,
			Class clazz, Object bean, String[] ignoreFields) {
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		Field[] fields = clazz.getDeclaredFields();
		List<String> names = new ArrayList<String>();
		List<Object> values = new ArrayList<Object>();
		Object pkValue = null;
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			if (primaryKey.equalsIgnoreCase(f.getName())) {
				pkValue = bw.getPropertyValue(f.getName());
			} else {
				boolean isIgnore = false;
				for (int j = 0, len = ignoreFields.length; j < len; j++) {
					if (ignoreFields[j].equalsIgnoreCase(f.getName())) {
						isIgnore = true;
						break;
					}
				}
				if (!isIgnore) {
					names.add(f.getName());
					values.add(bw.getPropertyValue(f.getName()));
				}
			}
		}
		values.add(pkValue);
		String sql = String.format("update %1$s set %2$s where %3$s", table,
				StringUtils.join(names, "=?,") + "=?", primaryKey + " = ?");

		logger.debug(sql + ",params:" + values.toString());
		return jt.update(sql, values.toArray());
	}

	/**
	 * 更新数据
	 * 
	 * @param table
	 *            表名
	 * @param primaryKey
	 *            主键
	 * @param data
	 *            数据
	 * @param fields
	 *            字段列表
	 * @return
	 */
	@Override
	public int update(String table, String primaryKey, Map data, String fields) {
		String[] names = fields.split(",");
		List<Object> values = new ArrayList<Object>();
		Object pkValue = null;
		for (int i = 0; i < names.length; i++) {
			if (primaryKey.equalsIgnoreCase(names[i])) {
				pkValue = data.get(primaryKey);
			}
			values.add(data.get(names[i]));

		}
		values.add(pkValue);
		String sql = String.format("update %1$s set %2$s where %3$s", table,
				StringUtils.join(names, "=?,") + "=?", primaryKey + " = ?");
		logger.debug("sqlParameters:" + StringUtils.join(values, ","));
		logger.debug(sql);
		return jt.update(sql, values.toArray());
	}

	@Override
	public int update(String table, String[] primaryKeys, String[] fields,
			Map data) {
		List<Object> values = new ArrayList<Object>();
		for (int i = 0, len = fields.length; i < len; i++) {
			values.add(data.get(fields[i]));
		}
		for (int i = 0, len = primaryKeys.length; i < len; i++) {
			values.add(data.get(primaryKeys[i]));
		}
		String sql = String.format("update %1$s set %2$s where %3$s", table,
				StringUtils.join(fields, " = ?,") + " = ?",
				StringUtils.join(primaryKeys, " = ? and ") + " = ?");
		return jt.update(sql, values.toArray());
	}

	/**
	 * 执行表更新
	 * 
	 * @param table
	 *            表名，例如 tbRole
	 * @param primaryKey
	 *            主键，例如 RoleID
	 * @param clazz
	 *            对应的Bean的类型
	 * @param bean
	 *            包含数据的Bean
	 * @param fields
	 *            需要更新的字段列表，字段之间用逗号分隔
	 * @return 更新的行数
	 */
	@Override
	public int update(String table, String primaryKey, Object bean,
			String fields) {
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		String[] names = fields.split(",");
		List<Object> values = new ArrayList<Object>();
		Object pkValue = null;
		for (int i = 0; i < names.length; i++) {
			if (primaryKey.equalsIgnoreCase(names[i])) {
				continue;
			}
			values.add(bw.getPropertyValue(names[i]));
		}
		pkValue = bw.getPropertyValue(primaryKey);
		values.add(pkValue);
		String sql = String.format("update %1$s set %2$s where %3$s", table,
				StringUtils.join(names, "=?,") + "=?", primaryKey + " = ?");
		logger.debug("sqlParameters:" + StringUtils.join(values, ","));
		logger.debug(sql);
		return jt.update(sql, values.toArray());
	}

	/**
	 * 删除数据
	 * 
	 * @param table
	 *            表名
	 * @param primaryKey
	 *            主键名
	 * @param value
	 *            主键值
	 * @return 删除的记录数
	 */
	@Override
	public int delete(String table, String primaryKey, int value) {
		String sql = String.format("delete from %1$s where %2$s = ?", table,
				primaryKey);
		logger.debug("sqlParameters:" + value);
		logger.debug(sql);
		return jt.update(sql, value);
	}

	@Override
	public int delete(String table, String primaryKey, String value) {
		String sql = String.format("delete from %1$s where %2$s = ?", table,
				primaryKey);
		logger.debug("sqlParameters:" + value);
		logger.debug(sql);
		return jt.update(sql, value);
	}

	/**
	 * 删除数据
	 * 
	 * @param table
	 *            表名
	 * @param values
	 *            主键值数组
	 * @param primarykeys
	 *            主键，多个用逗号分隔
	 * @return
	 */
	@Override
	public int delete(String table, Object[] values, String primarykeys) {
		String sql = String.format("delete from %1$s where %2$s", table,
				primarykeys.replaceAll(",", "=? and ") + "=?");
		logger.debug("sqlParameters:" + StringUtils.join(values, ","));
		logger.debug(sql);
		return jt.update(sql, values);
	}

	@Override
	public int delete(String table, String[] primaryKeys, Map data) {
		List<Object> values = new ArrayList<Object>();
		for (String key : primaryKeys) {
			values.add(data.get(key));
		}
		String sql = String.format("delete from %1$s where %2$s", table,
				StringUtils.join(primaryKeys, " = ? and ") + " = ?");
		return jt.update(sql, values.toArray());
	}

	@Override
	public synchronized int getAvaiableID(String table, String primaryKey,
			boolean asc) {
		String sql = null;
		if (asc) {
			sql = String.format("select nvl(max(%1$s),0) + 1 from  %2$s",
					primaryKey, table);
		} else {
			sql = String.format("select nvl(min(%1$s),-1) - 1 from  %2$s",
					primaryKey, table);
		}
		logger.debug(sql);
		return jt.queryForInt(sql);
	}

	@Override
	public List<TreeNode> makeTree(String sql) {
		return makeTree(sql, null, null, true);
	}

	@Override
	public List<TreeNode> makeTree(String sql, String[] params) {
		return makeTree(sql, params, null, true);
	}

	@Override
	public List<TreeNode> makeTree(String sql, String[] params,
			String expandTypes) {
		return makeTree(sql, params, expandTypes, true);
	}

	@Override
	public SqlRowSetMetaData getMetaData(String tableName) {
		String sql = MessageFormat.format("select * from {0} where 1 = 2",
				tableName);
		return jt.queryForRowSet(sql).getMetaData();
	}

	@Override
	public void execFilter(List<TreeNode> list, int recID) {
		for (Iterator<TreeNode> itr = list.iterator(); itr.hasNext();) {
			TreeNode node = itr.next();
			Integer filterID = -1;
			try {
				Integer attr = Integer.parseInt(node.getAttributes()
						.get("FILTERID").toString());
				if (attr == 0)
					continue;
				filterID = attr;
			} catch (Exception ex) {
				logger.error(ex);
			}
			if (filterID == -1)
				continue;

			boolean result = getFilterResult(filterID, recID);
			if (!result) {
				itr.remove();
				continue;
			}
			if (null != node.getChildren() && node.getChildren().size() != 0) {
				execFilter(node.getChildren(), recID);
			}
		}

	}

	@Override
	public void batchUpdate(String sql, final Object[] arr, final Object[] ids) {
		int[] result = jt.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				int index = 0;
				if (arr != null) {
					for (; index < arr.length; index++) {
						ps.setObject(index + 1, arr[index]);
					}
				}
				ps.setObject(index + 1, ids[i]);
			}

			@Override
			public int getBatchSize() {
				return ids.length;
			}
		});
		if (result == null || result.length != ids.length)
			throw new BatchUpdateException();
	}

	@Override
	public void updateClobAsString(String table, String clobFiled,
			String[] primaryKeys, final Object[] params) {
		String sql = String.format("update %1$s set %2$s where %3$s", table,
				clobFiled + " = ? ", StringUtils.join(primaryKeys, " = ? and ")
						+ " = ?");
		jt.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				/*
				 * lobHandler.getLobCreator().setClobAsString(ps, 1, (String)
				 * params[0]); for (int i = 1; i < params.length; i++) {
				 * ps.setObject(i + 1, params[i]); }
				 */
			}

		});
	}

	@Override
	public int count(String table, String[] primaryKeys, Object[] params) {
		String sql = String.format("select count(1) from %1$s where %2$s",
				table, StringUtils.join(primaryKeys, " = ? and ") + " = ? ");
		return jt.queryForInt(sql, params);
	}

	@Override
	public int count(String sql, Object[] params) {
		return jt.queryForInt(sql, params);
	}

	@Override
	public boolean getFilterResult(Integer filterID, Integer recID) {
		boolean result = true;
		String sql = "select SQLEXP from tbfilter t where t.filterID = ?";
		List<String> list = jt.query(sql, new Object[] { filterID },
				new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet rs, int index)
							throws SQLException {
						return rs.getString("SQLEXP");
					}
				});
		if (list == null || list.size() == 0)
			return result;
		try {
			result = jt.queryForInt(list.get(0).replaceAll("%d",
					recID.toString())) > 0;
		} catch (Exception ex) {
			logger.error("exec filter error,SQL is[:" + list.get(0) + "]");
		}
		return result;
	}

	@Override
	public int update(String sql, Object[] params) {
		return jt.update(sql, params);
	}

	@Override
	public int getSeqID(String seqName) {
		String sql = "select %1$s.nextval from dual";
		return jt.queryForInt(String.format(sql, seqName));
	}

	@Override
	public List<TreeNode> makeTree(String sql, final String[] params,
			final String expandTypes, final boolean sync) {
		return jt.query(sql, new ResultSetExtractor<List<TreeNode>>() {
			@Override
			public List<TreeNode> extractData(ResultSet rs)
					throws SQLException, DataAccessException {
				List<String> expands = null;
				if (!StringUtils.isEmpty(expandTypes)) {
					expands = Arrays.asList(expandTypes.split(","));
				} else {
					expands = new ArrayList<String>();
				}
				List<TreeNode> list = new ArrayList<TreeNode>();
				Stack<TreeNode> pNodes = new Stack<TreeNode>(); // 保存遍历的父节点
				int columnCount = rs.getMetaData().getColumnCount();
				boolean hasCheckedColumn = columnCount >= 6
						&& "checked".equalsIgnoreCase(rs.getMetaData()
								.getColumnLabel(6));
				int pLevel = 0; // 上级节点级别
				while (rs.next()) {
					TreeNode node = new TreeNode();
					node.setId(rs.getString(1));
					node.setText(rs.getString(2));
					if (pNodes.size() == 0) { // 将第一个节点加入
						pNodes.push(node);
					}
					List<TreeNode> children = new ArrayList<TreeNode>();
					node.setChildren(children);
					int level = rs.getInt(4);
					if (level == 1) {
						list.add(node);
					} else {
						while (level - pLevel != 1) {// 找到父节点
							pNodes.pop();
							pLevel--;
						}
						pNodes.peek().getChildren().add(node); // 将当前节点加入父节点的children中
					}
					pNodes.push(node);
					Map<String, Object> attributes = new HashMap<String, Object>();
					String type = rs.getString(5);
					attributes.put("type", type);
					if (expands.contains(type) || level == 1) { // 如果包含需要展开的类型，则展开。第一级默认展开
						node.setState("open");
					} else {
						node.setState("closed");
					}
					// 如果checked列,则设置checked属性
					if (hasCheckedColumn) {
						node.setChecked(rs.getInt(6) == 1);
					}
					node.setAttributes(attributes);
					node.setIconCls("icon-" + type);
					// 从第六列以后是要往TreeNode的attributes属性中添加的内容，attributes的key就是传过来的params
					if (params != null && params.length > 0) {
						for (int i = 0; i < params.length; i++) {
							attributes.put(params[i], rs.getString(6 + i));
						}
					} else {
						// 从第六列以后是要往TreeNode的attributes属性中添加的内容
						for (int i = 7; i <= columnCount; i++) {
							attributes.put(rs.getMetaData().getColumnName(i),
									rs.getString(i));
						}
					}
					pLevel = level;
				}
				if (sync) {// 如果是同步加载树节点则重置节点状态，异步不重置
					for (TreeNode treeNode : list) {
						setNodeState(treeNode);
					}
				}
				return list;
			}
		});
	}

	@Override
	public void dropTable(String tableName) {
		jt.execute("drop table " + tableName);
	}

	private <T> String buildSql(String table, Map<String, String> aliasMap,
			Class<T> clazz, Map<String, ?> conds) throws Exception {
		StringBuffer sql = new StringBuffer(buildSql(table, aliasMap, clazz));
		if (conds.isEmpty())
			return sql.toString();
		sql.append(" \n where ");
		for (Iterator<String> itr = conds.keySet().iterator(); itr.hasNext();) {
			String key = itr.next();
			sql.append(key).append("=?,");
		}
		sql.deleteCharAt(sql.length() - 1);
		logger.debug("buildsql:" + sql.toString());
		return sql.toString();
	}

	private <T> String buildSql(String table, Map<String, String> aliasMap,
			Class<T> clazz) throws Exception {
		Field[] fields = clazz.getDeclaredFields();
		if (fields.length == 0)
			throw new Exception("no fields declared in class ".concat(clazz
					.getName()));
		StringBuilder sb = new StringBuilder("select ");
		for (Field f : fields) {
			String key = f.getName();
			String keyAlias = aliasMap.get(key);
			if (StringUtils.isNotBlank(keyAlias)) {
				sb.append(keyAlias).append(" ").append(key).append(",");
			}
			;
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" from ").append(table);
		logger.debug("buildsql:" + sb.toString());
		return sb.toString();
	}

	private String buildSql(String table, Class<?> clazz) throws Exception {
		Field[] fields = clazz.getDeclaredFields();
		if (fields.length == 0)
			throw new Exception("no fields declared in class".concat(clazz
					.getName()));
		StringBuilder sb = new StringBuilder("select ");
		for (Field f : fields) {
			String key = f.getName();
			sb.append(key).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" from ").append(table);
		logger.debug("buildsql:" + sb.toString());
		return sb.toString();
	}

	/**
	 * 修复节点状态。EasyUI Tree中如果叶子节点设置为closed，点击时会再次异步加载整个树
	 * 
	 * @param node
	 */
	private void setNodeState(TreeNode node) {
		if (node.getChildren().size() == 0 && node.getState().equals("closed")) {
			node.setState("open");
		}
		for (TreeNode treeNode : node.getChildren()) {
			setNodeState(treeNode);
		}
	}
}
