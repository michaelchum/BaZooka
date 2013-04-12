package localization;

import navigation.Navigator;
import odometry.Odometer;
import lejos.nxt.LCD;

import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * Class that defines how to localize the robot using the ultrasonic sensor
 * 
 * @author Team 13
 * 
 */
public class USLocalizer {

	private Odometer myOdometer;
	private UltrasonicSensor mySensor;
	private Navigator myNav;
	RegulatedMotor myLeftMotor;
	RegulatedMotor myRightMotor;
	
	private static final int ROTATION_SPEED = 30;
	public static final int CLOCKWISE_ROTATION = -200, COUNTER_CLOCKWISE_ROTATION = 200;

	/**
	 * Constructor
	 * 
	 * @param sensor The middle ultrasonic sensor used for localization
	 * @param navigator The navigator used for displacements
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
	 * @param sensor The UltrasonicSensor used for localization
	 * @param navigator The Navigator used for rotation
	 */
	public static void doFallingEdgeLocalization(Odometer odometer,
			UltrasonicSensor sensor, Navigator navigator,
			RegulatedMotor leftMotor, RegulatedMotor rightMotor) {
		USLocalizer temp = new USLocalizer(odometer, sensor, navigator,
				leftMotor, rightMotor);
		temp.doFallingEdgeLocalization();

	}

	/**
	 * Convenience method for falling edge localization
	 * 
	 * @param sensor The UltrasonicSensor used for localization
	 * @param navigator The Navigator used for rotation
	 */
	public static void doFallingEdgeLocalization(Odometer odometer,
			UltrasonicSensor sensor, Navigator navigator,
			RegulatedMotor leftMotor, RegulatedMotor rightMotor,
			int rotationTarget) {
		USLocalizer temp = new USLocalizer(odometer, sensor, navigator,
				leftMotor, rightMotor);
		temp.doFallingEdgeLocalization(rotationTarget);

	}

	/**
	 * Perform a falling edge localization and updates the odometer
	 */
	public void doFallingEdgeLocalization() {

		// rotate clockwise until 255
		if (getFilteredData() < 255) {
			myLeftMotor.setSpeed((int) ROTATION_SPEED);
			myRightMotor.setSpeed((int) ROTATION_SPEED);

			myLeftMotor.forward();
			myRightMotor.backward();

		}
		while (getFilteredData() < 255) {
			
		}
		myLeftMotor.stop();
		myRightMotor.stop();


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

	}

	/**
	 * Perform a falling edge localization, rotating to the specified angle, and updates the odometer
	 * 
	 * @param rotationTarget The specified final rotation angle where the robot will stop after localizing
	 * 
	 */
	public void doFallingEdgeLocalization(int rotationTarget) {

		// rotate the robot until it sees no wall
		if (getFilteredData() < 33) {
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

		myNav.turnTo(rotationTarget, true);

		// if it fucks up, fix it
		if (!isReasonable(rotationTarget)) {
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

	}

	/**
	 * Rotates in the specified direction until no wall is not visible
	 * 
	 * @param direction The rotation speed which also provides the direction
	 * @return The current odometer theta reading after rotation has completed
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

		while (usDist < 255) {
			usDist = getFilteredData();
			LCD.clear(3);
			LCD.drawString("US: " + usDist, 0, 3);

		}

		Sound.beep();

		return myOdometer.getAng();
	}

	/**
	 * Rotates in the specified direction until a wall is visible
	 * 
	 * @param direction The rotation speed which also provides the direction
	 * @return The current odometer theta reading after a wall is detected and rotation stopped
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

		while (usDist > 33) {
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
	
	/**
	 * Compute the final angle where the robot is accurately pointing with respect to the floor
	 * 
	 * @param angleA The first angle latched
	 * @param angleB The second angle latched
	 * @return The actual angle the robot is facing with respect to the perpendicular walls
	 *            
	 */
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
	 * Checks to see if it can see a wall or not and compares it to the targeted angle
	 * 
	 * @param angle The angle to check
	 * @return Whether or not its current measured orientation is consistent with the walls' position
	 * 
	 */
	private boolean isReasonable(int angle) {
		if (angle >= 0 && angle <= 90 && getFilteredData() < 100) {
			return false;
		}
		return true;
	}

	/**
	 * Waits 50 ms between each reading in attempt to filter sudden false positives and false negatives
	 * 
	 * @return The filtered reading from the ultrasonic sensor
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
	
	/**
	 * Pause the thread for a specified interval of time
	 * 
	 * @param milliseconds Amount of time to pause in milliseconds
	 */
	private void pause(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}