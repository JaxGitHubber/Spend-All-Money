package model;

import redis.clients.jedis.Jedis;

public class JedisEntityDeleter {
	public static void del(String name) {
		Jedis jedis = new Jedis("localhost", 6379);
		jedis.del(name);
		jedis.close();
	}
}
