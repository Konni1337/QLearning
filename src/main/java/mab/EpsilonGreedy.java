package mab;

import Core.Bomb;
import Core.Playboard;
import Core.Player;
import Core.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.*;

import static mab.util.Config.*;

/**
 * Created by Allquantor on 05.10.14.
 */
public class EpsilonGreedy extends User {


	private int aliveCounter = 0;

	//this is logger use it intern
	//Logger logger = Logger.getRootLogger();

	private Bandit pastBandit;
	private Action pastAction;
	private Bandit currentBanditFromState;

	private boolean iAmAlive = false;
	private boolean isEnemyAlive = false;
	Jedis redisClient = new Jedis("localhost", 6379);
	private List<Bandit> banditPersister;

	public EpsilonGreedy(int playerID) {
		super(playerID);
		//logger.info("Player with PlayerID" + playerID + " starts the game");
		init();
	}


	private void incrementLatestID() {
		redisClient.set(REDISKEY_LATEST_BANDIT_ID, String.valueOf(getLatestIDBandit() + 1));
	}

	public int getLatestIDBandit() {
		int parsedID = 0;
		try {
			parsedID = Integer.parseInt(redisClient.get(REDISKEY_LATEST_BANDIT_ID));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return parsedID;
	}

	GsonBuilder builder = new GsonBuilder();
	public Gson gson = builder.create();

	@Override
	public int getAction(Playboard stateObject) {
		if (iAmAlive) {
			aliveCounter += 1;
			//logger.info("The Player_" + this.getId() + " is<" + aliveCounter + ">" + " Rounds alive");
		} else {
		//	logger.info("The Player_" + this.getId() + " is<" + aliveCounter + ">" + " Rounds alive");
			aliveCounter = 0;
		}

		//update decision states to know what is happened since the last round
		updateDecisionStates(stateObject);
		// update arm which was pulled in previous step
		updateArm();
		// parse bandit from current state
		currentBanditFromState = getBanditFromState(stateObject);
		// check if this bandit exist in persistence
		Bandit possibleBandit = banditExists(currentBanditFromState);

		// epsilonGreegyPull arm for prediction, save the pulled action & bandit
		if (possibleBandit != null) {
			pastBandit = possibleBandit;
			pastAction = possibleBandit.epsilonGreegyPull();
			return pastAction.actionID;
		} else {
			for (int id : stateObject.getPossibleActions()) {
				currentBanditFromState.addArm(new BanditArm(new Action(id)));
			}

			saveToPersistance(currentBanditFromState);
			incrementLatestID();
			pastBandit = currentBanditFromState;
			pastAction = currentBanditFromState.epsilonGreegyPull();

			return pastAction.actionID;
		}


	}

	@Override
	public void resetMove() {

	}

	@Override
	public void gameOver(boolean won) {

	}

	/**
	 * update decision states
	 *
	 * @param stateObject
	 */
	private void updateDecisionStates(Playboard stateObject) {
		Set<Player> spieler = stateObject.getPlayers();
		for (Player s : spieler) {

			if (s.getId() == this.getId()) {
				iAmAlive = s.isAlive();
			} else {
				isEnemyAlive = s.isAlive();
			}
		}

	}

	/**
	 * updates arm
	 */

	public void updateArm() {

		if (pastBandit != null && pastAction != null) {

			if (iAmAlive && isEnemyAlive) {
				pastBandit.updateArm(pastAction, 1.0);
			} else if (!iAmAlive && isEnemyAlive) {
				pastBandit.updateArm(pastAction, -1.0);
			} else if (iAmAlive && !isEnemyAlive) {
				pastBandit.updateArm(pastAction, -1.0);
			} else if (!iAmAlive && !isEnemyAlive) {
				pastBandit.updateArm(pastAction, -1.0);
			}

			saveToPersistance(pastBandit);

			// for the rewarding
			// should be discuss
		}

	}

	/**
	 * build bandit form state
	 *
	 * @param stateObject
	 * @return
	 */

	private Bandit getBanditFromState(Playboard stateObject) {
		Map<String, Object> reflectMap = new HashMap<>();
		int i = 0;


		Set<Player> spieler = stateObject.getPlayers();

		for (Player s : spieler) {
			if (s.getId() == this.getId()) {
				reflectMap.put(OWN_POS_X, String.valueOf(s.getX()));
				reflectMap.put(OWN_POS_Y, String.valueOf(s.getY()));
			} else {
				reflectMap.put(ENEMY_POS_X, String.valueOf(s.getX()));
				reflectMap.put(ENEMY_POS_Y, String.valueOf(s.getY()));
			}
		}


		Set<Bomb> bombs = stateObject.getBombs();
		String bombPrefix = "bomb:";
		int counter = 0;


		for (Bomb b : bombs) {
			String key = String.valueOf(bombPrefix + counter);
			String value = String.valueOf(b.getX()) + String.valueOf(b.getY()) + String.valueOf(b.getCounter());
			reflectMap.put(key, value);
			counter += 1;
		}


		Situation s = new Situation(getLatestIDBandit() + 1, reflectMap);
		Bandit b = new Bandit(getLatestIDBandit() + 1, s);
		return b;
	}

	// * Persistance Operations * //

	private void saveToPersistance(Bandit bandit) {
		redisClient.set(String.valueOf(this.getId()) + "_Player" + String.valueOf(bandit.id), gson.toJson(bandit));
		updatePersistance(bandit);
	}

	private void updatePersistance(Bandit b) {
		Bandit possibleBandit = banditExists(b);
		if (b != null) banditPersister.remove(possibleBandit);
		banditPersister.add(b);
	}

	private Bandit banditExists(Bandit currentBandit) {
		return banditPersister.stream().
				filter(b -> b.equals(currentBandit)).
				findFirst().
				orElse(null);
	}

	/**
	 * init the list of possible bandit if you start bandit
	 */
	private void init() {
		banditPersister = new ArrayList<>();
		Set<String> keys = redisClient.keys(String.valueOf(this.getId()) + "_Player" + "*");
		for (String k : keys) {
			// get the json here
			String banditJson = redisClient.get(k);
			try {
				Bandit bandit = gson.fromJson(banditJson, Bandit.class);
				banditPersister.add(bandit);
			} catch (Exception e) {
				System.out.println("No bandit was found in redis call");
				e.printStackTrace();
			}
		}
	}


}
