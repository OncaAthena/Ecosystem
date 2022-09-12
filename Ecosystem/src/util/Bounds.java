package util;

public class Bounds {
	
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
	
	public float width() {
		return (right - left);
	}
	public float height() {
		return (down - up);
	}

	
	
}
