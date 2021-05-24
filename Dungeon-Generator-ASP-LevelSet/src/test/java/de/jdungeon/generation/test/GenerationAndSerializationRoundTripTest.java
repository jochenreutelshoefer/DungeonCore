package de.jdungeon.generation.test;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import org.junit.jupiter.api.Test;

import de.jdungeon.dungeon.builder.AbstractASPDungeonFactory;
import de.jdungeon.dungeon.builder.DungeonGenerationException;
import de.jdungeon.dungeon.builder.serialization.LevelDTO;
import de.jdungeon.dungeon.level.LevelSome;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenerationAndSerializationRoundTripTest {

	@Test
	public void testRoundTrip() throws DungeonGenerationException {

		LevelSome level = new LevelSome(AbstractASPDungeonFactory.Mode.Generate);
		level.create();
		LevelDTO object1 = level.getDTO();

		Json json = prepareJson();
		String text = json.prettyPrint(object1);
		LevelDTO object2 = json.fromJson(LevelDTO.class, text);

		assertEquals(object1, object2);
	}

	private Json prepareJson() {
		Json json = new Json();
		json.setTypeName("exoticTypeName");
		json.setUsePrototypes(false);
		json.setIgnoreUnknownFields(true);
		json.setOutputType(JsonWriter.OutputType.json);
		return json;
	}
}
