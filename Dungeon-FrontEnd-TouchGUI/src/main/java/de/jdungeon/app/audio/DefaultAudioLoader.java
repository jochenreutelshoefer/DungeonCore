package de.jdungeon.app.audio;

import java.io.IOException;
import java.io.InputStream;

import de.jdungeon.game.*;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class DefaultAudioLoader implements AudioLoader {

    private final Audio audio;
    private final Game game;

    public DefaultAudioLoader(Audio audio, Game game) {
        this.audio = audio;
        this.game = game;
    }

    @Override
    public AbstractAudioSet createAudioSet(String[] files) {
        Logger logger = GameEnv.getInstance().getGame().getLogger();
        if (files.length == 0) {
            logger.warning(this.getClass().getSimpleName(), "Empty list of sound files on de.jdungeon.audio set initialization!");
        }
        AudioSet set = new AudioSet();
        for (String file : files) {
            String fullFilename = "sounds/" + file;
            if (fileExists(fullFilename)) {
                Sound sound = audio.createSound(fullFilename);
                if (sound != null) {
                    set.addSound(sound);
                } else {
                    logger.warning(this.getClass().getSimpleName(), "Sound could not be created: " + file);
                }

            } else {
                logger.warning(this.getClass().getSimpleName(), "Sound file not found: " + fullFilename);
            }
        }
        return set;
    }


    private boolean fileExists(String file) {
        FileIO fileIO = game.getFileIO();
        try {
            InputStream inputStream = fileIO.readFile(file);
            inputStream.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}

