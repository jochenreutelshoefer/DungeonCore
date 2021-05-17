package de.jdungeon.dungeon.builder.serialization;

import java.util.Objects;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;

import de.jdungeon.dungeon.builder.DoorMarker;

public class RoomDTO extends AbstractDTO {
	LocationDTO location;
	ChestDTO chest;
	ItemDTO item;

	public RoomDTO() {
	}

	public LocationDTO getLocation() {
		return location;
	}

	public ChestDTO getChest() {
		return chest;
	}

	public ItemDTO getItem() {
		return item;
	}

	private static final String LOCATION = "location";
	private static final String CHEST = "chest";
	private static final String ITEM = "item";

	/*
	For the serialization we do not need the collections locationsReachableWithoutKey and locationsNotReachableWithoutKey,
	as serialization happens after the dungeon generation process
	Therefore, we have custom json serialization hooks to simplify the serialization.
	 */
	@Override
	public void write(Json json) {
		json.writeValue(LOCATION, this.location);
		json.writeValue(CHEST, this.chest);
		json.writeValue(ITEM, this.item);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {



		// for some reason the deserializer always creates objects for nulls.
		// therefore we need to check for null here manually
		JsonValue jsonDataLoc = jsonData.get(LOCATION);
		if (!jsonDataLoc.isEmpty()) {
			this.location = json.readValue(LocationDTO.class, jsonDataLoc);
		}
		JsonValue jsonDataChest = jsonData.get(CHEST);
		if (!jsonDataChest.isEmpty()) {
			this.chest = json.readValue(ChestDTO.class, jsonDataChest);
		}

		JsonValue jsonDataItem = jsonData.get(ITEM);
		if (!jsonDataItem.isEmpty()) {
			this.item = json.readValue(ItemDTO.class, jsonDataItem);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RoomDTO roomDTO = (RoomDTO) o;
		return Objects.equals(location, roomDTO.location) && Objects.equals(chest, roomDTO.chest) && Objects
				.equals(item, roomDTO.item);
	}

	@Override
	public int hashCode() {
		return Objects.hash(location, chest, item);
	}
}
