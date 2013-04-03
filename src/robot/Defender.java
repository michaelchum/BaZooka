package robot;

import bluetooth.PlayerRole;
import bluetooth.StartCorner;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import localization.LightLocalizer;
import localization.USLocalizer;
import odometry.LCDInfo;
import odometry.OdometryCorrection;
import defense.navigateToDefensiveZone;
import defense.patrol;

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

	/**
	 * First locate and then patrol in front of the basket
	 * 
	 * @see robot.Robot#play()
	 */
	@Override
	public void play() {

	}
	
	public void localize(StartCorner startingCorner) {
		myCatapult.arm();
		// localize and position at the first block
		USLocalizer.doFallingEdgeLocalization(myOdo, myNav, USSensor, leftMotor, rightMotor);
		LightLocalizer.doLocalization(myOdo, myNav, centerSensor, leftMotor, rightMotor);
		
		// navigate to the middle of the first tile depending on StartCorner
		// myNav.TravelTo(15.0,15.0);
		// myNav.TurnTo(90.0);
	}
	
	public void play(PlayerRole role, int bx, int by, int w1, int w2, int d1, StartCorner startingCorner) {
		// localize(startingCorner);
		
		// LCD
		// LCDInfo LCD = new LCDInfo(myOdo, myUSSensor, leftSensor, centerSensor, rightSensor);

		// initialize correction threads
		// OdometryCorrection myOdoCorrection = new OdometryCorrection(myOdo, centerSensor, leftMotor, rightMotor);
		// myOdoCorrection.start();
		// OdometryAngleCorrection myOdoAngleCorrection = new OdometryAngleCorrection(myOdo, leftSensor, rightSensor, leftMotor, rightMotor);
		// myOdoAngleCorrection.start();
		
		/* NEED TO WRITE ALL THESE USING NAVIGATOR */
		
		// navigateToDefensiveZone.navigate(w1, w2, d1, myNav, myOdo);
		// patrol.start(w1, w2, d1, myNav, myOdo);
	}

}
