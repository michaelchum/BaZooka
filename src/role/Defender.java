package role;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import localization.LightLocalizer;
import localization.USLocalizer;
import bluetooth.StartCorner;


/**
 * Specific role of defender
 * 
 * @author Team 13
 * 
 */
public class Defender extends Robot {
	/**
	 * Constructor - same as Robot constructor
	 * 
	 * @param catapultMotor
	 * @param leftMotor
	 * @param rightMotor
	 * @param leftLightPort
	 * @param centerLightPort
	 * @param rightLightPort
	 * @param USPort
	 */
	public Defender(NXTRegulatedMotor catapultMotor,NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor,
			SensorPort leftLightPort, SensorPort centerLightPort, SensorPort rightLightPort, SensorPort USPort) {
		super(catapultMotor, leftMotor, rightMotor, leftLightPort,
				centerLightPort, rightLightPort, USPort);
	}

	@Override
	public void play(StartCorner startingCorner, int bx, int by, int w1,
			int w2, int d1, int goalX, int goalY) {
		myCatapult.arm();
		LightLocalizer.doLocalization(myOdo, myNav, centerSensor, leftMotor, rightMotor, startingCorner);
		USLocalizer.doFallingEdgeLocalization(myOdo, USSensor, myNav, leftMotor, rightMotor);
		myNav.travelTo(15, 15);
		
		myNav.navigateTo((goalX * 30) - 15, goalY - ((w2 * 30) + 15)); //navigate to defensive zone
		myNav.travelTo(goalX*30, goalY - ((w2 * 30))); //travel in front of the goal
		
	}
	
	private void patrol() {
		
	}
	
	private void block() {
		
	}


}
