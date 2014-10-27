package qLearning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import qLearning.util.Config;
import Core.Playboard;

public class EpsilonGreedy implements Policy {

	private float epsilon = Config.EPSILON;

	public EpsilonGreedy() {
	}

	@Override
	public Action getAction(State state, Playboard playboard) {
		if (!stateExists(state)) {
			initializeQValues(state, playboard);
		}

		Random r = new Random();
		if (r.nextDouble() < epsilon) {
			return getRandomAction(getQValuesFromState(state));
		} else {
			return getNextAction(getQValuesFromState(state));
		}
	}

	private void initializeQValues(State state, Playboard playboard) {
		for (int actionId : playboard.getPossibleActions()) {
			new QValue(state, new Action(actionId)).save();
		}
	}

	private boolean stateExists(State state) {
		// TODO implement stateExists
		return false;
	}

	private Action getRandomAction(Set<QValue> qValues) {
		List<QValue> qValueList = new ArrayList<>();
		qValueList.addAll(qValues);
		Collections.shuffle(qValueList);
		return qValueList.get(0).getAction();
	}

	private Action getNextAction(Set<QValue> qValues) {
		QValue nextQ = null;
		for (QValue qValue : qValues) {
			if (nextQ != null) {
				if (nextQ.getValue() < qValue.getValue()) {
					nextQ = qValue;
				}
			} else {
				nextQ = qValue;
			}
		}
		return nextQ.getAction();
	}

	private Set<QValue> getQValuesFromState(State state) {
		// TODO Auto-generated method stub
		return null;
	}

}
