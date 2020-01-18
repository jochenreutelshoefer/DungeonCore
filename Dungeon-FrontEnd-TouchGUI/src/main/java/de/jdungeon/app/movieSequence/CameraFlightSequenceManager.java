package de.jdungeon.app.movieSequence;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CameraFlightSequenceManager {

	private final List<CameraFlightSequence> sequences = new LinkedList<>();

	public void addSequence(CameraFlightSequence movie) {
		this.sequences.add(movie);
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
