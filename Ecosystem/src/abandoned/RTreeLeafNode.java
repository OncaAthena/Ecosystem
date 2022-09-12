package abandoned;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import entities.Entity;
import main.Camera;

@Deprecated
public class RTreeLeafNode extends RTreeNode {

	private List<Entity> elements = new ArrayList<>();
	private List<Entity> removingElems = new ArrayList<>();
	
	public RTreeLeafNode(int nodeCap, RTreeNode parent) {
		super(nodeCap, parent);
	}
	
	@Override
	public void Render(Graphics g, Camera c ) {
		
		RenderBounds(g, c);
		
		for (int i = 0; i < elements.size(); i++) {
			elements.get(i).render(g, c);
		}
		for (int i = 0; i < elementsToReinsert.size(); i++) {
			Entity child = elementsToReinsert.get(i);
			child.render(g, c);
			System.out.println("Leaf with reinsertion!!!!!!");
		}
		for (int i = 0; i < removingElems.size(); i++) {
			Entity child = removingElems.get(i);
			child.render(g, c);
			System.out.println("Leaf with reinsertion!!!!!!");
		}

	}
	@Override
	public void Update(double delta) {
		removingElems = new ArrayList<>();
		for (int i = 0; i < elements.size(); i++) {
			Entity e = elements.get(i);
			//e.Update(delta);
			updateElement(e);
		}
		
		if (dying) die();
	}

	
	
	
	
	
	
	@Override
	public void insert(Entity e) {
		elements.add(e);
		checkBounds(e, true);
		verifyNode();
	}
	
	public void remove(Entity e) {
		removingElems.add(e);
		elements.remove(e);
		recalculateBounds();
		
	}
	
	public void updateElement(Entity e) {
		if (!inside(e)){
			// TODO : Implementar buffer
			if (isRoot()) {
				checkBounds(e, false);
			}else {
				remove(e);
				parent.updateElement(e);
			}
		}
	}
	
	
	
	public void checkBounds(Entity e, boolean propagate) {
		boolean flag = false;
		if (e.getX() < leftBound) {
			leftBound = e.getX();
			flag = true;
		}
		if (e.getX() > rightBound) {
			rightBound = e.getX();
			flag = true;
		}
		if (e.getY() < upBound) {
			upBound = e.getY();
			flag = true;
		}
		if (e.getY() > downBound) {
			downBound = e.getY();
			flag = true;
		}
		if (flag && propagate && parent !=null) {
			parent.checkBounds(this);
		}
	}

	@Override
	protected void split() {
		
		boolean isCurrentRoot = isRoot();
		if (isCurrentRoot) {
			parent = new RTreeNode(nodeCap, null);
		}
		
		int[] ineffPair = mostInefficientPair();
		RTreeLeafNode split1 = new RTreeLeafNode(nodeCap, parent);
		RTreeLeafNode split2 = new RTreeLeafNode(nodeCap, parent);
		parent.addNode(split1);
		parent.addNode(split2);
		split1.insert(elements.get(ineffPair[0]));
		split2.insert(elements.get(ineffPair[1]));
		
		if (ineffPair[1] > ineffPair[0]) {
			elements.remove(ineffPair[1]);
			elements.remove(ineffPair[0]);
		} else{
			elements.remove(ineffPair[0]);
			elements.remove(ineffPair[1]);
		}
		
		while (elements.size()>1) {
			ineffPair = mostInefficientPair();
			float addedArea = 0.0f;
			Entity ir1 = elements.get(ineffPair[0]);
			Entity ir2 = elements.get(ineffPair[1]);
			addedArea += split1.areaIncrease(ir1);
			addedArea += split2.areaIncrease(ir2);
			addedArea -= split1.areaIncrease(ir2);
			addedArea -= split2.areaIncrease(ir1);
			
			if (addedArea < 0) {
				split1.insert(ir1);
				split2.insert(ir2);
			}else {
				split1.insert(ir2);
				split2.insert(ir1);
			}
	
			if (ineffPair[1] > ineffPair[0]) {
				elements.remove(ineffPair[1]);
				elements.remove(ineffPair[0]);
			} else{
				elements.remove(ineffPair[0]);
				elements.remove(ineffPair[1]);
			}
			
		}
		
		while (elements.size()>0) {
			Entity c = elements.get(0);
			boolean is1smaller = split1.areaIncrease(c) < split2.areaIncrease(c);
			if (is1smaller) split1.insert(c);
			else split2.insert(c);
			elements.remove(0);
		}
		
		if (isCurrentRoot) parent.setAsRoot(tree);
		else dying = true;
		
		parent.verifyNode();
	
	}
	
	

	
	public void recalculateBounds() {
		resetBounds();
		for (int i = 0; i < elements.size(); i++) {
			Entity e = elements.get(i);
			checkBounds(e, false);
		}
	}
	
	@Override
	public 	List<Entity> getElements(){
		return elements;
	}

	
	@Override
	public int count() {
		return elements.size();
	}

	
	
	
	
	
	
	
	

	@Override
	protected int[] mostInefficientPair() {
		List<Entity> children = elements;

		int mi1 = 0;
		int mi2 = 1;
		
		float worseArea = -1;
		
		for(int i = 0; i < children.size()-1; i++) {
			Entity c1 = children.get(i);
			for (int j = i+1; j< children.size();j++) {
				Entity c2 = children.get(j);
				
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


	private float mbr2(Entity c1, Entity c2) {
		float l, r, u, d;
		if (c1.getX() < c2.getX()) {
			l = c1.getX();
			r = c2.getX();
		}else {
			l = c2.getX();
			r = c1.getX();
		}
		if (c1.getY() < c2.getY()) {
			u = c1.getY();
			d = c2.getY();
		}else {
			u = c2.getY();
			d = c1.getY();
		}
		
		return area(l, r, u, d);
	}

}