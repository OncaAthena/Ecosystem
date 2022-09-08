package main;

import javax.swing.JFrame;

public class SimWindow {
	private JFrame jframe;
	
	public SimWindow(SimPanel simPanel) {
		jframe = new JFrame();
		jframe.setSize(400, 400);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(simPanel);
		jframe.setLocationRelativeTo(null);
		jframe.pack();
		jframe.setVisible(true);
		
	}

}
