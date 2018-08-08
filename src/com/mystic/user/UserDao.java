package com.mystic.user;

import java.util.List;
import java.util.Map;

import com.mystic.util.Pagination;

public interface UserDao {

	User getUser(String logonname, String password);

	void userListPagination(Pagination pagination, Map<String, Object> param);

	int userAdd(User user);

	int userDelById(int userid);

	User userGetById(int userid);

	int userEdit(User user);

	List<User> userListByOrganizationId(int organizationId);
	
	int passwordEdit(User user);

	List<User> UserGetByRoleId(int roleid);

	User userGetByName(String logonname);

}
