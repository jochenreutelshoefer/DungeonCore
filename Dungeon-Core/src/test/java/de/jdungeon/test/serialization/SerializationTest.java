package de.jdungeon.test.serialization;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import junit.framework.TestCase;
import org.junit.Test;

import de.jdungeon.dungeon.Chest;
import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.Lock;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.item.Key;

public class SerializationTest extends TestCase {

	@Test
	public void testKeySerialization() {
		Key object1 = new Key("foo");
		Json json = prepareJson();
		String text = json.prettyPrint(object1);
		Key object2 = json.fromJson(Key.class, text);

		assertEquals(object1, object2);

	}

	@Test
	public void testLockSerialization() {
		Chest chest = new Chest();
		Lock<Chest> object1 = new Lock<>(new Key("foo"), chest);
		Json json = prepareJson();
		String text = json.prettyPrint(object1);
		Lock object2 = json.fromJson(Lock.class, text);

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
