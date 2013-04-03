package offense;

public class navigateToLoadingArea {

	/**
	 * 
	 * Navigate to the ball dispenser
	 * @author Michael
	 * 
	 */
	
	int bx;
	int by;

	public navigateToLoadingArea(int bx, int by) {
		this.bx=bx;
		this.by=by;
	}
	
	public static void navigate(int bx, int by){
		navigateToLoadingArea temp = new navigateToLoadingArea(bx, by);
		temp.navigateToDispenser();
		temp.rotateToDispenser();
	}
	
	public void navigateToDispenser(){
	}
	
	public void rotateToDispenser(){
	}
	 

}
