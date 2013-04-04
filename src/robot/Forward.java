package robot;

import navigation.Navigator;
import navigation.navigateHome;
import odometry.Odometer;
import odometry.OdometryCorrection;
import odometry.RightAngleCorrection;
import odometry.LCDInfo;
import localization.LightLocalizer;
import localization.USLocalizer;
import bluetooth.PlayerRole;
import bluetooth.StartCorner;
import offense.navigateToLoadingArea;
import offense.loadBall;
import offense.navigateToFiringArea;
import offense.fireBall;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;


/**
 * Specific role of forward
 * 
 * @author Michael
 * 
 */
public class Forward extends Robot {
	/**
	 * Constructor - same as Robot constructor
	 * 
	 * @param catapultMotor
	 * @param leftMotor
	 * @param rightMotor
	 * @param leftLightPort
	 * @param centerLightPort
	 * @param rightLightPort
	 * @param USPort
	 */
	public Forward(NXTRegulatedMotor catapultMotor,NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor, 
			SensorPort leftLightPort, SensorPort centerLightPort, SensorPort rightLightPort, SensorPort USPort) {
		super(catapultMotor, leftMotor, rightMotor, leftLightPort, centerLightPort, rightLightPort, USPort);
	}

	/**
	 * First localize and then search for ball dispenser, retrieve ball and find basket, aim and shoot. Repeat twice. 5 balls each time
	 * 
	 * @see robot.Robot#play()
	 */
	@Override
	public void play() {
	}

	public void localize(StartCorner startingCorner) {
		
		myCatapult.arm(); // arm the robot's lever for mobility
		
		// localize and position at the first block NEED TO IMPLEMENT 
		/* STARTERCORNER TO BOTH LOCALIZERS */
		//USLocalizer.doFallingEdgeLocalization(myOdo, myNav, USSensor, leftMotor, rightMotor);
		//LightLocalizer.doLocalization(myOdo, myNav, centerSensor, leftMotor, rightMotor);
		
		// navigate to the middle of the first tile depending on StartCorner
		// myNav.TravelTo(15.0,15.0);
		// myNav.TurnTo(90.0);
	}

	/**
	 * Arbitrates among the specific behaviors of a
	 * robot that is playing as a forward
	 * 
	 * @see robotBehavior.Robot#play()
	 */

	public void play(PlayerRole role, int bx, int by, int w1, int w2, int d1, StartCorner startingCorner, int goalX, int goalY) {
		LCDInfo LCD = new LCDInfo(myOdo, USSensor, leftSensor, centerSensor, rightSensor);
		
		// testing
		myOdo.setPosition(new double [] {15.0, 15.0, 90.0}, new boolean [] {true, true, true});
		myNav.navigateTo(75,75);
		
		//localize(startingCorner);

		// initialize correction threads
		//OdometryCorrection myOdoCorrection = new OdometryCorrection(myOdo, centerSensor, leftMotor, rightMotor);
		//myOdoCorrection.start();
		//OdometryAngleCorrection myOdoAngleCorrection = new OdometryAngleCorrection(myOdo, leftSensor, rightSensor, leftMotor, rightMotor);
		//myOdoAngleCorrection.start();
		
		/* NEED TO WRITE ALL THESE USING NAVIGATOR */
		
		/*
		navigateToLoadingArea.navigate(bx, by, myNav, myOdo);
		loadBall.load(bx, by, myNav, myOdo);
		navigateToFiringArea.navigateRightSpot(goalX, goalY, d1, myNav, myOdo);
		fireBall.fire(myCatapult, goalX, goalY);
		
		navigateToLoadingArea.navigate(bx, by, myNav, myOdo);
		loadBall.load(bx, by, myNav, myOdo);
		navigateToFiringArea.navigateLeftSpot(goalX, goalY, d1, myNav, myOdo);
		fireBall.fire(myCatapult, goalX, goalY);
		
		navigateHome.navigate(startingCorner, myNav, myOdo);
		*/
		
	}
}
