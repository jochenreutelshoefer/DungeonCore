/*
 * Created on 14.06.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.action;

import dungeon.PositionInRoomInfo;
import figure.FigureInfo;

public class ScoutAction extends Action{
	private final FigureInfo figure;
	private final int dir;
	private final PositionInRoomInfo position;

	public ScoutAction(FigureInfo figure, int dir) {
		super();
		position = figure.getPos();
		this.figure = figure;
		this.dir = dir;
	}
	
	public int getDirection() {
		return dir;
	}
}
