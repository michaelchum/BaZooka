 package odometry;

import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import lejos.util.Delay;


/**
 * Retrieve readings from a light sensor and confirms when a line has been detected. A separate thread is created in order to maximize detection during OdometryAngleCorrection
 * 
 * @author Michael
 *
 */
public class LightData extends Thread{
	
	private static final long CORRECTION_PERIOD = 10;
	private LightSensor ls;			
	public boolean isLine = false;								
	
	/**
	 * Constructor
	 * Pass in light sensor with default 10ms sleep time (optimal)
	 * @param ls - the light sensor to use
	 */
	public LightData(LightSensor ls){	
		this.ls =ls;
	}
	
	/**
	 * Run the scanning thread
	 */
		public void run() {
		long correctionStart, correctionEnd;
		while (true) {
			correctionStart = System.currentTimeMillis();
			
			int lightValue = ls.getLightValue();
			Delay.msDelay(17);
			int a = ls.getLightValue();
			Delay.msDelay(17);
			int b = ls.getLightValue();
			Delay.msDelay(17);
			int c = ls.getLightValue();
			Delay.msDelay(17); 
			int d = ls.getLightValue();
			
			int delta1 = lightValue-a;
			int delta2 = lightValue-b;
			int delta3 = lightValue-c;
			int delta4 = lightValue-d;

			// beeps when sensor detects line, 
			if (delta1 < 2 && (delta2 > 2)||(delta3 > 2)||(delta4 > 2)){
				Sound.beep(); 
				isLine = true;
			}
			
			// this ensure the odometry correc.tion occurs only once every period
			correctionEnd = System.currentTimeMillis();
			if (correctionEnd - correctionStart < CORRECTION_PERIOD) {
				try {
					Thread.sleep(CORRECTION_PERIOD
							- (correctionEnd - correctionStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometry correction will be
					// interrupted by another thread
				}
			}
		}
	}
	

	/**
	 * 
	 * Mutator Variable controlling whether a line is detected or not
	 * @param isLine Set true or false (if there is a line)
	 */
	public void setIsLine(boolean isLine){
		this.isLine = isLine;
	}
	
	/**
	 * Accessor Variable controlling whether a line is detected or not
	 * @return True if line detected, false otherwise
	 */
	public boolean getIsLine(){
		return isLine;
	}
	

}
