package areas;

import java.awt.Graphics;
import java.util.*;

import entities.Entity;
import main.Camera;

public class RTreeNode{
	
	//TODO Make this not ugly
	private float infinity = 1000000;
	public float leftBound = infinity;
	public float rightBound = -infinity;
	public float upBound = infinity;
	public float downBound = -infinity;
	
	protected RTree tree;
	
	protected int nodeCap;
	private int underLimBuffer = 2;
	protected RTreeNode parent;
	private List<RTreeNode> children = new ArrayList<>();
	
	public RTreeNode(int nodeCap, RTreeNode parent) {
		this.nodeCap = nodeCap;
		this.parent = parent;
	}
	
	// Variables for entityManagement
	
	public void Render(Graphics g, Camera c ) {
		for (int i = 0; i < children.size(); i++) {
			children.get(i).Render(g, c);
		}
	}
	
	public void Update(double delta) {
		for (int i = 0; i < children.size(); i++) {
			children.get(i).Update(delta);
		}
	}

	
	
	
	
	
	// Variables for dataStructure
	
	public void setAsRoot(RTree t) {
		tree = t;
		tree.setRoot(this);
	}
	
	public void addNode(RTreeNode node) {
		children.add(node);
	}
	public void removeNode(RTreeNode node) {
		children.remove(node);
	}
	
	public void insert (Entity e) {
		float x = e.getX(), y = e.getY();
		RTreeNode childToInsert = children.get(0);
		float areaIncrease = childToInsert.areaIncrease(x, x, y, y);
		for (int i = 1; i < children.size(); i++) {
			RTreeNode c = children.get(i);
			float nai = c.areaIncrease(x, x, y, y);
			if (nai < areaIncrease) {
				childToInsert = c;
				areaIncrease = nai;
			}
		}
		childToInsert.insert(e);
	}
	
	
	public void checkBounds(RTreeNode node) {
		boolean flag = false;
		if (node.leftBound < leftBound) {
			leftBound = node.leftBound;
			flag = true;
		}
		if (node.rightBound > rightBound) {
			rightBound = node.rightBound;
			flag = true;
		}
		if (node.upBound < upBound) {
			upBound = node.upBound;
			flag = true;
		}
		if (node.downBound > downBound) {
			downBound = node.downBound;
			flag = true;
		}
		if (flag && parent !=null) {
			parent.checkBounds(this);
		}
	}

	protected void split() {
		int[] ineffPair = mostInefficientPair();
		
		boolean isCurrentRoot = false;
		if (parent == null) {
			parent = new RTreeNode(nodeCap, null);
			isCurrentRoot = true;
		}
		
		RTreeNode split1 = new RTreeNode(nodeCap, parent);
		RTreeNode split2 = new RTreeNode(nodeCap, parent);
		parent.addNode(split1);
		parent.addNode(split2);
		split1.addNode(children.get(ineffPair[0]));
		split2.addNode(children.get(ineffPair[1]));
		
		if (ineffPair[1] > ineffPair[0]) {
			children.remove(ineffPair[1]);
			children.remove(ineffPair[0]);
		} else{
			children.remove(ineffPair[0]);
			children.remove(ineffPair[1]);
		}
		
		while (children.size()>1) {
			ineffPair = mostInefficientPair();
			float addedArea = 0.0f;
			RTreeNode ir1 = children.get(ineffPair[0]);
			RTreeNode ir2 = children.get(ineffPair[1]);
			addedArea += mbr2(split1, ir1);
			addedArea += mbr2(split2, ir2);
			addedArea -= mbr2(split1, ir2);
			addedArea -= mbr2(split2, ir1);
			
			if (addedArea < 0) {
				split1.addNode(ir1);
				split2.addNode(ir2);
			}else {
				split1.addNode(ir2);
				split2.addNode(ir1);
			}

			if (ineffPair[1] > ineffPair[0]) {
				children.remove(ineffPair[1]);
				children.remove(ineffPair[0]);
			} else{
				children.remove(ineffPair[0]);
				children.remove(ineffPair[1]);
			}
			
		}
		
		while (children.size()>0) {
			RTreeNode c = children.get(0);
			boolean is1smaller = mbr2(split1, c) < mbr2(split2, c);
			if (is1smaller) split1.addNode(c);
			else split2.addNode(c);
			children.remove(0);
		}
		
		if (parent != null) parent.removeNode(this);
		if (isCurrentRoot) parent.setAsRoot(tree);

	}
	
	protected int[] mostInefficientPair() {
		int mi1 = 0;
		int mi2 = 1;
		
		float worseArea = -1;
		
		for(int i = 0; i < children.size()-1; i++) {
			RTreeNode c1 = children.get(i);
			for (int j = i+1; j< children.size();j++) {
				RTreeNode c2 = children.get(j);
				
				float na = mbr2(c1, c2); 
				if (na > worseArea) {
					mi1 = i;
					mi2 = j;
					worseArea = na;
					
				}
			}
		}
		return new int [] {mi1, mi2};
	}
	
	private void die() {
		//TODO: Die!
	}
	
	public float mbr2(RTreeNode n1, RTreeNode n2) {
		return n1.areaIncrease(n2.leftBound, n2.rightBound, n2.upBound, n2.downBound);
	}
	
	public float areaIncrease(float l, float r, float u, float d) {
		if (leftBound<l) l = leftBound;
		if (rightBound > r) r = rightBound;
		if (upBound<u) u = upBound;
		if (downBound>d) d = downBound;
		float newArea = area(l, r, u, d);
		float oldArea = area();
		
		return (newArea - oldArea);
	}
	protected float areaIncrease(Entity e) {
		return areaIncrease(e.getX(), e.getX(), e.getY(), e.getY());
	}

	
	protected float area(float l, float r, float u, float d) {
		return (r-l)*(d-u);
	}
	public float area() {
		return area(leftBound, rightBound, upBound, downBound);
	}
	
	public boolean overlap(float l, float r, float u, float d) {
		boolean overlap = l < rightBound && r > leftBound
				&& u < downBound && d > upBound;
		return overlap;
	}
	

	public void verifyNode() {
		if (children.size() > nodeCap) split();
		if (children.size() < (nodeCap/2 - underLimBuffer)) die();
	}
	
	public List<RTreeNode> getChildren(){
		return children;
	}


}
