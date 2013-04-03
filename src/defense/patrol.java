package defense;

/**
 * 
 * Intelligently navigate to the defensive zone
 * 
 * @author Michael
 *
 */
public class patrol{
	int w1;
	int w2;
	int d1; 
	
	
	/**
	 * Constructor
	 */
	public patrol(int w1, int w2, int d1) {
		this.w1 = w1;
		this.w2 = w2;
		this.d1 = d1;
	}
	
	public static void start(int w1, int w2, int d1) {
		patrol temp = new patrol(w1, w2, d1);
		temp.start();
	}
	
	public void start(){
		
	}
	

}

