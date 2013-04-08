package odometry;

import lejos.nxt.LCD;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.LightSensor;
import lejos.util.Timer;
import lejos.util.TimerListener;

/**
 * Constantly updates LCD with Robot info
 * @author Team 13
 *
 */
public class LCDInfo implements TimerListener{
	public static final int LCD_REFRESH = 80; // display refresh rate to be change for precision purposes
	private Odometer odo;
	private Timer lcdTimer;
	
	// display of the ultrasonic sensor distance has been implemented for testing purposes
	private UltrasonicSensor us;
	private LightSensor leftLight, middleLight, rightLight;
	private int distance;
	private int leftVal, middleVal, rightVal;
	
	// arrays for displaying data
	private double [] pos;
	
	/**
	 * Constructor
	 * @param odo
	 * @param us
	 * @param leftLight
	 * @param middleLight
	 * @param rightLight
	 */
	public LCDInfo(Odometer odo, UltrasonicSensor us, LightSensor leftLight, LightSensor middleLight, LightSensor rightLight) {
		this.odo = odo;
		this.lcdTimer = new Timer(LCD_REFRESH, this);
		this.us = us;
		this.leftLight = leftLight;
		this.middleLight = middleLight;
		this.rightLight = rightLight;
		
		// initialize the arrays for displaying data
		pos = new double [3];
		
		// start the timer
		lcdTimer.start();
	}
	
	/**
	 * Display updated info after set time interval
	 */
	public void timedOut() { 
		odo.getPosition(pos);
		distance = us.getDistance();
		leftVal = leftLight.readValue();
		middleVal = middleLight.readValue();
		rightVal = rightLight.readValue();
		
		LCD.clear();
		LCD.drawString("X: ", 0, 0);
		LCD.drawString("Y: ", 0, 1);
		LCD.drawString("H: ", 0, 2);
		LCD.drawString(formattedDoubleToString(pos[0], 2), 3, 0);
		LCD.drawString(formattedDoubleToString(pos[1], 2), 3, 1);
		LCD.drawString(formattedDoubleToString(pos[2], 2), 3, 2);
		LCD.drawString("US: ", 0, 3);
		LCD.drawInt(distance, 5, 3);
		LCD.drawString("midLight: ", 0, 4);
		LCD.drawInt(middleVal, 11, 4);
		LCD.drawString("leftLight: ", 0, 5);
		LCD.drawInt(leftVal, 12, 5);
		LCD.drawString("rightLight: ", 0, 6);
		LCD.drawInt(rightVal, 13, 6);
	}
	
	private static String formattedDoubleToString(double x, int places) { // taken from lab LAB 2 OdometryDisplay.java
		String result = "";
		String stack = "";
		long t;
		
		// put in a minus sign as needed
		if (x < 0.0)
			result += "-";
		
		// put in a leading 0
		if (-1.0 < x && x < 1.0)
			result += "0";
		else {
			t = (long)x;
			if (t < 0)
				t = -t;
			
			while (t > 0) {
				stack = Long.toString(t % 10) + stack;
				t /= 10;
			}
			result += stack;
		}
		
		// put the decimal, if needed
		if (places > 0) {
			result += ".";
		
			// put the appropriate number of decimals
			for (int i = 0; i < places; i++) {
				x = Math.abs(x);
				x = x - Math.floor(x);
				x *= 10.0;
				result += Long.toString((long)x);
			}
		}
		
		return result;
	}
}
