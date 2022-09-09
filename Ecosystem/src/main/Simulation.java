package main;

import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.*;
import java.util.Random;

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
		
	
	
	private int initialNumCreatures = 400;
	private int spawnPerUpdate = 10;
	private List<Creature> creatures = new ArrayList<>();
	
	
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
		for (int i = 0; i < spawnPerUpdate; i++) {
			int x = random.nextInt(leftBound, rightBound);
			int y = random.nextInt(upBound, downBound);
			Creature c = new Creature(x, y, this);
			creatures.add(c);
		}
			
	}
	
	private void update(double delta) {
		
		for (int i = 0; i < creatures.size(); i++) {
			creatures.get(i).Update(delta);
		}
		camera.update();
		if (creatures.size()<initialNumCreatures) {
			spawnCreatures();
		}
		
	}
	
	void render(Graphics g) {
		for (int i = 0; i < creatures.size(); i++) {
			creatures.get(i).Render(g, camera);
		}
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