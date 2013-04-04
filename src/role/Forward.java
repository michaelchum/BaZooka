package role;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import localization.LightLocalizer;
import localization.USLocalizer;
import bluetooth.StartCorner;

/**
 * Specific role of forward
 * 
 * @author Michael
 * 
 */
public class Forward extends Robot {
	static final double distanceFromCenterToRamp = 6.5, loadingDistance = 5,
			distanceToWall = 17;
	private double loadingX = 0, loadingY = 0, preciseLoadingX = 0,
			preciseLoadingY = 0, loadingHeading = 0;
	private double closestX = 0, closestY = 0;

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
	public Forward(NXTRegulatedMotor catapultMotor,
			NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor,
			SensorPort leftLightPort, SensorPort centerLightPort,
			SensorPort rightLightPort, SensorPort USPort) {
		super(catapultMotor, leftMotor, rightMotor, leftLightPort,
				centerLightPort, rightLightPort, USPort);
	}

	@Override
	public void play(StartCorner startingCorner, int bx, int by, int w1,
			int w2, int d1, int goalX, int goalY) {

		myCatapult.arm();
		USLocalizer.doFallingEdgeLocalization(myOdo, USSensor, myNav,
				leftMotor, rightMotor);
		LightLocalizer.doLocalization(myOdo, myNav, centerSensor, leftMotor,
				rightMotor);

		computeLoadingCoordinates(bx, by);

		// navigate to loading area and load balls
		myNav.navigateTo(loadingX, loadingY);
		myNav.travelTo(preciseLoadingX, preciseLoadingY);
		myNav.turnTo(loadingHeading, true);
		loadFiveBalls();

		// localize again (pushing the button fucks it up)
		computeClosestIntersection();
		myNav.travelTo(closestX, closestY);
		LightLocalizer.doLocalization(myOdo, myNav, centerSensor, leftMotor,
				rightMotor, closestX, closestY);

		// navigate to firing area
		myNav.navigateTo(135, goalY - ((d1 + 1) * 30) - 15);
		myNav.travelTo(150, goalY - ((d1 + 1) * 30));
		LightLocalizer.doLocalization(myOdo, myNav, centerSensor, leftMotor,
				rightMotor); // localize before shooting
		shootFiveBalls();

	}

	/**
	 * Need to change values if the arena changes
	 * 
	 * @param bx
	 * @param by
	 */
	private void computeLoadingCoordinates(int bx, int by) {
		if (bx == -1) { // western wall
			preciseLoadingX = bx + loadingDistance;
			preciseLoadingY = by + distanceFromCenterToRamp;
			loadingHeading = 180;
			
			loadingX = -15;
			loadingY = (by * 30) - 15;
		}

		if (by == -1) { // southern wall
			preciseLoadingX = bx * 30 - distanceFromCenterToRamp;
			preciseLoadingY = by * 30 + loadingDistance;
			loadingHeading = 270;
			
			loadingY = -15;
			loadingX = (bx * 30) - 15;
		}

		if (bx == 11) { // eastern wall
			preciseLoadingX = bx * 30 - loadingDistance;
			preciseLoadingY = by * 30 - distanceFromCenterToRamp;
			// loadingX should be between 16-18 cm of the wall

			loadingX = 315;
			loadingY = (by * 30) - 15;
			loadingHeading = 0;
		}

	}

	/**
	 * Need to change these max values if the arena changes
	 */
	private void computeClosestIntersection() {
		double currentX = myOdo.getX();
		double currentY = myOdo.getY();

		if (currentX < 0) {
			closestX = 0;
		} else if (currentX > 300) {
			closestX = 300;
		} else {
			closestX = Math.round(myOdo.getX() / 30) * 30;
		}

		if (currentY < 0) {
			closestY = 0;
		} else if (currentY > 300) {
			closestY = 300;
		} else {
			closestX = Math.round(myOdo.getX() / 30) * 30;
		}

	}

	public void loadFiveBalls() {
		DifferentialPilot myPilot = new DifferentialPilot(5.36, 5.36, 16.32,
				leftMotor, rightMotor, false);
		myPilot.setTravelSpeed(5);
		for (int i = 0; i < 5; i++) {
			myPilot.travel(5);
			Delay.msDelay(1000);
			myPilot.travel(-5);
		}
	}

	private void shootFiveBalls() {
		myNav.turnTo(90, true);
		for (int i = 0; i < 5; i++) {
			myCatapult.carry();
			myCatapult.shootCenter();
		}
	}

}
