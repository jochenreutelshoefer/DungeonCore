package item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 04.01.21.
 */
public class Items {

	/**
	 * Samples a subset of size 'n' of the given Item collection 'pool'.
	 *
	 * @param pool collection of overall items to be sampled of
	 * @param n sample size
	 * @return a size n random subset of pool items
	 */
	public static List<Item> selectRandomN(List<Item> pool, int n) {
		if(pool.size() < n) {
			throw new IllegalArgumentException("Sample size must not be larger that overall element pool: "+ n +" out of pool: "+pool);
		}
		Set<Integer> selectedIndices = new HashSet<>();
		while(selectedIndices.size() < n) {
			int newIndex = (int) (Math.random() * pool.size());
			selectedIndices.add(newIndex);
		}
		List<Item> result = new ArrayList<>();
		for (Integer selectedIndex : selectedIndices) {
			result.add(pool.get(selectedIndex));
		}
		return result;
	}

}
