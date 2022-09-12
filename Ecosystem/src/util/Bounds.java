package util;

import java.util.Random;

public class Bounds {
	
	private static Random random = new Random();
	
	public float left;
	public float right;
	public float up;
	public float down;
	
	public Bounds(float left, float right, float up, float down) {
		this.left = left;
		this.right = right;
		this.up = up;
		this.down = down;
	}
	
	public boolean inside(float x, float y) {
		boolean hIn = left<x && x<right; 
		boolean vIn = up<y && y<down;
		return hIn&&vIn;
	}
	public boolean overlap(Bounds other) {
		boolean hOl = left<other.right && other.left<right; 
		boolean vOl= up<other.down && other.up<down;
		return hOl&&vOl;
	}
	
	public float width() {
		return (right - left);
	}
	public float height() {
		return (down - up);
	}

	public float randX() {
		return random.nextFloat(left, right);
	}
	public float randY() {
		return random.nextFloat(up, down);
	}
	
}
