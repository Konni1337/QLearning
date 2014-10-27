package mab;

/**
 * Created by Allquantor on 06.10.14.
 */

public class Action {
	public int actionID;

	public Action(int actionID) {
		this.actionID = actionID;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Action)) return false;
		Action action = (Action) o;

		if (actionID != action.actionID) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return actionID;
	}
}
