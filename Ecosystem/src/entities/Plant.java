package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Camera;

public class Plant extends Entity {

	private BufferedImage foodImg;

	
	public Plant(float x, float y) {
		super(x, y);
		foodImg = loadImg("food.png");
	}
	
	public void render(Graphics g, Camera c) {
	float z = c.getZoom();
	g.drawImage(foodImg, c.xOffset(getX()),c.yOffset(getY()), 
				(int) (foodImg.getWidth() * z), (int)(foodImg.getHeight() * z) , null);
	
	}

}
