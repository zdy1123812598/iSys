package com.mystic.organization;

import java.util.List;
import java.util.Map;

import com.mystic.util.Pagination;

public interface OrganizationDao {

	int organizationAdd(String organizationName, int creatorid);

	void organizationListPagination(Pagination pagination,
			Map<String, Object> param);

	Organization organizationGetById(int organizationid);

	int organizationEdit(Organization organization);

	int organizationDelById(int organizationid);

	List<Organization> organizationListAll();

}
