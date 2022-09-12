package abandoned;

import java.awt.Graphics;

import entities.Entity;
import main.Camera;

@Deprecated
public class RTree {
	
	private int nodeCap;
	private RTreeNode root;
	
	public RTree(int nodeCap) {
		this.nodeCap = nodeCap;
		root = new RTreeLeafNode(nodeCap, null);
		root.setAsRoot(this);
	}
	
	public void Render(Graphics g, Camera c ) {
		root.Render(g, c);
		System.out.println("Nodes in depth 1: "+ root.count());
		System.out.println("Reinserts to depth 1: "+ root.reinsertCount());
//		System.out.println("Tree depth: " + root.treeDepth());
//		System.out.println("Root class: " + root.getClass().getTypeName());
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
