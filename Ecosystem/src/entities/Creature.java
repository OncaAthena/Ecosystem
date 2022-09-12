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
	
	// Positional and dynamics variable declarations
	private float speed = 0.2f;
	private final double second = 1000000000;
	private double changeDirectionCountdown = 5.0 * second;
	private double changeDirectionTime = 5.0 * second;
	private float xSpeed, ySpeed;
	private float angle = 0;
	public static final float Size = 32;

	// Reference variable declarations
	private BufferedImage img;
	private Random random;
	// TODO: Remove the reference to the simulation
	private Simulation sim;
	
	/**
	 * Constructor
	 * 
	 * @author Onca
	 * @param x: initial x position
	 * @param y: initial y position
	 * @param sim: reference to simulation
	 */
	public Creature(float x, float y, Simulation sim) {
		super(x, y);
		this.sim = sim;
		img = loadImg("creature.png");
		random = new Random();
		changeDirection();
		
		changeDirectionTime = random.nextDouble(2.0, 8.0)*second;
	}

	/***************************************************************************
	 * Section: Behavior functions
	 * 
	 **************************************************************************/
	public void move() {
		x = getX() + xSpeed;
		y = getY() + ySpeed;
	}
	
	public void changeDirection() {
		double angle = (random.nextFloat() * 2 * Math.PI);
		xSpeed = (float) (Math.cos(angle) * speed);
		ySpeed = (float) (Math.sin(angle) * speed);
		this.angle = (float)angle;
	}
    
	/***************************************************************************
	 * Section: System functions
	 * 
	 **************************************************************************/
	
	/**
	 * Logic frame that updates position of element and adjust accordingly
	 * 
	 * @author Onca
	 * @param delta: update duration in nanoseconds.
	 */
	@Override
	public void update(double delta) {
		changeDirectionCountdown -= delta;
		if (changeDirectionCountdown <= 0){
			changeDirection();
//			System.out.println("Direction changed!" + delta);
			changeDirectionCountdown += changeDirectionTime;
		}
		if (getX() < sim.leftBound) {
			xSpeed = Math.abs(xSpeed);
			angle = (float) (Math.PI - angle);
		}
		if (getX() > sim.rightBound) {
			xSpeed = -Math.abs(xSpeed);
			angle = (float) (Math.PI - angle);
		}
		if (getY() < sim.upBound) {
			ySpeed = Math.abs(ySpeed);
			angle *= -1;

		}
		if (getY() > sim.downBound) {
			ySpeed = -Math.abs(ySpeed); /**/
			angle *= -1;
		}
		
		move();
	}

	/**
	 * @author Onca
	 * @param delta: update duration in nanoseconds.
	 * 
	 * Graphic frame that renders element to the screen.
	 */	
	@Override
	public void render(Graphics g, Camera c) {
		if (c.cannotRenderCreature()) return;
		
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
		
		g.drawRect(c.xOffset(x), c.yOffset(y), (int)(z*Size), (int)(z*Size));
		
		g.drawImage(rotImg, c.xOffset(getX()),c.yOffset(getY()), 
				(int) (rotImg.getWidth() * z), (int)(rotImg.getHeight() * z) , null);
		
		c.creatureRendered();
	}
	

}
