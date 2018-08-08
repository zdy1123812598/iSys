package com.mystic.login;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.mystic.link.Link;
import com.mystic.link.LinkManager;
import com.mystic.role.Role;
import com.mystic.role.RoleManager;

@Component
public class LoginManager {
	@SuppressWarnings("unused")
	private final Log logger = LogFactory.getLog(LoginManager.class);

	@Autowired
	private JdbcTemplate jt = null;

	@Autowired
	public LoginDao loginDao;

	@Autowired
	public RoleManager roleManager;

	@Autowired
	public LinkManager linkManager;

	public Set<Link> linkListByid(int userid) {
		// 根据id获取角色列表
		List<Role> roles = roleManager.roleListByUserId(userid);
		List<Link> links = new ArrayList<Link>();
		for (int i = 0; i < roles.size(); i++) {
			List<Link> link_temp = linkManager.linkListByRoleId(roles.get(i)
					.getRoleId());
			links.addAll(link_temp);
			for (int j = 0; j < link_temp.size(); j++) {
				if (0 != link_temp.get(j).getParentid()) {
					Link link = linkManager.linkGetById(link_temp.get(j)
							.getParentid());
					links.add(link);
				}
			}
		}
		// links = new ArrayList<Link>(new LinkedHashSet<Link>(links));
		Set<Link> linkset = new HashSet<Link>();
		linkset.addAll(links);
		return linkset;
	}
}
