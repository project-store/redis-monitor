package com.bee.redisflag.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weiwei
 * 
 * 导航节点
 */
public class NavNode {
	/** 导航节点类型：一级导航 */
	public static final int NAV_NODE_TYPE_TOP_NAV = 1;
	/** 导航节点类型：页内导航 */
	public static final int NAV_NODE_TYPE_PAGE_NAV = 2;

	private Integer id;
	private String name;
	private String icon;
	private String path;
	private String description;
	private Integer type;
	private List<NavNode> children = new ArrayList<>();
	private NavNode parent;

	public NavNode() {
	}

	public NavNode(String name, String path, String icon, String description) {
		this.name = name;
		this.icon = icon;
		this.path = path;
		this.description = description;
	}

	public NavNode(String name, String url, String icon) {
		this(name, url, icon, "");
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
		if (this.id == null) {
			this.id = path.hashCode();
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<NavNode> getChildren() {
		return children;
	}

	public void setChildren(List<NavNode> children) {
		this.children = children;
		for (NavNode navNode : children) {
			navNode.setParent(this);
		}
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public NavNode getParent() {
		return parent;
	}

	public void setParent(NavNode parent) {
		this.parent = parent;
	}

	//是否有子节点
	public boolean hasChild() {
		return children.size() > 0;
	}

	//是否有一级导航类别（type=NAV_NODE_TYPE_TOP_NAV）的子节点
	public boolean hasTopNavChild() {
		for (NavNode navNode : this.children) {
			if (navNode.getType() == NAV_NODE_TYPE_TOP_NAV) {
				return true;
			}
		}
		return false;
	}

	//获取一级导航类别（type=NAV_NODE_TYPE_TOP_NAV）的子节点
	public List<NavNode> getTopNavChildren() {
		List<NavNode> topNavNodes = new ArrayList<>();
		for (NavNode navNode : this.children) {
			if (navNode.getType() == NAV_NODE_TYPE_TOP_NAV) {
				topNavNodes.add(navNode);
			}
		}
		return topNavNodes;
	}

	@Override
	public String toString() {
		return name;
	}

}
