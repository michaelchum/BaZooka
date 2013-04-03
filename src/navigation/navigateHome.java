package navigation;

import bluetooth.StartCorner;
import navigation.Navigator;
import odometry.Odometer;

/**
 * 
 * Intelligently navigate back to initial corner
 * 
 * @author Michael
 *
 */
public class navigateHome{
	StartCorner startingCorner;
	int startX;
	int startY;
	Navigator nav;
	Odometer odo;
	
	/**
	 * Constructor
	 */
	public navigateHome(StartCorner startingCorner, Navigator nav, Odometer odo) {
		this.startX = startingCorner.getX();
		this.startY = startingCorner.getY();
		this.nav = nav;
		this.odo = odo;
	}
	
	public static void navigate(StartCorner startingCorner, Navigator nav, Odometer odo) {
		navigateHome temp = new navigateHome(startingCorner, nav, odo);
		temp.navigate();
	}
	
	public void navigate(){
		
	}
	

}

