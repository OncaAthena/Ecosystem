package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.Camera;
import main.Simulation;

public class KeyboardInputs implements KeyListener{
	
	Simulation sim;
	Camera cam;
	
	public KeyboardInputs(Simulation sim, Camera cam) {
		this.sim = sim;
		this.cam = cam;
		System.out.println("KeyboardInputs has been initialized!");
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("A key was pressed");
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			cam.setLeft(true);
			break;
		case KeyEvent.VK_D:
			cam.setRight(true);
			break;
		case KeyEvent.VK_W:
			cam.setUp(true);
			break;
		case KeyEvent.VK_S:
			cam.setDown(true);
			break;
		case KeyEvent.VK_OPEN_BRACKET:
			cam.setZoomIn(true);
			break;
		case KeyEvent.VK_CLOSE_BRACKET:
			cam.setZoomOut(true);
			break;
		
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			cam.setLeft(false);
			break;
		case KeyEvent.VK_D:
			cam.setRight(false);
			break;
		case KeyEvent.VK_W:
			cam.setUp(false);
			break;
		case KeyEvent.VK_S:
			cam.setDown(false);
			break;
		case KeyEvent.VK_OPEN_BRACKET:
			cam.setZoomIn(false);
			break;
		case KeyEvent.VK_CLOSE_BRACKET:
			cam.setZoomOut(false);
			break;
		}
	}

}
