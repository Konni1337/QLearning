package qLearning;

import qLearning.util.Config;
import Core.Playboard;

public class QLearning {
	private int myId;
	private Action oldAction;
	private State oldState;
	private QValue qValue;
	private Policy policy;

	public QLearning(Policy policy, int myId) {
		this.policy = policy;
		this.myId = myId;
	}

	public int getAction(Playboard playboard) {
		State newState = new State(playboard, myId);
		update(newState, new Reward(playboard, myId).call(newState, oldState));
		oldAction = policy.getAction(newState, playboard);
		oldState = newState;
		return oldAction.getValue();
	}

	public float update(State newState, float reward) {
		float total = reward + (Config.DISCOUNT_FACTOR * QValue.getMax(newState));
		qValue.incrementVisited();
		float learningRate = 1.0f / qValue.getVisited();
		qValue.setValue((1.0f - learningRate) * qValue.getValue() + learningRate * total);
		return 0.0f;
	}

	public int getLatestStateId() {
		int parsedID = Integer.MAX_VALUE;
		try {
			parsedID = Integer.parseInt(Config.REDIS_CLIENT.get(Config.REDISKEY_LATEST_STATE_ID));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return parsedID;
	}

}