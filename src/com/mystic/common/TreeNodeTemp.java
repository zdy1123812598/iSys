package com.mystic.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用树节点
 * 
 * @author zdy
 * @since 2017-04-20
 * 
 */
public class TreeNodeTemp {

	private int id;
	private String text;
	private String type;
	private List<TreeNodeTemp> nodes = new ArrayList<TreeNodeTemp>();
	private Map<String, Object> state;
	private String icon;
	private String href;
	private String[] tags;
	private String pid;
	private String rootid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<TreeNodeTemp> getNodes() {
		return nodes;
	}

	public void setNodes(List<TreeNodeTemp> nodes) {
		this.nodes = nodes;
	}

	public Map<String, Object> getState() {
		return state == null ? new HashMap<String, Object>() : state;
	}

	public void setState(Map<String, Object> state) {
		this.state = state;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getRootid() {
		return rootid;
	}

	public void setRootid(String rootid) {
		this.rootid = rootid;
	}

}
