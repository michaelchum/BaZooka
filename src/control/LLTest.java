package control;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import localization.LightLocalizer;
import navigation.Map;
import navigation.Navigator;
import odometry.Odometer;

/**
 * Light localization test
 * @author Team 13
 *
 */
public class LLTest {

	/**
	 * Entry point of execution
	 * @param args - ignored
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Button.waitForAnyPress();

		Odometer odo = new Odometer(Motor.B, Motor.C, 20, true);
		Map map= new Map(odo, 30);
		UltrasonicSensor USSensor = new UltrasonicSensor(SensorPort.S4);
		Navigator nav = new Navigator(odo, map, USSensor);
		LightSensor ls = new LightSensor(SensorPort.S2);
		
		LightLocalizer.doLocalization(odo, nav, ls, Motor.B, Motor.C);
		
	
		Button.waitForAnyPress();
	}

}
