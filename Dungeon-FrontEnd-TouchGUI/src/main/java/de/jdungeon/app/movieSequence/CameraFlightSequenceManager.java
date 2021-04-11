package de.jdungeon.app.movieSequence;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class CameraFlightSequenceManager {

	private final List<CameraFlightSequence> sequences = new Vector<>();



	public void addSequence(CameraFlightSequence movie) {
		boolean skip = false;
		if(!this.sequences.isEmpty()) {
			CameraFlightSequence lastInQueue = this.sequences.get(this.sequences.size() - 1);
			if(movie.getTargetPosition().equals(lastInQueue.getTargetPosition())) {
				// we skip this, not to scroll to the same position twice
				skip = true;
			}
		}

		if(!skip) {
			this.sequences.add(movie);
		}
	}

	public synchronized CameraFlightSequence getCurrentSequence(float time) {
		Iterator<CameraFlightSequence> iterator = sequences.iterator();
		while (iterator.hasNext()) {
			CameraFlightSequence head = iterator.next();
			if (head.isFinished(time)) {
				// remove completed sequence
				iterator.remove();
			}
			return head;

		}
		return null;
	}

	public boolean containsFlight(String cameraFlightTitle) {
		for (CameraFlightSequence sequence : sequences) {
			if(sequence.getTitle().equals(cameraFlightTitle)) {
				return true;
			}
		}
		return false;
	}
}
