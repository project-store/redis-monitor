package com.bee.redisflag.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 导航节点辅助类
 * 
 * @author weiwei1
 * 
 */
public class NavHelper {

	// 导航节点
	public static List<NavNode> navNodes = new ArrayList<>();
	public static List<NavNode> topNavNodes = new ArrayList<>();

	/**
	 * 初始化
	 * 
	 * @param navNodes
	 */
	public static void init(List<NavNode> navNodes) {
		for (NavNode navNode : navNodes) {
			if (navNode.getType() == NavNode.NAV_NODE_TYPE_TOP_NAV) {
				NavHelper.topNavNodes.add(navNode);
			}
			addNodes(navNode);
		}
	}

	/**
	 * 添加节点
	 * 
	 * @param navNode
	 */
	private static void addNodes(NavNode navNode) {
		NavHelper.navNodes.add(navNode);
		if (navNode.hasChild()) {
			for (NavNode child : navNode.getChildren()) {
				addNodes(child);
			}
		}
	}

	/**
	 * 获取当前路径所属的节点
	 * 
	 * @param path
	 * @return
	 */
	public static NavNode getNavNodeByPath(String path) {
		for (NavNode navNode : navNodes) {
			if (Pattern.matches(navNode.getPath(), path)) {
				// if (StringUtils.equals(navNode.getPath(), path)) {
				return navNode;
			}
		}
		return null;
	}

	/**
	 * 获取当前路径的节点集合
	 * 
	 * @param path
	 * @return
	 */
	public static List<NavNode> getNavNodeLinkByPath(String path) {
		List<NavNode> navNodeLink = new ArrayList<>();
		NavNode leafNode = getNavNodeByPath(path);
		if (leafNode == null) {
			return navNodeLink;
		}
		appendParent(navNodeLink, leafNode);
		// 按根节点到末节点的顺序排序
		Collections.reverse(navNodeLink);
		return navNodeLink;
	}

	private static void appendParent(List<NavNode> navNodeLink, NavNode leafNode) {
		navNodeLink.add(leafNode);
		if (leafNode.getParent() != null) {
			appendParent(navNodeLink, leafNode.getParent());
		}
	}

}
