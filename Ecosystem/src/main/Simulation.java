package main;

import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.*;
import java.util.Random;

import areas.RTree;
import entities.Creature;

public class Simulation implements Runnable {
	private SimWindow simWindow;
	private SimPanel simPanel;
	private Thread simThread;
	private Camera camera;
	private final int FPS_SET = 60;
	private final int UPS_SET = 480;
	private Random random;
	
	public int leftBound = 0;
	public int rightBound = 1720;
	public int upBound = 0;
	public int downBound = 1540;
		
	
	
	private int initialNumCreatures = 100;
	private int spawnPerUpdate = 10;
	private int rtNodeCap = 10;
	private RTree creatures = new RTree(rtNodeCap);
	
	
	public Simulation() {
		
		initClasses();
		

		simPanel = new SimPanel(this, camera);
		simWindow = new SimWindow(simPanel);
		simPanel.addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent componentEvent) {
		        updateWindowSize();
		    }
		});
		simPanel.requestFocus();

		initCamera();

		simPanel.requestFocus();

		startSimThread();
	}

	private void initCamera() {
		updateWindowSize();
	}
	public void updateWindowSize() {
		camera.updateWindowSize(simPanel);
	}

	private void initClasses() {
		random = new Random();
		camera = new Camera(this);
		spawnCreatures();
	}
	
	private void spawnCreatures() {
		for (int i = 0; i < initialNumCreatures; i++) {
			int x = random.nextInt(leftBound, rightBound);
			int y = random.nextInt(upBound, downBound);
			Creature c = new Creature(x, y, this);
			creatures.insert(c);
		}
			
	}
	
	private void update(double delta) {
		
		creatures.Update(delta);
		camera.update();
	}
	
	void render(Graphics g) {
		creatures.Render(g, camera);
	}

	private void startSimThread() {
		simThread = new Thread(this);
		simThread.start();
	}

	@Override
	public void run() {
		long currentTime = System.nanoTime();
		long lastFrame = currentTime;
		long lastUpdate = currentTime;
		long lastCheck = currentTime;

		double second = 1000000000.0;
		double timePerFrame = second/FPS_SET;
		double timePerUpdate = second/UPS_SET;
		
		int frames = 0;
		int updates = 0;
		int time = 0;
		
		while(true) {
			currentTime = System.nanoTime();
			
			if (currentTime-lastUpdate >= timePerUpdate) {
				update(timePerUpdate);
				lastUpdate += timePerUpdate;
				updates ++;
			}

			
			if (currentTime-lastFrame >= timePerFrame) {
				simPanel.repaint();
				lastFrame += timePerFrame;
				frames ++;
			}
			
			if (currentTime-lastCheck >= second) {
				time++;
				System.out.println("FPS: " + frames +" | UPS: " 
						+ updates + " | Time elapsed: " + time+"s");
				lastCheck = currentTime;
				
				updates = 0;
				frames = 0;
			}
		}
		
	}

}