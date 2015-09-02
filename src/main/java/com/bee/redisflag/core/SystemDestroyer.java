package com.bee.redisflag.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.bee.redisflag.data.RedisClusterHolder;
import com.bee.redisflag.model.RedisNode;

/**
 * @author weiwei 系统终止程序
 */
public class SystemDestroyer {

	private Log logger = LogFactory.getLog(SystemDestroyer.class);
	private ApplicationContext applicationContext;

	public SystemDestroyer(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void destroy() throws Exception {
		logger.info("Start System destroyed...");
		logger.info(applicationContext);
		closeJedisClient();
	}

	private void closeJedisClient() {
		logger.info("开始释放redis连接...");
		for (String key : RedisClusterHolder.getNodeMapping().keySet()) {
			try {
				RedisNode redisNode = RedisClusterHolder.getNodeMapping().get(key);
				redisNode.getJedis().close();
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				logger.info(key + "已释放");
			}
		}
	}
}
