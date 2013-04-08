package odometry;

import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.nxt.NXTRegulatedMotor;

/**
 * Odometer class modified for our robot
 * @author Team 13
 *
 */
public class Odometer implements TimerListener {

	private Timer timer;
	private NXTRegulatedMotor leftMotor, rightMotor;
	private final int DEFAULT_TIMEOUT_PERIOD = 20;
	public double leftRadius, rightRadius, width;
	private double x, y, theta;
	private double[] oldDH, dDH;
	
	/**
	 * Constructor
	 * @param leftMotor
	 * @param rightMotor
	 * @param INTERVAL
	 * @param autostart
	 */
	public Odometer (NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor, int INTERVAL, boolean autostart) {
		
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		
		// default values, modify for your robot
		this.rightRadius = 2.68;
		this.leftRadius = 2.68;
		this.width = 16.32;
		
		this.x = 0.0;
		this.y = 0.0;
		this.theta = 90.0;
		this.oldDH = new double[2];
		this.dDH = new double[2];

		if (autostart) {
			// if the timeout interval is given as <= 0, default to 20ms timeout 
			this.timer = new Timer((INTERVAL <= 0) ? INTERVAL : DEFAULT_TIMEOUT_PERIOD, this);
			this.timer.start();
		} else
			this.timer = null;
	}
	
	/**
	 * Stop timerlistener
	 */
	public void stop() {
		if (this.timer != null)
			this.timer.stop();
	}
	/**
	 * Start timerListener
	 */
	public void start() {
		if (this.timer != null)
			this.timer.start();
	}
	

	/**
	 * Calculates displacement and heading as title suggests
	 * @param data
	 */
	private void getDisplacementAndHeading(double[] data) {
		int leftTacho, rightTacho;
		leftTacho = leftMotor.getTachoCount();
		rightTacho = rightMotor.getTachoCount();

		data[0] = (leftTacho * leftRadius + rightTacho * rightRadius) * Math.PI / 360.0;
		data[1] = (rightTacho * rightRadius - leftTacho * leftRadius) / width;
	}
	

	/**
	 * Recompute the odometer values using the displacement and heading changes
	 */
	public void timedOut() {
		this.getDisplacementAndHeading(dDH);
		dDH[0] -= oldDH[0];
		dDH[1] -= oldDH[1];

		// update the position in a critical region
		synchronized (this) {
			theta += dDH[1];
			theta = fixDegAngle(theta);

			x += dDH[0] * Math.cos(Math.toRadians(theta));
			y += dDH[0] * Math.sin(Math.toRadians(theta));
		}

		oldDH[0] += dDH[0];
		oldDH[1] += dDH[1];
	}

	/**
	 * Accessor
	 * @return current x-coordinate
	 */
	public double getX() {
		synchronized (this) {
			return x;
		}
	}

	/**
	 * Accessor
	 * @return current y-coordinate
	 */
	public double getY() {
		synchronized (this) {
			return y;
		}
	}

	/**
	 * Accessor
	 * @return current angle
	 */
	public double getAng() {
		synchronized (this) {
			return theta;
		}
	}
	
	/**
	 * Mutator
	 * @param d - the new x coordinate
	 */
	public void setX(double d) {
		synchronized (this) {
			this.x = d;
		}
	}
	
	/**
	 * Mutator
	 * @param d - the new y coordinate
	 */
	public void setY(double d) {
		synchronized (this) {
			this.y = d;
		}
	}
	
	/**
	 * Mutator
	 * @param d - the new value for theta
	 */
	public void setTheta(double d) {
		synchronized (this) {
			this.theta = d;
		}
	}

	/**
	 * Mutator
	 * @param position - array of doubles representing x, y, and theta
	 * @param update - array of booleans representing whether or not each corresponding value is mutable
	 */
	public void setPosition(double[] position, boolean[] update) {
		synchronized (this) {
			if (update[0])
				x = position[0];
			if (update[1])
				y = position[1];
			if (update[2])
				theta = position[2];
		}
	}

	/**
	 * Accessor
	 * @param position - an array of doubles to insert x, y, and theta
	 */
	public void getPosition(double[] position) {
		synchronized (this) {
			position[0] = x;
			position[1] = y;
			position[2] = theta;
		}
	}

	/**
	 * Accessor
	 * @return array of doubles representing x, y, and theta
	 */
	public double[] getPosition() {
		synchronized (this) {
			return new double[] { x, y, theta };
		}
	}
	
	/**
	 * Accessor
	 * @return array of left and right motors
	 */
	public NXTRegulatedMotor [] getMotors() {
		return new NXTRegulatedMotor[] {this.leftMotor, this.rightMotor};
	}
	
	/**
	 * Accessor
	 * @return left motors
	 */
	public NXTRegulatedMotor getLeftMotor() {
		return this.leftMotor;
	}
	
	/**
	 * Accessor
	 * @return right motor
	 */
	public NXTRegulatedMotor getRightMotor() {
		return this.rightMotor;
	}

	
	/**
	 * Fixes the angle so its between 0 and 360
	 * @param angle
	 * @return an equivalent angle between 0 and 360
	 */
	public static double fixDegAngle(double angle) {
		if (angle < 0.0)
			angle = 360.0 + (angle % 360.0);

		return angle % 360.0;
	}

	/**
	 * 
	 * @param a - angle A
	 * @param b - angle b
	 * @return the minimum angle between angle a and angle b
	 */
	public static double minimumAngleFromTo(double a, double b) {
		double d = fixDegAngle(b - a);

		if (d < 180.0)
			return d;
		else
			return d - 360.0;
	}
}
