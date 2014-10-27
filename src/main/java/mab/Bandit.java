package mab;

import mab.util.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static mab.util.Config.*;


/**
 * Created by Allquantor on 05.10.14.
 */

public class Bandit {

	Situation situation;

	List<BanditArm> arms = new ArrayList<>();
	int id;

	Bandit(int id, Situation s) {
		this.id = id;
		this.situation = s;
	}

	void addArm(BanditArm arm) {
		arms.add(arm);
	}

	void updateArm(Action a, Double reward) {
		BanditArm armToUpdate = arms.stream().filter(arm -> arm.action.equals(a)).findFirst().orElse(null);
		//imperative languages are so freaking ugly
		arms.remove(armToUpdate);
		armToUpdate.update(reward);
		arms.add(armToUpdate);
	}

	Action epsilonGreegyPull() {

		Random r = new Random();
		if (r.nextDouble() > EPSILON) {
			Collections.shuffle(arms);
			return arms.get(0).action;
		} else {
			return getMaxArm().action;
		}
	}

	Action softMaxPull() {
		Tuple[] probs = new Tuple[arms.size()];

		double z  = 0.0;
		for (int i = 0; i < arms.size(); i++) {
			z += Math.exp(arms.get(i).reward / TAU);
		}

		for(int j = 0; j < arms.size(); j ++) {
			Tuple t = new Tuple(arms.get(j).reward / z, arms.get(j));
			probs[j] = t;
		}

		Random r = new Random();
		double random = r.nextDouble();
		double cum_prob = 0.0;
		for (int q = 0; q < probs.length; q++) {
			cum_prob += (double) probs[q].getLeft();
			if(cum_prob > random){
				return ((BanditArm) probs[q].getRight()).action;
			}
		}
		return ((BanditArm) probs[probs.length - 1].getRight()).action;
	}


	BanditArm getMaxArm() {
		//sort
		arms.sort((a1, a2) -> Double.compare(a1.reward, a2.reward));
		// get max
		return arms.get(arms.size() - 1);
	}


	/**
	 * the equals is defined for check
	 * only the situation parameter
	 * because if check on redis happened
	 * the arm reward values does not play any role
	 *
	 * @param o
	 * @return
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Bandit)) return false;

		Bandit bandit = (Bandit) o;

		if (!situation.equals(bandit.situation)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return situation.hashCode();
	}
}
