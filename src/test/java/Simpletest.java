import redis.clients.jedis.Jedis;

public class Simpletest {
	public static void main(String[] args) throws Exception {
		final String host = "192.168.5.139";
		final int port = 6379;
		final String key = "aa";

		for (int i = 0; i < 100; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Jedis jedis = new Jedis(host, port);
					System.out.println(jedis.get(key));
					try {
						Thread.sleep(3000l);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					jedis.close();
				}
			}).start();

		}

		// Jedis jedis = new Jedis(host, port);
		// for (int i = 0; i < 10; i++) {
		// jedis.rpush("aa", "Lvalue" + i);
		// }

		// while (jedis.llen(key) > 0) {
		// System.out.println(jedis.lpop(key));
		// }

		// while (true) {
		// List<String> blpop = jedis.blpop(10, key);
		// if (CollectionUtils.isNotEmpty(blpop)) {
		// System.out.println(blpop.get(1));
		// }
		// }

		// jedis.close();

	}
}
