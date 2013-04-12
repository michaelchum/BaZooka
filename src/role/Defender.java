package role;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import localization.LightLocalizer;
import bluetooth.StartCorner;
import odometry.LCDInfo;
import odometry.OdometryCorrection;
import odometry.OdometryAngleCorrection;

/**
 * Specific role of defender
 * 
 * @author Team 13
 * 
 */
public class Defender extends Robot {
	private final double defensiveZoneX = 135;
	private double defensiveZoneY;
	private double defensiveLocalizationX, defensiveLocalizationY;

	/**
	 * Constructor Same as Robot constructor
	 * 
	 * @param catapultMotor
	 * @param leftMotor
	 * @param rightMotor
	 * @param leftLightPort
	 * @param centerLightPort
	 * @param rightLightPort
	 * @param USPort
	 */
	public Defender(NXTRegulatedMotor catapultMotor,
			NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor,
			SensorPort leftLightPort, SensorPort centerLightPort,
			SensorPort rightLightPort, SensorPort USPort) {
		super(catapultMotor, leftMotor, rightMotor, leftLightPort,
				centerLightPort, rightLightPort, USPort);
	}

	@Override
	public void play(StartCorner startingCorner, int bx, int by, int w1,
			int w2, int d1, int goalX, int goalY) {
		
		// Compute defensive zone coordinates
		defensiveZoneY = goalY - (w2 * 30) - 15;
		defensiveLocalizationX = goalX;
		defensiveLocalizationY = defensiveZoneY - 15;
		myCatapult.arm();
		@SuppressWarnings("unused")
		LCDInfo info = new LCDInfo(myOdometer, USSensor, leftSensor,
				centerSensor, rightSensor);

		localize(startingCorner);
		postLocalize(startingCorner);
		OdometryCorrection myOdometryCorrection = new OdometryCorrection(
				myOdometer, centerSensor, leftMotor, rightMotor);
		myOdometryCorrection.start();
		myNav.navigateTo(defensiveZoneX, defensiveZoneY); // navigate
															// to
															// defensive
		myNav.travelTo(goalX, defensiveZoneY);
		myNav.turnTo(0, true);
	
	}

	/**
	 * Patrol when in front of the basket
	 */
	private void patrol() {
		while (true) {
			for (int index = 0; index < 5; index++) {
				myNav.navigateTo(195, defensiveZoneY);
				myNav.navigateTo(105, defensiveZoneY);
			}
			myNav.navigateTo(defensiveZoneX, defensiveZoneY);
			myNav.travelTo(defensiveZoneX - 15, defensiveZoneY - 15);
			LightLocalizer.doLocalization(myOdometer, myNav, centerSensor,
					leftMotor, rightMotor, defensiveLocalizationX,
					defensiveLocalizationY);

			// make sure the odometer has been set properly
			myOdometer.setX(defensiveLocalizationX);
			myOdometer.setY(defensiveLocalizationY);
			myOdometer.setTheta(90.0);
			

		}

	}
}
