package audio;

import java.util.List;

import de.jdungeon.game.Sound;

public interface AbstractAudioSet {

	void playRandomSound();

	List<Sound> getSounds();
}
