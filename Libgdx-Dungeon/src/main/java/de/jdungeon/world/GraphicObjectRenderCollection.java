package de.jdungeon.world;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import figure.Figure;
import figure.FigureInfo;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import graphics.GraphicObject;
import graphics.JDGraphicObject;
import graphics.JDImageProxy;

import de.jdungeon.asset.Assets;
import de.jdungeon.util.Pair;

/**
 * This is a utility class containing render information (list of object render information).
 *
 *  Each pair in the prepared list holds 1) a GraphicObject holding information about the game object and view information like position and size
 *  and 2) a AtlasRegion obtain from the Asset management, providing the rendering engine the texture to be drawn.
 *
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.01.20.
 */
public class GraphicObjectRenderCollection {

	private boolean initialized = false;

	private final List<GraphicObject> graphicObjects = new LinkedList<>();

	// use transport list to overcome concurrent modification problem efficiently (is fetched by other thread)
	private final List<Pair<GraphicObject, TextureAtlas.AtlasRegion>> preparedOjects = new LinkedList<>();
	private final List<Pair<GraphicObject, TextureAtlas.AtlasRegion>> preparedObjectsTransport = new LinkedList<>();

	public List<GraphicObject> getGraphicObjects() {
		return graphicObjects;
	}

	public void clear() {
		synchronized (graphicObjects) {
			graphicObjects.clear();
		}
		initialized = false;
	}

	public void addObject(GraphicObject object) {
		synchronized (graphicObjects) {
			graphicObjects.add(object);
		}
	}

	@Override
	public String toString() {
		return graphicObjects.toString();
	}

	public List<Pair<GraphicObject, TextureAtlas.AtlasRegion>> getRenderInformation() {
		synchronized (graphicObjects) {
			if(! initialized) {
				// initialize textures lazy BY THE RENDERING THREAD because of OPEN GL Context issues
				for (GraphicObject object : graphicObjects) {
					preparedOjects.add(createAtlasRegionPair(object));
				}
				initialized = true;
			}
		}
		preparedObjectsTransport.clear();
		preparedObjectsTransport.addAll(preparedOjects);
		return preparedObjectsTransport;
	}


	private Pair<GraphicObject, TextureAtlas.AtlasRegion> createAtlasRegionPair(GraphicObject graphicObject) {
		TextureAtlas.AtlasRegion atlasRegion = null;
		if (graphicObject instanceof JDGraphicObject) {
			JDGraphicObject object = ((JDGraphicObject) graphicObject);
			atlasRegion = findAtlasRegion(object.getLocatedImage().getImage(), graphicObject);

		}
		else {
			JDImageProxy<?> image = graphicObject.getImage();
			atlasRegion = findAtlasRegion(image, graphicObject);
		}

		return new Pair(graphicObject, atlasRegion);
	}

	private TextureAtlas.AtlasRegion findAtlasRegion(JDImageProxy<?> image, GraphicObject graphicObject) {

		Object clickableObject = graphicObject.getClickableObject();
		Class<? extends Figure> figureClass = null;
		if (clickableObject instanceof FigureInfo) {
			figureClass = ((FigureInfo) clickableObject).getFigureClass();
		}
		if (Hero.class.equals(figureClass)) {
			int heroCode = ((HeroInfo) clickableObject).getHeroCode();
			Hero.HeroCategory heroCategory = Hero.HeroCategory.fromValue(heroCode);
			if (heroCategory == Hero.HeroCategory.Warrior) {
				return Assets.instance.getWarriorTexture(image);
			}
		}
		if(figureClass != null) {
			return Assets.instance.getFigureTexture(figureClass, image);
		}

		// else look into default atlas
		return Assets.instance.getDungeonTexture(image);
	}

}
