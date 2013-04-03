package offense;

import navigation.Navigator;
import odometry.Odometer;

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
	Navigator nav;
	Odometer odo;
	
	/**
	 * Constructor
	 */
	public navigateToFiringArea(int goalX, int goalY, int d1, Navigator nav, Odometer odo) {
		this.goalX = goalX;
		this.goalY = goalY;
		this.d1 = d1;
		this.nav = nav;
		this.odo = odo;
	}
	
	public static void navigateLeftSpot(int goalX, int goalY, int d1, Navigator nav, Odometer odo) {
		navigateToFiringArea temp = new navigateToFiringArea( goalX, goalY, d1, nav, odo);
		temp.navigateToLeftSpot();
		temp.aim();
	}
	
	public static void navigateRightSpot(int goalX, int goalY, int d1, Navigator nav, Odometer odo) {
		navigateToFiringArea temp = new navigateToFiringArea( goalX, goalY, d1, nav, odo);
		temp.navigateToRightSpot();
		temp.aim();
	}
	
	public void navigateToLeftSpot(){
		
	}
	
	public void navigateToRightSpot(){
		
	}
	
	public void navigateToMiddleSpot(){
		
	}
	
	public void aim(){
		
	}

}

