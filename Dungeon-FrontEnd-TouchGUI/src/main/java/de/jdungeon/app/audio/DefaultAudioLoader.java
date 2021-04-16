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
    private FileIO fileIO;

    public DefaultAudioLoader(Audio audio, FileIO fileIO) {
        this.audio = audio;
        this.fileIO = fileIO;
    }

    @Override
    public AbstractAudioSet createAudioSet(String[] files) {
        Logger logger = GameEnv.getInstance().getGame().getLogger();
        if (files.length == 0) {
            logger.warning(this.getClass().getSimpleName(), "Empty list of sound files on audio set initialization!");
        }
        AudioSet set = new AudioSet();
        for (String file : files) {
            //String fullFilename = "assets/sounds/" + file;
            //if (fileExists(fullFilename)) {
                Sound sound = audio.createSound(file);
                if (sound != null) {
                    set.addSound(sound);
                } else {
                    logger.warning(this.getClass().getSimpleName(), "Sound could not be created: " + file);
                }

           // } else {
           //     logger.warning(this.getClass().getSimpleName(), "Sound file not found: " + fullFilename);
           // }
        }
        return set;
    }


    private boolean fileExists(String file) {
        try {
            InputStream inputStream = fileIO.readFile(file);
            inputStream.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}

