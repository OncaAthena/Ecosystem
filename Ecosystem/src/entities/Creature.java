package entities;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;

import main.Camera;
import main.Simulation;

public class Creature extends Entity {
	
	private float speed = 0.2f;
	private final double second = 1000000000;
	private double changeDirectionCountdown = 5.0 * second;
	private double changeDirectionTime = 5.0 * second;
	private float xSpeed, ySpeed;
	private Random random;
	private float angle = 0;
	
	private Simulation sim;
	private BufferedImage img;
	
	public Creature(float x, float y, Simulation sim) {
		super(x, y);
		this.sim = sim;
		loadImg();
		random = new Random();
		changeDirection();
		
		changeDirectionTime = random.nextDouble(2.0, 8.0)*second;
	}
	
	public void Update(double delta) {
		changeDirectionCountdown -= delta;
		if (changeDirectionCountdown <= 0){
			changeDirection();
//			System.out.println("Direction changed!" + delta);
			changeDirectionCountdown += changeDirectionTime;
		}
		if (x < sim.leftBound) {
			xSpeed = Math.abs(xSpeed);
			angle = (float) (Math.PI - angle);
		}
		if (x > sim.rightBound) {
			xSpeed = -Math.abs(xSpeed);
			angle = (float) (Math.PI - angle);
		}
		if (y < sim.upBound) {
			ySpeed = Math.abs(ySpeed);
			angle *= -1;

		}
		if (y > sim.downBound) {
			ySpeed = -Math.abs(ySpeed); /**/
			angle *= -1;
		}
		
		move();
	}
	
	public void move() {
		x += xSpeed;
		y += ySpeed;
	}
	
	public void changeDirection() {
		double angle = (random.nextFloat() * 2 * Math.PI);
		xSpeed = (float) (Math.cos(angle) * speed);
		ySpeed = (float) (Math.sin(angle) * speed);
		this.angle = (float)angle;
	}
	
	
	public void Render(Graphics g, Camera c) {
		
		// Code that will rotate the creature's image
		final double sin = Math.abs(Math.sin(angle));
		final double cos = Math.abs(Math.cos(angle));
		final int w = (int) Math.floor(img.getWidth() * cos + img.getHeight() * sin);
		final int h = (int) Math.floor(img.getHeight() * cos + img.getWidth() * sin);
		final BufferedImage rotatedImage = new BufferedImage(w, h, img.getType());
		final AffineTransform at = new AffineTransform();
		at.translate(w / 2, h / 2);
		at.rotate(angle,0, 0);
		at.translate(-img.getWidth() / 2, -img.getHeight() / 2);
		final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage rotImg = rotateOp.filter(img,rotatedImage);
		
		float z = c.getZoom();
		
		
		g.drawImage(rotImg, c.xOffset(x),c.yOffset(y), 
				(int) (rotImg.getWidth() * z), (int)(rotImg.getHeight() * z) , null);
	}
	
	private void loadImg() {
		InputStream is = getClass().getResourceAsStream("/creature.png");
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
		
	}

}
