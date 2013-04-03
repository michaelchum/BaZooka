BaZooka
=======

DPM Team 13

Final values for our robot which work perfectly: TileWidth = 30.0. Radius - 2.68. Width = 16.32.
Odometer and Navigations are the ones provided by the TA. 90.0 degrees is north!

Navigator is a wavefront algorithm which maps the entire map and adds obstacles to it when one is detected and find the shortest path to destination avoiding all obstacles.

Inspired by the following websites if you want to understand
http://www.societyofrobots.com/programming_wavefront.shtml
http://www.robotc.net/blog/2011/08/08/robotc-advanced-training/

CURRENT STATUS

Odometry - Done
Navigation - Done
Navigator - Done
OdometryCorrection - Done
Map - Done
Bluetooth - Done
Catapult - Done, need to take numbers from Danial
OdometryAngleCorrection - Need tweaking (Michael)

TO BE WORKED ON, in order of priority

USLocalizer - Working, need tweaking, need to implement for all 4 corners (Clark)
LightLocalizer - Working, need tweaking, need to implement all 4 corners (Clark)

Not too hard with the use of Navigator and Navigation
navigateToFiringArea - TBA Need to implement d1, w1, w2 which will be provided by bluetooth, need two position in diagonal with basket to avoid defender
fireBall - TBA Need to make angle according according to current firingPosition and basket
navigateToBallDispenser - TBA Need to implement bx, by
loadBall - TBA Test to load ball according to bx, by, might need mechanical improvements
navigateToDefensiveZone - TBA Need to implement w1, w2
patrol - TBA
navigateHome - TBA







