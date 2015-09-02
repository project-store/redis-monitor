package com.bee.redisflag.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bee.redisflag.model.RedisCluster;
import com.bee.redisflag.model.RedisNode;

public class RedisClusterHolder {
	private static List<RedisCluster> redisClusters = new ArrayList<>();
	private static Map<String, RedisNode> nodeMapping = new HashMap<>();

	public static void init(List<RedisCluster> redisClusters) {
		RedisClusterHolder.redisClusters = redisClusters;
		for (RedisCluster redisCluster : redisClusters) {
			for (RedisNode node : redisCluster.getNodes()) {
				nodeMapping.put(node.getHost() + ":" + node.getPort(), node);
			}
		}
	}

	public static List<RedisCluster> getRedisClusters() {
		return redisClusters;
	}

	public static void setRedisClusters(List<RedisCluster> redisClusters) {
		RedisClusterHolder.redisClusters = redisClusters;
	}

	public static Map<String, RedisNode> getNodeMapping() {
		return nodeMapping;
	}

	public static void setNodeMapping(Map<String, RedisNode> nodeMapping) {
		RedisClusterHolder.nodeMapping = nodeMapping;
	}

}
