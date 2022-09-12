package areas;

import java.awt.Graphics;
import java.util.*;
import entities.*;
import main.Camera;
import util.Bounds;

public class Chunk {
	
	private List<Creature> creatures = new ArrayList<Creature>(); 
	private List<Creature> creaturesToRemove = new ArrayList<Creature>(); 
	private List<Plant> plants= new ArrayList<Plant>(); 
	
	private int plantsToSpawn = 10;
	
	private Bounds bounds;
	
	public Chunk(Bounds bounds) {
		this.bounds = bounds;
		spawnPlants();
		
//		System.out.println("Chunk located at: "+ bounds.left + ", " + bounds.up);

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
	
	private void spawnPlants() {
		for (int i = 0;i < plantsToSpawn; i++) {
			Plant p = new Plant(bounds.randX(), bounds.randY());
			plants.add(p);
//			System.out.println("Plant located at: "+ p.getX() + ", " + p.getY());
		}
	}
	
	public void update(double delta) {
		removeCreatures();
		
		for (int i =0; i<creatures.size();i++) {
			Creature creature = creatures.get(i);
			checkCollisions(creature);
			creature.update(delta);
		}
	}
	private void checkCollisions(Creature creature) {
		float x = creature.getX();
		float y = creature.getY();
		float cs = Creature.Size;
		Bounds coll = new Bounds(x,x+cs,y,y+cs);
		for (int i=0; i < plants.size(); i++) {
			Plant p = plants.get(i);
			if (coll.inside(p.getX(), p.getY())) {
//				System.out.println("Plant eaten!");
				plants.remove(i);
				i--;
			}
		}
		
	}

	public void render(Graphics g, Camera c) {
		//System.out.println("Chunk render: " + creatures.size() + " creatures");
		for (int i =0; i<creatures.size();i++) {
			Creature creature = creatures.get(i);
			creature.render(g, c);
		}
		for (int i =0; i<plants.size();i++) {
			Plant plant = plants.get(i);
			plant.render(g, c);
		}
	}
	
	public List<Creature> getCreatures(){
		return new ArrayList<Creature>(creatures);
	}


}
