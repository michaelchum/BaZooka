package navigation;

import bluetooth.StartCorner;

/**
 * 
 * Intelligently navigate back to initial corner
 * 
 * @author Michael
 *
 */
public class navigateHome{
	StartCorner startingCorner;
	int startX;
	int startY;
	
	/**
	 * Constructor
	 */
	public navigateHome(StartCorner startingCorner) {
		this.startX = startingCorner.getX();
		this.startY = startingCorner.getY();
	}
	
	public static void navigate(StartCorner startingCorner) {
		navigateHome temp = new navigateHome(startingCorner);
		temp.navigate();
	}
	
	public void navigate(){
		
	}
	

}

