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
import odometry.OdometryCorrection;
import odometry.OdometryAngleCorrection;

/**
 * Specific role of forward
 * 
 * @author Team 13
 * 
 */
public class Forward extends Robot {
	static final double CENTER_TO_RAMP = 6.5, LOADING_DISTANCE = 5,
			DISTANCE_TO_WALL = 22;
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
	public double leftFiringX, leftFiringY, leftPosX, leftPosY,
			leftFiringAngle, rightFiringX, rightFiringY, rightPosX, rightPosY,
			rightFiringAngle;
	public double firingCoordsX, firingCoordsY, firingPosX, firingPosY,
			firingAngle;

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

		localize(startingCorner);

		postLocalize(startingCorner);

		computeLoadingCoordinates(bx, by);
		computeLoadingLocalizationCoords(bx, by);
		computeFiringCoordinates(d1, bx, goalX, goalY);

		while (true) {
			/* BALL LOADING SEQUENCE */

			// navigate to loading tile
			myNav.navigateTo(loadingTileX, loadingTileY);

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

			// localize again (pushing the button fucks it up)
			myNav.travelTo(loadingLocalizationX, loadingLocalizationY);
			LightLocalizer.doLocalization(myOdometer, myNav, centerSensor,
					leftMotor, rightMotor, loadingLocalizationX,
					loadingLocalizationY);

			// make sure the odometer has been set properly
			myOdometer.setX(loadingLocalizationX);
			myOdometer.setY(loadingLocalizationY);
			myOdometer.setTheta(90.0);

			// localize again (pushing the button fucks it up)
			myNav.travelTo(loadingLocalizationX, loadingLocalizationY);
			LightLocalizer.doLocalization(myOdometer, myNav, centerSensor,
					leftMotor, rightMotor, loadingLocalizationX,
					loadingLocalizationY);

			// make sure the odometer has been set properly
			myOdometer.setX(loadingLocalizationX);
			myOdometer.setY(loadingLocalizationY);
			myOdometer.setTheta(90.0);

			/* FIRING SEQUENCE LEFT OR RIGHT DEPENDING ON bx */

			// navigate to firing area
			myNav.navigateTo(firingCoordsX, firingCoordsY);
			myNav.travelTo(firingPosX, firingPosY);

			// localize before shooting
			LightLocalizer.doLocalization(myOdometer, myNav, centerSensor,
					leftMotor, rightMotor, leftPosX, leftPosY);
			// make sure the odometer has been set properly
			myOdometer.setX(firingPosX);
			myOdometer.setY(firingPosY);
			myOdometer.setTheta(90.0);
			myNav.turnTo(firingAngle, true);

			shootFiveBallsCenter();

			// localize after shooting
			LightLocalizer.doLocalization(myOdometer, myNav, centerSensor,
					leftMotor, rightMotor, leftPosX, leftPosY);
			// make sure the odometer has been set properly
			myOdometer.setX(firingPosX);
			myOdometer.setY(firingPosY);
			myOdometer.setTheta(90.0);

			/* RETURN HOME */
			// returnHome(startingCorner);

		}
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
			preciseLoadingX = bx * 30 + DISTANCE_TO_WALL;
			preciseLoadingY = by * 30 + CENTER_TO_RAMP;
			loadingHeading = 180;

			loadingTileX = -15;
			loadingTileY = (by * 30) - 15;
		}

		if (by == -1) { // southern wall
			preciseLoadingX = bx * 30 - CENTER_TO_RAMP;
			preciseLoadingY = by * 30 + DISTANCE_TO_WALL;
			loadingHeading = 270;

			loadingTileY = -15;
			loadingTileX = (bx * 30) - 15;
		}

		if (bx == 11) { // eastern wall
			preciseLoadingX = bx * 30 - DISTANCE_TO_WALL;
			preciseLoadingY = by * 30 - CENTER_TO_RAMP;

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
		myPilot.travel(4.5);
		Delay.msDelay(40000);
		myPilot.travel(-10);
	}

	private void shootFiveBallsCenter() {
		for (int i = 0; i < 6; i++) {
			myCatapult.carry();
			myCatapult.shootCenter();
		}
	}

	private void computeFiringCoordinates(int d1, int bx, int goalX, int goalY) {

		if (d1 == 8) {
			leftFiringX = 45;
			leftFiringY = 45;
			leftFiringAngle = 0;

			rightFiringX = 255;
			rightFiringY = 45;
			rightFiringAngle = 0;
		} else if (d1 == 7) {
			leftFiringX = 45;
			leftFiringY = 75;
			leftFiringAngle = 0;

			rightFiringX = 255;
			rightFiringY = 75;
			rightFiringAngle = 0;

		} else if (d1 < 7) {
			leftFiringX = 45;
			leftFiringY = 105;
			leftFiringAngle = 0;

			rightFiringX = 255;
			rightFiringY = 105;
			rightFiringAngle = 0;
		}

		leftPosX = leftFiringX + 15;
		leftPosY = leftFiringY - 15;
		rightPosX = rightFiringX - 15;
		rightPosY = rightFiringY - 15;

		/* IF BALL DISPENSER IS ON THE LEFT SIDE SHOOT FROM THE LEFT */
		if (bx < 5) {
			firingCoordsX = leftFiringX;
			firingCoordsY = leftFiringY;
			firingAngle = leftFiringAngle;
		}
		/* IF BALL DISPENSER IS ON THE RIGHT SIDE SHOOT FROM THE RIGHT */
		else {
			firingCoordsX = rightFiringX;
			firingCoordsY = rightFiringY;
			firingAngle = rightFiringAngle;
		}
	}

	public void returnHome(StartCorner startingCorner) {
		if (startingCorner == StartCorner.BOTTOM_LEFT) {
			myNav.navigateTo(15.0, 15.0);
			myNav.turnTo(90.0, true);
		} else if (startingCorner == StartCorner.BOTTOM_RIGHT) {
			myNav.navigateTo(285.0, 15.0);
			myNav.turnTo(90.0, true);
		} else if (startingCorner == StartCorner.TOP_RIGHT) {
			myNav.navigateTo(285.0, 285.0);
			myNav.turnTo(270.0, true);
		} else if (startingCorner == StartCorner.TOP_LEFT) {
			myNav.navigateTo(15.0, 285.0);
			myNav.turnTo(270.0, true);
		}
	}
}
