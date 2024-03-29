package localization;

import navigation.Navigator;
import odometry.Odometer;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

import java.lang.Math;

import bluetooth.StartCorner;

/**
 * 
 * Class that defines how to localize using the middle light sensor
 * @author Team 13
 *
 */

public class LightLocalizer {

	// tweaking of the distance between the sensor and center had to be done in
	// order to get a more precise and accurate final position for light localization
	private final float LS_TO_CENTER = 11.5f;
	public static int ROTATION_SPEED = 30;

	private Odometer myOdometer;
	private Navigator myNav;
	private LightSensor ls;
	NXTRegulatedMotor myLeftMotor, myRightMotor;
	
	int val;
	private int baseValue = 0;

	public LightLocalizer(Odometer odo, Navigator nav, LightSensor ls,
			NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
		this.myOdometer = odo;
		this.ls = ls;
		this.myNav = nav;
		this.myLeftMotor = leftMotor;
		this.myRightMotor = rightMotor;

		// turn on the light
		ls.setFloodlight(true);
	}

	/**
	 * Convenience method for localizing at 0, 0
	 * 
	 * @param odo
	 * @param nav
	 * @param ls
	 * @param leftMotor
	 * @param rightMotor
	 * @param corner
	 */
	public static void doLocalization(Odometer odo, Navigator nav,
			LightSensor ls, NXTRegulatedMotor leftMotor,
			NXTRegulatedMotor rightMotor) {
		LightLocalizer temp = new LightLocalizer(odo, nav, ls, leftMotor,
				rightMotor);
		temp.doLocalization();
	}

	
	/**
	 * Convenience method for localizing anywhere to a specific node on the map
	 * 
	 * @param odo
	 * @param nav
	 * @param ls
	 * @param leftMotor
	 * @param rightMotor
	 * @param closestX
	 * @param closestY
	 */
	public static void doLocalization(Odometer odo, Navigator nav,
			LightSensor ls, NXTRegulatedMotor leftMotor,
			NXTRegulatedMotor rightMotor, double closestX, double closestY) {
		LightLocalizer temp = 
				new LightLocalizer(odo, nav, ls, leftMotor,
				rightMotor);
		temp.doLocalization(closestX, closestY);
	}

	
	/**
	 * Convenience method for localizing at one of the four corners of the map
	 * 
	 * @param odo
	 * @param nav
	 * @param ls
	 * @param leftMotor
	 * @param rightMotor
	 * @param corner
	 */
	public static void doLocalization(Odometer odo, Navigator nav,
			LightSensor ls, NXTRegulatedMotor leftMotor,
			NXTRegulatedMotor rightMotor, StartCorner corner) {
		LightLocalizer temp = new LightLocalizer(odo, nav, ls, leftMotor,
				rightMotor);
		temp.doLocalization(corner);
	}

	/**
	 * Localizes around the origin (0,0)
	 */
	public void doLocalization() {
		
		myNav.turnTo(45, true);
		ls.setFloodlight(true);
		pause(1000);

		baseValue = ls.readValue();
		val = baseValue;

		float thetaWest = rotateTilLineDetected(0, false);
		pause(1000);
		float thetaNorth = rotateTilLineDetected(500, false);
		pause(1000);
		float thetaEast = rotateTilLineDetected(500, false);
		pause(1000);
		float thetaSouth = rotateTilLineDetected(500, true);
		pause(1000);
		float deltaX = computeCoordinate(thetaSouth, thetaNorth, LS_TO_CENTER);
		float deltaY = computeCoordinate(thetaEast, thetaWest, LS_TO_CENTER);

		myOdometer.setX(deltaX);
		myOdometer.setY(deltaY);
		myOdometer.setTheta(myOdometer.getAng()
				+ computeDeltaTheta(thetaSouth, thetaNorth));

		pause(1000);

		myNav.travelTo(0, 0);
		myNav.turnTo(90.0, true);
		//sweepForLine();
		myOdometer.setTheta(90.0);
		pause(1000);

		//ls.setFloodlight(false);
	}

	/**
	 * Localizes around any particular gridline intersection (node) on the map
	 * 
	 * @param gridX The X coordinate of the gridline intersection
	 * @param gridY The Y coordinate of the gridline intersection
	 */
	public void doLocalization(double gridX, double gridY) {
		doLocalization();
		myOdometer.setX(gridX);
		myOdometer.setY(gridY);
	}

	/**
	 * Localizes at any starting corner
	 * 
	 * @param corner Starting corner 1-4
	 */
	public void doLocalization(StartCorner corner) {
		doLocalization();

		myOdometer.setX(corner.getX() * 30.0);
		myOdometer.setY(corner.getY() * 30.0);

		double currentTheta = myOdometer.getAng();
		
		switch (corner) {
		case BOTTOM_LEFT:
			myOdometer.setTheta(currentTheta);
		case TOP_LEFT:
			myOdometer.setTheta(currentTheta + 270);
		case TOP_RIGHT:
			myOdometer.setTheta(currentTheta + 180);
		case BOTTOM_RIGHT:
			myOdometer.setTheta(currentTheta + 90);
		default:
		}
	}
	
	/**
	 * Rotate the robot until a line is detected
	 * 
	 * @param initialPause Interval of the initial pause in milliseconds
	 * @param stopOnceDone Whether to stop the motors or not when a line is detected
	 * @return The angle given by the odometer when a line is detected
	 */
	private float rotateTilLineDetected(int initialPause, boolean stopOnceDone) {
		float theta = 0;

		myLeftMotor.setSpeed(ROTATION_SPEED);
		myRightMotor.setSpeed(ROTATION_SPEED);
		myLeftMotor.forward();
		myRightMotor.backward();

		pause(initialPause);

		boolean lineDetected = false;
		while (lineDetected == false) {
			val = ls.readValue();
			Delay.msDelay(50);

			LCD.drawString("LS: ", 0, 4);
			LCD.drawString("LS: " + val, 0, 4);

			if ((baseValue - val) > 8) {
				lineDetected = true;
				theta = (float) myOdometer.getAng();
				if (stopOnceDone) {
					myLeftMotor.stop();
					myRightMotor.stop();
				}
				Sound.beep();
			}

		}

		return theta;
	}

	/**
	 * 
	 * @param theta1 Angle when the first line is detected
	 * @param theta2 Angle when the second line is detected
	 * @param d Distance between the light sensor and the center of rotation
	 * @return The exact coordinate where the robot's center of rotation is located on the map
	 */
	
	private float computeCoordinate(float theta1, float theta2, float d) {
		return (float) (-d * Math.cos(Math.toRadians(theta1 - theta2) / 2));
	}

	private float computeDeltaTheta(float theta1, float theta2) {
		return (theta1 - theta2) / 2 - theta1 + 180;
	}

	private void pause(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Rotate the robot until a line is detected and stop the motors
	 */
	private void sweepForLine() {
		DifferentialPilot myPilot = new DifferentialPilot(5.36, 5.36,
				16.32, myLeftMotor, myRightMotor, false);
		val = ls.readValue();
		myPilot.setRotateSpeed(1);
		double sweepAngle = 1;
		double direction = 1;
		double sweptSoFar = 0;
		while ((baseValue - val) < 8) {
			
			val = ls.readValue();
			myPilot.rotate(direction, false);
			sweptSoFar += direction;
			if (sweptSoFar == sweepAngle) {
				direction *= -1;
				sweepAngle *= -2;
			}
			
		}
		myPilot.stop();
		myPilot.quickStop();
	}

}
