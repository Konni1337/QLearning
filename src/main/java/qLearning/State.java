package qLearning;

import static mab.util.Config.ENEMY_POS_X;
import static mab.util.Config.ENEMY_POS_Y;
import static mab.util.Config.OWN_POS_X;
import static mab.util.Config.OWN_POS_Y;

import java.util.HashMap;
import java.util.Map;

import qLearning.util.Config;
import Core.Bomb;
import Core.Playboard;
import Core.Player;

public class State {

	private Map<String, Object> reflectMap = new HashMap<>();

	public State(Playboard playboard, int myPlayerId) {
		initializeReflectMap(playboard, myPlayerId);
	}

	private void initializeReflectMap(Playboard playboard, int myPlayerId) {
		addPlayers(playboard, myPlayerId);
		addBombs(playboard);
	}

	private void addPlayers(Playboard playboard, int myPlayerId) {
		for (Player s : playboard.getPlayers()) {
			if (s.getId() == myPlayerId) {
				reflectMap.put(OWN_POS_X, String.valueOf(s.getX()));
				reflectMap.put(OWN_POS_Y, String.valueOf(s.getY()));
			} else {
				reflectMap.put(ENEMY_POS_X, String.valueOf(s.getX()));
				reflectMap.put(ENEMY_POS_Y, String.valueOf(s.getY()));
			}
		}

	}

	private void addBombs(Playboard playboard) {
		int counter = 0;
		for (Bomb b : playboard.getBombs()) {
			String key = String.valueOf(Config.BOMB_PREFIX + counter);
			String value = String.valueOf(b.getX()) + String.valueOf(b.getY()) + String.valueOf(b.getCounter());
			reflectMap.put(key, value);
			counter += 1;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reflectMap == null) ? 0 : reflectMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (reflectMap == null) {
			if (other.reflectMap != null)
				return false;
		} else if (!reflectMap.equals(other.reflectMap))
			return false;
		return true;
	}

}
