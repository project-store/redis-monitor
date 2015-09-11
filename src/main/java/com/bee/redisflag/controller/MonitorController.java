package com.bee.redisflag.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bee.redisflag.data.RedisClusterHolder;
import com.bee.redisflag.model.RedisNode;

import redis.clients.jedis.Jedis;

@Controller
public class MonitorController {

	// @RequestMapping("/board")
	// public ModelAndView board() {
	// Map<String, Object> data = new HashMap<>();
	// return new ModelAndView("board").addAllObjects(data);
	// }

	@RequestMapping("/clusters")
	public ModelAndView clusters() {
		Map<String, Object> data = new HashMap<>();
		data.put("clusters", RedisClusterHolder.getRedisClusters());
		return new ModelAndView("clusters").addAllObjects(data);
	}

	@RequestMapping(value = "/monitor/{node:.*}", produces = { "text/html" })
	public Object monitor(@PathVariable("node") String node) {
		Map<String, Object> data = new HashMap<>();
		RedisNode redisNode = RedisClusterHolder.getNodeMapping().get(node);
		Jedis jedis = redisNode.connect();
		data.put("node", node);
		data.put("info", jedis.info());
		jedis.close();
		return new ModelAndView("monitor").addAllObjects(data);
	}

	@ResponseBody
	@RequestMapping("/monitor/{node:.*}")
	public Object monitorData(@PathVariable("node") String node) throws IOException {
		RedisNode redisNode = RedisClusterHolder.getNodeMapping().get(node);
		Jedis jedis = redisNode.connect();
		String redis_info = jedis.info();
		Properties redis_info_properties = PropertiesLoaderUtils.loadProperties(new InputStreamResource(new ByteArrayInputStream(redis_info.getBytes())));
		redis_info_properties.put("dbSize", jedis.dbSize());
		jedis.close();
		return redis_info_properties;
	}

	@RequestMapping(value = "/config/{node:.*}", produces = { "text/html" })
	public Object config(@PathVariable("node") String node) {
		Map<String, Object> data = new HashMap<>();
		RedisNode redisNode = RedisClusterHolder.getNodeMapping().get(node);
		Jedis jedis = redisNode.connect();
		data.put("node", node);
		List<String> configStrList = jedis.configGet("*");
		Map<String, Object> configs = new HashMap<>();
		for (int i = 0; i < configStrList.size(); i += 2) {
			configs.put(configStrList.get(i), configStrList.get(i + 1));
		}
		data.put("configs", configs);
		jedis.close();
		return new ModelAndView("config").addAllObjects(data);
	}

	@RequestMapping(value = "/slowlog/{node:.*}", produces = { "text/html" })
	public Object slowlog(@PathVariable("node") String node) {
		Map<String, Object> data = new HashMap<>();
		RedisNode redisNode = RedisClusterHolder.getNodeMapping().get(node);
		Jedis jedis = redisNode.connect();
		data.put("node", node);
		data.put("slowLogs", jedis.slowlogGet());
		jedis.close();
		return new ModelAndView("slowlog").addAllObjects(data);
	}

}
