/*
 * This abstract class is extended by all objects that will be drawn
 */
public abstract class Sprite {
	private int xCoord;
	private int yCoord;
	private int width;
	private int height;
	
	/*
	 * Constructor, takes in x, y, width, height
	 */
	public Sprite(int x, int y, int w, int h) {
		this.xCoord = x;
		this.yCoord = y;
		this.width = w;
		this.height = h;
	}

	/*
	 * Calculates if this sprite is in bounds of another x, y, width, and height
	 */
	public boolean isInBounds(int x, int y, int width, int height) {
		if(this.xCoord + this.width < x || this.yCoord + this.height < y) {
			return false;
		}
		else if (x + width < this.getxCoord() || y + height < this.getyCoord()) {
			return false;
		}
		return true;
		
	}


	/*
	 * Calculates if this sprite after moving based on a vector is in bounds of another x, y, width, and height
	 */
	public boolean isInBounds(int x, int y, int width, int height, Vector v) {
		if(this.xCoord + this.width + v.getX() < x || this.yCoord + this.height + v.getY() < y) {
			return false;
		}
		else if (x + width < this.getxCoord() + v.getX() || y + height < this.getyCoord() + v.getY()) {
			return false;
		}
		return true;
		
	}
	
	/*
	 * Calculates if any two rectangles are in bounds of each other
	 */
	public static boolean isInBounds(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
		if(x1 + w1 < x2 || y1 + h1 < y2) {
			return false;
		}
		else if (x2 + w2 < x1 || y2 + h2 < y1) {
			return false;
		}
		return true;
	}
	
	//----------------------------------------------------------------------------------------------------------------------------
	
	public String toString() {
		return "location: (" +  xCoord + ", " + yCoord + "); "+"width: "+width+" height: "+height;
	}
	
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the xCoord
	 */
	public int getxCoord() {
		return xCoord;
	}

	/**
	 * @param xCoord the xCoord to set
	 */
	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	/**
	 * @return the yCoord
	 */
	public int getyCoord() {
		return yCoord;
	}

	/**
	 * @param yCoord the yCoord to set
	 */
	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}
}