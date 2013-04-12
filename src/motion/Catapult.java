package motion;

import lejos.nxt.NXTRegulatedMotor;
import lejos.util.Delay;

/**
 * 
 * The functions of the robots catapult
 * 
 * @author Team 13
 * 
 */

public class Catapult {

	NXTRegulatedMotor centreM;
	
  	/**
	* Constructor
	* @param m The catapult's motor
	*/
	public Catapult(NXTRegulatedMotor m) {
		centreM = m;
	}
 
	/**
	 * From initial position, rotate the lever arm to armed position in which it will not interfere navigation
	 */
	public void arm() {
		centreM.resetTachoCount();
		centreM.setSpeed(150);
		centreM.setAcceleration(500);
		centreM.rotateTo(-178);//-228
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
		centreM.rotateTo(-58);
		centreM.setSpeed(25);
		centreM.rotateTo(0);
		centreM.stop();
		Delay.msDelay(2000);
	}
	
	/**
	 * Shoot the ball and rotate back to armed position
	 */
	public void shootCenter() {
		centreM.setAcceleration(100000);
		centreM.setSpeed(100000);
		centreM.rotate(115);
		centreM.stop();
		Delay.msDelay(2000);
		   
		centreM.setSpeed(150);
		centreM.rotateTo(0);
		centreM.stop();
		Delay.msDelay(1000);
	}
}
