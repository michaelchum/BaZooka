package offense;

import motion.Catapult;

public class fireBall {

	/**
	 * 
	 * Fire ball to the basket
	 * @author Michael
	 * 
	 */
	
	Catapult myCatapult;
	int goalX;
	int goalY;

	public fireBall(Catapult myCatapult, int goalX, int goalY) {
		this.myCatapult = myCatapult;
		this.goalX = goalX;
		this.goalY = goalY;
	}
	

	public static void fire(Catapult myCatapult, int goalX, int goalY){
		fireBall temp = new fireBall(myCatapult, goalX, goalY);
		temp.fire();
	}
	
	public void fire(){
		
	}
	
}
