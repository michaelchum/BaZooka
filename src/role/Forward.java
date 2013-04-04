package role;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import localization.LightLocalizer;
import localization.USLocalizer;
import bluetooth.StartCorner;

/**
 * Specific role of forward
 * 
 * @author Michael
 * 
 */
public class Forward extends Robot {
	static final double distanceFromCenterToRamp = 6.5, loadingDistance = 5;

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
	public Forward(NXTRegulatedMotor catapultMotor,
			NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor,
			SensorPort leftLightPort, SensorPort centerLightPort,
			SensorPort rightLightPort, SensorPort USPort) {
		super(catapultMotor, leftMotor, rightMotor, leftLightPort,
				centerLightPort, rightLightPort, USPort);
	}

	@Override
	public void play(StartCorner startingCorner, int bx, int by, int w1,
			int w2, int d1, int goalX, int goalY) {

		myCatapult.arm();
		LightLocalizer.doLocalization(myOdo, myNav, centerSensor, leftMotor,
				rightMotor, startingCorner);
		USLocalizer.doFallingEdgeLocalization(myOdo, myNav, USSensor,
				leftMotor, rightMotor);

		// compute loading coordinates

		double loadingX = 0, loadingY = 0, loadingHeading = 0;
		if (bx < 0) { // western wall
			loadingX = bx + loadingDistance;
			loadingY = by + distanceFromCenterToRamp;
			loadingHeading = 180;
		}

		if (by < 0) { // southern wall
			loadingX = bx - distanceFromCenterToRamp;
			loadingY = by + loadingDistance;
			loadingHeading = 270;
		}

		if (bx > 6 && by > 0) { // eastern wall
			loadingX = bx - loadingDistance;
			loadingY = by - distanceFromCenterToRamp;
			loadingHeading = 0;
		}

		myNav.navigateTo(loadingX, loadingY);
		myNav.turnTo(loadingHeading, false);
		loadFiveBalls();

		myNav.navigateTo(goalX, goalY - (d1 + 15));
		shootFiveBalls();
		LightLocalizer.doLocalization(myOdo, myNav, centerSensor, leftMotor,
				rightMotor);
	}

	public void loadFiveBalls() {
		DifferentialPilot myPilot = new DifferentialPilot(5.36, 5.36, 16.32,
				leftMotor, rightMotor, false);
		for (int i = 0; i < 5; i++) {
			myPilot.travel(5);
			myPilot.travel(-5);
		}
	}

	private void shootFiveBalls() {
		for (int i = 0; i < 5; i++) {
			myCatapult.carry();
			myCatapult.shootCenter();
		}
	}

}
