package com.mystic.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mystic.common.TreeNodeTemp;
import com.mystic.link.Link;
import com.mystic.link.LinkManager;
import com.mystic.util.Pagination;

@Component
public class RoleManager {
	@SuppressWarnings("unused")
	private final Log logger = LogFactory.getLog(RoleManager.class);
	@Autowired
	public RoleDao roleDao;

	@Autowired
	public LinkManager linkManager;

	public int roleAdd(String roleName, int creatorid) {
		return roleDao.roleAdd(roleName, creatorid);
	}

	public void roleListPagination(Pagination pagination,
			Map<String, Object> param) {
		roleDao.roleListPagination(pagination, param);

	}

	public Role roleGetById(int roleid) {
		return roleDao.roleGetById(roleid);
	}

	public int roleEdit(Role role) {
		return roleDao.roleEdit(role);
	}

	public int roleDelById(int roleid) {
		return roleDao.roleDelById(roleid);
	}

	public int roleUserInsert(int roleid, int userid) {
		return roleDao.roleUserInsert(roleid, userid);
	}

	public int roleUserDelete(int roleid) {
		return roleDao.roleUserDelete(roleid);
	}

	public int roleGetLastID() {
		return roleDao.roleGetLastID();
	}

	public List<Role> roleListByUserId(int userid) {
		return roleDao.roleListByUserId(userid);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<TreeNodeTemp> premissionTree(int roleid) {
		List<TreeNodeTemp> result_1 = new ArrayList();
		List<Link> linkOne = linkManager.linkAllList();
		List<Integer> listPremission = new ArrayList<Integer>();

		if (roleid != 0) {
			List<Map<String,Object>> premissions = roleDao
					.getPremissionByRoleId(roleid);

			for (int k = 0; k < premissions.size(); k++) {
				int linkid = Integer.valueOf(premissions.get(k).get("linkid").toString());
				if (!listPremission.contains(linkid)) {
					listPremission.add(linkid);

				}
				int parentId = linkManager.linkGetById(linkid).getParentid();
				if (!listPremission.contains(parentId)) {
					listPremission.add(parentId);
				}

			}
		}

		for (int i = 0; i < linkOne.size(); i++) {
			TreeNodeTemp treeNode1_1 = new TreeNodeTemp();
			treeNode1_1.setId(linkOne.get(i).getLinkid());
			treeNode1_1.setText(linkOne.get(i).getLinkname());
			treeNode1_1.setType("linkOne");
			List<Link> linkTwo = linkManager.getLinkListByParentId(linkOne.get(
					i).getLinkid());
			List<TreeNodeTemp> result_2 = new ArrayList();
			for (int j = 0; j < linkTwo.size(); j++) {
				TreeNodeTemp treeNode1_2 = new TreeNodeTemp();
				treeNode1_2.setId(linkTwo.get(j).getLinkid());
				treeNode1_2.setText(linkTwo.get(j).getLinkname());
				treeNode1_2.setType("linkTwo");
				Map<String, Object> stateTwo = new HashMap<String, Object>();
				stateTwo.put("disabled", false);
				stateTwo.put("expanded", false);
				stateTwo.put("selected", false);
				if (listPremission.contains(linkTwo.get(j).getLinkid())) {
					stateTwo.put("checked", true);
				} else {
					stateTwo.put("checked", false);
				}

				treeNode1_2.setState(stateTwo);
				result_2.add(treeNode1_2);

			}

			treeNode1_1.setNodes(result_2);

			Map<String, Object> state = new HashMap<String, Object>();
			state.put("disabled", false);
			state.put("expanded", false);
			state.put("selected", false);
			if (listPremission.contains(linkOne.get(i).getLinkid())) {
				state.put("checked", true);
			} else {
				state.put("checked", false);
			}

			treeNode1_1.setState(state);
			result_1.add(treeNode1_1);
		}

		return result_1;

	}

	@SuppressWarnings("rawtypes")
	public int premissionEdit(int roleid, List<Map> premissions) {
		int del = roleDao.delPremission(roleid);
		if (del < 0) {
			return del;
		}
		int add = 0;
		for (int i = 0; i < premissions.size(); i++) {
			Map m = premissions.get(i);
			add = roleDao.addPremission(
					Integer.valueOf(m.get("roleid").toString()),
					Integer.valueOf(m.get("linkid").toString()));
			if (add < 0) {
				return add;
			}
		}
		return 1;

	}

	public int premissionDel(int roleid) {
		int del = roleDao.delPremission(roleid);

		return del;

	}
}
