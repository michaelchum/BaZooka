package offense;

import navigation.Navigator;
import odometry.Odometer;

public class loadBall {

	/**
	 * 
	 * Load ball when in front of dispenser
	 * @author Michael
	 * 
	 */
	
	int bx;
	int by;
	Navigator nav;
	Odometer odo;
	double initialX;
	double initialY;

	public loadBall(int bx, int by, Navigator nav, Odometer odo) {
		this.bx=bx;
		this.by=by;
		this.nav = nav;
		this.odo = odo;
		initialX = odo.getX();
		initialY = odo.getY();
	}
	
	public static void load(int bx, int by, Navigator nav, Odometer odo){
		loadBall temp = new loadBall(bx, by, nav, odo);
		temp.travelToDispenser();
		temp.loadFiveBalls();
		temp.returnToInitial();
	}
	
	public void travelToDispenser(){
		
	}
	
	public void loadFiveBalls(){
		
	}
	
	public void returnToInitial(){
		
	}
	
}
