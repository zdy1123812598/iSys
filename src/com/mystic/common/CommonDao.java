package com.mystic.common;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

@SuppressWarnings("rawtypes")
public interface CommonDao {

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
			Object[] value);

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
			Object value);

	public <T> List<T> select(String table, Map<String, String> aliasMap,
			Class<T> clazz, Map<String, ?> conds) throws Exception;

	public <T> List<T> select(String table, Map<String, String> aliasMap,
			Class<T> clazz) throws Exception;

	public <T> List<T> select(String table, Class<T> clazz) throws Exception;

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
	public List<Map<String, Object>> select(String table, String primaryKey,
			Object value);

	/**
	 * 根据一个字段或若干字段查询一个字段或若干字段
	 * 
	 * @param tableName
	 *            表名
	 * @param primaryKeys
	 *            所依据字段
	 * @param fieldNames
	 *            查询字段
	 * @param values
	 *            所依据字段的值
	 * @return 结果集
	 */
	public List<Map<String, Object>> selectForList(String tableName,
			String[] primaryKeys, String[] fieldNames, Object[] values);

	/**
	 * 根据一个字段或若干字段查询一个字段或若干字段
	 * 
	 * @param tableName
	 *            表名
	 * @param primaryKeys
	 *            所依据字段
	 * @param fieldNames
	 *            查询字段
	 * @param values
	 *            所依据字段的值
	 * @param orderKey
	 *            排序字段
	 * @param asc
	 *            true:升序，false:降序
	 * @return 结果集
	 */
	public List<Map<String, Object>> selectForList(String tableName,
			String[] primaryKeys, String[] fieldNames, Object[] values,
			String orderKey, boolean asc);

	/**
	 * 根据一个字段或若干字段查询一个字段
	 * 
	 * @param tableName
	 *            表名
	 * @param primaryKeys
	 *            所依据字段
	 * @param fieldName
	 *            查询字段
	 * @param value
	 *            所依据字段的值
	 * @return map
	 */
	public Map<String, Object> selectForMap(String tableName,
			String[] primaryKeys, String fieldName, Object[] values);

	/**
	 * 根据一个字段或若干字段查询若干字段
	 * 
	 * @param tableName
	 *            表名
	 * @param primaryKeys
	 *            所依据字段
	 * @param fieldNames
	 *            查询字段
	 * @param values
	 *            所依据字段的值
	 * @return map
	 */
	public Map<String, Object> selectForMap(String tableName,
			String[] primaryKeys, String[] fieldNames, Object[] values);

	/**
	 * 根据一个字段或若干字段查询一个字符串类型的字段
	 * 
	 * @param tableName
	 *            表名
	 * @param primaryKeys
	 *            所依据字段
	 * @param fieldName
	 *            查询字段
	 * @param values
	 *            所依据字段的值
	 * @return String
	 */
	public String selectForString(String tableName, String[] primaryKeys,
			String fieldName, Object[] values);

	/**
	 * 根据一个字段或若干字段查询一个Object类型的字段
	 * 
	 * @param tableName
	 *            表名
	 * @param primaryKeys
	 *            所依据字段
	 * @param fieldName
	 *            查询字段
	 * @param values
	 *            所依据字段的值
	 * @return Object
	 */
	public Object selectForObject(String tableName, String[] primaryKeys,
			String fieldName, Object[] values);

	/**
	 * 根据自定义SQL查询数据
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> selectForList(String sql, Object[] params);

	public List<Map<String, Object>> selectForList(String sql);

	/**
	 * 根据一列或若干列查找为整数的单个字段
	 * 
	 * @param fieldName
	 *            为整数的字段
	 * @param table
	 *            表名
	 * @param fieldNames
	 *            所依据字段
	 * @param values
	 *            所依据字段的值
	 * @return Integer
	 */
	public Integer queryForInt(String fieldName, String table,
			String[] fieldNames, Object[] values);

	/**
	 * 判断是否存在物理表
	 * 
	 * @param tableName
	 * @return 存在：true，不存在：false
	 */
	public boolean isTableExists(String tableName);

	/**
	 * 根据一列或若干列修改其它
	 * 
	 * @param table
	 *            表名
	 * @param primaryKey
	 *            所依据字段
	 * @param values
	 *            数据
	 * @param fields
	 *            修改字段
	 * @return
	 */
	public int update(String table, String[] fields, String[] primaryKeys,
			Object[] values);

	/**
	 * 根据一列或若干列修改一个或若干字段，数据来源是bean
	 * 
	 * @param table
	 *            要修改的表名
	 * @param primaryKeys
	 *            依据字段数组，必须在bean中有同名属性
	 * @param fields
	 *            要修改的字段数组，必须在bean中有同名属性
	 * @param bean
	 *            数据源
	 * @return
	 */
	public int update(String table, String[] primaryKeys, String[] fields,
			Object bean);

	/**
	 * 插入数据
	 * 
	 * @param table
	 *            表名
	 * @param data
	 *            数据数组
	 * @param fields
	 *            需要插入的字段列表，数组
	 * @return
	 */
	public int insert(String table, Object[] data, String[] fields);

	/**
	 * 查询所有数据 select *
	 * 
	 * @param table
	 *            表名
	 * @param data
	 *            依据字段值
	 * @param fields
	 *            所依据字段
	 * @param orderKey
	 *            排序字段
	 * @param asc
	 *            true:升序，false:降序
	 * @return 多行数据
	 */
	public List<Map<String, Object>> select(String table, Object[] data,
			String[] fields, String orderKey, boolean asc);

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
	public int insert(String table, Class clazz, Object bean);

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
	public int insert(String table, Class clazz, Object bean, String fields);

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
	public int insert(String table, Object[] data, String fields);

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
	public int insert(String table, Map data, String fields);

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
	public int update(String table, String primaryKey, Class clazz, Object bean);

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
	public int updateWithIgnoreFields(String table, String primaryKey,
			Class clazz, Object bean, String[] ignoreFields);

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
	public int update(String table, String primaryKey, Map data, String fields);

	/**
	 * 根据一个或多个字段修改其他字段，数据源是map
	 * 
	 * @param table
	 * @param primaryKeys
	 * @param data
	 * @param fields
	 * @return
	 */
	public int update(String table, String[] primaryKeys, String[] fields,
			Map data);

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
	public int update(String table, String primaryKey, Object bean,
			String fields);

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
	public int delete(String table, String primaryKey, int value);

	public int delete(String table, String primaryKey, String value);

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
	public int delete(String table, Object[] values, String primarykeys);

	/**
	 * 根据一个或多个字段删除数据，数据源是map
	 * 
	 * @param table
	 * @param primaryKeys
	 * @param data
	 * @return
	 */
	public int delete(String table, String[] primaryKeys, Map data);

	/**
	 * 获得可用的ID
	 * 
	 * @param table
	 *            表名
	 * @param primaryKey
	 *            主键名
	 * @param asc
	 *            是否升序（取最大值+1）
	 * @return
	 */
	public int getAvaiableID(String table, String primaryKey, boolean asc);

	/**
	 * 根据sql语句生成easyui的树结构
	 * 
	 * @param sql
	 *            查询语句，必须符合如下规范：前面几个字段的依次为：节点标识，名称，类型,上级节点标识，节点层次，类型,是否勾选
	 * @return
	 */
	public List<TreeNode> makeTree(String sql);

	/**
	 * 根据sql语句生成easyui的树结构
	 * 
	 * @param sql
	 * @param params
	 *            查询字段从第六列开始往后要放入attributes中的字段
	 * @return
	 */
	public List<TreeNode> makeTree(String sql, String[] params);

	/**
	 * 根据sql语句生成easyui的树结构
	 * 
	 * @param sql
	 * @param params
	 *            查询字段从第六列开始往后要放入attributes中的字段
	 * @param expandTypes
	 *            要展开节点的类型，以逗号分隔
	 * @return
	 */
	public List<TreeNode> makeTree(String sql, String[] params,
			String expandTypes);

	/**
	 * 获得表的元数据信息
	 * 
	 * @param tableName
	 *            表名
	 * @return
	 */
	public SqlRowSetMetaData getMetaData(String tableName);

	/**
	 * 执行过滤条件
	 * 
	 * @param list
	 * @param recID
	 */
	public void execFilter(List<TreeNode> list, final int recID);

	/**
	 * 批处理
	 * 
	 * @param sql
	 * @param arr
	 *            批处理中不变的参数
	 * @param ids
	 *            批处理中每次不同的参数
	 */
	public void batchUpdate(String sql, Object[] arr, Object[] ids);

	/**
	 * 根据一个或若干字段修改
	 * 
	 * @param table
	 * @param clobFiled
	 * @param primaryKeys
	 */
	public void updateClobAsString(String table, String clobFiled,
			String[] primaryKeys, Object[] params);

	/**
	 * 统计符合条件的数目
	 * 
	 * @param table
	 * @param primaryKeys
	 *            所依据的字段
	 * @param params
	 *            参数
	 * @return
	 */
	public int count(String table, String[] primaryKeys, Object[] params);

	/**
	 * 统计符合条件的数目
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public int count(String sql, Object[] params);

	/**
	 * 执行过滤sql
	 * 
	 * @param filterID
	 * @param recID
	 * @return true：符合条件，false：不符合条件
	 */
	public boolean getFilterResult(final Integer filterID, final Integer recID);

	/**
	 * 根据可执行sql进行数据库更新操作
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public int update(String sql, Object[] params);

	/**
	 * 获取序列下一个值
	 * 
	 * @param seqName
	 * @return
	 */
	public int getSeqID(String seqName);

	/**
	 * 构造树
	 * 
	 * @param sql
	 *            查询sql语句
	 * @param params
	 *            查询字段从第六列开始往后要放入attributes中的字段
	 * @param expandTypes
	 *            需要展开的节点类型，多个之间用逗号分隔。例如：group,service
	 * @return
	 */
	List<TreeNode> makeTree(String sql, String[] params, String expandTypes,
			boolean async);

	/**
	 * drop table
	 * 
	 * @param tableName
	 */
	public void dropTable(String tableName);

}
