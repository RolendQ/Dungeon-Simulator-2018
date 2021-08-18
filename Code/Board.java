import java.util.ArrayList;
import java.util.HashSet;

/*
 * This class is the board representing where the AI dot and player can travel
 * Contains rectangles that make up the board
 */
public class Board {
	private ArrayList<Rectangle> rects;
	
	/*
	 * Constructor, creates an arraylist of rectangles
	 */
	public Board() {
		rects = new ArrayList<Rectangle>();
	}
	
	/*
	 * Adds a rectangle to the list
	 */
	public void addRectangle(int x1, int y1, int x2, int y2) {
		rects.add(new Rectangle(x1,y1,x2,y2));
	}
	
	/*
	 * Calculates if a given sprite is in any of the rectangles
	 */
	public boolean isInBounds(Sprite s) {
		for (int i = 0; i < rects.size(); i++) {
			Rectangle rect = rects.get(i);
			if (s.getxCoord() > rect.getxCoord() && s.getxCoord() + s.getWidth() < rect.getxCoord() + rect.getWidth() && s.getyCoord() > rect.getyCoord() && s.getyCoord() + s.getHeight() < rect.getyCoord() + rect.getHeight()) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Calculates if a given sprite after moving based on a vector is in any of the rectangles
	 */
	public boolean isInBounds(Sprite s, Vector v) {
		for (int i = 0; i < rects.size(); i++) {
			Rectangle rect = rects.get(i);
			if (s.getxCoord()+v.getX() > rect.getxCoord() && s.getxCoord()+v.getX() + s.getWidth() < rect.getxCoord() + rect.getWidth() && s.getyCoord()+v.getY() > rect.getyCoord() && s.getyCoord()+v.getY() + s.getHeight() < rect.getyCoord() + rect.getHeight()) {
				return true;
			}
		}
		return false;
	}
	
	public void clear() {
		rects.clear();
	}
	
	//----------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * @return the rects
	 */
	public ArrayList<Rectangle> getRects() {
		return rects;
	}

	/**
	 * @param rects the rects to set
	 */
	public void setRects(ArrayList<Rectangle> rects) {
		this.rects = rects;
	}
	
	
}
