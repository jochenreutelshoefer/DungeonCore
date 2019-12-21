package de.jdungeon.app.movieSequence;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MovieSequenceManager {

	private final List<MovieSequence> sequences = new LinkedList<>();

	public void addSequence(MovieSequence movie) {
		this.sequences.add(movie);
	}

	public synchronized MovieSequence getCurrentSequence(float time) {
		Iterator<MovieSequence> iterator = sequences.iterator();
		while (iterator.hasNext()) {
			MovieSequence head = iterator.next();
			if (head.isFinished(time)) {
				// remove completed sequence
				iterator.remove();
			}
			return head;

		}
		return null;
	}

}
