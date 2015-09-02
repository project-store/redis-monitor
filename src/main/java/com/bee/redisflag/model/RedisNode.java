package com.bee.redisflag.model;

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

	public Jedis getJedis() {
		if (jedis == null) {
			jedis = new Jedis(host, port);
			jedis.getClient().setDb(0);
		}
		return jedis;
	}

}
