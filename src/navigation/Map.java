package navigation;

/**
 * A map has been written using the wavefront technique, which consists of mapping the entire field in a 2D array: a "wavefront grid".
 * Each number on the grid represents a 30' by 30' tile on the field. The system inserts numbers into the grid, in order to link the 
 * robot to its target locations, thereafter the robot will travel block per block following a pattern of number until it reaches the
 * goal. For more information, the method has been entirely based on the following URL and open source information.
 * 
 * http://www.societyofrobots.com/programming_wavefront.shtml
 * http://www.robotc.net/blog/2011/08/08/robotc-advanced-training/
 * http://www.mcs.alma.edu/LMICSE/LabMaterials/AlgoComp/Lab4/AlgCoL4.htm
 * https://code.google.com/p/mindstormsproject/source/browse/trunk/LejosProject/src/it/uniba/wavefront/GridWalker.java?r=5
 * https://code.google.com/p/mindstormsproject/source/browse/trunk/LejosProject/src/it/uniba/wavefront/Grid.java?r=6
 * 
 * @author Team 13
 * 
 */

import odometry.Odometer;

public class Map {
	
	Odometer odo;
	double tileWidth;
	int nothing = 0;
	int obstacle = 1;
	int goal = 1;
	int robot = 99;
	double [] coordsX = new double[10];
	double [] coordsY = new double[10];

	int[][] grid = new int[][] {{1,0,0,0,0,0,0,0,0,1}, 
			   			       	{1,0,0,0,0,0,0,0,0,1}, 
			   			       	{1,0,0,0,0,0,0,0,0,1}, 
			   			       	{1,0,0,0,0,0,0,0,0,1}, 
			   			       	{1,0,0,0,0,0,0,0,0,1}, 
			   			       	{1,0,0,0,0,0,0,0,0,1}, 
			   			       	{1,0,0,0,0,0,0,0,0,1}, 
			   			       	{1,0,0,0,0,0,0,0,0,1}, 
			   			       	{1,0,0,0,0,0,0,0,0,1}, 
			   			       	{1,0,0,0,0,0,0,0,0,1}};
	
	public Map(Odometer odo, double tileWidth){
		this.odo = odo;
		this.tileWidth = tileWidth;
		
		this.tileWidth = tileWidth;
		double sumX = tileWidth/2;
		double sumY = tileWidth/2 + tileWidth*9;
		
		// build x coordinates of map
		for(int i=0;i<this.coordsX.length;i++){
			this.coordsX[i] = sumX;
			sumX = sumX + this.tileWidth;
		}
		
		//build y coordinates of map
		for(int j=0;j<this.coordsY.length;j++){
			this.coordsY[j] = sumY;
			sumY = sumY - this.tileWidth;
		}
	}
	
	public int currentI(){
		double currentY = getClosest(coordsY, odo.getY());
		int currentI = 0;
		for(int j=0; j<coordsY.length; j++){
			double delta = 3.00;
			if(Math.abs(currentY-coordsY[j]) < delta){
				currentI=j;
				break;
			}
		}
		return currentI;
	}
	
	public int currentJ(){
		double currentX = getClosest(coordsX, odo.getX());
		int currentJ = 0;
		for(int i=0; i<coordsX.length; i++){
			double delta = 3.00;
			if(Math.abs(currentX-coordsX[i]) < delta){
				currentJ=i;
				break;
			}
		}
		return currentJ;
	}
	
	public int destJ(double destX){
		int destJ = 0;
		for(int i=0; i<coordsX.length; i++){
			double delta = 2.00; 
			if(Math.abs(destX-coordsX[i]) <= delta){
				destJ = i;
				break;
			}
		}
		return destJ;
	}
	
	
	public int destI(double destY){
		int destI = 0;
		for(int j=0; j<coordsY.length; j++){
			double delta = 2.00; 
			if(Math.abs(destY-coordsY[j]) <= delta){
				destI = j;
				break;
			}
		}
		return destI;
	}
	
	public double destX(int destJ){
		return this.coordsX[destJ];
	}
	
	public double destY(int destI){
		return this.coordsY[destI];
	}
	
    public void insertValue(int i, int j, int value){
        grid[i][j]= value;
    }

    public int getValue(int i, int j) {
        if(i>=0 && i<10 && j>=0 && j<10)
                return grid[i][j];
        else return -3;
    }
    
	public double getClosest(double[] array, double position) {
	    double lowestDiff = Double.MAX_VALUE;
	    double result = 0.0;
	    for (double i : array) {
	        double diff = Math.abs(position - i);
	        if (diff < lowestDiff) {
	            lowestDiff = diff;
	            result = i;
	        }
	    }
	    return result;
	}
	
	public int getHighest(int[][] grid){
		int highest= 1;
		for(int i = 0; i < grid.length; i++) {
		    for(int j = 0; j < grid.length; j++){
		    	if(highest < grid[i][j] && grid[i][j]!= 99) {
			    	highest = grid[i][j];
		    	}
		    }
		}
		return highest;
	}
	
	// filer map for impossible paths
	public int[][] filterMap(int[][] grid){
		return grid;
	}
}
