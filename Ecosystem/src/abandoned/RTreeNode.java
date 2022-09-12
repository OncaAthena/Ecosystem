package abandoned;

import java.awt.Graphics;
import java.util.*;

import entities.Entity;
import main.Camera;
import util.Bounds;

@Deprecated
public class RTreeNode{
	
	private boolean debuggingDeath = false;
	
	//TODO Make this not ugly
	private float infinity = 1000000;
	public float leftBound = infinity;
	public float rightBound = -infinity;
	public float upBound = infinity;
	public float downBound = -infinity;
	
	
	public boolean dying = false; 
	
	protected RTree tree;
	
	protected int nodeCap;
	protected int underLimBuffer = 2;
	protected RTreeNode parent;
	private List<RTreeNode> children = new ArrayList<>();
	private List<RTreeNode> nodesToReinsert = new ArrayList<>();
	protected List<Entity> elementsToReinsert= new ArrayList<>();
	
	public RTreeNode(int nodeCap, RTreeNode parent) {
		this.nodeCap = nodeCap;
		this.parent = parent;
	}
	
	// Variables for entityManagement
	
	
	// TODO: Render only if in screen
	public void Render(Graphics g, Camera c ) {
		RenderBounds(g, c);
		for (int i = 0; i < children.size(); i++) {
			RTreeNode child = children.get(i);
			if (child.overlap(c.getBounds())){
				child.Render(g, c);
			}
		}
		for (int i = 0; i < nodesToReinsert.size(); i++) {
			RTreeNode child = nodesToReinsert.get(i);
			if (child.overlap(c.getBounds())){
				child.Render(g, c);
			}
		}
		for (int i = 0; i < elementsToReinsert.size(); i++) {
			Entity child = elementsToReinsert.get(i);
			child.render(g, c);
			
		}
	}
	
	public void Update(double delta) {
		for (int i = 0; i < children.size(); i++) {
			RTreeNode c = children.get(i); 
			c.Update(delta);
			
			if (c.count() < (nodeCap/2 - underLimBuffer)) {
				kill(c);
			}
		}
		
		if (dying){
			die();
		}
		
	}

	
	
	protected  void RenderBounds(Graphics g, Camera c ) {
		float z = c.getZoom();
		g.drawRect(c.xOffset(leftBound), c.yOffset(upBound), (int)(z*(rightBound-leftBound)+32), (int)(z*(downBound-upBound)+32));
	}
	
	
	// Variables for dataStructure
	
	public void setAsRoot(RTree t) {
		tree = t;
		tree.setRoot(this);
	}
	
