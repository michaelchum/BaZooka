package role;

import bluetooth.StartCorner;
import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import localization.LightLocalizer;
import localization.USLocalizer;

import motion.Catapult;
import navigation.Navigator;
import navigation.Map;
import odometry.Odometer;

/**
 * Abstract robot class of a standard robot
 * 
 * @author Team 13
 * 
 */

public abstract class Robot {

	protected Catapult myCatapult;
	protected Navigator myNav;
	protected Map myMap;
	protected Odometer myOdometer;
	protected UltrasonicSensor USSensor;
	protected LightSensor leftSensor, centerSensor, rightSensor;
	protected NXTRegulatedMotor leftMotor, rightMotor;
	protected double closestX, closestY;

	/**
	 * Constructor
	 * 
	 * @param catapultMotor
	 * @param leftMotor
	 * @param rightMotor
	 * @param leftLightPort
	 * @param centerLightPort
	 * @param rightLightPort
	 * @param USPort
	 */
	public Robot(NXTRegulatedMotor catapultMotor, NXTRegulatedMotor leftMotor,
			NXTRegulatedMotor rightMotor, SensorPort leftLightPort,
			SensorPort centerLightPort, SensorPort rightLightPort,
			SensorPort USPort) {
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		USSensor = new UltrasonicSensor(USPort);
		centerSensor = new LightSensor(centerLightPort);
		leftSensor = new LightSensor(leftLightPort);
		rightSensor = new LightSensor(rightLightPort);
		myCatapult = new Catapult(catapultMotor);
		myOdometer = new Odometer(leftMotor, rightMotor, 20, true);
		myMap = new Map(myOdometer, 30.00);
		myNav = new Navigator(myOdometer, myMap, USSensor);
		leftSensor.setFloodlight(false);
		rightSensor.setFloodlight(false);
	}

	/**
	 * Virtual function detailing how the robot will play (must be overwritten)
	 * 
	 * @param startingCorner The corner the robot debuts the game
	 * @param bx The coordinate of ball dispenser (in grid tiles, not cm)
	 * @param by Y coordinate of ball dispenser (in grid tiles, not cm)
	 * @param w1 Closer boundary of defensive zone (in grid tiles, not cm)
	 * @param w2 Further boundary of defensive zone (in grid tiles, not cm)
	 * @param d1 Distance from goal (in grid tiles, not cm)
	 * @param goalX Coordinates of the goal (in cm)
	 * @param goalY Coordinates of the goal (in cm)
	 */
	public abstract void play(StartCorner startingCorner, int bx, int by,
			int w1, int w2, int d1, int goalX, int goalY);

	/**
	 * Localizes at the start of the game
	 * 
	 * @param startingCorner The starting corner of the robot
	 */
	protected void localize(StartCorner startingCorner) {
		USLocalizer.doFallingEdgeLocalization(myOdometer, USSensor, myNav,
				leftMotor, rightMotor);

		LightLocalizer.doLocalization(myOdometer, myNav, centerSensor,
				leftMotor, rightMotor, startingCorner);
	}

	/**
	 * Computer the closest intersection (node) the the robot
	 */
	protected void computeClosestIntersection() {
		double currentX = myOdometer.getX();
		double currentY = myOdometer.getY();

		if (currentX < 0) {
			closestX = 0;
		} else if (currentX > 300) {
			closestX = 300;
		} else {
			closestX = Math.round(myOdometer.getX() / 30) * 30;
		}

		if (currentY < 0) {
			closestY = 0;
		} else if (currentY > 300) {
			closestY = 300;
		} else {
			closestX = Math.round(myOdometer.getX() / 30) * 30;
		}
	}
	
	/**
	 * Adjust localization for starting corner.  Also in localization itself but this ensures change to odometer after localization persists
	 * @param startingCorner
	 */
	protected void postLocalize(StartCorner startingCorner) {
		
		double newX = startingCorner.getX() * 30;
		double newY = startingCorner.getY() * 30;
		myOdometer.setX(newX);
		myOdometer.setY(newY);
		if (startingCorner == StartCorner.BOTTOM_LEFT) {
			myOdometer.setTheta(90);
			myNav.travelTo(15.0, 15.0);
			myNav.turnTo(90.0, true);
		} else if (startingCorner == StartCorner.BOTTOM_RIGHT){
			myOdometer.setTheta(180);
			myNav.travelTo(285.0, 15.0);
			myNav.turnTo(90.0, true);
		}
		else if (startingCorner == StartCorner.TOP_RIGHT) {
			myOdometer.setTheta(270);
//			myOdometer.setPosition(new double[] { 300.0, 300.0, 270.0 },
//					new boolean[] { true, true, true });
			myNav.travelTo(285.0, 285.0);
			myNav.turnTo(270.0, true);
		}
		else if (startingCorner == StartCorner.TOP_LEFT) {
			myOdometer.setTheta(0);
//			myOdometer.setPosition(new double[] { 0.0, 300.0, 0.0 },
//					new boolean[] { true, true, true });
			myNav.travelTo(15.0, 285.0);
			myNav.turnTo(270.0, true);
		}
		

	}
}
