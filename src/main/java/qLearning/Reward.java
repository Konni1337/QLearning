package qLearning;

import qLearning.util.Config;
import Core.Playboard;
import Core.Player;

public class Reward {

	private boolean isMeAlive;
	private boolean isEnemyAlive;

	public Reward(Playboard playboard, int myId) {
		for (Player player : playboard.getPlayers()) {
			if (player.getId() == myId) {
				isMeAlive = player.isAlive();
			} else {
				isEnemyAlive = player.isAlive();
			}
		}
	}

	public float call(State newState, State oldState) {
		if (!isMeAlive && isEnemyAlive) {
			return Config.I_AM_DEAD_REWARD;
		} else if (isMeAlive && !isEnemyAlive) {
			return Config.ENEMY_DEAD_REWARD;
		} else if (!isMeAlive && !isEnemyAlive) {
			return Config.BOTH_DEAD_REWARD;
		} else {
			return Config.NO_ONE_DEAD_REWARD;
		}
	}

}
