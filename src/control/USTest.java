package control;

import navigation.Map;
import navigation.Navigator;
import odometry.Odometer;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import localization.USLocalizer;
import role.Defender;
import role.Forward;
import role.Robot;
import bluetooth.BluetoothConnection;
import bluetooth.PlayerRole;
import bluetooth.StartCorner;
import bluetooth.Transmission;
import bluetooth.ParseTransmission;

/**
 * Main controller of the system
 * 
 * @author Team 13
 * 
 */
public class USTest {

	/**
	 * Entry point of execution Opens up a BT connection, receives transmission,
	 * and plays the game
	 * 
	 * @param args
	 *            - ignored
	 */
	public static void main(String[] args) {

		Button.waitForAnyPress();

		Odometer odo = new Odometer(Motor.B, Motor.C, 20, true);
		Map map= new Map(odo, 30);
		UltrasonicSensor USSensor = new UltrasonicSensor(SensorPort.S4);
		Navigator nav = new Navigator(odo, map, null);
		
		USLocalizer.doFallingEdgeLocalization(odo, USSensor, nav, Motor.B, Motor.C, 0);
		
	
		Button.waitForAnyPress();
	}
}