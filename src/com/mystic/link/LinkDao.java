package com.mystic.link;

import java.util.List;
import java.util.Map;

import com.mystic.util.Pagination;

public interface LinkDao {

	void linkListPagination(Pagination pagination, Map<String, Object> param);

	int linkAdd(Link link);

	int linkDelById(int linkid);

	Link linkGetById(int linkid);

	int linkEdit(Link link);

	List<Link> linkAllList();

	List<Link> getLinkListByParentId(int parentid);

	List<Link> linkListByRoleId(int roleid);

}
