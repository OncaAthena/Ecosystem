package main;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import inputs.KeyboardInputs;

public class SimPanel extends JPanel{
		
	Simulation sim;
	public SimPanel(Simulation sim, Camera cam) {
		
		this.sim = sim;
		setPanelSize();
		addKeyListener(new KeyboardInputs(sim, cam));
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		sim.render(g);
	}
	
	private void setPanelSize() {
		Dimension minSize = new Dimension(720, 540);
		Dimension maxSize = new Dimension(3840,2160);
		Dimension prefSize = new Dimension(720,540);/*(1280, 800);*/
		setMinimumSize(minSize);
		setMaximumSize(maxSize);
		setPreferredSize(prefSize);
		

		
	}

}
