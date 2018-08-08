package com.mystic.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 通用树节点
 * 
  * @author zdy
 * @since 2017-04-20
 * 
 */
public class TreeNode {
	
	public static final String TREENODE_STATE_OPEN = "open";
	public static final String TREENODE_STATE_CLOSED = "closed";
	public static final String TREE_ROOT_ID = "-1";
	
	public static final String TREENODE_ICON_FOLDER = "icon-group";
	public static final String TREENODE_ICON_FILE = "icon-file";
	
	private String id;
	private String text;
	private String pID;
	private String iconCls;
	private String state;
	private boolean checked;
	private List<TreeNode> children = new ArrayList<TreeNode>();
	private Map<String,Object> attributes;
	private String rootID;
	
	public TreeNode(){}
	
	public TreeNode(String id,String text){
		this(id,text,"","",TREENODE_STATE_OPEN,false);
	}
	
	public TreeNode(String id,String text,String pID,String iconCls){
		this(id,text,pID,iconCls,TREENODE_STATE_OPEN,false);
	}
	
	public boolean isRootNode(){
		return null == id?true:getRootID().equalsIgnoreCase(id);
	}
	
	public TreeNode(String id,String text,String pID,String iconCls,String state,boolean checked){
		this.id = id;
		this.text = text;
		this.pID = pID;
		this.iconCls = iconCls;
		this.state = state;
		this.checked = checked;
	}
	
	@JsonIgnore
	public Object getAttribute(String key){
		return this.attributes.get(key);
	}
	
	@JsonIgnore
	public Integer getNodeLevel(){
		return ValueUtils.getInt(getAttribute("NODELEVEL"),-1);
	}
	
	//不处理children
	@JsonIgnore
	public TreeNode clone(){
		TreeNode newNode = new TreeNode(this.id,this.text,this.pID,this.iconCls,this.state,this.checked);
		if(attributes == null)
			return newNode;
		Set<String> keySet = attributes.keySet();
		if(keySet.size() == 0)
			return newNode;
		for(Iterator<String> itr = keySet.iterator();itr.hasNext();){
			String key = itr.next();
			newNode.addAttribute(key,attributes.get(key));
		}
		return newNode;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getpID() {
		return null == pID || "".equals(pID) ? TREE_ROOT_ID : pID;
	}
	public void setpID(String pID) {
		this.pID = pID;
	}
	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public Map<String, Object> getAttributes() {
		return attributes == null ? new HashMap<String, Object>() : attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public String getRootID() {
		return null == rootID?TREE_ROOT_ID:rootID;
	}

	public void setRootID(String rootID) {
		this.rootID = rootID;
	}

	public void addAttribute(String key,Object value){
		if(null == this.attributes)
			this.attributes = new HashMap<String, Object>();
		this.attributes.put(key, value);
	}
	
}
