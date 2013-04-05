package role;

import bluetooth.StartCorner;
import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

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
	}

	
	/**
	 * Virtual function detailing how the robot will play (must be overriden)
	 * @param startingCorner - the corner it starts in
	 * @param bx - x-coordinate of ball dispenser (in grid tiles, not cm)
	 * @param by - y-coordinate of ball dispenser (in grid tiles, not cm)
	 * @param w1 - closer boundary of defensive zone (in grid tiles, not cm)
	 * @param w2 - farther boundary of defensive zone (in grid tiles, not cm)
	 * @param d1 - distance from goal (in grid tiles, not cm)
	 * @param goalX - coordinates of goal (in cm)
	 * @param goalY - coordinates of goal (in cm)
	 */
	public abstract void play(StartCorner startingCorner, int bx, int by,
			int w1, int w2, int d1, int goalX, int goalY);
}
