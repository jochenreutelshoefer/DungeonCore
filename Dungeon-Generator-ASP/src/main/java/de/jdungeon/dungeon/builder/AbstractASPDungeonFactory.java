package de.jdungeon.dungeon.builder;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.builder.serialization.LevelDTO;
import de.jdungeon.game.JDEnv;
import de.jdungeon.level.DungeonFactory;
import de.jdungeon.log.Log;

public abstract class AbstractASPDungeonFactory implements DungeonFactory {

	public enum Mode {
		Generate,
		Read;
	}


	protected DungeonResult dungeonBuild;
	Mode mode = Mode.Generate;

	public AbstractASPDungeonFactory(Mode mode) {
		this.mode = mode;
	}

	public AbstractASPDungeonFactory() {
	}

	@Override
	public LevelDTO getDTO() {
		if(this.dungeonBuild == null) {
			throw new IllegalStateException("May not be called before 'create()' method!");
		}
		return this.dungeonBuild.getDungeonDTO();
	}

	@Override
	public void create() throws DungeonGenerationException {
		if(this.mode == Mode.Generate) {
			this.doGenerate();
		} else if(this.mode == Mode.Read) {
			String levelFolder = JDEnv.getLevelFolderRelative(this.getClass().getSimpleName());
			if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
				levelFolder = "assets/" + levelFolder;
			}
			String fileListFileName = "filelist.txt";
			String filenamefilePath = levelFolder + "/" + fileListFileName;
			FileHandle fileHandleFolder = Gdx.files.getFileHandle(filenamefilePath, Files.FileType.Internal);
			if (!fileHandleFolder.exists()) {
				Log.severe("File list for asset subfolder not found: " + levelFolder + " (" + filenamefilePath + ")");
				return;
			}
			String content = fileHandleFolder.readString();
			String[] fileNames = content.split("\n");

			//FileHandle fileHandle = Gdx.files.getFileHandle(levelFolder + fileListFileName, Files.FileType.Internal);
			//String fileList = fileHandle.readString();
			//String[] fileNames = fileList.split(System.getProperty("line.separator"));
			int randomIndex = (int) (Math.random() * fileNames.length);
			String levelToLoad = levelFolder + "/" + fileNames[randomIndex];
			FileHandle fileHandleLevelToLoad = Gdx.files.getFileHandle(levelToLoad, Files.FileType.Internal);
			String levelJson = fileHandleLevelToLoad.readString();
			Json json = prepareJson();
			LevelDTO level = json.fromJson(LevelDTO.class, levelJson);
			this.dungeonBuild = new DTODungeonResult(level, "foobar");
		}
	}

	private Json prepareJson() {
		Json json = new Json();
		json.setTypeName("exoticTypeName");
		json.setUsePrototypes(false);
		json.setIgnoreUnknownFields(true);
		json.setOutputType(JsonWriter.OutputType.json);
		return json;
	}

	@Override
	public JDPoint getHeroEntryPoint() {
		return dungeonBuild.getDungeonDTO().getStartPosition();
	}

	protected abstract void doGenerate() throws DungeonGenerationException;

	@Override
	public Dungeon getDungeon() {
		return dungeonBuild.getDungeon();
	}

}
