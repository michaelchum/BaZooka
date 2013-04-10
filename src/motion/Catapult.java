package motion;

import lejos.nxt.NXTRegulatedMotor;
import lejos.util.Delay;

/**
 * 
 * The catapult of the robot
 * 
 * @author Team 13
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
	// start from initial position and lock the arm at armed position
	public void arm() {
		centreM.resetTachoCount();
		centreM.setSpeed(150);
		centreM.setAcceleration(500);
		centreM.rotateTo(-228);//213
		centreM.stop();
		  
		// reset angle so that 0 is the armed position
		centreM.resetTachoCount();
		Delay.msDelay(800);
	}
	
	/**
	 * From armed position, retrieve a ball and rotate back to armed position
	 */
	public void carry() {
		centreM.setAcceleration(3500);
		centreM.setSpeed(90);
		centreM.rotateTo(-55);
		centreM.setSpeed(25);
		centreM.rotateTo(0);
		centreM.stop();
		Delay.msDelay(2000);
	}
	
	/**
	 * Shoot the ball and rotate back to armed position from the centered position or any sides determined
	 */
	public void shootCenter() {
		centreM.setAcceleration(100000);
		centreM.setSpeed(100000);
		centreM.rotateTo(115);
		centreM.stop();
		Delay.msDelay(2000);
		   
		centreM.setSpeed(150);
		centreM.rotateTo(0);
		centreM.stop();
		Delay.msDelay(1000);
	}
}
