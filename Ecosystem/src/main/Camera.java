package main;

public class Camera {
	
	private float x = 0, y = 0;
	private float xSpeed = 0.0f, ySpeed = 0.0f; 
	private float zoom = 1.0f;
	private float speed = 0.5f;
	private float zoomSpeed = 1.005f;
	private boolean left = false, right = false,
			up = false, down = false, 
			zoomIn = false, zoomOut = false;
	
	
	private Simulation sim;
	private int halfHeight;
	private int halfWidth;
	
	public Camera(Simulation sim) {
		this.sim = sim;

	}
	
	public void update() {
		move();
	}
	
	
	private void move() {
		xSpeed = 0;
		ySpeed = 0;
		
	
		if (left && !right) xSpeed = -speed /zoom;
		else if (right && !left) xSpeed = speed /zoom; 
		if (up && !down) ySpeed = -speed /zoom;
		else if (down && !up) ySpeed = speed /zoom;
		if (zoomIn && !zoomOut) zoom *= zoomSpeed; 
		else if (zoomOut && !zoomIn) zoom /= zoomSpeed;
		
		x += xSpeed;
		y += ySpeed;
		
	}

	public void updateWindowSize(SimPanel sp) {
		halfWidth = sp.getWidth()/2;
		halfHeight = sp.getHeight()/2;
	}
	
	public int xOffset(float xObj) {
		return (int)(zoom * (xObj - x) + halfWidth);
	}
	public int yOffset(float yObj) {
		return (int)(zoom*(yObj - y) + halfHeight);
	}


	public boolean isLeft() {
		return left;
	} public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isUp() {
		return up;
	} public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isDown() {
		return down;
	} public void setDown(boolean down) {
		this.down = down;
	}
	
	public boolean isZoomingIn() {
		return zoomIn;
	} public void setZoomIn(boolean zoomIn) {
		this.zoomIn = zoomIn;
	}
	
	public boolean isZoomingOut() {
		return zoomOut;
	} public void setZoomOut(boolean zoomOut) {
		this.zoomOut = zoomOut;
	}

	public float getZoom() {
		return zoom;
	}
	

}