	public void addNode(RTreeNode node) {
		children.add(node);
		//verifyNode();
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
			if (!c.dying) {
				float nai = c.areaIncrease(x, x, y, y);
				if (nai < areaIncrease) {
					childToInsert = c;
					areaIncrease = nai;
				}
			}
		}
		childToInsert.insert(e);
	}
	
	public void updateElement(Entity e) {
		if (hasChildren()) {
			if (inside(e)) {
				insert(e);
			}else {
				if (isRoot()) {
					insert(e);
				}else {
					// TODO : Implementar buffer	
					parent.updateElement(e);
				}
			}
		}else if (isRoot()) {
			System.out.println("Raiz não tem filho!");
			children.add(new RTreeNode(nodeCap, this));
			insert(e);
		}else {
			// TODO : Implementar buffer	
			parent.updateElement(e);
		}
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
		if (flag && !isRoot()) {
			parent.checkBounds(this);
		}
	}

	protected void split() {}
	protected void splitN() {
		
		boolean isCurrentRoot = isRoot();
		if (isCurrentRoot) {
			parent = new RTreeNode(nodeCap, null);
		}
		
		int[] ineffPair = mostInefficientPair();
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
		
		if (isCurrentRoot) parent.setAsRoot(tree);
		else parent.removeNode(this);
		
		

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
	
	protected void kill(RTreeNode c) {
		c.dying = true;
		while (c.hasChildren()) {
			RTreeNode node = c.getChildren().get(0);
			nodesToReinsert.add(node);
			c.getChildren().remove(0);
		}
		while (c.hasElements()) {
			Entity elem= c.getElements().get(0);
			elementsToReinsert.add(elem);
			c.getElements().remove(0);
		}
		reinsertAll();
	}
	
	private void reinsertAll() {
/*		while (nodesToReinsert.size() > 0) {
			RTreeNode n = nodesToReinsert.get(0);
			insertNode(n);
			nodesToReinsert.remove(0);
		} /**/
		while (elementsToReinsert.size() > 0) {
			Entity n = elementsToReinsert.get(0);
			insert(n);
			elementsToReinsert.remove(0);
		}
	}
	
	private void insertNode(RTreeNode node){
			RTreeNode childToInsert = children.get(0);
			float areaIncrease = childToInsert.areaIncrease
					(node.leftBound, node.rightBound, node.upBound, node.downBound);
			for (int i = 1; i < children.size(); i++) {
				RTreeNode nc = children.get(i);
				if (!nc.dying) {				
					float nai = nc.areaIncrease
							(node.leftBound, node.rightBound, node.upBound, node.downBound);
					if (nai < areaIncrease) {
						childToInsert = nc;
						areaIncrease = nai;
					}
				}
			}
			
			childToInsert.addNode(node);

		}/**/
	
	
/*	protected void kill(RTreeLeafNode c) {
		List<Entity> toReinsert = c.getElements();
		for (int i = 0;i < toReinsert.size(); i++) {
			Entity e = toReinsert.get(i);
			insert(e);
		}
		
		c.dying = true;
		
	}/**/
	
	protected void die() {
		//TODO: Die!
		boolean canDie = true;
		//TODO: Die if I have to!!!!!
		
		if (hasChildren()) {
			if (debuggingDeath) System.out.println("Died with children!");
			canDie = false;
		}
		if (hasElements()) {
			if (debuggingDeath) System.out.println("Died with Elements! " + getElements().size());
			canDie = false;
		}
		if (elementsToReinsert.size()>0) {
			if (debuggingDeath) System.out.println("Died without reinserting elements!");
			canDie = false;
		}
		if (nodesToReinsert.size()>0) {
			if (debuggingDeath) System.out.println("Died without reinserting nodes!");
			canDie = false;
		}
		
		dying = canDie;

		if (canDie) parent.removeNode(this);
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
	
	public boolean overlap(Bounds b) {
		return overlap(b.left, b.right, b.up, b.down);
	}
	
	public boolean overlap(float l, float r, float u, float d) {
		boolean overlap = l < rightBound && r > leftBound
				&& u < downBound && d > upBound;
		return overlap;
	}
	public boolean inside(float x, float y) {
		boolean vInside = leftBound < x && x < rightBound;
		boolean hInside = upBound < y && y < downBound;
		return vInside && hInside;
	}
	public boolean inside (Entity e) {
		return inside(e.getX(), e.getY());
	}
	

	public void verifyNode() {
		if (count() > nodeCap) split();
	}
	
	public List<RTreeNode> getChildren(){
		return children;
	}
	
	public boolean isRoot() {
		return (parent == null);
	}
	
	public int count() {
		return children.size();
	}
	public int reinsertCount() {
		return nodesToReinsert.size();
	}
	
	protected void resetBounds() {
		leftBound = infinity;
		rightBound = -infinity;
		upBound = infinity;
		downBound = -infinity;

	}
	
	public boolean hasChildren() {
		return children.size() > 0;
	}
	public boolean hasElements() {
		return getElements().size() > 0;
	}
	
	public 	List<Entity> getElements(){
		return elementsToReinsert;
	}
	
	public int treeDepth() {
		if (hasChildren()) return (children.get(0).treeDepth()+1);
		else return 0;
	}



}
