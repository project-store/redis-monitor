package com.bee.redisflag.model;

import java.util.List;

public class RedisCluster {
	private String name;
	private List<RedisNode> nodes;

	public RedisCluster() {
	}

	public RedisCluster(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RedisNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<RedisNode> nodes) {
		this.nodes = nodes;
	}

}
