package main;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class SimPanel extends JPanel{
		
	Simulation sim;
	public SimPanel(Simulation sim) {
		
		this.sim = sim;
		setPanelSize();
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		sim.render(g);
		
		g.drawImage(null, 0, 0, null);
		
		g.drawRect(100, 100, 100, 100);
		g.fillOval(300, 200, 10, 10);
		
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
