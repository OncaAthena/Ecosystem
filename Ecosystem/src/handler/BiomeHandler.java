package handler;

import abandoned.EntityList;
import areas.Biome;

public class BiomeHandler extends EntityList {
	
	private int xSize, ySize;
	private Biome[][] biomes;
	
	private int leftBound, rightBound, upBound, downBound;
	private float width, height;
	
	
	public BiomeHandler (int xSize, int ySize, int leftBound, int rightBound, int upBound, int downBound) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.leftBound = leftBound;
		this.rightBound = rightBound;
		this.upBound = upBound;
		this.downBound = downBound;
		
		width = (leftBound - rightBound)/xSize;
		height = upBound - downBound/ySize;
		
		biomes = new Biome[xSize][ySize];
	}

}
