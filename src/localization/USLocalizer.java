/**
 *
 */
package localization;

import navigation.Navigator;
import odometry.Odometer;
import lejos.nxt.LCD;

import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * Class that defines how to localize the robot using ultrasonic sensors
 * 
 * @author Team 13
 * 
 */
public class USLocalizer {

	private UltrasonicSensor mySensor;
	private Odometer myOdometer;
	
	private Navigator myNav;
	private static final int ROTATION_SPEED = 30;

	// public final int myRotationTarget;
	public static final int CLOCKWISE_ROTATION = -200,
			COUNTER_CLOCKWISE_ROTATION = 200;

	RegulatedMotor myLeftMotor;
	RegulatedMotor myRightMotor;

	// private boolean timedOut = false;

	/**
	 * Constructor
	 * 
	 * @param sensor
	 * @param navigator
	 */
	public USLocalizer(Odometer odometer, UltrasonicSensor sensor,
			Navigator navigator, RegulatedMotor leftMotor,
			RegulatedMotor rightMotor) {
		mySensor = sensor;
		myNav = navigator;
		myOdometer = odometer;
		myLeftMotor = leftMotor;
		myRightMotor = rightMotor;
		

	}

	/**
	 * Convenience method for falling edge localization
	 * 
	 * @param sensor
	 *            - the UltrasonicSensor used
	 * @param navigator
	 *            - the Navigator
	 */
	public static void doFallingEdgeLocalization(Odometer odometer,
			UltrasonicSensor sensor, Navigator navigator,
			RegulatedMotor leftMotor, RegulatedMotor rightMotor) {
		USLocalizer temp = new USLocalizer(odometer, sensor, navigator,
				leftMotor, rightMotor);
		temp.doFallingEdgeLocalization();

	}

	/**
	 * Does a falling edge localization and updates the odometer
	 */
	public void doFallingEdgeLocalization() {

		// rotate the robot until it sees no wall

		if (getFilteredData() < 50) {
			rotateTilWallIsNotVisible(CLOCKWISE_ROTATION);
		}

		// keep rotating until the robot sees a wall, then latch the angle
		double angleA = rotateTilWallIsVisible(CLOCKWISE_ROTATION);
		// switch direction and wait until it sees no wall
		rotateTilWallIsNotVisible(COUNTER_CLOCKWISE_ROTATION);
		// keep rotating until the robot sees a wall, then latch the angle
		double angleB = rotateTilWallIsVisible(COUNTER_CLOCKWISE_ROTATION);
		// angleA is clockwise from angleB, so assume the average of the
		// angles to the right of angleB is 45 degrees past 'north'
		double dTheta = computeDeltaTheta(angleA, angleB);

		// update the odometer position (example to follow:)
		myOdometer.setTheta(myOdometer.getAng() + dTheta);
		LCD.drawString("Turning to 45", 0, 1);

		myNav.turnTo(45, true);

		// if it fucks up, fix it
		if (!isReasonable(45)) {
			double theta = myOdometer.getAng();
			DifferentialPilot myPilot = new DifferentialPilot(5.36, 5.36,
					16.32, myLeftMotor, myRightMotor, false);
			myPilot.setRotateSpeed(30);
			myPilot.rotate(180);
			myOdometer.setTheta(theta);
		}

		LCD.drawString("x: " + myOdometer.getX(), 0, 5);
		LCD.drawString("y: " + myOdometer.getY(), 0, 6);
		// LCD.drawString("Theta: " + myOdometer.getTheta(),0, 7);

		pause(1000);

		LCD.clear(0);
		LCD.drawString("Theta: " + myOdometer.getAng(), 0, 7);

	}

	/**
	 * Rotates in the specified direction until the wall is not visible
	 * 
	 * @param direction
	 * @return The current odometer theta reading after rotation is complete
	 */
	private double rotateTilWallIsNotVisible(int direction) {
		pause(1000);

		myLeftMotor.setSpeed((int) ROTATION_SPEED);
		myRightMotor.setSpeed((int) ROTATION_SPEED);

		if (direction == CLOCKWISE_ROTATION) {
			myLeftMotor.forward();
			myRightMotor.backward();
		} else {
			myLeftMotor.backward();
			myRightMotor.forward();
		}

		LCD.drawString("TilNotVisible" + Integer.toString(direction), 0, 7);

		int usDist = getFilteredData();
		LCD.clear(3);
		LCD.drawString("US: " + usDist, 0, 3);

		while (usDist < 50) {
			usDist = getFilteredData();
			LCD.clear(3);
			LCD.drawString("US: " + usDist, 0, 3);

		}

		Sound.beep();

		return myOdometer.getAng();
	}

	/**
	 * Rotates in the specified direction until the wall is visible
	 * 
	 * @param direction
	 * @return The current odometer theta reading after rotation is complete
	 */
	private double rotateTilWallIsVisible(int direction) {
		myLeftMotor.setSpeed((int) ROTATION_SPEED);
		myRightMotor.setSpeed((int) ROTATION_SPEED);
		pause(1000);
		LCD.drawString("TilVisible" + Integer.toString(direction), 0, 7);
		if (direction == CLOCKWISE_ROTATION) {
			myLeftMotor.forward();
			myRightMotor.backward();
		} else {
			myLeftMotor.backward();
			myRightMotor.forward();
		}

		int usDist = getFilteredData();
		LCD.clear(3);
		LCD.drawString("US: " + usDist, 0, 3);

		while (usDist > 35) {
			usDist = getFilteredData();
			LCD.clear(3);
			LCD.drawString("US: " + usDist, 0, 3);

		}

		myLeftMotor.stop(true);
		myRightMotor.stop(false);

		Sound.beep();

		double angleA = myOdometer.getAng();
		LCD.drawString("A: " + angleA, 0, 4);
		pause(3000);
		return angleA;
	}

	private double computeDeltaTheta(double angleA, double angleB) {
		double deltaTheta = 45 - ((angleA + angleB) / 2);
		deltaTheta += 180;
		if (deltaTheta < 0) {
			deltaTheta += 360;
		}
		LCD.drawString("Theta: " + deltaTheta, 0, 6);
		return deltaTheta % 360;

	}

	/**
	 * Checks to see if it can see a wall or not and compares it to the targeted
	 * angle
	 * 
	 * @param angle
	 * @return - whether or not its current measured orientation is consistent
	 *         with whether or not it can see the wall
	 */
	private boolean isReasonable(int angle) {
		if (angle >= 0 && angle <= 90 && getFilteredData() < 100) {
			return false;
		}
		return true;
	}

	/**
	 * Waits 50 ms between each reading
	 * 
	 * @return the reading from the ultrasonic sensor
	 */
	private int getFilteredData() {
		int distance;

		// do a ping
		mySensor.ping();

		// wait for the ping to complete
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}

		// there will be a delay here
		distance = mySensor.getDistance();

		return distance;
	}

	private void pause(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}