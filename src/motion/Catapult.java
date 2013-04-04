package motion;

import lejos.nxt.NXTRegulatedMotor;
import lejos.util.Delay;

/**
 * 
 * The catapult of the robot
 * @author Michael
 * 
 */

public class Catapult {

	NXTRegulatedMotor centreM;
	
  	/**
	* Constructor
	* @param - The catapult's motor
	*/
	public Catapult(NXTRegulatedMotor m) {
		centreM = m;
	}
 
	/**
	 * From initial position, rotate the lever arm to armed position
	 */
	public void arm() {
		   centreM.resetTachoCount();
		   centreM.setSpeed(150);
		   centreM.setAcceleration(500);
		   
		   centreM.rotateTo(-230);
		   centreM.stop();
		  
		   // reset angle so that 0 is the armed position
		   centreM.resetTachoCount();
		   Delay.msDelay(500);
	}
	
	/**
	 * From armed position, retrieve a ball and rotate back to armed position
	 */
	public void carry() {
	   centreM.setSpeed(30);
	   centreM.setAcceleration(2500);
	   centreM.rotateTo(-33);
	   centreM.setSpeed(25);
	   centreM.rotateTo(0);
	   centreM.stop();
	   Delay.msDelay(2000);
   }

	/**
	 * Shoot the ball and rotate back to armed position
	 */
	public void shootCenter() {
	   centreM.setSpeed(2500);
	   centreM.rotateTo(100);
	   centreM.stop();
	   Delay.msDelay(2000);
	   
	   centreM.setSpeed(150);
	   centreM.rotateTo(0);
	   centreM.stop();
	   Delay.msDelay(1000);
	}
	
	public void block() {
		
	}

}
