package odometry;

import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.util.Delay;

public class RightAngleCorrection extends Thread {
	private static final long CORRECTION_PERIOD = 800;
	private static final int FORWARD_SPEED = 175;
	private Odometer myOdometer;
	private LightSensor leftSensor, rightSensor;
	private NXTRegulatedMotor leftMotor, rightMotor;

	private double sensorDistance = 18.5; // distance measured between the light sensor's position
	
	double initialAngle, initialX, initialY; // coordinates when first line is detected
	double secondAngle, secondX, secondY;  // coordinates when second line is detected
	double actualAngle, actualX, actualY;  // actual robot coordinates
	double deltaPosition, deltaX,deltaY; // change in robot position
	
	boolean leftLineDetected = false;
	boolean rightLineDetected = false;
	boolean leftLineFirst = false;
	int valL;
	int valR;
	int preValL;
	int preValR;
	
	/**
	 * Constructor for angle correction
	 * @param odo - the myOdometer
	 * @param leftSensor - left light sensor
	 * @param rightSensor - right light sensor
	 * @param leftMotor - left motor
	 * @param rightMotor - right motor
	 */
	
	public RightAngleCorrection(Odometer odometer, LightSensor leftSensor, LightSensor rightSensor, NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor){
		this.myOdometer = odometer;
		this.leftSensor = leftSensor;
		this.rightSensor = rightSensor;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		leftSensor.setFloodlight(true);
		rightSensor.setFloodlight(true);
	}
	
	public void run() {
		
		while (true){
			
			try {Thread.sleep(25);} catch (InterruptedException e) {} // important to limit resources from the CPU

			// activate angle correction only when the robot is moving in a straight line
			while (leftMotor.getSpeed()==FORWARD_SPEED && rightMotor.getSpeed()==FORWARD_SPEED) {
				
				leftLineDetected = false;
				rightLineDetected = false;
				preValL = leftSensor.getLightValue();
				preValR = rightSensor.getLightValue();
				
				while (!leftLineDetected || !rightLineDetected){
					
					valL = leftSensor.getLightValue();
					valR = rightSensor.getLightValue();
					
					if ((valL-preValL) > 8){
						leftLineDetected = true;
						Sound.beep();
						if(!rightLineDetected){
							leftLineFirst = true;
							initialAngle = myOdometer.getAng();
							initialX = myOdometer.getX();
							initialY = myOdometer.getY();
						}
						else{
						     secondAngle = myOdometer.getAng();
						     secondX = myOdometer.getX();
						     secondY = myOdometer.getY();
						}
					}
					
					if ((valR-preValR) > 8){
						rightLineDetected = true;
						Sound.beep();
						if(!leftLineDetected){
							initialAngle = myOdometer.getAng();
							initialX = myOdometer.getX();
							initialY = myOdometer.getY();
						}
						else{
						    secondAngle = myOdometer.getAng();
						    secondX = myOdometer.getX();
						    secondY = myOdometer.getY();
						}
					}
					
					preValL = valL;
					preValR = valR;
					Delay.msDelay(25); // delay is necessary for interval between val and preVal long enough
				}	
				
				if (leftLineDetected && rightLineDetected){ // correct the angle and restart loop if both lines are detected
					correct(); 
				}
			}
		}
	}

	private void correct(){
		 
		if (350 < myOdometer.getAng() && myOdometer.getAng() < 10){
			actualAngle = correctedTheta();
		}
		else if (80 < myOdometer.getAng() && myOdometer.getAng() < 100){
			actualAngle = 90 + correctedTheta();
		}
		else if(170 < myOdometer.getAng() && myOdometer.getAng() < 190){
			actualAngle = 180 + correctedTheta();
		}
		else if(260 < myOdometer.getAng() && myOdometer.getAng() < 280){
			actualAngle = 270 + correctedTheta();
		}
		  
		// check sensors detected same line
		if((myOdometer.getAng()-10) < actualAngle && actualAngle < myOdometer.getAng()+10){
			
			myOdometer.setTheta(actualAngle); // correct the angle
			Sound.beepSequence();
			  
			// reset the sequence
			valL = leftSensor.readValue();
			preValL = leftSensor.readValue();
			valR = rightSensor.readValue();
			preValR = rightSensor.readValue();
			this.leftLineDetected = false;
			this.rightLineDetected = false;
			this.leftLineFirst = false;

			try {Thread.sleep(CORRECTION_PERIOD);} catch (InterruptedException e) {} // correction once every period
		}
		
	}
	
	/**
	  * Input positions and return the corrected angle
	  * @return corrected angle
	  */
	 private double correctedTheta(){
		 deltaX = secondX-initialX;
		 deltaY = secondY-initialY;
		 deltaPosition = Math.sqrt(Math.pow(deltaX,2)+ Math.pow(deltaY, 2)); // calculate change in robots position, relative to change in both X,Y coordinates
	
		 actualAngle = Math.toDegrees(Math.atan2(deltaPosition,sensorDistance)); // compute the actual angle in degrees
		   
		 if (!leftLineFirst){ // If right sensor detects first
			 actualAngle = (-actualAngle+360);
		 }
		  
		 return actualAngle;
	 }
}
