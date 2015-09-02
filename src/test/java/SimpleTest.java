import java.io.UnsupportedEncodingException;

import redis.clients.jedis.Jedis;

public class SimpleTest {
	public static void main(String[] args) throws UnsupportedEncodingException {
		Jedis jedis = new Jedis("192.168.5.139", 6379);
		// for (int i = 0; i < 100000; i++) {
		// // jedis.set("test" + i, "testvalue" + i);
		// System.out.println(jedis.get("test" + i));
		// }
		jedis.get("asd");
		jedis.close();
	}
}
