package com.bee.redisflag.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONArray;
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
		RedisClusterHolder.init(JSONArray.parseArray(json.toString(), RedisCluster.class));
		logger.info("开始创建Redis连接...");
		Collection<RedisNode> values = RedisClusterHolder.getNodeMapping().values();
		for (RedisNode redisNode : values) {
			try {
				redisNode.connect();
				logger.info(redisNode + "连接已创建");
			} catch (Exception e) {
				logger.warn(redisNode + "连接创建失败!", e);
			}
		}
	}
}
