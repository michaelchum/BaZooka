package offense;

import motion.Catapult;
import navigation.Navigator;
import odometry.Odometer;

public class fireBall {
	Navigator nav;
	Odometer odo;
	Catapult cata;
	int goalX;
	int goalY;

	/**
	 * 
	 * Fire ball to the basket
	 * @author Michael
	 * 
	 */
	
	public fireBall(Catapult cata, int goalX, int goalY){
		this.goalX = goalX;
		this.goalY = goalY;
	}
	
	public static void fire(Catapult cata, int goalX, int goalY){
		fireBall temp = new fireBall(cata, goalX, goalY);
		temp.shootFiveTimes();
	}
	
	public void shootFiveTimes(){
		
	}
	
}
