package defense;

/**
 * 
 * Intelligently navigate to the defensive zone
 * 
 * @author Michael
 *
 */
public class navigateToDefensiveZone{
	private static final int defenseSpotX = 0;
	private static final int defenseSpotY = 0;
	int w1;
	int w2;
	int d1; 
	
	
	/**
	 * Constructor
	 */
	public navigateToDefensiveZone(int w1, int w2, int d1) {
		this. w1 = w1;
		this.w2 = w2;
		this.d1 = d1;
	}
	
	public static void navigate(int w1, int w2, int d1) {
		navigateToDefensiveZone temp = new navigateToDefensiveZone(w1, w2, d1);
		temp.navigate();
	}
	
	public void navigate(){
		
	}
	

}

