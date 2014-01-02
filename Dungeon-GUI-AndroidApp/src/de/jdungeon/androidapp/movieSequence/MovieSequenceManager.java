package de.jdungeon.androidapp.movieSequence;

import java.util.Stack;

public class MovieSequenceManager {

	private final Stack<MovieSequence> sequences = new Stack<MovieSequence>();

	public void addSequence(MovieSequence movie) {
		this.sequences.push(movie);
	}

	public MovieSequence getCurrentSequence(float time) {
		while (!sequences.isEmpty()) {
			MovieSequence top = sequences.peek();
			if (top.isFinished(time)) {
				// remove completed sequence
				sequences.pop();
			} else {
				return top;
			}

		}
		return null;
	}

}
