package role;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import localization.LightLocalizer;
import localization.USLocalizer;
import bluetooth.StartCorner;


/**
 * Specific role of defender
 * 
 * @author Michael
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
		USLocalizer.doFallingEdgeLocalization(myOdo, myNav, USSensor, leftMotor, rightMotor);
		myNav.navigateTo(goalX, goalY - (w2 * 30 + 15)); //navigate to defensive zone
		
		
	}
	
	private void patrol() {
		
	}
	
	private void block() {
		
	}


}
