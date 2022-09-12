package handler;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.List;

import areas.Chunk;
import entities.*;
import main.Camera;
import util.Bounds;

public class EntityHandler {

	private Chunk[][] chunks;
	private int xSize, ySize;
	private Bounds bounds;
	private float xChunkSize, yChunkSize;
	
	public EntityHandler(int xSize, int ySize, Bounds bounds) {
		this.bounds = bounds;
		this.xSize = xSize;
		this.ySize = ySize;
		xChunkSize = bounds.width()/xSize;
		yChunkSize = bounds.height()/ySize;
//		System.out.println("chunk size: "+ xChunkSize + " x " + yChunkSize);
		
		chunks = new Chunk[xSize][ySize];
		for (int i = 0; i<xSize; i++) {
			for (int j = 0; j<xSize; j++) {
				System.out.println("Initializing chunks: "+ ((100*i+(100*j/ySize))/xSize)+ "%...");
				chunks[i][j] = new Chunk(chunkBounds(i, j));
			}
		}
		
	}

	public void insert(Creature c) {
		chunkAt(c.getX(), c.getY()).insert(c);
	}
	public void update(double delta) {
		for (int i =0; i<xSize;i++) {
			for (int j = 0; j<xSize;j++) {
				
				chunks[i][j].update(delta);
				
			}
		}
		
		updateCreatures();
	}
	public void render(Graphics g, Camera c) {
		for (int i =0; i<xSize;i++) {
			for (int j = 0; j<ySize;j++) {
				chunks[i][j].render(g, c);				
			}
		}
	}
	
	private void updateCreatures(){
		for (int i =0; i<xSize;i++) {
			for (int j = 0; j<ySize;j++) {
				Chunk ch = chunks[i][j];
				List<Creature> iterator = ch.getCreatures();
				for (int k =0; k < iterator.size(); k++) {
					Creature c = iterator.get(k);
					float x = c.getX(), y = c.getY();
					if (!chunkBounds(i, j).inside(x, y)) {
						insert(c);
						ch.queueRemoval(c);
					}
					
				}
			}
		}
	}
	
	private Bounds chunkBounds(int i, int j) {
		Bounds b = new Bounds(	bounds.left + (i*xChunkSize),
								bounds.left + ((i+1)*xChunkSize),
								bounds.up + (j*xChunkSize),
								bounds.up+ ((j+1)*xChunkSize));
		return b;
	}
	
	private Chunk chunkAt(float x, float y) {
		int i=0, j=0;
		if (bounds.inside(x, y)) {
			i = (int) (x/xChunkSize);
			j = (int) (y/yChunkSize);
		}
		return chunks[i][j];
	}

}
