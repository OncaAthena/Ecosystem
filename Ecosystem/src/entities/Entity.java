package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

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

	protected BufferedImage loadImg(String path) {
		BufferedImage img = null;
		InputStream is = getClass().getResourceAsStream("/" + path);
		try {
			img = ImageIO.read(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return img;
		
	}


}
