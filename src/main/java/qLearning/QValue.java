package qLearning;

import java.util.HashSet;
import java.util.Set;
import qLearning.util.Config;
import qLearning.util.FloatHelper;

public class QValue {

	private State state;
	private Action action;
	private int visited;
	private float value;

	public QValue(State state, Action action, int visited, float value) {
		this.state = state;
		this.action = action;
		this.visited = visited;
		this.value = value;
	}

	public QValue(State state, Action action) {
		new QValue(state, action, Config.INITIAL_VISITED, Config.INITIAL_Q_VALUE);
	}

	public static float getMax(State state) {
		Set<Float> qValues = new HashSet<>();
		// TODO get qValues from redis
		return FloatHelper.getMax(qValues);
	}

	public void incrementVisited() {
		visited++;
	}

	public State getState() {
		return state;
	}

	public Action getAction() {
		return action;
	}

	public int getVisited() {
		return visited;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public void save() {
		// TODO save this to redis
	}

}
