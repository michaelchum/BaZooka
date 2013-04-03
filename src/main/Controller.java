package main;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import robot.Forward;
import robot.Defender;
import bluetooth.BluetoothConnection;
import bluetooth.PlayerRole;
import bluetooth.StartCorner;
import bluetooth.Transmission;
import bluetooth.ParseTransmission;

/**
 * Main controller of the system
 * 
 * @author Michael
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
		BluetoothConnection myBTConnection = new BluetoothConnection();
		Transmission t = myBTConnection.getTransmission();
		PlayerRole role = t.role;
		int bx = t.bx;
		int by = t.by;
		int w1 = t.w1;
		int w2 = t.w2;
		int d1 = t.d1;
		StartCorner startingCorner = t.startingCorner;
		*/
		
		// position of the basket
		final int goalX = 150;
		final int goalY = 300;
		
		// testing values
		int bx = 0;
		int by = 0;
		int w1 = 0;
		int w2 = 0;
		int d1 = 0;
		
		// 1 is (0,0), 2 is (0,10), 3 is (10,10), 4 is (10,0)
		StartCorner startingCorner;
		startingCorner = StartCorner.lookupCorner(1);
		// 2 is defender, 1 is attacker
		PlayerRole role;
		role = PlayerRole.lookupRole(1);
		
		if (role==PlayerRole.ATTACKER){
		Forward forward = new Forward(Motor.A, Motor.B, Motor.C, SensorPort.S1,
				SensorPort.S2, SensorPort.S3, SensorPort.S4);
		forward.play(role, bx, by, w1, w2, d1, startingCorner, goalX, goalY);
		}
		
		else if (role==PlayerRole.DEFENDER){
		Defender defender = new Defender(Motor.A, Motor.B, Motor.C, SensorPort.S1,
				SensorPort.S2, SensorPort.S3, SensorPort.S4);
		defender.play(role, bx, by, w1, w2, d1, startingCorner);
		}
		
		else {Button.waitForAnyPress();}	
		Button.waitForAnyPress();
	}
}
