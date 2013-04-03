package localization;

import navigation.Navigator;
import odometry.Odometer;

import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.NXTRegulatedMotor;

public class USLocalizer {
	// slower rotation for less slip and more accuracy
	public static double ROTATION_SPEED = 15;
	private double leftRadius = 2.69, rightRadius = 2.69, width = 16.32;
	private double rotationSpeed, forwardSpeed;
	private Odometer myOdometer;
	private Navigator myNav;
	private UltrasonicSensor myUSSensor;
	private NXTRegulatedMotor leftMotor, rightMotor;
	
	int distance;
	// the rudimentary filter from lab 1 wall following has been implemented with lower frequency in order to filter high noise form us sensor
	int FILTER_OUT = 15;
	int filterControl = 0;
	

	public USLocalizer(Odometer odometer, Navigator nav, UltrasonicSensor USSensor, NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
		this.myOdometer = odometer;
		this.myUSSensor = USSensor;
		this.myNav = nav;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
	}
	
	public static void doFallingEdgeLocalization(Odometer odometer, Navigator nav, UltrasonicSensor USSensor, NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
			USLocalizer temp = new USLocalizer(odometer, nav, USSensor, leftMotor, rightMotor);
			temp.doFallingEdgeLocalization();
		}
	
	public void doFallingEdgeLocalization() {
		myOdometer.setPosition(new double []{0.0, 0.0, 0.0}, new boolean []{true, true, true});
		//position array
		double [] pos = new double [3];
		// angle alpha and beta as in tutorial
		double angleA, angleB;
		double angleActual;
		double initialDist = getFilteredData();
		try { Thread.sleep(1000); } catch (InterruptedException e) {}
		
			if ( initialDist < 255 ){
				// rotate the robot until it sees no wall
				setRotationSpeed(ROTATION_SPEED);
				while(getFilteredData() < 255) {}
				Sound.beep();
				myOdometer.setPosition(new double []{0.0, 0.0, 0.0}, new boolean []{true, true, true});
				leftMotor.stop(); 
				rightMotor.stop();
				try { Thread.sleep(1500); } catch (InterruptedException e) {}
			}
			
			// keep rotating until the robot sees a wall, then latch the angle, this is the first falling edge angleA
			// after some sampling and testing we determined that the wall is about at a distance between 33 and 35 makes the readings accurate
			
			setRotationSpeed(ROTATION_SPEED);
			// sleep this thread for one second in order to avoid double data when sensor is too fast
			try { Thread.sleep(1000); } catch (InterruptedException e) {}
			// we've determined that the us sensor jumps to around ~33-35 when encountering the wall
			while(getFilteredData() < 80) {}
			while(getFilteredData() > 20) {}
			Sound.beep();
			myOdometer.getPosition(pos);
			angleA = pos[2];
			leftMotor.stop();
			rightMotor.stop();
			try { Thread.sleep(1500); } catch (InterruptedException e) {}
			
			// switch direction and wait until it sees no wall
			// keep rotating until the robot sees a wall, then latch the angle, this is the second falling edge angleB
			
			setRotationSpeed(-ROTATION_SPEED);
			// sleep this thread for one second in order to avoid double data take in a row
			try { Thread.sleep(1000); } catch (InterruptedException e) {}
			while(getFilteredData() < 80) {}
			while(getFilteredData() > 20) {}
			Sound.playTone(1000,150);
			myOdometer.getPosition(pos);
			angleB = pos[2];
			leftMotor.stop();
			rightMotor.stop();
			try { Thread.sleep(1500); } catch (InterruptedException e) {}
			
			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'
			// formulas in the tutorial didn't work well, we derived our own
			
			if ( angleA < angleB ) {
				angleActual = ((angleA + angleB)/2) + 135;
			}
			
			else { 
				angleActual = ((angleA + angleB)/2) - 45; 
			}
			
			// update the odometer's position (example to follow:)
			myNav.turnTo(angleActual, true);
			myOdometer.setPosition(new double [] {0.0, 0.0, 90.0}, new boolean [] {true, true, true});
			Sound.playTone(1000,150);
	}
	
	public void setRotationSpeed(double speed) {
		rotationSpeed = speed;
		setSpeeds(forwardSpeed, rotationSpeed);
	}
	
	public void setSpeeds(double forwardSpeed, double rotationalSpeed) {
		double leftSpeed, rightSpeed;

		this.forwardSpeed = forwardSpeed;
		this.rotationSpeed = rotationalSpeed; 

		leftSpeed = (forwardSpeed + rotationalSpeed * width * Math.PI / 360.0) *
				180.0 / (leftRadius * Math.PI);
		rightSpeed = (forwardSpeed - rotationalSpeed * width * Math.PI / 360.0) *
				180.0 / (rightRadius * Math.PI);

		// set motor directions
		if (leftSpeed > 0.0)
			leftMotor.forward();
		else {
			leftMotor.backward();
			leftSpeed = -leftSpeed;
		}
		
		if (rightSpeed > 0.0)
			rightMotor.forward();
		else {
			rightMotor.backward();
			rightSpeed = -rightSpeed;
		}
		
		// set motor speeds
		if (leftSpeed > 900.0)
			leftMotor.setSpeed(900);
		else
			leftMotor.setSpeed((int)leftSpeed);
		
		if (rightSpeed > 900.0)
			rightMotor.setSpeed(900);
		else
			rightMotor.setSpeed((int)rightSpeed);
	}
	
	private int getFilteredData() {
		// do a ping
		myUSSensor.ping();
		
		// wait for the ping to complete
		try { Thread.sleep(100); } catch (InterruptedException e) {}
		
		// there will be a delay here
		distance = myUSSensor.getDistance();
		
		//Rudimentary filter from wall following lab
		if (distance == 255 && filterControl < FILTER_OUT) {
			// bad value, do not set the distance variable, however do increment the filter value
			filterControl ++;
		} else if (distance == 255){
			// true 255, therefore set distance to 255
		} else {
			// distance went below 255, therefore reset everything.
			filterControl = 0;
		}
		return distance;
	}

}
