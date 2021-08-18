import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

/*
 * This class draws all the elements
 */
public class DisplayBoard extends JPanel {
	private Population pop;
	private Start start;
	private Goal goal;
	private Board board;
	private ArrayList<Wall> walls;
	private ArrayList<Enemy> enemies;
	private ArrayList<Enemy2> enemies2;
	private Sprite selectedSprite;
	private boolean selectedShowAll;
	private boolean playerTestingMode;
	
	private Player player;
	
	/*
	 * Constructor, takes in all the necessary objects to draw
	 */
	public DisplayBoard(Player player,Population pop, Start start, Goal goal, Board board, ArrayList<Wall> walls, ArrayList<Enemy> enemies, ArrayList<Enemy2> enemies2, Sprite selectedSprite) {
		this.player = player;
		this.pop = pop;
		this.start = start;
		this.goal = goal;
		this.board = board;
		this.walls = walls;
		this.enemies = enemies;
		this.enemies2 = enemies2;
		this.selectedSprite = selectedSprite;
	}
	
	/*
	 * Paints everything using Graphics
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		// Boards
		for (Rectangle r : board.getRects()) {
			g.setColor(new Color(255,255,255));
			g.fillRect(r.getxCoord(), r.getyCoord(), r.getWidth(), r.getHeight());
		}
		
		// Walls
		g.setColor(new Color(0, 0, 0));
		for (int i = 0; i < walls.size(); i++) {
			g.drawRect(walls.get(i).getxCoord(), walls.get(i).getyCoord(), walls.get(i).getWidth(), walls.get(i).getHeight());
			g.fillRect(walls.get(i).getxCoord(), walls.get(i).getyCoord(), walls.get(i).getWidth(), walls.get(i).getHeight());
		}
		
		// Goal
		g.setColor(new Color(0, 0, 0));
		g.drawRect(goal.getxCoord(), goal.getyCoord(), goal.getWidth(), goal.getHeight());
		g.setColor(new Color(89, 255, 86));
		g.fillRect(goal.getxCoord(), goal.getyCoord(), goal.getWidth(), goal.getHeight());
		
		// Player and AI
		if (playerTestingMode) {
			g.setColor(new Color(127, 176, 255));
			g.fillRect(player.getxCoord(),player.getyCoord(),player.getWidth(),player.getHeight());
			g.setColor(new Color(0, 0, 0));
			g.drawRect(player.getxCoord(),player.getyCoord(),player.getWidth(),player.getHeight());
		} else {
			if (selectedShowAll) {
				// Draws dead first so its underneath
				for (int i = 1; i < pop.getPopulation().size(); i++) {
					AI current = pop.getPopulation().get(i);
					if (current.getIsDead()) {
						g.setColor(new Color(0, 0, 0));
						g.drawOval(current.getxCoord(), current.getyCoord(), current.getWidth(), current.getHeight());
						g.setColor(new Color(255, 255, 255));
						g.fillOval(current.getxCoord(), current.getyCoord(), current.getWidth(), current.getHeight());
					}
				}
				for (int i = 1; i < pop.getPopulation().size(); i++) {
					AI current = pop.getPopulation().get(i);
					if (!current.getIsDead()) {
						g.setColor(new Color(0, 0, 0));
						g.drawOval(current.getxCoord(), current.getyCoord(), current.getWidth(), current.getHeight());
						g.setColor(new Color(255, 0, 0));
						g.fillOval(current.getxCoord(), current.getyCoord(), current.getWidth(), current.getHeight());
					}
				}
			}
			
			// Best AI
			AI best = pop.getPopulation().get(0);
			g.setColor(new Color(0, 0, 0));
			g.drawOval(best.getxCoord(), best.getyCoord(), best.getWidth(), best.getHeight());
			g.setColor(new Color(19, 247, 160));
			g.fillOval(best.getxCoord(), best.getyCoord(), best.getWidth(), best.getHeight());
		}
		
		// Start
		g.setColor(new Color(0, 0, 0));
		g.drawRect(start.getxCoord(), start.getyCoord(), start.getWidth(), start.getHeight());
		g.setColor(new Color(255, 86, 207));
		g.fillRect(start.getxCoord(), start.getyCoord(), start.getWidth(), start.getHeight());
		
		// Enemy (Sentry)
		for (int i = 0; i < enemies.size(); i++) {
			if (!playerTestingMode) {
				g.setColor(new Color(0, 0, 0));
				g.drawOval(enemies.get(i).getX1(), enemies.get(i).getY1(), enemies.get(i).getWidth(), enemies.get(i).getHeight());
				g.drawOval(enemies.get(i).getX2(), enemies.get(i).getY2(), enemies.get(i).getWidth(), enemies.get(i).getHeight());
				g.setColor(new Color(200,200,200));
				g.fillOval(enemies.get(i).getX2(), enemies.get(i).getY2(), enemies.get(i).getWidth(), enemies.get(i).getHeight());
				g.fillOval(enemies.get(i).getX1(), enemies.get(i).getY1(), enemies.get(i).getWidth(), enemies.get(i).getHeight());
			}
			
			//
			g.setColor(new Color(0, 0, 0));
			g.drawOval(enemies.get(i).getxCoord(), enemies.get(i).getyCoord(), enemies.get(i).getWidth(), enemies.get(i).getHeight());
			g.setColor(new Color(2, 46, 232));
			g.fillOval(enemies.get(i).getxCoord(), enemies.get(i).getyCoord(), enemies.get(i).getWidth(), enemies.get(i).getHeight());
		}
		
		// Enemy 2 (Spinner)
		for (int i = 0; i < enemies2.size(); i++) {
			// Shadow of start and end
			if (!playerTestingMode) {
				g.setColor(new Color(0, 0, 0));
				g.drawOval(enemies2.get(i).getCenterX(), enemies2.get(i).getCenterY(), enemies2.get(i).getWidth(), enemies2.get(i).getHeight());
				g.setColor(new Color(200,200,200));
				g.fillOval(enemies2.get(i).getCenterX(), enemies2.get(i).getCenterY(), enemies2.get(i).getWidth(), enemies2.get(i).getHeight());
			}
			//
			g.setColor(new Color(0, 0, 0));
			g.drawOval(enemies2.get(i).getxCoord(), enemies2.get(i).getyCoord(), enemies2.get(i).getWidth(), enemies2.get(i).getHeight());
			g.setColor(new Color(1, 142, 36));
			g.fillOval(enemies2.get(i).getxCoord(), enemies2.get(i).getyCoord(), enemies2.get(i).getWidth(), enemies2.get(i).getHeight());
		}
		
		// Selection Highlight
		g.setColor(new Color(255, 228, 57));
		g2.setStroke(new BasicStroke(3));
		if (selectedSprite != null) {
			if (selectedSprite instanceof Enemy) {
				g2.drawOval(selectedSprite.getxCoord(),selectedSprite.getyCoord(),selectedSprite.getWidth(),selectedSprite.getHeight());
				g2.drawOval(((Enemy) selectedSprite).getX1(),((Enemy) selectedSprite).getY1(),selectedSprite.getWidth(),selectedSprite.getHeight());
				g2.drawOval(((Enemy) selectedSprite).getX2(),((Enemy) selectedSprite).getY2(),selectedSprite.getWidth(),selectedSprite.getHeight());
			} else if(selectedSprite instanceof Enemy2) {
				g2.drawOval(selectedSprite.getxCoord(),selectedSprite.getyCoord(),selectedSprite.getWidth(),selectedSprite.getHeight());
				g2.drawOval(((Enemy2) selectedSprite).getCenterX(),((Enemy2) selectedSprite).getCenterY(),selectedSprite.getWidth(),selectedSprite.getHeight());
			} else {
				g2.drawRect(selectedSprite.getxCoord(),selectedSprite.getyCoord(),selectedSprite.getWidth(),selectedSprite.getHeight());
			}
		}
		
		// Border
		g.setColor(new Color(0, 0, 0));
		g2.setStroke(new BasicStroke(6));
		g.drawRect(3, 3, 600, 600);
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @return the selectedSprite
	 */
	public Sprite getSelectedSprite() {
		return selectedSprite;
	}

	/**
	 * @param selectedSprite the selectedSprite to set
	 */
	public void setSelectedSprite(Sprite selectedSprite) {
		this.selectedSprite = selectedSprite;
	}

	/**
	 * @return the selectedShowAll
	 */
	public boolean getSelectedShowAll() {
		return selectedShowAll;
	}

	/**
	 * @param selectedShowAll the selectedShowAll to set
	 */
	public void setSelectedShowAll(boolean selectedShowAll) {
		this.selectedShowAll = selectedShowAll;
	}

	/**
	 * @return the playerTestingMode
	 */
	public boolean isPlayerTestingMode() {
		return playerTestingMode;
	}

	/**
	 * @param playerTestingMode the playerTestingMode to set
	 */
	public void setPlayerTestingMode(boolean playerTestingMode) {
		this.playerTestingMode = playerTestingMode;
	}
}
