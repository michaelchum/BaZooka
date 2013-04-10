package control;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
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
public class Controller {

	/**
	 * Entry point of execution Opens up a BT connection, receives transmission,
	 * and plays the game
	 * 
	 * @param args
	 *            - ignored
	 */
	public static void main(String[] args) {

		Button.waitForAnyPress();

		/*
		 * BluetoothConnection myBTConnection = new BluetoothConnection();
		 * Transmission t = myBTConnection.getTransmission(); PlayerRole role =
		 * t.role; int bx = t.bx; int by = t.by; int w1 = t.w1; int w2 = t.w2;
		 * int d1 = t.d1; StartCorner startingCorner = t.startingCorner;
		 */

		// position of the basket
		int goalX = 150;
		int goalY = 300;

		// testing values
		int bx = 11;
		int by = 8;
		int w1 = 2;
		int w2 = 2;
		int d1 = 7;

		
		/**
		 * BOTTOM LEFT	X1 (0,0) "BL"
		 * BOTTOM RIGHT	X2 (10,0) "BR"
		 * TOP RIGHT	X3 (10,10) "TR"
		 * TOP LEFT		X4 (0,10) "TL"
		 */
		StartCorner startingCorner;
		startingCorner = StartCorner.lookupCorner(3);
		
		
		// 2 is defender, 1 is attacker
		PlayerRole role;
		role = PlayerRole.lookupRole(1);
		Robot robot;
		if (role == PlayerRole.ATTACKER) {
			robot = new Forward(Motor.A, Motor.B, Motor.C, SensorPort.S1,
					SensorPort.S2, SensorPort.S3, SensorPort.S4);
			//((Forward) robot).loadFiveBalls();
		} else {
			robot = new Defender(Motor.A, Motor.B, Motor.C, SensorPort.S1,
					SensorPort.S2, SensorPort.S3, SensorPort.S4);
		}

		 robot.play(startingCorner, bx, by, w1, w2, d1, goalX, goalY);
	
		Button.waitForAnyPress();
	}
}
