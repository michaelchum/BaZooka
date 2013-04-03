package offense;

/**
 * 
 * Intelligently navigate to the firing area
 * 
 * @author Michael
 *
 */
public class navigateToFiringArea{
	int goalX;
	int goalY;
	int d1;
	
	/**
	 * Constructor
	 */
	public navigateToFiringArea(int goalX, int goalY, int d1) {
		this.goalX = goalX;
		this.goalY = goalY;
		this.d1 = d1;
	}
	
	public static void navigate(int goalX, int goalY, int d1) {
		navigateToFiringArea temp = new navigateToFiringArea(goalX, goalY, d1);
		temp.navigateToSpot();
	}
	
	public void navigateToSpot(){
		
	}
	

}

