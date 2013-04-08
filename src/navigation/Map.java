package navigation;



import odometry.Odometer;

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
public class Map {
	
	private Odometer myOdometer;
	double tileWidth;
	public static final int NOTHING = 0;
	public static final int OBSTACLE = 1;
	public static final int GOAL = 1;
	public static final int ROBOT = 99;
	private double [] coordsX = new double[10];
	private double [] coordsY = new double[10];

	private int[][] grid = new int[][] {{1,0,0,0,0,0,0,0,0,1}, 
			   			       	{1,0,0,0,0,0,0,0,0,1}, 
			   			       	{1,0,0,0,0,0,0,0,0,1}, 
			   			       	{1,0,0,0,0,0,0,0,0,1}, 
			   			       	{1,0,0,0,0,0,0,0,0,1}, 
			   			       	{1,0,0,0,0,0,0,0,0,1}, 
			   			       	{1,0,0,0,0,0,0,0,0,1}, 
			   			       	{1,0,0,0,0,0,0,0,0,1}, 
			   			       	{1,0,0,0,0,0,0,0,0,1}, 
			   			       	{1,0,0,0,0,0,0,0,0,1}};
	
	/**
	 * Constructor
	 * @param odo
	 * @param tileWidth
	 */
	public Map(Odometer odo, double tileWidth){
		this.myOdometer = odo;
		this.tileWidth = tileWidth;
		
		this.tileWidth = tileWidth;
		double sumX = tileWidth/2;
		double sumY = tileWidth/2 + tileWidth*9;
		
		// build x coordinates of map
		for(int i=0;i<this.getCoordsX().length;i++){
			this.getCoordsX()[i] = sumX;
			sumX = sumX + this.tileWidth;
		}
		
		//build y coordinates of map
		for(int j=0;j<this.getCoordsY().length;j++){
			this.getCoordsY()[j] = sumY;
			sumY = sumY - this.tileWidth;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int currentI(){
		double currentY = getClosest(getCoordsY(), myOdometer.getY());
		int currentI = 0;
		for(int j=0; j<getCoordsY().length; j++){
			double delta = 3.00;
			if(Math.abs(currentY-getCoordsY()[j]) < delta){
				currentI=j;
				break;
			}
		}
		return currentI;
	}
	
	/**
	 * 
	 * @return
	 */
	public int currentJ(){
		double currentX = getClosest(getCoordsX(), myOdometer.getX());
		int currentJ = 0;
		for(int i=0; i<getCoordsX().length; i++){
			double delta = 3.00;
			if(Math.abs(currentX-getCoordsX()[i]) < delta){
				currentJ=i;
				break;
			}
		}
		return currentJ;
	}
	
	/**
	 * 
	 * @param destX
	 * @return
	 */
	public int destJ(double destX){
		int destJ = 0;
		for(int i=0; i<getCoordsX().length; i++){
			double delta = 2.00; 
			if(Math.abs(destX-getCoordsX()[i]) <= delta){
				destJ = i;
				break;
			}
		}
		return destJ;
	}
	
	/**
	 * 
	 * @param destY
	 * @return
	 */
	public int destI(double destY){
		int destI = 0;
		for(int j=0; j<getCoordsY().length; j++){
			double delta = 2.00; 
			if(Math.abs(destY-getCoordsY()[j]) <= delta){
				destI = j;
				break;
			}
		}
		return destI;
	}
	
	
	/**
	 * 
	 * @param destJ
	 * @return
	 */
	public double destX(int destJ){
		return this.getCoordsX()[destJ];
	}
	
	/**
	 * 
	 * @param destI
	 * @return
	 */
	public double destY(int destI){
		return this.getCoordsY()[destI];
	}
	
	/**
	 * 
	 * @param i
	 * @param j
	 * @param value
	 */
    private void insertValue(int i, int j, int value){
        getGrid()[i][j]= value;
    }

    /**
     * 
     * @param i
     * @param j
     * @return
     */
    private int getValue(int i, int j) {
        if(i>=0 && i<10 && j>=0 && j<10)
                return getGrid()[i][j];
        else return -3;
    }
    
    /**
     * 
     * @param array
     * @param position
     * @return
     */
	private double getClosest(double[] array, double position) {
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
	
	/**
	 * 
	 * @param grid
	 * @return
	 */
	private int getHighest(int[][] grid){
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
	
	/**
	 * 
	 * @param grid
	 * @return
	 */
	private int[][] filterMap(int[][] grid){
		return grid;
	}

	/**
	 * Accessor
	 * @return - 2D array of ints representing a grid which represents tha map
	 */
	public int[][] getGrid() {
		return grid;
	}

	/**
	 * Mutator
	 * @param grid - the new grid
	 */
	public void setGrid(int[][] grid) {
		this.grid = grid;
	}

	
	/**
	 * Accessor
	 * @return
	 */
	double [] getCoordsX() {
		return coordsX;
	}

	/**
	 * Mutator
	 * @param coordsX
	 */
	void setCoordsX(double [] coordsX) {
		this.coordsX = coordsX;
	}

	
	/**
	 * Accessor
	 * @return
	 */
	double [] getCoordsY() {
		return coordsY;
	}

	/**
	 * Mutator
	 * @param coordsY
	 */
	void setCoordsY(double [] coordsY) {
		this.coordsY = coordsY;
	}
}
