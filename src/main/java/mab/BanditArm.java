package mab;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Allquantor on 05.10.14.
 */
public class BanditArm {


	double reward = 10.0d;
	int pullCount = 0;

	final Action action;


	public BanditArm(Action a) {
		this.action = a;
	}



	void update(double currentReward) {
		double newCount = (double)(pullCount + 1);
		double newRewardValue = (pullCount / newCount) * currentReward + (1 / newCount) * reward;

		this.reward = newRewardValue;
		this.pullCount = (int) newCount;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BanditArm)) return false;

		BanditArm banditArm = (BanditArm) o;

		if (pullCount != banditArm.pullCount) return false;
		if (Double.compare(banditArm.reward, reward) != 0) return false;
		if (!action.equals(banditArm.action)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(reward);
		result = (int) (temp ^ (temp >>> 32));
		result = 31 * result + pullCount;
		result = 31 * result + action.hashCode();
		return result;
	}
}

class BanditArmSerializer implements JsonSerializer<Situation> {
	public JsonElement serialize(final Situation situation, final Type type, final JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		//todo update serializer if situation changes
		result.add("id", new JsonPrimitive(situation.id));
		//result.add("name", new JsonPrimitive(situation.getName()));

		return result;
	}
}


