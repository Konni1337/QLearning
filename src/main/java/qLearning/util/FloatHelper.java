package qLearning.util;

import java.util.Set;

public class FloatHelper {

	public static float getMax(Set<Float> numbers) {
		float maxFloat = Float.MIN_VALUE;
		for (Float number : numbers) {
			if (number > maxFloat) {
				maxFloat = number;
			}
		}
		return maxFloat;
	}

}
