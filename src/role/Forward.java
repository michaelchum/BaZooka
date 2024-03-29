	package role;

import odometry.LCDInfo;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import localization.LightLocalizer;
import bluetooth.StartCorner;
import odometry.OdometryCorrection;

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
	 * Constructor Same as Robot constructor
	 * 
	 * @param catapultMotor
	 * @param leftMotor
	 * @param rightMotor
	 * @param leftLightPort
	 * @param centerLightPort
	 * @param rightLightPort
	 * @param USPort
	 * 
	 * 
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
		
		OdometryCorrection myOdometryCorrection = new OdometryCorrection(myOdometer, centerSensor, leftMotor, rightMotor);
		myOdometryCorrection.start();
	
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
			
			myNav.travelTo(loadingTileX, loadingTileY);

			/* FIRING SEQUENCE LEFT OR RIGHT DEPENDING ON bx */

			// navigate to firing area
			myNav.navigateTo(firingCoordsX, firingCoordsY);
			myNav.travelTo(firingPosX, firingPosY);

			// localize before shooting
			LightLocalizer.doLocalization(myOdometer, myNav, centerSensor,
					leftMotor, rightMotor, firingPosX, firingPosY);
			// make sure the odometer has been set properly
			myOdometer.setX(firingPosX);
			myOdometer.setY(firingPosY);
			myOdometer.setTheta(90.0);
			myNav.turnTo(firingAngle, true);
			
			myNav.goForward(23.0);

			shootFiveBallsCenter();
			
			myNav.travelTo(firingPosX, firingPosY);

			// localize after shooting
			LightLocalizer.doLocalization(myOdometer, myNav, centerSensor,
					leftMotor, rightMotor, firingPosX, firingPosY);
			// make sure the odometer has been set properly
			myOdometer.setX(firingPosX);
			myOdometer.setY(firingPosY);
			myOdometer.setTheta(90.0);

			/* RETURN HOME */
			returnHome(startingCorner);

		}

	/**
	 * Compute loading coordinates, values change according to the location of the ball dispenser
	 * 
	 * @param bx Coordinate of ball dispenser in tiles
	 * @param by Coordinate of ball dispenser in tiles
	 */
	private void computeLoadingCoordinates(int bx, int by) {
		if (bx == -1) { // western wall
			preciseLoadingX = bx * 30 + DISTANCE_TO_WALL;
			preciseLoadingY = by * 30 + CENTER_TO_RAMP;
			loadingHeading = 180;

			loadingTileX = 15;
			loadingTileY = (by * 30) - 15;
		}

		if (by == -1) { // southern wall
			preciseLoadingX = bx * 30 - CENTER_TO_RAMP;
			preciseLoadingY = by * 30 + DISTANCE_TO_WALL;
			loadingHeading = 270;

			loadingTileY = 15;
			loadingTileX = (bx * 30) - 15;
		}

		if (bx == 11) { // eastern wall
			preciseLoadingX = bx * 30 - DISTANCE_TO_WALL;
			preciseLoadingY = by * 30 - CENTER_TO_RAMP;

			loadingTileX = 285;
			loadingTileY = (by * 30) - 15;
			loadingHeading = 0;
		}
	}

	/**
	 * Compute loading localization coordinates
	 * 
	 * @param bx X coordinate of ball dispenser in tiles
	 * @param by Y coordinate of ball dispenser in tiles
	 * 
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

	
	/**
	 * Load five balls when directly in front of ball dispenser
	 */
	private void loadFiveBalls() {
		DifferentialPilot myPilot = new DifferentialPilot(5.36, 5.36, 16.32,
				leftMotor, rightMotor, false);
		myPilot.setTravelSpeed(5);
		myPilot.travel(6.0);
		Delay.msDelay(40000);
		myPilot.travel(-11);
	}

	/**
	 * Catapult five balls towards the basket
	 */
	private void shootFiveBallsCenter() {
		for (int i = 0; i < 6; i++) {
			myCatapult.carry();
			myCatapult.shootCenter();
		}
	}

/**
 * The compute the coordinates on which the robot will navigate to according to d1 and the coordinates of the ball dispenser
 * @param d1 Length of the forward line in tiles
 * @param bx X coordinate of the dispenser
 * @param goalX X coordinate of the goal
 * @param goalY Y coordinate of the goal
 */
private void computeFiringCoordinates(int d1, int bx, int goalX, int goalY) {
		
		if(d1==8){
			leftFiringX = 135;//60,30
			leftFiringY = 45;
			leftFiringAngle = 89.0;
			
			rightFiringX = 165; //240,30
			rightFiringY = 45;
			rightFiringAngle = 89.0;
			/*
			leftFiringX = 45;//60,30
			leftFiringY = 45;
			leftFiringAngle = 0;
			
			rightFiringX = 255; //240,30
			rightFiringY = 45;
			rightFiringAngle = 107.0;
			*/
		}
		else if(d1==7){
			leftFiringX = 45;//60,60
			leftFiringY = 75;
			leftFiringAngle = 67.0;
			
			rightFiringX = 255;//240,60
			rightFiringY = 75;
			rightFiringAngle = 109.0;
			
		}
		else if(d1<7){
			leftFiringX = 45;//60,90
			leftFiringY = 105;
			leftFiringAngle = 65.0;
			
			rightFiringX = 255;//240,90
			rightFiringY = 105;
			rightFiringAngle = 112.0;
		}
		
		leftPosX = leftFiringX+15;
		leftPosY = leftFiringY-15;
		rightPosX = rightFiringX-15;
		rightPosY = rightFiringY-15;
		
		/* IF BALL DISPENSER IS ON THE LEFT SIDE SHOOT FROM THE LEFT */
		if(bx<5){
			firingCoordsX = leftFiringX;
			firingCoordsY = leftFiringY;
			firingAngle = leftFiringAngle;
			firingPosX = leftPosX;
			firingPosY = leftPosY;
		}
		/* IF BALL DISPENSER IS ON THE RIGHT SIDE SHOOT FROM THE RIGHT */
		else{
			firingCoordsX = rightFiringX;
			firingCoordsY = rightFiringY;
			firingAngle = rightFiringAngle;
			firingPosX = rightPosX;
			firingPosY = rightPosY;
		}
	}

	/**
	 * Navigate back to starting corner
	 */
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
