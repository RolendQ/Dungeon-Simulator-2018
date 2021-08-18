
/*
 * This class is for the Sentry enemies. They travel back and forth between two points
 */
public class Enemy extends Sprite {
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private int speed;
	private boolean isForward = true;
	private Vector v;
	
	/*
	 * Constructor, takes in width, height, x and y for 2 points, and speed
	 */
	public Enemy(int w, int h, int x1, int y1, int x2, int y2, int speed) {
		super(x1, y1, w, h);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		setSpeed(speed);
	}

	/*
	 * Sets the speed and calculates a new vector for the path between the two points
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
		int deltaX = x2 - x1;
		int deltaY = y2 - y1;
		double angle = Math.atan2(deltaY, deltaX);
		
		v = new Vector((int) (4*speed*Math.cos(angle)),(int) (4*speed*Math.sin(angle)));
	}
	
	//------------------------------------------------------------------------------------------------------------------------

	/**
	 * @return the x1
	 */
	public int getX1() {
		return x1;
	}

	/**
	 * @param x1 the x1 to set
	 */
	public void setX1(int x1) {
		this.x1 = x1;
	}

	/**
	 * @return the y1
	 */
	public int getY1() {
		return y1;
	}

	/**
	 * @param y1 the y1 to set
	 */
	public void setY1(int y1) {
		this.y1 = y1;
	}

	/**
	 * @return the x2
	 */
	public int getX2() {
		return x2;
	}

	/**
	 * @param x2 the x2 to set
	 */
	public void setX2(int x2) {
		this.x2 = x2;
	}

	/**
	 * @return the y2
	 */
	public int getY2() {
		return y2;
	}

	/**
	 * @param y2 the y2 to set
	 */
	public void setY2(int y2) {
		this.y2 = y2;
	}

	/**
	 * @return the isForward
	 */
	public boolean isForward() {
		return isForward;
	}

	/**
	 * @param isForward the isForward to set
	 */
	public void setForward(boolean isForward) {
		this.isForward = isForward;
	}

	/**
	 * @return the v
	 */
	public Vector getVector() {
		return v;
	}

	/**
	 * @param v the v to set
	 */
	public void setVector(Vector v) {
		this.v = v;
	}

	/**
	 * @return the speed
	 */
	public int getSpeed() {
		return speed;
	}
	
	
	
}
