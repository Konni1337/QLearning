package qLearning;

import Core.Playboard;

public interface Policy {

	public Action getAction(State state, Playboard playboard);

}
