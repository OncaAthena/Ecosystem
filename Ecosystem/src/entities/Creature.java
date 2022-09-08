package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;

public class Creature extends Entity {
	
	private float speed = 0.2f;
	private final double second = 1000000000;
	private double changeDirectionCountdown = 5.0 * second;
	private double changeDirectionTime = 5.0 * second;
	private float xSpeed, ySpeed;
	private Random random;
	
	private BufferedImage img;
	
	public Creature(float x, float y) {
		super(x, y);
		loadImg();
		random = new Random();
		changeDirection();
	}
	
	public void Update(double delta) {
		changeDirectionCountdown -= delta;
		if (changeDirectionCountdown <= 0){
			changeDirection();
//			System.out.println("Direction changed!" + delta);
			changeDirectionCountdown += changeDirectionTime;
		}
		if (x < 0) xSpeed = Math.abs(xSpeed);
		if (x > 720) xSpeed = -Math.abs(xSpeed);
		if (y < 0) ySpeed = Math.abs(ySpeed);
		if (y > 540) ySpeed = -Math.abs(ySpeed); /**/
		
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
	}
	
	
	public void Render(Graphics g) {
		g.drawImage(img, (int) x,(int) y, null);
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
