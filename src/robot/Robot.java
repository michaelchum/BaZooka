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
 * @author Michael
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
		myCatapult = new Catapult(catapultMotor);
		myOdometer = new Odometer(leftMotor, rightMotor, 20, true);
		myMap = new Map(myOdometer, 30.00);
		USSensor = new UltrasonicSensor(USPort);
		myNav = new Navigator(myOdometer, myMap, USSensor);
		centerSensor = new LightSensor(centerLightPort);
		leftSensor = new LightSensor(leftLightPort);
		rightSensor = new LightSensor(rightLightPort);
	}

	public abstract void play();
}
