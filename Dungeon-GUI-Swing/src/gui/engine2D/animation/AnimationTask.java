/*
 * Created on 01.12.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui.engine2D.animation;

import figure.FigureInfo;

public class AnimationTask {
	
	int round;
	Animation ani;
	
	public AnimationTask( Animation ani, int round) {
	
		this.round = round;
		this.ani = ani;
	}

	/**
	 * @return Returns the round.
	 */
	public int getRound() {
		return round;
	}

	/**
	 * @param round The round to set.
	 */
	public void setRound(int round) {
		this.round = round;
	}
	
	public int getLength() {
		return ani.getLength();
	}

	/**
	 * @return Returns the ani.
	 */
	public Animation getAni() {
		return ani;
	}

	/**
	 * @return Returns the figure.
	 */
	public FigureInfo getFigure() {
		return ani.getObject();
	}

}
