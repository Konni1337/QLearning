import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Allquantor on 20.10.14.
 */
public  class Test {
	public static void main(String[] args) {
		Jedis redisClient = new Jedis("localhost",6379);
		Set<String> allKeys = redisClient.keys("1_Player*");
		Set<String> allValue = new HashSet<>();
		for(String k : allKeys) {
			allValue.add(redisClient.get(k));

		}
		System.out.println("allKeysSize:" + allKeys.size() + ":::" + "allValuesSize:" + allValue.size());
	}
}
