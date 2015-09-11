import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPoolTest {
	public static void main(String[] args) throws Exception {
		String host = "192.168.5.139";
		int port = 6379;
		final String key = "aa";

		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(500);
		poolConfig.setMaxIdle(100);
		poolConfig.setMinIdle(20);
		poolConfig.setMaxWaitMillis(1000 * 100);
		final JedisPool jedisPool = new JedisPool(poolConfig, host, port);

		for (int i = 0; i < 500; i++) {
			final int tmp = i;
			new Thread(new Runnable() {
				@Override
				public void run() {
					Jedis jedis = jedisPool.getResource();
//					for (int j = 0; j < 1000; j++) {
//						System.out.println(tmp + "_" + j);
//						jedis.set("test" + tmp + "_" + j, "testvalue" + tmp + "_" + j);
//					}
					try {
						Thread.sleep(3000l);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					jedis.close();
				}
			}).start();

		}
		Thread.sleep(30000l);
		jedisPool.close();

		
		

	}
}
