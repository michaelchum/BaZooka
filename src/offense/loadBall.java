package offense;

public class loadBall {

	/**
	 * 
	 * Load ball when in front of dispenser
	 * @author Michael
	 * 
	 */
	
	int bx;
	int by;

	public loadBall(int bx, int by) {
		this.bx=bx;
		this.by=by;
	}
	
	public static void load(int bx, int by){
		loadBall temp = new loadBall(bx, by);
		temp.load();
	}
	
	public void load(){
	}
	
}
