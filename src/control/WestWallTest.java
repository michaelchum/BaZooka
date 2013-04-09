/**
 * 
 */
package control;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import role.Defender;
import role.Forward;
import role.Robot;
import bluetooth.PlayerRole;
import bluetooth.StartCorner;

/**
 * @author Team 13
 *
 */
public class WestWallTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Button.waitForAnyPress();
		
		// position of the basket
		int goalX = 150;
		int goalY = 300;

		// testing values
		int bx = -1; //west wall
		int by = 7;
		int w1 = 0;
		int w2 = 0;
		int d1 = 7;

		
		/**
		 * BOTTOM LEFT	X1 (0,0) "BL"
		 * BOTTOM RIGHT	X2 (10,0) "BR"
		 * TOP RIGHT	X3 (10,10) "TR"
		 * TOP LEFT		X4 (0,10) "TL"
		 */
		StartCorner startingCorner;
		startingCorner = StartCorner.lookupCorner(4); //top left (0, 300)
		
		
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
