package qLearning.util;

import redis.clients.jedis.Jedis;

/**
 * Created by Allquantor on 09.10.14.
 */
public final class Config {

	/* QLearning default values */

	// 0.0 < DISCOUNT_FACTOR < 1.0
	public static final float DISCOUNT_FACTOR = 0.0f;
	// 0.0 < INITIAL_Q_VALUE < 1.0
	public static final float INITIAL_Q_VALUE = 0;
	// fixed
	public static final int INITIAL_VISITED = 0;

	/* reward values */
	public static final float I_AM_DEAD_REWARD = -5.0f;
	public static final float ENEMY_DEAD_REWARD = 5.0f;
	public static final float BOTH_DEAD_REWARD = -1.0f;
	public static final float NO_ONE_DEAD_REWARD = 1.0f;

	/* policy default values */

	// for epsilon greedy
	public static final float EPSILON = 0.05f;
	// temperatur value for softmax
	public static final float TAU = 0.05f;

	/* redis */
	public static Jedis REDIS_CLIENT = new Jedis("localhost", 6380);

	public static final String OWN_POS_X = "OWN_POS_X";
	public static final String OWN_POS_Y = "OWN_POS_Y";
	public static final String ENEMY_POS_X = "ENEMY_POS_X";
	public static final String ENEMY_POS_Y = "ENEMY_POS_Y";
	public static final String BOMB_SET = "BOMB_SET";
	public static final String BOMB_PREFIX = "bomb:";

	public static final String REDISKEY_LATEST_STATE_ID = "latestIDBandit";
	public static final int ID_INCREMENT = 1;

}
