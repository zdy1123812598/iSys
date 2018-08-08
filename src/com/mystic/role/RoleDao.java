package com.mystic.role;

import java.util.List;
import java.util.Map;

import com.mystic.util.Pagination;

public interface RoleDao {

	int roleAdd(String roleName, int creatorid);

	void roleListPagination(Pagination pagination, Map<String, Object> param);

	Role roleGetById(int roleid);

	int roleEdit(Role role);

	int roleDelById(int roleid);

	int roleUserInsert(int roleid, int userid);

	int roleUserDelete(int roleid);

	int roleGetLastID();

	List<Role> roleListByUserId(int userid);

	List<Map<String,Object>> getPremissionByRoleId(int roleid);

	int addPremission(int roleid, int linkid);

	int delPremission(int roleid);

}
