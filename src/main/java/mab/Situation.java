package mab;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Allquantor on 05.10.14.
 */
public class Situation {
	public final int id;
	public final Map<String, Object> stateObjectReflect;


	// other  bla bla
	Situation(int id, Map<String, Object> stateReflect) {
		this.id = id;
		this.stateObjectReflect = stateReflect;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Situation)) return false;

		Situation situation = (Situation) o;

		for(String k : stateObjectReflect.keySet()){
			if(!(this.stateObjectReflect.get(k).equals(situation.stateObjectReflect.get(k)))){
				return false;
			}

		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + stateObjectReflect.hashCode();
		return result;
	}

}
