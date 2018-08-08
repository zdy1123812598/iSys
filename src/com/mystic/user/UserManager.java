package com.mystic.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.mystic.common.TreeNodeTemp;
import com.mystic.organization.Organization;
import com.mystic.organization.OrganizationManager;
import com.mystic.role.RoleManager;
import com.mystic.util.Pagination;

@Component
public class UserManager {
	@SuppressWarnings("unused")
	private final Log logger = LogFactory.getLog(UserManager.class);

	@Autowired
	private JdbcTemplate jt = null;

	@Autowired
	public UserDao userDao;

	@Autowired
	public RoleManager roleManager;

	@Autowired
	public OrganizationManager organizationManager;

	public User getUser(String logonname, String password) {
		return userDao.getUser(logonname, password);
	}

	public void userListPagination(Pagination pagination,
			Map<String, Object> param) {
		userDao.userListPagination(pagination, param);
	}

	public int userAdd(User user) {
		return userDao.userAdd(user);
	}

	public int userDelById(int userid) {
		return userDao.userDelById(userid);
	}

	public User userGetById(int userid) {
		return userDao.userGetById(userid);
	}

	public int userEdit(User user) {
		return userDao.userEdit(user);
	}
	
	public int passwordEdit(User user) {
		return userDao.passwordEdit(user);
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<TreeNodeTemp> userTreeGet(int roleid) {
		List<TreeNodeTemp> result_1 = new ArrayList();
		List<Organization> organzations = organizationManager
				.organizationListAll();
		List<Integer> listOrganization = new ArrayList<Integer>();
		List<Integer> listUser = new ArrayList<Integer>();
		if (roleid != 0) {
			List<User> user = userDao.UserGetByRoleId(roleid);

			for (int k = 0; k < user.size(); k++) {
				if (!listOrganization.contains(user.get(k).getOrganizationid())) {
					listOrganization.add(user.get(k).getOrganizationid());
				}
				if (!listUser.contains(user.get(k).getUserid())) {
					listUser.add(user.get(k).getUserid());
				}
			}
		}
		for (int i = 0; i < organzations.size(); i++) {
			TreeNodeTemp treeNode1_1 = new TreeNodeTemp();
			treeNode1_1.setId(organzations.get(i).getOrganizationId());
			treeNode1_1.setText(organzations.get(i).getOrganizationName());
			treeNode1_1.setType("organization");
			List<User> users = userDao.userListByOrganizationId(organzations
					.get(i).getOrganizationId());
			List<TreeNodeTemp> result_2 = new ArrayList();

			for (int j = 0; j < users.size(); j++) {
				TreeNodeTemp treeNode1_2 = new TreeNodeTemp();
				treeNode1_2.setId(users.get(j).getUserid());
				treeNode1_2.setText(users.get(j).getUsername());
				treeNode1_2.setType("user");
				Map<String, Object> stateUser = new HashMap<String, Object>();
				stateUser.put("disabled", false);
				stateUser.put("expanded", false);
				stateUser.put("selected", false);
				if (listUser.contains(users.get(j).getUserid())) {
					stateUser.put("checked", true);
				} else {
					stateUser.put("checked", false);
				}

				treeNode1_2.setState(stateUser);
				result_2.add(treeNode1_2);
			}
			treeNode1_1.setNodes(result_2);
			Map<String, Object> state = new HashMap<String, Object>();
			state.put("disabled", false);
			state.put("expanded", false);
			state.put("selected", false);
			if (listOrganization.contains(organzations.get(i)
					.getOrganizationId())) {
				state.put("checked", true);
			} else {
				state.put("checked", false);
			}

			treeNode1_1.setState(state);
			result_1.add(treeNode1_1);
		}

		return result_1;
	}

	public User userGetByName(String logonname) {
		
		return userDao.userGetByName(logonname);
	}
}
