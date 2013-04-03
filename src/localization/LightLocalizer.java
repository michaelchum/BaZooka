package localization;

import navigation.Navigator;
import odometry.Odometer;


import lejos.nxt.Sound;
import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.util.Delay;

import java.lang.Math;


public class LightLocalizer {
	
	// tweaking of the distance between the sensor and center had to be done in order to get a more precise final position for light localization
	private double LS_TO_CENTER = 15.35;
	
	private Odometer myOdometer;
	private Navigator myNav;
	private LightSensor ls;
	NXTRegulatedMotor leftMotor, rightMotor;
	int val;
	private int lineCount;
	private double thetaX, thetaY, posX, posY, deltaTheta;
	private double leftRadius = 2.69, rightRadius = 2.69, width = 16.32;
	private double rotationSpeed, forwardSpeed;
	public static double ROTATION_SPEED = -50;
	private boolean rotating;
	
	public LightLocalizer(Odometer odo, Navigator nav, LightSensor ls, NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
		this.myOdometer = odo;
		this.ls = ls;
		this.myNav = nav;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		
		// turn on the light
		ls.setFloodlight(true);
	}
	
	public static void doLocalization(Odometer odo, Navigator nav, LightSensor ls, NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
		LightLocalizer temp = new LightLocalizer(odo, nav, ls, leftMotor, rightMotor);
		temp.doLocalization();
	}
	
	public void doLocalization() {
	
		int preVal = ls.getLightValue();
		
		// pause the system before implementing lightLocalizer in order to avoid oscillations from usLocalizer
		try { Thread.sleep(1000); } catch (InterruptedException e) {}
		
		// store positions from newly implemented odometer
		double[] pos = new double[3];
		
		// store all angles determined during line detections
		double[] lineAngles = new double[4];	
	
		// drive to corner location
		//myNav.travelTo(1,1);
		myNav.turnTo(90.0, true);
		try { Thread.sleep(1000); } catch (InterruptedException e) {}
		
		// assume and set current position as 0,0 before angle detection
		myOdometer.setPosition(new double [] {0.0, 0.0, 90.0}, new boolean [] {true, true, false});
		
		// set line count to 0
		lineCount = 0;
		
		// start rotating and clock all 4 gridlines
		setRotationSpeed(ROTATION_SPEED);
		rotating = true;
		
		while (rotating) {
			try { Thread.sleep(25); } catch (InterruptedException e) {}
			
			// continuous update of the lightvalue
			val = ls.getLightValue();
			Delay.msDelay(100);
			// during our testing phase, the lightvalue was constant around ~44 and dropped way below at ~35 when reaching a line therefore we put a threshold of 40
			if ((val-preVal)> 7) {
			
				// store angle when a line is detected
				myOdometer.getPosition(pos);
				Sound.beep();
				lineAngles[lineCount] = pos[2];
				lineCount++;
				
				// sleep for a few ms in order to avoid counting a line twice
				try { Thread.sleep(450); } catch (InterruptedException e) {}
			}
			
			// stop rotation after 4 lines have been detected
			if (lineCount > 4 || lineCount == 4){
				Sound.buzz();
				myOdometer.getPosition(pos);
				rotating = false;
			}
		
			preVal = val;		
		} // end of line detection while loop
		try { Thread.sleep(1000); } catch (InterruptedException e) {}
		
		leftMotor.stop();
		rightMotor.stop();
		myNav.turnTo(90.0, true);
				
		// do trigonometry to compute (0,0) and 90 degrees
		// following the tutorial's calculations
		thetaX = lineAngles[1]-lineAngles[3];
		thetaY = lineAngles[0]-lineAngles[2];
		posX = -(Math.abs(LS_TO_CENTER*Math.cos(thetaY/2)));
		posY = -(Math.abs(LS_TO_CENTER*Math.cos(thetaX/2)));
		deltaTheta = (thetaY/2) - lineAngles[0] + 270;
		
		// set the new position in odometer
		myOdometer.setPosition(new double [] {posX, posY, 0.0}, new boolean [] {true, true, false});
		Sound.playTone(1000,150);
		
		// when done travel to (0,0)
		myNav.travelTo(0.0, 0.0);	
		myNav.turnTo(90.0, true);
		
		// set the new more accurate angle deltaTheta
		myOdometer.setPosition(new double [] {0.0, 0.0, deltaTheta}, new boolean [] {false, false, true});
		myNav.turnTo(90.0, true);
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

}
