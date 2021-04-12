package de.jdungeon.world;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.other.Fir;
import de.jdungeon.graphics.GraphicObject;
import de.jdungeon.graphics.JDGraphicObject;
import de.jdungeon.graphics.JDImageLocated;
import de.jdungeon.graphics.JDImageProxy;

import de.jdungeon.asset.Assets;
import de.jdungeon.util.Pair;

/**
 * This is a utility class containing render information (list of object render information).
 *
 *  Each pair in the prepared list holds 1) a GraphicObject holding information about the de.jdungeon.game object and view information like position and size
 *  and 2) a AtlasRegion obtain from the Asset management, providing the rendering engine the texture to be drawn.
 *
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.01.20.
 */
public class GraphicObjectRenderCollection {

	private boolean initialized = false;

	private final List<GraphicObject> graphicObjects = new CopyOnWriteArrayList<>();
	//private final Array<GraphicObject> graphicObjects = new Array<GraphicObject>();

	// use CopyOnWriteArrayList list to overcome concurrent modification problem efficiently (is fetched by render thread)
	//private final List<Pair<GraphicObject, TextureAtlas.AtlasRegion>> preparedOjects = new CopyOnWriteArrayList<>();
	private final Array<Pair<GraphicObject, TextureAtlas.AtlasRegion>> preparedOjects = new Array<>();

	public List<GraphicObject> getGraphicObjects() {
		return graphicObjects;
	}

	public void clear() {
		graphicObjects.clear();
		initialized = false;
	}

	public void addObject(GraphicObject object) {
		if(object != null) {
			graphicObjects.add(object);
		}
	}

	@Override
	public String toString() {
		return graphicObjects.toString();
	}

	/*
	 *	RENDER THREAD
	 */
	public Array<Pair<GraphicObject, TextureAtlas.AtlasRegion>> getRenderInformation() {
			if(! initialized) {
				// initialize textures lazy BY THE RENDERING THREAD because of OPEN GL Context issues
				for (GraphicObject object : graphicObjects) {
					preparedOjects.add(createAtlasRegionPair(object));
				}
				initialized = true;
			}
		return preparedOjects;
	}


	/*
	 *	RENDER THREAD
	 */
	private Pair<GraphicObject, TextureAtlas.AtlasRegion> createAtlasRegionPair(GraphicObject graphicObject) {
		if(graphicObject == null) return null;

		TextureAtlas.AtlasRegion atlasRegion = null;
		if (graphicObject instanceof JDGraphicObject) {
			JDGraphicObject object = ((JDGraphicObject) graphicObject);
			JDImageLocated locatedImage = object.getLocatedImage();
			if(locatedImage != null) {
				JDImageProxy<?> image = locatedImage.getImage();
				if(image != null) {
					atlasRegion = findAtlasRegion(image, graphicObject);
				}
			}
		}
		else {
			JDImageProxy<?> image = graphicObject.getImage();
			atlasRegion = findAtlasRegion(image, graphicObject);
		}

		return new Pair(graphicObject, atlasRegion);
	}

	/*
	 *	RENDER THREAD
	 */
	private TextureAtlas.AtlasRegion findAtlasRegion(JDImageProxy<?> image, GraphicObject graphicObject) {

		Object clickableObject = graphicObject.getClickableObject();
		Class<? extends Figure> figureClass = null;
		if (clickableObject instanceof FigureInfo) {
			if(((FigureInfo)clickableObject).getFigureClass().equals(Fir.class)) {
				// we need an exception for the Fir as it does not have animations
				return Assets.instance.getDungeonTexture(image);
			} else {
				return Assets.instance.getFigureTexture(((FigureInfo) clickableObject).getFigurePresentation(), image);

			}
		}


		// else look into default atlas
		return Assets.instance.getDungeonTexture(image);
	}

}
