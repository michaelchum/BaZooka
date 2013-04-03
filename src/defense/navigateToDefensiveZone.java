package defense;

import navigation.Navigator;
import odometry.Odometer;

/**
 * 
 * Intelligently navigate to the defensive zone
 * 
 * @author Michael
 *
 */
public class navigateToDefensiveZone{
	int w1;
	int w2;
	int d1; 
	Navigator nav;
	Odometer odo;
	
	/**
	 * Constructor
	 */
	public navigateToDefensiveZone(int w1, int w2, int d1, Navigator nav, Odometer odo) {
		this. w1 = w1;
		this.w2 = w2;
		this.d1 = d1;
		this.nav = nav;
		this.odo = odo;
	}
	
	public static void navigate(int w1, int w2, int d1, Navigator nav, Odometer odo) {
		navigateToDefensiveZone temp = new navigateToDefensiveZone(w1, w2, d1, nav, odo);
		temp.navigate();
	}
	
	public void navigate(){
		
	}
	

}

