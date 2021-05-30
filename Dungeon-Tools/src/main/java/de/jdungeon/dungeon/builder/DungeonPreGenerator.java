package de.jdungeon.dungeon.builder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import org.apache.commons.io.IOUtils;

import de.jdungeon.asset.Assets;
import de.jdungeon.dungeon.LevelSetDTOReadDungeonManager;
import de.jdungeon.dungeon.builder.serialization.LevelDTO;
import de.jdungeon.dungeon.level.DTOLevel;

public class DungeonPreGenerator {

	public static void main(String[] args) throws DungeonGenerationException, IOException {
		LevelSetDTOReadDungeonManager manager = new LevelSetDTOReadDungeonManager();

		for (int i = 0; i < manager.getNumberOfStages(); i++) {
			List<DungeonFactory> dungeonFactories = manager.getDungeonOptions(i);
			for (DungeonFactory dungeonFactory : dungeonFactories) {
				Json json = prepareJson();
				dungeonFactory.create();
				if (dungeonFactory instanceof DTOLevel) {
					LevelDTO levelDTO = ((DTOLevel) dungeonFactory).getDTO();
					String text = json.prettyPrint(levelDTO);
					File levelFolder = Assets.getLevelFolderAbsolute(dungeonFactory.getClass().getSimpleName());
					levelFolder.mkdirs();
					File targetFile = new File(levelFolder, dungeonFactory.getName() + levelDTO.hashCode() + ".json");
					FileWriter outputWriter = new FileWriter(targetFile);
					IOUtils.write(text, outputWriter);
					outputWriter.flush();
					outputWriter.close();
				}
			}
		}
	}

	private static Json prepareJson() {
		Json json = new Json();
		json.setTypeName("exoticTypeName");
		json.setUsePrototypes(false);
		json.setIgnoreUnknownFields(true);
		json.setOutputType(JsonWriter.OutputType.json);
		return json;
	}
}
