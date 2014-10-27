package qLearning;

import Core.Playboard;
import Core.User;

public class KI extends User {

	private QLearning qLearning;

	public KI(int id) {
		super(id);
		this.qLearning = new QLearning(new EpsilonGreedy(), id);
	}

	@Override
	public int getAction(Playboard playboard) {
		return qLearning.getAction(playboard);
	}

	@Override
	public void resetMove() {
		// only used for Human		
	}

	@Override
	public void gameOver(boolean won) {
		// TODO handle gameover reward
	}

}
