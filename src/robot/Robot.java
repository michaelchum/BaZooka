package robot;

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
 * @author Clark
 * 
 */

public abstract class Robot {

	protected Catapult myCatapult;
	protected Navigator myNav;
	protected Map myMap;
	protected Odometer myOdo;
	protected UltrasonicSensor USSensor;
	protected LightSensor leftSensor, centerSensor, rightSensor;
	protected NXTRegulatedMotor leftMotor, rightMotor;

/**
 * Constructor
 * @param catapultMotor
 * @param leftMotor
 * @param rightMotor
 * @param leftLightPort
 * @param centerLightPort
 * @param rightLightPort
 * @param USPort
 */
	public Robot(NXTRegulatedMotor catapultMotor, NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor, SensorPort leftLightPort, SensorPort centerLightPort, SensorPort rightLightPort, SensorPort USPort) {
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		USSensor = new UltrasonicSensor(USPort);
		centerSensor = new LightSensor(centerLightPort);
		leftSensor = new LightSensor(leftLightPort);
		rightSensor = new LightSensor(rightLightPort);
		myCatapult = new Catapult(catapultMotor);
		myOdo = new Odometer(leftMotor, rightMotor, 20, true);
		myMap = new Map(myOdo, 30.00);
		myNav = new Navigator(myOdo, myMap, USSensor);
	}

	public abstract void play();
}
