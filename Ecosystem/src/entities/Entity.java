package entities;

import java.awt.Graphics;

import main.Camera;

public abstract class Entity {
	
	protected float x, y;
	
	public Entity(float x, float  y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}
	
	public void render(Graphics g, Camera c ) {

	}
	public void update(double delta) {

	}


}
