package com.mystic.common;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Component;

/**
 * 数据库相关操作
 * 
 * @author ZDY
 * @since 2016-09-13
 * 
 */

@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DataManager {

	private Log logger = LogFactory.getLog(DataManager.class);

	@Autowired
	private JdbcTemplate jt = null;

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
	public Object select(String table, String primaryKey, Class clazz,
			Object value) {
		String sql = String.format("select * from %1$s where %2$s = ?", table,
				primaryKey);
		List<Object> list = jt.query(sql, new Object[] { value },
				new BeanPropertyRowMapper(clazz));
		if (list == null || list.size() == 0)
			return null;
		return list.get(0);
	}

	public <T> List<T> select(String table, Map<String, String> aliasMap,
			Class<T> clazz, Map<String, ?> conds) {
		String sql = buildSql(table, aliasMap, clazz, conds);
		Object[] values = new Object[conds.keySet().size()];
		Integer index = 0;
		for (Iterator<String> itr = conds.keySet().iterator(); itr.hasNext();) {
			String key = itr.next();
			values[index++] = conds.get(key);
		}
		return jt.query(sql, values, new BeanPropertyRowMapper<T>(clazz));
	}

	public <T> List<T> select(String table, Map<String, String> aliasMap,
			Class<T> clazz) {
		String sql = buildSql(table, aliasMap, clazz);
		return jt.query(sql, new BeanPropertyRowMapper<T>(clazz));
	}

	public <T> List<T> select(String table, Map<String, String> aliasMap,
			Class<T> clazz, String conds) {
		String sql = buildSql(table, aliasMap, clazz, conds);
		return jt.query(sql, new BeanPropertyRowMapper<T>(clazz));
	}

	public <T> List<T> select(String table, Class<T> clazz) {
		String sql = buildSql(table, clazz);
		logger.debug(sql);
		return jt.query(sql, new BeanPropertyRowMapper<T>(clazz));
	}

	private <T> String buildSql(String table, Map<String, String> aliasMap,
			Class<T> clazz, Map<String, ?> conds) {
		StringBuffer sql = new StringBuffer(buildSql(table, aliasMap, clazz));
		if (conds.isEmpty())
			return sql.toString();
		sql.append(" \n where ");
		for (Iterator<String> itr = conds.keySet().iterator(); itr.hasNext();) {
			String key = itr.next();
			sql.append(key).append("=? ");
		}
		sql.deleteCharAt(sql.length() - 1);
		logger.debug("buildsql:" + sql.toString());
		return sql.toString();
	}

	private <T> String buildSql(String table, Map<String, String> aliasMap,
			Class<T> clazz, String conds) {
		StringBuffer sql = new StringBuffer(buildSql(table, aliasMap, clazz));
		if (conds.isEmpty())
			return sql.toString();
		sql.append(" \n where ").append(conds);
		logger.debug("buildsql:" + sql.toString());
		return sql.toString();
	}

	private <T> String buildSql(String table, Map<String, String> aliasMap,
			Class<T> clazz) {
		Field[] fields = clazz.getDeclaredFields();
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

	private String buildSql(String table, Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
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
	public int insert(String table, Object[] data, String fields) {
		String[] names = StringUtils.split(fields, ",");
		String sql = String.format("insert into %1$s (%2$s) values (%3$s)",
				table, StringUtils.join(names, ','),
				StringUtils.rightPad("?", names.length * 2 - 1, ",?"));
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
	public int insert(String table, Map data, String fields) {
		String[] names = StringUtils.split(fields, ",");
		String sql = String.format("insert into %1$s (%2$s) values (%3$s)",
				table, StringUtils.join(names, ','),
				StringUtils.rightPad("?", names.length * 2 - 1, ",?"));
		List list = new ArrayList();
		for (String name : names) {
			list.add(data.get(name));
		}
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
	public int update(String table, String primaryKey, Map data, String fields) {
		String[] names = fields.split(",");
		List<Object> values = new ArrayList<Object>();
		Object pkValue = data.get(primaryKey);
		for (int i = 0; i < names.length; i++) {
			values.add(data.get(names[i]));
		}
		values.add(pkValue);
		String sql = String.format("update %1$s set %2$s where %3$s", table,
				StringUtils.join(names, "=?,") + "=?", primaryKey + " = ?");
		logger.debug(sql);
		logger.debug("sqlParameters:" + StringUtils.join(values, ","));
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
	public int update(String table, String primaryKey, Class clazz,
			Object bean, String fields) {
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		String[] names = fields.split(",");
		List<Object> values = new ArrayList<Object>();
		Object pkValue = bw.getPropertyValue(primaryKey);
		for (int i = 0; i < names.length; i++) {
			values.add(bw.getPropertyValue(names[i]));
		}
		values.add(pkValue);
		String sql = String.format("update %1$s set %2$s where %3$s", table,
				StringUtils.join(names, "=?,") + "=?", primaryKey + " = ?");
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
	public int delete(String table, String primaryKey, Object value) {
		String sql = String.format("delete %1$s where %2$s = ?", table,
				primaryKey);
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
	public int delete(String table, Object[] values, String primarykeys) {
		String sql = String.format("delete %1$s where %2$s", table,
				primarykeys.replaceAll(",", "=? and ") + "=?");
		logger.debug(sql);
		return jt.update(sql, values);

	}

	/**
	 * 获得可用的ID
	 * 
	 * @param table
	 *            表名
	 * @param primaryKey
	 *            主键名
	 * @param asc
	 *            是否升序（取最大值+1）
	 * @param startValue
	 *            起始值
	 * @return
	 */
	public synchronized int getAvaiableID(String table, String primaryKey,
			Integer startValue, boolean asc) {
		String sql = null;
		if (asc) {
			sql = String.format("select nvl(max(%1$s),0) from  %2$s",
					primaryKey, table);
		} else {
			sql = String.format("select nvl(min(%1$s),0) from  %2$s",
					primaryKey, table);
		}
		logger.debug(sql);
		Integer base = jt.queryForInt(sql);
		if (asc) {
			base = base < startValue ? startValue : base;
			return base + 1;
		} else {
			base = base > startValue ? startValue : base;
			return base - 1;
		}
	}

	public int getAvaiableID(String table, String primaryKey, boolean asc) {
		return getAvaiableID(table, primaryKey, 0, asc);
	}

	public int getAvaiableID(String table, String primaryKey, Integer startValue) {
		return getAvaiableID(table, primaryKey, startValue, true);
	}

	/**
	 * 调用存储过程，无返回结果的
	 * 
	 * @param proc
	 *            表名
	 * @param clazz
	 *            对应的Bean的类型
	 * @param bean
	 *            数据对象
	 * @param fields
	 *            存储过程的参数，对应为bean的属性值，逗号分隔
	 */
	public void callProcedure(String proc, Class clazz, Object bean,
			String fields) {
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		String[] names = StringUtils.split(fields, ",");
		List<Object> values = new ArrayList<Object>();
		for (int i = 0; i < names.length; i++) {
			values.add(bw.getPropertyValue(names[i]));
		}
		String sql = String.format("{call %1$s(%2$s)}", proc,
				StringUtils.rightPad("?", names.length * 2 - 1, ",?"));

		class MyPreparedStatementCallback implements
				PreparedStatementCallback<Object> {
			final Object[] values;

			MyPreparedStatementCallback(Object[] values) {
				this.values = values;
			}

			@Override
			public Object doInPreparedStatement(PreparedStatement ps)
					throws SQLException, DataAccessException {
				for (int i = 0; i < values.length; ++i)
					ps.setObject(i + 1, values[i]);
				ps.execute();
				return null;
			}
		}

		logger.debug(sql);
		jt.execute(sql, new MyPreparedStatementCallback(values.toArray()));
	}

	class MyCallableStatementCreator implements CallableStatementCreator {
		final String sql;
		final Object[] values;
		final int[] outTypes;

		public MyCallableStatementCreator(String sql, Object[] values,
				int[] outTypes) {
			this.sql = sql;
			this.values = values;
			this.outTypes = outTypes;
		}

		public CallableStatement createCallableStatement(Connection con)
				throws SQLException {
			CallableStatement cs = con.prepareCall(sql);
			for (int i = 0; i < values.length; ++i)
				cs.setObject(i + 1, values[i]);

			for (int j = 0; j < outTypes.length; ++j)
				cs.registerOutParameter(values.length + j + 1, outTypes[j]);
			return cs;
		}
	}

	class MyCallableStatementCallback implements
			CallableStatementCallback<Object[]> {
		int startIndex;
		int count;

		MyCallableStatementCallback(int startIndex, int count) {
			this.startIndex = startIndex;
			this.count = count;
		}

		public Object[] doInCallableStatement(CallableStatement cs)
				throws SQLException, DataAccessException {
			cs.execute();

			Object arrays[] = new Object[count];
			for (int i = 0; i < count; ++i) {
				arrays[i] = cs.getObject(startIndex + i);
			}
			return arrays;
		}
	}

	/**
	 * 调用存储过程，有单条返回结果的
	 * 
	 * @param proc
	 *            表名
	 * @param clazz
	 *            对应的Bean的类型
	 * @param bean
	 *            数据对象
	 * @param fields
	 *            存储过程的参数，对应为bean的属性值，逗号分隔
	 * @param outTypes
	 *            主键值
	 * @return 数据
	 */
	public Object[] callProcedureForObjects(String proc, Class clazz,
			Object bean, String fields, int[] outTypes) {
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		String[] names = StringUtils.split(fields, ",");
		List<Object> values = new ArrayList<Object>();
		for (int i = 0; i < names.length; i++) {
			values.add(bw.getPropertyValue(names[i]));
		}
		String sql = String.format("{call %1$s(%2$s)}", proc, StringUtils
				.rightPad("?", (names.length + outTypes.length) * 2 - 1, ",?"));
		logger.debug(sql);
		return jt.execute(new MyCallableStatementCreator(sql, values.toArray(),
				outTypes), new MyCallableStatementCallback(names.length + 1,
				outTypes.length));
	}

	/**
	 * 调用存储过程，有单条返回结果的
	 * 
	 * @param proc
	 *            表名
	 * @param values
	 *            存储过程的参数
	 * @param outTypes
	 *            主键值
	 * @return 数据
	 */
	public Object[] callProcedureForObjects(String proc, Object[] values,
			int[] outTypes) {
		String sql = String
				.format("{call %1$s(%2$s)}", proc, StringUtils.rightPad("?",
						(values.length + outTypes.length) * 2 - 1, ",?"));
		logger.debug(sql);
		return jt.execute(
				new MyCallableStatementCreator(sql, values, outTypes),
				new MyCallableStatementCallback(values.length + 1,
						outTypes.length));
	}

	/**
	 * 根据一个或多个字段查询一个字符串字段
	 * 
	 * @param table
	 *            表名
	 * @param primaryKeys
	 *            所依据字段
	 * @param field
	 *            要查询的字段
	 * @param params
	 *            所依据字段的值
	 * @return
	 */
	public String queryForString(String table, String[] primaryKeys,
			String field, Object[] params) {
		String sql = String.format("select %1$s from %2$s where %3$s", field,
				table, StringUtils.join(primaryKeys, " = ? and ") + " = ? ");
		logger.debug(sql);
		return jt.queryForObject(sql, params, String.class);
	}

	public Map<String, Object> queryForMap(String sql, Object[] args) {
		List<Map<String, Object>> list = jt.queryForList(sql, args);
		if (list == null || list.size() == 0)
			return new HashMap<String, Object>();
		return list.get(0);
	}

	/**
	 * 根据一个或多个字段查询是否有记录
	 * 
	 * @param table
	 *            表名
	 * @param primaryKeys
	 *            所依据字段
	 * @param params
	 *            所依据字段的值
	 * @return
	 */
	public int queryForCount(String table, String[] primaryKeys, Object[] params) {
		String sql = String.format("select count(*) from %1$s where %2$s",
				table, StringUtils.join(primaryKeys, " = ? and ") + " = ? ");
		logger.debug(sql);
		return jt.queryForInt(sql, params);
	}

	/**
	 * 根据一个字段或若干字段修改另外的一个或多个字段
	 * 
	 * @param table
	 *            表名
	 * @param fields
	 *            要修改的字段
	 * @param primaryKeys
	 *            所依据的字段
	 * @param params
	 *            参数
	 * @return
	 */
	public int update(String table, String[] fields, String[] primaryKeys,
			Object[] params) {
		String sql = String.format("update %1$s set %2$s where %3$s", table,
				StringUtils.join(fields, " = ?,") + " = ?",
				StringUtils.join(primaryKeys, " = ? and ") + " = ?");
		logger.debug(sql);
		return jt.update(sql, params);
	}

}
