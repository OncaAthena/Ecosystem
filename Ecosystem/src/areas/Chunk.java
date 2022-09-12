package areas;

import java.awt.Graphics;
import java.util.*;
import entities.*;
import main.Camera;

public class Chunk {
	
	private List<Creature> creatures = new ArrayList<Creature>(); 
	private List<Creature> creaturesToRemove = new ArrayList<Creature>(); 
	
	public Chunk() {
		
	}
	
	public void  insert(Creature c) {
		creatures.add(c);
	}
	public void queueRemoval(Creature c) {
		//System.out.println("Removal!");
		creaturesToRemove.add(c);
	}
	
	private void removeCreatures() {
		for (int i = 0; i < creaturesToRemove.size();i++) {
			creatures.remove(creaturesToRemove.get(i));
		}
		creaturesToRemove = new ArrayList<Creature>();
	}
	
	public void  insert(Entity c) {
		System.out.println("Wrong insert!");
		//creatures.add(c);
	}
	public void update(double delta) {
		removeCreatures();
		
		for (int i =0; i<creatures.size();i++) {
			Creature creature = creatures.get(i);
			creature.update(delta);
		}
	}
	public void render(Graphics g, Camera c) {
		//System.out.println("Chunk render: " + creatures.size() + " creatures");
		for (int i =0; i<creatures.size();i++) {
			Creature creature = creatures.get(i);
			creature.render(g, c);
		}
	}
	
	public List<Creature> getCreatures(){
		return new ArrayList<Creature>(creatures);
	}


}
