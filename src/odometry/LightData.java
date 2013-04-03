 package odometry;

import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import lejos.util.TimerListener;
import lejos.util.Timer;

public class LightData implements TimerListener{
	
	private LightSensor ls;			
	private Timer timer;										//timer for timedOut()
	private int sleepTime = 42; 								//50 millisecond is optimal time for ls reading sleep
	private boolean isLine = false;								//boolean for line detection
	private int lsValue;
	private int val;
	private int preVal;

	/**
	 * @param args
	 */
	
	/**
	 * constructor
	 * pass in light sensor w/ default 50 sleep time (optimal)
	 * @param ls - the light sensor to use
	 */
	public LightData(LightSensor ls){	
		this.ls =ls;
		this.timer = new Timer(sleepTime,this);
		preVal = getLSData();
	}
	

	/**
	 * constructor
	 * pass in light sensor & user defined sleep time
	 * @param ls - the light sensor to use
	 * @param sleepTime - can choose the sleep time
	 */
	public LightData(LightSensor ls, int sleepTime){	
		this.ls =ls;
		this.sleepTime = sleepTime;
		this.timer = new Timer(this.sleepTime,this);
		setIsLine(false);
		preVal = getLSData();
	}
	
	/**
	 * method to be repeated, gets the light readings once every (sleepTime - constant/or user chosen)
	 * then sets isLine to true if sees line.
	 */
	public void timedOut() {
		val = getLSData();
		if ((val-preVal) > 8) {
			setIsLine(true);
			Sound.beep();
		}
		preVal = val;
	}

	/**
	 * starts the timer
	 */
	public void start(){
		timer.start();
		setIsLine(false);
	}
	
	/**
	 * stop the timer
	 */
	public void stop(){
		timer.stop();
	}
	
	/**
	 * Will read the values of the light sensor
	 * @return lsValue
	 */
	public int getLSData(){
		lsValue = ls.getNormalizedLightValue();
		return lsValue;
	}
	
	/**
	 * set true or false (if there is a line)
	 * @param isLine
	 */
	public void setIsLine(boolean isLine){
		this.isLine = isLine;
	}
	
	/**
	 * 
	 * @return true if line detected, false otherwise
	 */
	public boolean getIsLine(){
		return isLine;
	}
	

}
