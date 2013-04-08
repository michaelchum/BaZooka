package role;

import odometry.LCDInfo;
import odometry.Odometer;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import localization.LightLocalizer;
import localization.USLocalizer;
import bluetooth.StartCorner;

/**
 * Specific role of forward
 * 
 * @author Team 13
 * 
 */
public class Forward extends Robot {
	static final double distanceFromCenterToRamp = 6.5, loadingDistance = 5,
			distanceToWall = 22;
	private double loadingTileX = 0, loadingTileY = 0, preciseLoadingX = 0,
			preciseLoadingY = 0, loadingHeading = 0;

	private double loadingLocalizationX, loadingLocalizationY;// coordinates
																// of
																// the
																// grid
																// intersection
																// where
																// the
																// robot
																// will
																// localize
																// before
																// loading
																// balls

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
		@SuppressWarnings("unused")
		LCDInfo info = new LCDInfo(myOdometer, USSensor, leftSensor,
				centerSensor, rightSensor);
		// localize(startingCorner);
		postLocalize(startingCorner);

		computeLoadingCoordinates(bx, by);
		computeLoadingLocalizationCoords(bx, by);

		// navigate to loading tile
		myNav.navigateTo(loadingTileX, loadingTileY);
		//
		// localize
		myNav.travelTo(loadingLocalizationX, loadingLocalizationY);

		LightLocalizer.doLocalization(myOdometer, myNav, centerSensor,
				leftMotor, rightMotor, loadingLocalizationX,
				loadingLocalizationY);

		// make sure the odometer has been set properly
		myOdometer.setX(loadingLocalizationX);
		myOdometer.setY(loadingLocalizationY);
		myOdometer.setTheta(90.0);

		myNav.travelTo(preciseLoadingX, preciseLoadingY);
		myNav.turnTo(loadingHeading, true);
		loadFiveBalls();
		//
		// // localize again (pushing the button fucks it up)
		// myNav.travelTo(loadingLocalizationX, loadingLocalizationY);
		//
		// LightLocalizer.doLocalization(myOdometer, myNav, centerSensor,
		// leftMotor, rightMotor, loadingLocalizationX,
		// loadingLocalizationY);
		//
		// // make sure the odometer has been set properly
		// myOdometer.setX(loadingLocalizationX);
		// myOdometer.setY(loadingLocalizationY);
		// myOdometer.setTheta(90.0);
		//
		// // navigate to firing area
		// myNav.navigateTo(135, goalY - ((d1 + 1) * 30) - 15);
		// myNav.travelTo(150, goalY - ((d1 + 1) * 30));
		// LightLocalizer.doLocalization(myOdometer, myNav, centerSensor,
		// leftMotor, rightMotor, 150, goalY - ((d1 + 1) * 30)); // localize
		// // before
		// // shooting
		// // make sure the odometer has been set properly
		// myOdometer.setX(150);
		// myOdometer.setY(goalY - ((d1 + 1) * 30));
		// myOdometer.setTheta(90.0);
		//
		// shootFiveBallsCenter();

	}

	/**
	 * Compute loading coordinates Need to change values if the arena changes
	 * 
	 * @param bx
	 *            - x-coordinate of ball dispenser in tiles
	 * @param by
	 *            - y-coordinate of ball dispenser in tiles
	 */
	private void computeLoadingCoordinates(int bx, int by) {
		if (bx == -1) { // western wall
			preciseLoadingX = bx * 30 + distanceToWall;
			preciseLoadingY = by * 30 + distanceFromCenterToRamp;
			loadingHeading = 180;

			loadingTileX = -15;
			loadingTileY = (by * 30) - 15;
		}

		if (by == -1) { // southern wall
			preciseLoadingX = bx * 30 - distanceFromCenterToRamp;
			preciseLoadingY = by * 30 + distanceToWall;
			loadingHeading = 270;

			loadingTileY = -15;
			loadingTileX = (bx * 30) - 15;
		}

		if (bx == 11) { // eastern wall
			preciseLoadingX = bx * 30 - distanceToWall;
			preciseLoadingY = by * 30 - distanceFromCenterToRamp;

			loadingTileX = 315;
			loadingTileY = (by * 30) - 15;
			loadingHeading = 0;
		}

	}

	/**
	 * Compute loading localization coordinates
	 * 
	 * @param bx
	 *            - x-coordinate of ball dispenser in tiles
	 * @param by
	 *            - y-coordinate of ball dispenser in tiles
	 */
	private void computeLoadingLocalizationCoords(int bx, int by) {
		if (bx < 0) { // west wall
			loadingLocalizationX = 30;
			loadingLocalizationY = by * 30;
		}

		if (bx == 11) { // east wall
			loadingLocalizationX = 270;
			loadingLocalizationY = by * 30;
		}

		if (by == -1) { // southern wall
			loadingLocalizationY = 30;
			loadingLocalizationX = bx * 30;
		}
	}

	private void loadFiveBalls() {
		DifferentialPilot myPilot = new DifferentialPilot(5.36, 5.36, 16.32,
				leftMotor, rightMotor, false);
		myPilot.setTravelSpeed(5);
		myPilot.travel(5);
		Delay.msDelay(30000);
		myPilot.travel(-10);
	}

	private void shootFiveBallsCenter() {
		myNav.turnTo(90, true);
		for (int i = 0; i < 5; i++) {
			myCatapult.carry();
			myCatapult.shootCenter();
		}
	}

	private void shootFiveBallsSide() {
		myNav.turnTo(90, true);
		for (int i = 0; i < 5; i++) {
			myCatapult.carry();
			myCatapult.shootSide();
		}
	}

}
