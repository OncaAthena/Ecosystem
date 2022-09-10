package areas;

import java.awt.Graphics;

import entities.Entity;
import main.Camera;

public class RTree {
	
	private int nodeCap;
	private RTreeNode root;
	
	public RTree(int nodeCap) {
		this.nodeCap = nodeCap;
		root = new RTreeLeafNode(nodeCap, null);
	}
	
	public void Render(Graphics g, Camera c ) {
		root.Render(g, c);
	}
	public void Update(double delta) {
		root.Update(delta);
	}

	
	
	public void setRoot(RTreeNode r) {
		root = r;
	}
	
	public void insert(Entity e) {
		root.insert(e);		
	}
	
}
