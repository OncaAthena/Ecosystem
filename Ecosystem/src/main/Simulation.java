package main;

import java.awt.Graphics;

import entities.Creature;

public class Simulation implements Runnable {
	private SimWindow simWindow;
	private SimPanel simPanel;
	private Thread simThread;
	private final int FPS_SET = 60;
	private final int UPS_SET = 480;
	
	private Creature creature;
	
	
	public Simulation() {
		initClasses();

		simPanel = new SimPanel(this);
		simWindow = new SimWindow(simPanel);
		simPanel.requestFocus();

		startSimThread();
	}

	private void initClasses() {
		creature = new Creature(200, 200);
	}
	
	private void update(double delta) {
		creature.Update(delta);
	}
	
	void render(Graphics g) {
		creature.Render(g);
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
				lastCheck += second;
				
				updates = 0;
				frames = 0;
			}
		}
		
	}

}