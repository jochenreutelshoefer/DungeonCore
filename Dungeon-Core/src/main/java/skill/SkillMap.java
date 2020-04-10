package skill;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.04.20.
 */
public class SkillMap {

	private final Map<Class<? extends Skill>, Skill> map = new HashMap<>();

	public <T extends Skill> void put(Class<T> clazz, T skill) {
		map.put(clazz, skill);
	}

	public <T extends Skill> T get(Class<T> clazz) {
		return (T)map.get(clazz);
	}
}
