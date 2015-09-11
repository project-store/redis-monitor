package com.bee.redisflag.model;

import java.io.ByteArrayInputStream;
import java.util.Properties;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisNode {
	private String name = "";
	private String host;
	private int port;
	private JedisPool jedisPool;

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
		if (jedisPool == null) {
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.setMaxTotal(20);
			poolConfig.setMaxIdle(10);
			poolConfig.setMinIdle(5);
			poolConfig.setMaxWaitMillis(1000 * 10);
			jedisPool = new JedisPool(poolConfig, host, port, 10000);
		}
		Jedis jedis = jedisPool.getResource();
		return jedis;
	}

	public void closeConnect() {
		jedisPool.close();
	}

	public String role() {
		try {
			Jedis jedis = connect();
			Properties redis_replication_properties = PropertiesLoaderUtils.loadProperties(new InputStreamResource(new ByteArrayInputStream(jedis.info("Replication").getBytes())));
			jedis.close();
			return redis_replication_properties.getProperty("role");
		} catch (Exception e) {
			e.printStackTrace();
			return "unknown";
		}
	}

	@Override
	public String toString() {
		return host + ":" + port;
	}

}
