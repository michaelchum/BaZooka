package offense;

import navigation.Navigator;
import odometry.Odometer;

public class navigateToLoadingArea {

	/**
	 * 
	 * Navigate to the ball dispenser
	 * @author Michael
	 * 
	 */
	
	int bx;
	int by;
	Navigator nav;
	Odometer odo;

	public navigateToLoadingArea(int bx, int by, Navigator nav, Odometer odo) {
		this.bx=bx;
		this.by=by;
		this.nav = nav;
		this.odo = odo;
	}
	
	public static void navigate(int bx, int by, Navigator nav, Odometer odo){
		navigateToLoadingArea temp = new navigateToLoadingArea(bx, by, nav, odo);
		temp.navigateToDispenser();
		temp.rotateToDispenser();
	}
	
	public void navigateToDispenser(){
		
	}
	
	public void rotateToDispenser(){
		
	}
	 
}
