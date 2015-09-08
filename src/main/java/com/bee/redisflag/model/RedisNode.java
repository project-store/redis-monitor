package com.bee.redisflag.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.bee.redisflag.core.SpringContext;

import redis.clients.jedis.Jedis;

public class RedisNode {
	private String name = "";
	private String host;
	private int port;
	private Jedis jedis;

	public RedisNode() {
	}

	public RedisNode(String name, String host, int port) {
		this.name = name;
		this.host = host;
		this.port = port;
	}

	public RedisNode(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Jedis connect() {
		if (jedis == null) {
			Integer timeout = SpringContext.getEnvironment().getProperty("redis.timeout", Integer.class, 10000);
			jedis = new Jedis(host, port, timeout);
			jedis.getClient().setDb(0);
		}
		return jedis;
	}

	public String role() {
		try {
			Properties redis_replication_properties = PropertiesLoaderUtils.loadProperties(new InputStreamResource(new ByteArrayInputStream(connect().info("Replication").getBytes())));
			return redis_replication_properties.getProperty("role");
		} catch (IOException e) {
			e.printStackTrace();
			return "unknown";
		}
	}

	@Override
	public String toString() {
		return host + ":" + port;
	}

}
