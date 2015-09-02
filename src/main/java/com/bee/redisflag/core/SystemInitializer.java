package com.bee.redisflag.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bee.redisflag.data.RedisClusterHolder;
import com.bee.redisflag.model.RedisCluster;
import com.bee.redisflag.model.RedisNode;

/**
 * @author weiwei 系统初始化程序
 */
public class SystemInitializer {

	private Log logger = LogFactory.getLog(SystemInitializer.class);
	private ApplicationContext applicationContext;

	public SystemInitializer(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void init() throws Exception {
		logger.info("Start System Init...");
		initRedisCluster();
		initNavNodes();
	}

	// 初始化导航节点
	public void initNavNodes() throws IOException {
		logger.info("初始化导航节点");
		File nav_map = applicationContext.getResource("classpath:map.json").getFile();
		StringBuffer navMapJson = new StringBuffer();
		BufferedReader br = new BufferedReader(new FileReader(nav_map));
		String data = br.readLine();
		while (data != null) {
			navMapJson.append(data);
			data = br.readLine();
		}
		br.close();
		NavHelper.init(JSONArray.parseArray(navMapJson.toString(), NavNode.class));
	}

	// redis集群初始化
	private void initRedisCluster() throws IOException, URISyntaxException {
		logger.info("初始化Redis集群");
		File nav_map = applicationContext.getResource("classpath:redis-cluster.json").getFile();
		StringBuffer json = new StringBuffer();
		BufferedReader br = new BufferedReader(new FileReader(nav_map));
		String data = br.readLine();
		while (data != null) {
			json.append(data);
			data = br.readLine();
		}
		br.close();
		List<RedisCluster> redisClusters = new ArrayList<>();
		JSONArray redisClusterArray = JSONArray.parseArray(json.toString());
		for (int i = 0; i < redisClusterArray.size(); i++) {
			JSONObject redisClusterJsonObj = redisClusterArray.getJSONObject(i);
			RedisCluster redisCluster = new RedisCluster(redisClusterJsonObj.getString("name"));
			List<RedisNode> nodes = new ArrayList<>();
			JSONArray nodesJsonArray = redisClusterJsonObj.getJSONArray("nodes");
			for (Object obj : nodesJsonArray) {
				String[] ip_port = StringUtils.split(obj.toString(), ":");
				RedisNode node = new RedisNode(ip_port[0], Integer.valueOf(ip_port[1]));
				nodes.add(node);
			}
			redisCluster.setNodes(nodes);
			redisClusters.add(redisCluster);
			RedisClusterHolder.init(redisClusters);

		}
	}
}
