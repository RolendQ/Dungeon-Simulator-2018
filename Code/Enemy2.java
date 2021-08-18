/*
 * This class is for the Spinner enemies. They revolve around a point
 */
public class Enemy2 extends Sprite {
	private int centerX;
	private int centerY;
	private int radius;
	private int speed;
	
	/*
	 * Constructor, takes in x and y for a center point, width, height, radius of circle, and speed
	 */
	public Enemy2(int x, int y, int w, int h, int radius, int speed) {
		super(x+radius, y, w, h);
		this.centerX = x;
		this.centerY = y;
		this.radius = radius;
		this.speed = speed;
	}
	
	/*
	 * Returns next vector based on the time since level started
	 */
	public Vector getNextMove(int time) {
		double radian = Math.PI / 16 * speed/2 * time;
		int x = (int) (radius * Math.cos(radian));
		int y = (int) (radius * Math.sin(radian));
		return new Vector(x,y);
	}
	
	//---------------------------------------------------------------------------------------------------------------------

	/**
	 * @return the centerX
	 */
	public int getCenterX() {
		return centerX;
	}

	/**
	 * @param centerX the centerX to set
	 */
	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	/**
	 * @return the centerY
	 */
	public int getCenterY() {
		return centerY;
	}

	/**
	 * @param centerY the centerY to set
	 */
	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	/**
	 * @return the radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(int radius) {
		this.radius = radius;
	}

	/**
	 * @return the speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	
	
	
	 
}