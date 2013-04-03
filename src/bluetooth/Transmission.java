/**
* @author Sean Lawlor, Stepan Salenikovich
* @date November 3, 2011
* @class ECSE 211 - Design Principle and Methods
*/
package bluetooth;

/**
 * Skeleton class to hold datatypes needed for final project
 * 
 * Simply all public variables so can be accessed with 
 * Transmission t = new Transmission();
 * int fx = t.fx;
 * 
 * and so on...
 * 
 * Also the role is an enum, converted from the char transmitted. (It should never be
 * Role.NULL)
 */

public class Transmission {
	/**
	 * The role, Defender or Attacker
	 */
	public PlayerRole role;
	/**
	 * Ball dispenser X tile position
	 */
	public int bx;
	/**
	 * Ball dispenser Y tile position
	 */
	public int by;
	/**
	 * Defender zone dimension 1
	 */
	public int w1;
	/**
	 * Defender zone dimension 2
	 */
	public int w2;
	/**
	 * Forward line distance from goal
	 */
	public int d1;
	/**
	 * starting corner, 1 through 4
	 */
	public StartCorner startingCorner;
	
}
