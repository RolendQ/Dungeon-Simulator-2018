import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
 * This class runs the actual program
 * Implements MouseListener and KeyListener for tracking mouse clicks and key presses
 */
public class App extends JFrame implements MouseListener, KeyListener {
	private JPanel contentPane;
	private Population population;
	private ArrayList<Wall> walls = new ArrayList<Wall>();
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<Enemy2> enemies2 = new ArrayList<Enemy2>();
	private DisplayBoard displayBoard;
	private Start start;
	private Goal goal;
	private Board board = new Board();
	private Sprite selectedSprite;
	private boolean selectingSpot;
	private int boardWidth;
	private int boardHeight;
	private int gen;
	private int dotsAlive;
	private int dotsFinished;
	private double closest;
	private int steps;
	private boolean isEnd;
	
	private Player player;
	private int[] playerMoves = {0,0,0,0};
	
	private JTextField generationText;
	private JTextField dotsAliveText;
	private JTextField dotsFinishedText;
	private JTextField closestText;
	private JTextField fastestText;
	
	private JSlider enemySpeed;
	private JTextArea xCoordText;
	private JTextArea yCoordText;
	private JButton deselectButton;
	private JButton moveButton;
	private JButton deleteButton;
	private JTextArea widthText;
	private JTextArea heightText;
	private JTextField widthBox;
	private JTextField heightBox;
	private JTextArea x2CoordText;
	private JTextArea y2CoordText;
	private JButton move2Button;
	private JTextArea selectionName;
	private JTextArea radiusText;
	private JTextField radiusBox;
	private JButton copyButton;
	
	private JTextField maxPopBox;
	private JTextField initialStepsBox;
	private JTextField stepsIncrementBox;
	private JTextField topFracBox;
	private JTextField bestFreqBox;
	private JTextField randFreqBox;
	private JTextField bestMutFreqBox;
	private JTextField topMutFreqBox;
	
	private boolean appRunning = false;
	private int enemyTimer;
	
	/*
	 * Runs this app
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame game = new App();
					game.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/*
	 * Resets the sprites to blank level
	 */
	public void resetSprites() {
		start = new Start(50,550,10,10);
		goal = new Goal(550,50,10,10);
		board.clear();
		board.addRectangle(0,0,boardWidth,boardHeight);
		
		closest = (int) (Math.hypot(start.getxCoord()-goal.getxCoord(), start.getyCoord()-goal.getyCoord()));
		walls.clear();
		enemies.clear();
		enemies2.clear();

	}
	
	/*
	 * Moves enemies back to where they should be when level restarts
	 */
	public void resetEnemiesPath() {
		enemyTimer = 0;
		for (Enemy e : enemies) {
			e.setxCoord(e.getX1());
			e.setyCoord(e.getY1());
		}
	}
	
	/*
	 * Resets population
	 */
	public void resetPop(int maxPop, int initialSteps, int stepsIncrement, double topFrac, double bestFreq, double randFreq, double bestMutFreq, double topMutFreq) {
		gen = 1;
		steps = 0;
		appRunning = false;
		dotsAlive = maxPop;
		
		population = new Population(maxPop, initialSteps, stepsIncrement, topFrac, bestFreq, randFreq, bestMutFreq, topMutFreq);
		population.setSpawn(start);
		
		// Fills the boxes for the settings of algorithm
		maxPopBox.setText(Integer.toString(maxPop));
		initialStepsBox.setText(Integer.toString(initialSteps));
		stepsIncrementBox.setText(Integer.toString(stepsIncrement));
		topFracBox.setText(Double.toString(topFrac));
		bestFreqBox.setText(Double.toString(bestFreq));
		randFreqBox.setText(Double.toString(randFreq));
		bestMutFreqBox.setText(Double.toString(bestMutFreq));
		topMutFreqBox.setText(Double.toString(topMutFreq));
		
		player = new Player(start.getxCoord(),start.getyCoord(),10,10);
		
		displayBoard = new DisplayBoard(player, population, start, goal, board, walls, enemies, enemies2, selectedSprite);
		displayBoard.setSelectedShowAll(true);
		displayBoard.setPlayerTestingMode(false);
		
		contentPane.add(displayBoard, BorderLayout.CENTER);
	}
	
	/*
	 * New generation, population breeds and resets enemies path
	 */
	public void newGen() {
		population.breed(population.selectBest());
		population.setSpawn(start);
		resetEnemiesPath();
		gen++;
		dotsAlive = population.getPopulation().size();
		closest = (int) (Math.hypot(start.getxCoord()-goal.getxCoord(), start.getyCoord()-goal.getyCoord()));
		steps = 0;
		dotsFinished = 0;
	}
	
	
	
	
	
	
	
	/*
	 * Constructor, setups all user interface features like buttons, text fields, and sliders
	 */
	public App() {
		// Basic one time, setup commands
		setTitle("Dungeon Simulator");
		int screenWidth = 1200;
		int screenHeight = 800;
		boardWidth = 600;
		boardHeight = 600;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, screenWidth, screenHeight);
		setResizable(false);
		addKeyListener(this);
		addMouseListener(this);
		setFocusable(true);
		this.requestFocus();
		
		// Content pane, has everything on it
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(25, 25, 25, 25));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// Button panel on bottom of screen
		JPanel buttonPanel = new JPanel();
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		// Main timer to move AI, player, and enemies
		Timer timer = new Timer(20, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Move player
				if (displayBoard.isPlayerTestingMode()) {
					for (int i = 0; i < playerMoves.length+1; i++) {
						// 4 is for not moving collision detection
						if (i == 4 || playerMoves[i] == 1) {
							int playerSpeed = 2;
							Vector v = new Vector(0,0);
							if (i == 0) {
								v = new Vector(0,-1*playerSpeed);
							} else if (i == 1) {
								v = new Vector(0,playerSpeed);
							} else if (i == 2) {
								v = new Vector(-1*playerSpeed,0);
							} else if (i == 3) {
								v = new Vector(playerSpeed,0);
							}
							if (board.isInBounds(player,v)) {
								boolean blocked = false;
								for (Wall w : walls) {
									if (player.isInBounds(w.getxCoord(),w.getyCoord(),w.getWidth(),w.getHeight(),v)) {
										blocked = true;
										break;
									}
								}
								for (Enemy e : enemies) {
									if (player.isInBounds(e.getxCoord(),e.getyCoord(),e.getWidth(),e.getHeight(),v)) {
										blocked = true;
										resetEnemiesPath();
										player.setxCoord(start.getxCoord());
										player.setyCoord(start.getyCoord());
										break;
									}
								}
								for (Enemy2 e : enemies2) {
									if (player.isInBounds(e.getxCoord(),e.getyCoord(),e.getWidth(),e.getHeight(),v)) {
										blocked = true;
										resetEnemiesPath();
										player.setxCoord(start.getxCoord());
										player.setyCoord(start.getyCoord());
										break;
									}
								}
								if (!blocked) {
									player.setxCoord(player.getxCoord()+v.getX());
									player.setyCoord(player.getyCoord()+v.getY());
								}
								if (player.isInBounds(goal.getxCoord(),goal.getyCoord(),goal.getWidth(),goal.getHeight())) {
									// Player wins
								}
							}
						}
					}
				} else {
					// Update info text at the top
					generationText.setText("Generation: "+gen);
					dotsAliveText.setText("Dots Alive: "+dotsAlive);
					dotsFinishedText.setText("Dots Finished: "+dotsFinished);
					closestText.setText("Closest Dist: "+Math.round(closest));
					fastestText.setText("Fewest Steps: "+steps);
					
					if (dotsFinished == 0) {
						steps++;
					};
					
					// Iterate through all AI and move them correctly
					for (int i = 0; i < population.getPopulation().size(); i++) {
						AI current = population.getPopulation().get(i);
						double dist = Math.hypot(current.getxCoord()-goal.getxCoord(), current.getyCoord()-goal.getyCoord());
						if (dist < closest) {
							closest = dist;
						}
						if (!current.getIsDead() && !current.getReachedGoal()) {
							Vector v = current.getCurrentMove();
							if (board.isInBounds(current,v)) {
								boolean blocked = false;
								for (int j = 0; j < walls.size(); j++) {
									if (current.isInBounds(walls.get(j).getxCoord(), walls.get(j).getyCoord(), walls.get(j).getWidth(), walls.get(j).getHeight(),v)) {
										blocked = true;
										break;
									}
								}
								for (int j = 0; j < enemies.size(); j++) {
									if (current.isInBounds(enemies.get(j).getxCoord(), enemies.get(j).getyCoord(), enemies.get(j).getWidth(), enemies.get(j).getHeight(),v)) {
										current.setIsDead(true);
										dotsAlive--;
										break;
									}
								}
								for (int j = 0; j < enemies2.size(); j++) {
									if (current.isInBounds(enemies2.get(j).getxCoord(), enemies2.get(j).getyCoord(), enemies2.get(j).getWidth(), enemies2.get(j).getHeight(),v)) {
										current.setIsDead(true);
										dotsAlive--;
										break;
									}
								}
								if (!blocked) {
									current.setxCoord(current.getxCoord() + v.getX());
									current.setyCoord(current.getyCoord() + v.getY());
								}
								if (current.isInBounds(goal.getxCoord(), goal.getyCoord(), goal.getWidth(), goal.getHeight())) {
									// AI reached goal so calculate fitness
									current.setReachedGoal(true);
									dotsFinished++;
									closest = 0;
									// Fewer steps taken, higher fitness
									current.setFitness(100000 + 100000000/(current.getStep()*current.getStep()));
								}
							}
						} 
						if (current.getIsDead() && !current.getReachedGoal()) {
							current.setFitness(10000 / ((dist/2) * (dist/2)));
						}
					}
					
					// Check if all AI died or ran out of steps
					if (population.isDone()) {
						newGen();
					}
				}
				
				
				
				// Fires every other, handles all enemies movement
				if (enemyTimer % 2 == 0) {
					// Moves sentries
					for (int i = 0; i < enemies.size(); i++) {
						Enemy current = enemies.get(i);
						Vector v = current.getVector();
						double routeDistance = Math.hypot(current.getX1() - current.getX2(), current.getY1() - current.getY2());
						if (current.isForward()) {
							if (Math.hypot(current.getxCoord() - current.getX2(),current.getyCoord() - current.getY2()) < 10 + (routeDistance / 20)) {
								current.setForward(false);
							}
							current.setxCoord(current.getxCoord() + v.getX());
							current.setyCoord(current.getyCoord() + v.getY());
						} else {
							if (Math.hypot(current.getxCoord() - current.getX1(),current.getyCoord() - current.getY1()) < 10 + (routeDistance / 20)){
								current.setForward(true);
							}
							current.setxCoord(current.getxCoord() - v.getX());
							current.setyCoord(current.getyCoord() - v.getY());
						}
						
					}
					// Moves spinners
					for (int i = 0; i < enemies2.size(); i++) {
						Enemy2 current = enemies2.get(i);
						Vector v = current.getNextMove(enemyTimer);
						current.setxCoord(current.getCenterX() + v.getX());
						current.setyCoord(current.getCenterY() + v.getY());
					}
				}
				if (appRunning) {
					enemyTimer++;
				}
				// Runs paintComponent in DisplayBoard to draw everything
				repaint();
			}
			
		});
		
		
		
		
		// Bottom GUI
		
		
		
		// Show All Dots check box
		JCheckBox showAllOrOneBox = new JCheckBox("Show All Dots");
		showAllOrOneBox.setSelected(true);
		buttonPanel.add(showAllOrOneBox);
		showAllOrOneBox.setToolTipText("Toggle if all dots or just the best is shown");
		showAllOrOneBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				if (showAllOrOneBox.isSelected()) {
					displayBoard.setSelectedShowAll(true);
					repaint();
				} else {
					displayBoard.setSelectedShowAll(false);
					repaint();
				}
			}
		});
		
		// Player speed slider
		JSlider playerSpeed = new JSlider(JSlider.HORIZONTAL,1,5,3);
		playerSpeed.setMinorTickSpacing(1);
		playerSpeed.setMajorTickSpacing(1);
		playerSpeed.setPaintLabels(true);
		playerSpeed.setPaintTicks(true);
		playerSpeed.setSnapToTicks(true);
		playerSpeed.setToolTipText("Adjust the speed of the simulation");
		playerSpeed.setBackground(new Color(230,230,230));
		playerSpeed.addChangeListener(new ChangeListener() {
		        public void stateChanged(ChangeEvent ce) {
		        	resetFocus();
		            timer.setDelay(10*(5 - ((JSlider) ce.getSource()).getValue()));
		            repaint();
		        }
		    });
		buttonPanel.add(playerSpeed, BorderLayout.WEST);
		
		// Player testing mode check box
		JCheckBox playerTestBox = new JCheckBox("Player Testing Mode");
		playerTestBox.setSelected(false);
		buttonPanel.add(playerTestBox);
		playerTestBox.setToolTipText("<html>Toggle player test mode versus AI learning mode."
				+ "<br>Player Test Mode: Control a blue square using arrow keys to test the level."
				+ "<br>AI Learning Mode: Watch red AI dots learn how to solve the level.</html>");
		playerTestBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				if (playerTestBox.isSelected()) {
					displayBoard.setPlayerTestingMode(true);
					resetEnemiesPath();
					player.setxCoord(start.getxCoord());
					player.setyCoord(start.getyCoord());
					repaint();
				} else {
					displayBoard.setPlayerTestingMode(false);
					repaint();
				}
			}
		});
		
		// Start and stop button
		JButton startButton = new JButton("Start");
		buttonPanel.add(startButton);
		startButton.setFont(new Font("Arial",1,20));
		startButton.setBackground(new Color(81, 214, 81));
		startButton.setToolTipText("Start and stop the simulation");
		startButton.setPreferredSize(new Dimension(80, 40));
		startButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				if (appRunning) {
					appRunning = false;
					startButton.setText("Start");
					startButton.setBackground(new Color(81, 214, 81));
					timer.stop();
				} else {
					appRunning = true;
					startButton.setText("Stop");
					startButton.setBackground(new Color(224, 76, 76));
					timer.start();
				}
			}
			
		});
		
		// Delete all sprites button
		JButton deleteAllButton = new JButton("Delete All Sprites");
		buttonPanel.add(deleteAllButton);
		deleteAllButton.setToolTipText("Delete all walls, sentries, spinners, and boards");
		deleteAllButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				walls.clear();
				enemies.clear();
				enemies2.clear();
				board.clear();
				resetSelection();
				repaint();
				JOptionPane.showMessageDialog(null,"Deleted all sprites successfully!");
			}
			
		});
		
		// Reset the program button
		JButton resetAllButton = new JButton("Reset All");
		buttonPanel.add(resetAllButton);
		resetAllButton.setToolTipText("Reset the entire program completely");
		resetAllButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				contentPane.remove(displayBoard);
				resetSprites();
				resetEnemiesPath();
				resetPop(200,25,20,0.5,0.5,0.2,0.10,0.15);
				resetSelection();
				startButton.setText("Start");
				startButton.setBackground(new Color(81, 214, 81));
				showAllOrOneBox.setSelected(true);
				playerTestBox.setSelected(false);
				generationText.setText("Generation: "+gen);
				dotsAliveText.setText("Dots Alive: "+dotsAlive);
				dotsFinishedText.setText("Dots Finished: "+dotsFinished);
				closestText.setText("Closest Dist: "+Math.round(closest));
				fastestText.setText("Fewest Steps: "+steps);
				timer.stop();
				repaint();
				JOptionPane.showMessageDialog(null,"Reset program successfully!");
			}
			
		});
		
		// Export the level button
		// Copies a code to the clipboard
		JButton codeButton = new JButton("Export Level");
		buttonPanel.add(codeButton);
		codeButton.setToolTipText("<html>Export the level"
				+ "<br>A code that can be used to import it will be copied to the clipboard</html>");
		codeButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				String code = "";
				code += "S"+"X"+start.getxCoord()+"Y"+start.getyCoord()+"Z";
				code += "G"+"X"+goal.getxCoord()+"Y"+goal.getyCoord()+"Z";
				for (Rectangle r : board.getRects()) {
					code += "B"+"X"+r.getxCoord()+"Y"+r.getyCoord()+"W"+r.getWidth()+"H"+r.getHeight()+"Z";
				}
				for (Wall w : walls) {
					code += "V"+"X"+w.getxCoord()+"Y"+w.getyCoord()+"W"+w.getWidth()+"H"+w.getHeight()+"Z";
				}
				for (Enemy e : enemies) {
					code += "E"+"M"+e.getX1()+"N"+e.getY1()+"J"+e.getX2()+"I"+e.getY2()+"W"+e.getWidth()+"H"+e.getHeight()+"D"+e.getSpeed()+"Z";
				}
				for (Enemy2 e : enemies2) {
					code += "P"+"C"+e.getCenterX()+"K"+e.getCenterY()+"W"+e.getWidth()+"H"+e.getHeight()+"D"+e.getSpeed()+"R"+e.getRadius()+"Z";
				}
				StringSelection formattedCode = new StringSelection(code.substring(0,code.length()-1));
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(formattedCode, null);
				JOptionPane.showMessageDialog(null,"The code for the current level has been copied to the clipboard");
			}
			
		});
		
		// Import text
		JTextArea importText = new JTextArea(
				"Import Level:",
				1,3
		);
		importText.setToolTipText("Import a level using its code");
		importText.setFont(new Font("Arial",1,14));
		importText.setBackground(new Color(230,230,230));
		buttonPanel.add(importText);
		
		// Import code box
		JTextField codeBox = new JTextField(10);
		buttonPanel.add(codeBox);
		codeBox.setToolTipText("Paste code in this box and press enter");
		codeBox.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				contentPane.remove(displayBoard);
				try {
					String[] sprites = codeBox.getText().split("Z");
					board.clear();
					walls.clear();
					enemies.clear();
					enemies2.clear();
					for (String s : sprites) {
						char type = s.charAt(0);
						String[] values;
						if (type == 'S') {
							values = s.substring(1).split("X|Y");
							start = new Start(Integer.parseInt(values[1]),Integer.parseInt(values[2]),10,10);
						} else if (type == 'G') {
							values = s.substring(1).split("X|Y");
							goal = new Goal(Integer.parseInt(values[1]),Integer.parseInt(values[2]),10,10);
						} else if (type == 'B') {
							values = s.substring(1).split("X|Y|W|H");
							board.addRectangle(Integer.parseInt(values[1]),Integer.parseInt(values[2]),Integer.parseInt(values[3]),Integer.parseInt(values[4]));
						} else if (type == 'V') {
							values = s.substring(1).split("X|Y|W|H");
							walls.add(new Wall(Integer.parseInt(values[1]),Integer.parseInt(values[2]),Integer.parseInt(values[3]),Integer.parseInt(values[4])));
						} else if (type == 'E') {
							values = s.substring(1).split("M|N|J|I|W|H|D");
							enemies.add(new Enemy(Integer.parseInt(values[5]),Integer.parseInt(values[6]),Integer.parseInt(values[1]),Integer.parseInt(values[2]),Integer.parseInt(values[3]),Integer.parseInt(values[4]),Integer.parseInt(values[7])));
						} else if (type == 'P') {
							values = s.substring(1).split("C|K|W|H|D|R");
							enemies2.add(new Enemy2(Integer.parseInt(values[1]),Integer.parseInt(values[2]),Integer.parseInt(values[3]),Integer.parseInt(values[4]),Integer.parseInt(values[6]),Integer.parseInt(values[5])));
						} else {
							contentPane.add(displayBoard, BorderLayout.CENTER);
							codeBox.setText("");
							JOptionPane.showMessageDialog(null,"Code could not be read","Invalid Input", JOptionPane.WARNING_MESSAGE);
							repaint();
							return;
						}
					}
					boolean oldSetting = displayBoard.getSelectedShowAll();
					boolean oldSetting2 = displayBoard.isPlayerTestingMode();
					player.setxCoord(start.getxCoord());
					player.setyCoord(start.getyCoord());
					displayBoard = new DisplayBoard(player, population, start, goal, board, walls, enemies, enemies2, selectedSprite);
					displayBoard.setSelectedShowAll(oldSetting);
					displayBoard.setPlayerTestingMode(oldSetting2);
					
					contentPane.add(displayBoard, BorderLayout.CENTER);
					resetSelection();
					startButton.setText("Start");
					startButton.setBackground(new Color(81, 214, 81));
					appRunning = false;
					timer.stop();
					applyChangesToAlgorithm();
					JOptionPane.showMessageDialog(null,"Imported level successfully!");
					codeBox.setText("");
					repaint();
				} catch (Exception e) {
					contentPane.add(displayBoard, BorderLayout.CENTER);
					codeBox.setText("");
					JOptionPane.showMessageDialog(null,"Code could not be read","Invalid Input", JOptionPane.WARNING_MESSAGE);
					repaint();
				}
			}
		});
		
		
		
		
		// Information text at the top
		
		
		
		
		
		JPanel displayPane = new JPanel();
		contentPane.add(displayPane, BorderLayout.NORTH);
		displayPane.setBackground(new Color(230,230,230));
		Border raisedBorder = BorderFactory.createRaisedBevelBorder();
		Border b1 = new EmptyBorder(5,25,5,25);
		displayPane.setBorder(BorderFactory.createCompoundBorder(raisedBorder,b1));
		Font displayFont = new Font("Arial",0,24);
		
		// Generation text
		generationText = new JTextField();
		generationText.setText("Generation: "+gen);
		generationText.setToolTipText("The current generation of AI dots the simulation is at");
		generationText.setColumns(10);
		generationText.setFont(displayFont);
		generationText.setEditable(false);
		displayPane.add(generationText);
		
		// Dots alive text
		dotsAliveText = new JTextField();
		dotsAliveText.setText("Dots Alive: "+dotsAlive);
		dotsAliveText.setToolTipText("The number of AI dots alive this generation");
		dotsAliveText.setColumns(10);
		dotsAliveText.setFont(displayFont);
		dotsAliveText.setEditable(false);
		displayPane.add(dotsAliveText);
		
		// Dots finished text
		dotsFinishedText = new JTextField();
		dotsFinishedText.setText("Dots Finished: "+dotsFinished);
		dotsFinishedText.setToolTipText("The number of dots that have reached the goal this generation");
		dotsFinishedText.setColumns(10);
		dotsFinishedText.setFont(displayFont);
		dotsFinishedText.setEditable(false);
		displayPane.add(dotsFinishedText);
		
		// Closest distance text
		closestText = new JTextField();
		closestText.setText("Closest Dist: "+closest);
		closestText.setToolTipText("The distance from the goal that the closest AI dot this generation reached. Zero if goal reached");
		closestText.setColumns(10);
		closestText.setFont(displayFont);
		closestText.setEditable(false);
		displayPane.add(closestText);
		
		// Fewest steps text
		fastestText = new JTextField();
		fastestText.setText("Fewest Steps: "+steps);
		fastestText.setToolTipText("The number of steps the fastest AI dot took to get to the goal");
		fastestText.setColumns(10);
		fastestText.setFont(displayFont);
		fastestText.setEditable(false);
		displayPane.add(fastestText);
		
		
		
		// Right panel
		
		
		
		// Panel for all the selected sprite options
		JPanel controlPane = new JPanel();
		contentPane.add(controlPane, BorderLayout.EAST);
		controlPane.setBackground(new Color(230,230,230));
		controlPane.setLayout(new BoxLayout(controlPane, BoxLayout.Y_AXIS));
		
		JPanel fieldPane = new JPanel(new GridLayout(4,4,20,20));
		fieldPane.setBackground(new Color(230,230,230));
		Border b2 = new EmptyBorder(25,40,25,40);
		fieldPane.setBorder(BorderFactory.createCompoundBorder(raisedBorder,b2));
		Font controlFont = new Font("Arial", 1, 24);
		
		// 1: Name of selected sprite
		selectionName = new JTextArea(
				"",
				1,3
		);
		selectionName.setToolTipText("<html>Selected Sprite"
				+ "<br>(Click on a sprite in the simulation window to select)</html>");
		selectionName.setBackground(new Color(230,230,230));
		selectionName.setFont(controlFont);
		fieldPane.add(selectionName, BorderLayout.EAST);
		
		
		// 2: Deselect button
		deselectButton = new JButton("Deselect");
		deselectButton.setToolTipText("Deselect this sprite");
		deselectButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				resetSelection();
			}
			
		});
		fieldPane.add(deselectButton);
		
		// 3: Delete button
		deleteButton = new JButton("Delete");
		deleteButton.setToolTipText("Delete this sprite");
		deleteButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				if (selectedSprite instanceof Enemy) {
					enemies.remove(selectedSprite);
				} else if (selectedSprite instanceof Enemy2) {
					enemies2.remove(selectedSprite);
				} else if (selectedSprite instanceof Wall) {
					walls.remove(selectedSprite);
				}
				resetSelection();
				repaint();
			}
			
		});
		fieldPane.add(deleteButton);
		
		// 4: Copy button
		copyButton = new JButton("Copy");
		copyButton.setToolTipText("Make an exact copy of this sprite");
		copyButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				if (selectedSprite instanceof Enemy) {
					enemies.add(new Enemy(selectedSprite.getWidth(), selectedSprite.getHeight(), ((Enemy) selectedSprite).getX1(), ((Enemy) selectedSprite).getY1(),
										  ((Enemy) selectedSprite).getX2(), ((Enemy) selectedSprite).getY2(), ((Enemy) selectedSprite).getSpeed()));
				} else if (selectedSprite instanceof Enemy2) {
					enemies2.add(new Enemy2(((Enemy2) selectedSprite).getCenterX(), ((Enemy2) selectedSprite).getCenterY(), selectedSprite.getWidth(),
											selectedSprite.getHeight(), ((Enemy2) selectedSprite).getRadius(), ((Enemy2) selectedSprite).getSpeed()));
				} else if (selectedSprite instanceof Wall) {
					walls.add(new Wall(selectedSprite.getxCoord(), selectedSprite.getyCoord(), selectedSprite.getWidth(), selectedSprite.getHeight()));
				} else if (selectedSprite instanceof Rectangle) {
					board.addRectangle(selectedSprite.getxCoord(), selectedSprite.getyCoord(), selectedSprite.getWidth(), selectedSprite.getHeight());
				}
				repaint();
			}
			
		});
		fieldPane.add(copyButton);
		
		// 5: X coord text
		xCoordText = new JTextArea(
				"X: 999",
				1,3
		);
		xCoordText.setToolTipText("X coordinate of sprite");
		xCoordText.setBackground(new Color(230,230,230));
		xCoordText.setFont(controlFont);
		fieldPane.add(xCoordText);
		
		// 6: X2 coord text
		x2CoordText = new JTextArea(
				"X2: 999",
				1,3
		);
		x2CoordText.setToolTipText("X coordinate of the second point of the sprite");
		x2CoordText.setBackground(new Color(230,230,230));
		x2CoordText.setFont(controlFont);
		fieldPane.add(x2CoordText);
		
		// 7: Width text
		widthText = new JTextArea(
				"Width:",
				1,3
		);
		widthText.setToolTipText("Width of sprite");
		widthText.setBackground(new Color(230,230,230));
		widthText.setFont(controlFont);
		fieldPane.add(widthText);
		
		// 8: Width box
		
		widthBox = new JTextField(5);
		widthBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				try {
					selectedSprite.setWidth(Integer.parseInt(widthBox.getText()));
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Width must be an integer","Invalid Input", JOptionPane.WARNING_MESSAGE);
				}
				repaint();
			}
		});
		fieldPane.add(widthBox);
		
		
		
		// 9: Y coord text
		yCoordText = new JTextArea(
				"Y: 999",
				1,3
		);
		yCoordText.setToolTipText("Y coordinate of sprite");
		yCoordText.setBackground(new Color(230,230,230));
		yCoordText.setFont(controlFont);
		fieldPane.add(yCoordText);
		
		// 10: Y2 coord text
		y2CoordText = new JTextArea(
				"Y2: 999",
				1,3
		);
		y2CoordText.setToolTipText("Y coordinate of the second point of the sprite");
		y2CoordText.setBackground(new Color(230,230,230));
		y2CoordText.setFont(controlFont);
		fieldPane.add(y2CoordText);
		
		// 11: Height text
		heightText = new JTextArea(
				"Height:",
				1,3
		);
		heightText.setToolTipText("Height of sprite");
		heightText.setBackground(new Color(230,230,230));
		heightText.setFont(controlFont);
		fieldPane.add(heightText);
		
		// 12: Height box
		
		heightBox = new JTextField(5);
		heightBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				try {
					selectedSprite.setHeight(Integer.parseInt(heightBox.getText()));
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Height must be an integer","Invalid Input", JOptionPane.WARNING_MESSAGE);
				}
				repaint();
			}
		});
		fieldPane.add(heightBox);
		
		// 13: Move sprite button
		moveButton = new JButton("Move");
		moveButton.setToolTipText("Press and then click on a spot to move the sprite to that exact location (uses top left corner to position)");
		moveButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				selectingSpot = true;
				if (selectedSprite instanceof Enemy) {
					moveButton.setText("Moving P1");
				} else if (selectedSprite instanceof Enemy2) {
					moveButton.setText("Moving C...");
				} else {
					moveButton.setText("Moving...");
				}
				move2Button.setText("Move P2");
				coordAdjustX = 0;
				coordAdjustY = 0;
				isEnd = false;
			}
			
		});
		fieldPane.add(moveButton);
		
		// 14: Move second point of sprite button
		move2Button = new JButton("Move P2");
		move2Button.setToolTipText("Press and then click on a spot to move the second point of the sprite to that location");
		move2Button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				selectingSpot = true;
				move2Button.setText("Moving P2");
				moveButton.setText("Move");
				coordAdjustX = 0;
				coordAdjustY = 0;
				isEnd = true;
			}
			
		});
		fieldPane.add(move2Button);
		
		// 15: Radius text
		radiusText = new JTextArea(
				"Radius:",
				1,3
		);
		radiusText.setToolTipText("Radius of the revolving sprite");
		radiusText.setBackground(new Color(230,230,230));
		radiusText.setFont(controlFont);
		fieldPane.add(radiusText);
		
		// 16: Radius box
		radiusBox = new JTextField(5);
		radiusBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				try {
					((Enemy2) selectedSprite).setRadius(Integer.parseInt(radiusBox.getText()));
					Vector v = ((Enemy2) selectedSprite).getNextMove(enemyTimer);
					selectedSprite.setxCoord(((Enemy2) selectedSprite).getCenterX() + v.getX());
					selectedSprite.setyCoord(((Enemy2) selectedSprite).getCenterY() + v.getY());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Radius must be an integer","Invalid Input", JOptionPane.WARNING_MESSAGE);
				}
				repaint();
			}
		});
		fieldPane.add(radiusBox);
		
		controlPane.add(fieldPane);
		
		// Enemy speed slider
		enemySpeed = new JSlider(JSlider.HORIZONTAL,0,5,1);
		enemySpeed.setMinorTickSpacing(1);
		enemySpeed.setMajorTickSpacing(1);
		enemySpeed.setPaintLabels(true);
		enemySpeed.setPaintTicks(true);
		enemySpeed.setSnapToTicks(true);
		enemySpeed.setToolTipText("Speed of the sprite");
		enemySpeed.setBackground(new Color(230,230,230));
		enemySpeed.addChangeListener(new ChangeListener() {
		        public void stateChanged(ChangeEvent ce) {
		        	resetFocus();
		        	if (selectedSprite instanceof Enemy) {
		        		((Enemy) selectedSprite).setSpeed(((JSlider) ce.getSource()).getValue());
		        	} else if (selectedSprite instanceof Enemy2) {
		        		((Enemy2) selectedSprite).setSpeed(((JSlider) ce.getSource()).getValue());
		        	} else {
		        		System.out.println("Slider Error");
		        	}
		            repaint();
		        }
		    });
		Border b3 = new EmptyBorder(5,25,5,25);
		enemySpeed.setBorder(BorderFactory.createCompoundBorder(raisedBorder,b3));
		controlPane.add(enemySpeed, BorderLayout.WEST);
		
		// Add sprites button panel
		JPanel fieldPane2 = new JPanel();
		fieldPane2.setBounds(100,100,150,200);
		fieldPane2.setBackground(new Color(230,230,230));
		Border b4 = new EmptyBorder(20,0,0,0);
		fieldPane2.setBorder(BorderFactory.createCompoundBorder(raisedBorder,b4));
		
		// Add wall button
		JButton addWallButton = new JButton("Add Wall");
		addWallButton.setToolTipText("<html>Generates a new wall sprite."
				+ "<br>Walls are black rectangles that the AI dots cannot pass through."
				+ "<br>Their positioning, width, and height can all be adjusted</html>");
		addWallButton.setPreferredSize(new Dimension(110, 40));
		addWallButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				walls.add(new Wall(boardWidth/2,boardHeight/2,200,20));
				selectedSprite = walls.get(walls.size()-1);
				resetSelection();
				displayBoard.setSelectedSprite(selectedSprite);
				selectionName.setText("Wall");
				selectedRect();
				repaint();
			}
			
		});
		fieldPane2.add(addWallButton);
		
		// Add sentry button
		JButton addEnemyButton = new JButton("Add Sentry");
		addEnemyButton.setToolTipText("<html>Generates a new sentry sprite"
				+ "<br>Sentries are blue circles that constantly travel between two points."
				+ "<br>AI dots die upon touching the blue circle."
				+ "<br>The simulation also has gray circles to represent the two points."
				+ "<br>Both their points, their width, height, and speed can all be adjusted</html>");
		addEnemyButton.setPreferredSize(new Dimension(110, 40));
		addEnemyButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				enemies.add(new Enemy(10,10,boardWidth/3,boardHeight/3,2*boardWidth/3,2*boardHeight/3,1));
				selectedSprite = enemies.get(enemies.size()-1);
				resetSelection();
				displayBoard.setSelectedSprite(selectedSprite);
				selectedEnemy();
				repaint();
			}
			
		});
		fieldPane2.add(addEnemyButton);
		
		// Add spinner button
		JButton addEnemy2Button = new JButton("Add Spinner");
		addEnemy2Button.setToolTipText("<html>Generates a new spinner sprite"
				+ "<br>Spinners are green circles that constantly revolve around a point."
				+ "<br>AI dots die upon touching the green circle."
				+ "<br>The simulation also has a gray circle to represent the center point"
				+ "<br>Their center point, width, height, speed, and radius can all be adjusted</html>");
		addEnemy2Button.setPreferredSize(new Dimension(110, 40));
		addEnemy2Button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				enemies2.add(new Enemy2(boardWidth/2,boardHeight/2,10,10,50,1));
				selectedSprite = enemies2.get(enemies2.size()-1);
				resetSelection();
				displayBoard.setSelectedSprite(selectedSprite);
				selectedEnemy2();
				repaint();
			}
			
		});
		fieldPane2.add(addEnemy2Button);
		
		// Add board button
		JButton addBoardButton = new JButton("Add Board");
		addBoardButton.setToolTipText("<html>Generates a new board sprite"
				+ "<br>Boards are white rectangles that represent the area the AI dots can move in."
				+ "<br>*Boards cannot be dragged to be repositioned</html>");
		addBoardButton.setPreferredSize(new Dimension(110, 40));
		addBoardButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				board.addRectangle(boardWidth/2,boardHeight/2,boardWidth/4,boardHeight/4);
				selectedSprite = board.getRects().get(board.getRects().size()-1);
				resetSelection();
				displayBoard.setSelectedSprite(selectedSprite);
				selectionName.setText("Board");
				selectedRect();
				repaint();
			}
			
		});
		fieldPane2.add(addBoardButton);
		
		controlPane.add(fieldPane2);
		
		// Panel for algorithm customization
		JPanel fieldPane3 = new JPanel(new GridLayout(4,4,20,10));
		fieldPane3.setBackground(new Color(230,230,230));
		Border b5 = new EmptyBorder(15,15,15,15);
		fieldPane3.setBorder(BorderFactory.createCompoundBorder(raisedBorder,b5));
		Font popFont = new Font("Arial",1,12);
		
		// 1: Population size text
		JTextArea maxPopText = new JTextArea(
				"Population Size:",
				1,3
		);
		maxPopText.setToolTipText("Number of AI dots per population");
		maxPopText.setBackground(new Color(230,230,230));
		maxPopText.setFont(popFont);
		fieldPane3.add(maxPopText);
		
		// 2: Population size box
		maxPopBox = new JTextField(5);
		maxPopBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				try {
					if (Integer.parseInt(maxPopBox.getText()) < 10 || Integer.parseInt(maxPopBox.getText()) > 500) {
						JOptionPane.showMessageDialog(null,"Population size must be an integer between 10 and 500 inclusive","Invalid Input", JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Population size must be an integer between 10 and 500 inclusive","Invalid Input", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		fieldPane3.add(maxPopBox);
		
		// 3: Best frequency text
		JTextArea bestFreqText = new JTextArea(
				"Best Freq:",
				1,3
		);
		bestFreqText.setToolTipText("<html>Frequency of AI dots inheriting the best AI dot's steps from previous generation"
				+ "<br>For example, a value of 0.5 means that 50% of the AI dots inherit steps from the best of the previous generation"
				+ "<br>Best frequency plus random frequency cannot be greater than 1.0</html>");
		bestFreqText.setBackground(new Color(230,230,230));
		bestFreqText.setFont(popFont);
		fieldPane3.add(bestFreqText);
		
		// 4: Best frequency box
		bestFreqBox = new JTextField(5);
		bestFreqBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				try {
					if (Double.parseDouble(bestFreqBox.getText()) < 0 || Double.parseDouble(bestFreqBox.getText()) > 1) {
						JOptionPane.showMessageDialog(null,"Best frequency must be between 0.0 and 1.0 inclusive","Invalid Input", JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Best frequency must be between 0.0 and 1.0 inclusive","Invalid Input", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		fieldPane3.add(bestFreqBox);
		
		// 5: Initial steps text
		JTextArea initialStepsText = new JTextArea(
				"Initial Steps:",
				1,3
		);
		initialStepsText.setToolTipText("Number of steps the first generation can take");
		initialStepsText.setBackground(new Color(230,230,230));
		initialStepsText.setFont(popFont);
		fieldPane3.add(initialStepsText);
		
		// 6: Initial steps box
		initialStepsBox = new JTextField(5);
		initialStepsBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				try {
					if (Integer.parseInt(initialStepsBox.getText()) < 0) {
						JOptionPane.showMessageDialog(null,"Initial steps must be an integer greater than or equal to 0","Invalid Input", JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Initial steps must be an integer greater than or equal to 0","Invalid Input", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		fieldPane3.add(initialStepsBox);
		
		// 7: Random frequency text
		JTextArea randFreqText = new JTextArea(
				"Random Freq:",
				1,3
		);
		randFreqText.setToolTipText("<html>Frequency of AI dots that have completely random steps"
				+ "<br>For example, a value of 0.2 means that 20% of the AI dots will have completely random steps"
				+ "<br>Best frequency plus random frequency cannot be greater than 1.0</html>");
		randFreqText.setBackground(new Color(230,230,230));
		randFreqText.setFont(popFont);
		fieldPane3.add(randFreqText);
		
		// 8: Random frequency box
		randFreqBox = new JTextField(5);
		randFreqBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				try {
					if (Double.parseDouble(randFreqBox.getText()) < 0 || Double.parseDouble(randFreqBox.getText()) > 1) {
						JOptionPane.showMessageDialog(null,"Random frequency must be between 0.0 and 1.0 inclusive","Invalid Input", JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Random frequency must be between 0.0 and 1.0 inclusive","Invalid Input", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		fieldPane3.add(randFreqBox);
		
		// 9: Steps increment text
		JTextArea stepsIncrementText = new JTextArea(
				"Steps Increment:",
				1,3
		);
		stepsIncrementText.setToolTipText("Number of extra steps each AI dot can take upon every new generation");
		stepsIncrementText.setBackground(new Color(230,230,230));
		stepsIncrementText.setFont(popFont);
		fieldPane3.add(stepsIncrementText);
		
		// 10: Steps increment box
		stepsIncrementBox = new JTextField(5);
		stepsIncrementBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				try {
					if (Integer.parseInt(stepsIncrementBox.getText()) < 0) {
						JOptionPane.showMessageDialog(null,"Steps increment must be an integer greater than or equal to 0","Invalid Input", JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Steps increment must be an integer greater than or equal to 0","Invalid Input", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		fieldPane3.add(stepsIncrementBox);
		
		// 11: Best mutation frequency text
		JTextArea bestMutFreqText = new JTextArea(
				"Best Mutation Freq:",
				1,3
		);
		bestMutFreqText.setToolTipText("<html>Frequency of mutations for the AI dots that inherit from the previous best.<br>These mutations randomly alter the steps of the AI dots. "
				+ "<br>For example, a value of 0.10 would roughly alter 10% of an AI dots steps that inherited from the previous best."
				+ "<br>The chance of mutating a step increases the later the step is</html>");
		bestMutFreqText.setBackground(new Color(230,230,230));
		bestMutFreqText.setFont(popFont);
		fieldPane3.add(bestMutFreqText);
		
		// 12: Best mutation frequency box
		bestMutFreqBox = new JTextField(5);
		bestMutFreqBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				try {
					if (Double.parseDouble(bestMutFreqBox.getText()) < 0 || Double.parseDouble(bestMutFreqBox.getText()) > 1) {
						JOptionPane.showMessageDialog(null,"Best mutation frequency must be between 0.0 and 1.0 inclusive","Invalid Input", JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Best mutation frequency must be between 0.0 and 1.0 inclusive","Invalid Input", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		fieldPane3.add(bestMutFreqBox);
		
		// 13: Top fraction text
		JTextArea topFracText = new JTextArea(
				"Top Fraction:",
				1,3
		);
		topFracText.setToolTipText("<html>Fraction of the best AI dots from the previous generation.<br>These have a chance to have their steps be inherited in the next generation. "
				+ "<br>For example, a value of 0.5 means the top 50% of all AI dots when sorted by their performance can influence the next generation.</html>");
		topFracText.setBackground(new Color(230,230,230));
		topFracText.setFont(popFont);
		fieldPane3.add(topFracText);
		
		// 14: Top fraction box
		topFracBox = new JTextField(5);
		topFracBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				try {
					if (Double.parseDouble(topFracBox.getText()) < 0 || Double.parseDouble(topFracBox.getText()) > 1) {
						JOptionPane.showMessageDialog(null,"Top fraction must be between 0.0 and 1.0 inclusive","Invalid Input", JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Top fraction must be between 0.0 and 1.0 inclusive","Invalid Input", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		fieldPane3.add(topFracBox);
	
		// 15: Top mutation frequency text
		JTextArea topMutFreqText = new JTextArea(
				"Top Mutation Freq:",
				1,3
		);
		topMutFreqText.setToolTipText("<html>Frequency of mutations for the AI dots that inherit from the top fraction of the previous generation.<br>These mutations randomly alter the steps of the AI dots. " 
								+ "<br>For example, a value of 0.10 would roughly alter 10% of an AI dots steps that was chosen from the top fraction."
								+ "<br>The chance of mutating a step increases the later the step is</html>");
		topMutFreqText.setBackground(new Color(230,230,230));
		topMutFreqText.setFont(popFont);
		fieldPane3.add(topMutFreqText);
		
		// 16: top mutation frequency box
		topMutFreqBox = new JTextField(5);
		topMutFreqBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				try {
					if (Double.parseDouble(topMutFreqBox.getText()) < 0 || Double.parseDouble(topMutFreqBox.getText()) > 1) {
						JOptionPane.showMessageDialog(null,"Top mutation frequency must be between 0.0 and 1.0 inclusive","Invalid Input", JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Top mutation frequency must be between 0.0 and 1.0 inclusive","Invalid Input", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		fieldPane3.add(topMutFreqBox);
		
		controlPane.add(fieldPane3);
		
		// Small button panel under algorithm customization fields
		JPanel buttonPanel2 = new JPanel();
		controlPane.add(buttonPanel2);
		
		// Default values for algorithm button
		JButton defaultLearningButton = new JButton("Back to Default Values");
		defaultLearningButton.setToolTipText("Fills all the above boxes with the default values. Press 'Apply Changes' to reset learning");
		defaultLearningButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				maxPopBox.setText("200");
				initialStepsBox.setText("25");
				stepsIncrementBox.setText("20");
				topFracBox.setText("0.5");
				bestFreqBox.setText("0.5");
				randFreqBox.setText("0.2");
				bestMutFreqBox.setText("0.10");
				topMutFreqBox.setText("0.15");
			}
			
		});
		buttonPanel2.add(defaultLearningButton);
		
		// Apply changes to algorithm
		JButton applyChangesButton = new JButton("Apply Changes (Resets Learning Process)");
		applyChangesButton.setToolTipText("<html>Creates a new learning algorithm using the values provided above.<br>WARNING: Resets the learning and the simulation</html>");
		applyChangesButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				resetFocus();
				applyChangesToAlgorithm();
				startButton.setText("Start");
				startButton.setBackground(new Color(81, 214, 81));
				timer.stop();
			}
			
		});
		buttonPanel2.add(applyChangesButton);
		resetSprites();
		resetEnemiesPath();
		resetPop(200,25,20,0.5,0.5,0.2,0.10,0.15);
		resetSelection();
		
	}
	
	
	// Selection methods (mostly to reduce repeating code)
	
	
	
	/*
	 * Applies the changes to the algorithm, makes new population
	 */
	public void applyChangesToAlgorithm() {
		contentPane.remove(displayBoard);
		try {
			int maxPop = Integer.parseInt(maxPopBox.getText());
			int initialSteps = Integer.parseInt(initialStepsBox.getText());
			int stepsIncrement = Integer.parseInt(stepsIncrementBox.getText());
			double topFrac = Double.parseDouble(topFracBox.getText());
			double bestFreq = Double.parseDouble(bestFreqBox.getText());
			double randFreq = Double.parseDouble(randFreqBox.getText());
			double bestMutFreq = Double.parseDouble(bestMutFreqBox.getText());
			double topMutFreq = Double.parseDouble(topMutFreqBox.getText());
			if (maxPop < 10 || maxPop > 500 || initialSteps < 0 || stepsIncrement < 0 || topFrac < 0 || topFrac > 1 ||
				bestFreq < 0 || bestFreq > 1 || randFreq < 0 || randFreq > 1 || bestMutFreq < 0 || bestMutFreq > 1 || topMutFreq < 0 || topMutFreq > 1) {
				JOptionPane.showMessageDialog(null,"Check your inputs","Invalid Input", JOptionPane.WARNING_MESSAGE);
				contentPane.add(displayBoard);
				return;
			}
			resetEnemiesPath();
			resetPop(maxPop,initialSteps,stepsIncrement,topFrac,bestFreq,randFreq,bestMutFreq,topMutFreq);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Check your inputs","Invalid Input", JOptionPane.WARNING_MESSAGE);
			contentPane.add(displayBoard);
			return;
		}
		resetSelection();
		appRunning = false;
		repaint();
	}
	/*
	 * Brings the focus back to screen after pressing a button, slider, or text field so that the key presses can be picked up
	 */
	public void resetFocus() {
		this.requestFocus();
	}
	
	/*
	 * Resets selected sprite. Hides all the sprite options
	 */
	public void resetSelection() {
		displayBoard.setSelectedSprite(null);
		selectionName.setText("None");
		moveButton.setText("Move");
		move2Button.setText("Move P2");
		moveButton.setToolTipText("Press and then click on a spot to move the sprite to that exact location (uses top left corner to position)");
		xCoordText.setToolTipText("X coordinate of sprite");
		yCoordText.setToolTipText("Y coordinate of sprite");
		xCoordText.setVisible(false);
		yCoordText.setVisible(false);
		x2CoordText.setVisible(false);
		y2CoordText.setVisible(false);
		deselectButton.setVisible(false);
		deleteButton.setVisible(false);
		moveButton.setVisible(false);
		move2Button.setVisible(false);
		widthText.setVisible(false);
		heightText.setVisible(false);
		widthBox.setVisible(false);
		heightBox.setVisible(false);
		radiusText.setVisible(false);
		radiusBox.setVisible(false);
		copyButton.setVisible(false);
		enemySpeed.setVisible(false);
		selectingSpot = false;
		isEnd = false;
		repaint();
	}
	
	/*
	 * Selected start or goal. Shows few sprite options
	 */
	public void selectedFixed() {
		deselectButton.setVisible(true);
		moveButton.setVisible(true);
		xCoordText.setVisible(true);
		yCoordText.setVisible(true);
		xCoordText.setText("X: "+selectedSprite.getxCoord());
		yCoordText.setText("Y: "+selectedSprite.getyCoord());
		repaint();
	}
	
	/*
	 * Selected board or wall. Shows some sprite options
	 */
	public void selectedRect() {
		deselectButton.setVisible(true);
		deleteButton.setVisible(true);
		moveButton.setVisible(true);
		xCoordText.setVisible(true);
		yCoordText.setVisible(true);
		widthText.setVisible(true);
		heightText.setVisible(true);
		widthBox.setVisible(true);
		heightBox.setVisible(true);
		copyButton.setVisible(true);
		xCoordText.setText("X: "+selectedSprite.getxCoord());
		yCoordText.setText("Y: "+selectedSprite.getyCoord());
		widthBox.setText(Integer.toString(selectedSprite.getWidth()));
		heightBox.setText(Integer.toString(selectedSprite.getHeight()));
		repaint();
	}
	
	/*
	 * Selected sentry. Shows many sprite options
	 */
	public void selectedEnemy() {
		selectionName.setText("Sentry");
		deselectButton.setVisible(true);
		deleteButton.setVisible(true);
		moveButton.setVisible(true);
		xCoordText.setVisible(true);
		yCoordText.setVisible(true);
		widthText.setVisible(true);
		heightText.setVisible(true);
		widthBox.setVisible(true);
		heightBox.setVisible(true);
		enemySpeed.setVisible(true);
		enemySpeed.setValue(((Enemy) selectedSprite).getSpeed());
		x2CoordText.setVisible(true);
		y2CoordText.setVisible(true);
		move2Button.setVisible(true);
		copyButton.setVisible(true);
		moveButton.setText("Move P1");
		moveButton.setToolTipText("Press and then click on a spot to move the first point of the sprite to that location");
		xCoordText.setText("X1: "+((Enemy) selectedSprite).getX1());
		xCoordText.setToolTipText("X coordinate of the first point of the sprite");
		yCoordText.setText("Y1: "+((Enemy) selectedSprite).getY1());
		yCoordText.setToolTipText("Y coordinate of the first point of the sprite");
		x2CoordText.setText("X2: "+((Enemy) selectedSprite).getX2());
		y2CoordText.setText("Y2: "+((Enemy) selectedSprite).getY2());
		widthBox.setText(Integer.toString(selectedSprite.getWidth()));
		heightBox.setText(Integer.toString(selectedSprite.getHeight()));
		repaint();
	}
	
	/*
	 * Selected spinner. Shows many sprite options
	 */
	public void selectedEnemy2() {
		selectionName.setText("Spinner");
		deselectButton.setVisible(true);
		deleteButton.setVisible(true);
		moveButton.setVisible(true);
		xCoordText.setVisible(true);
		yCoordText.setVisible(true);
		widthText.setVisible(true);
		heightText.setVisible(true);
		widthBox.setVisible(true);
		heightBox.setVisible(true);
		enemySpeed.setVisible(true);
		enemySpeed.setValue(((Enemy2) selectedSprite).getSpeed());
		radiusText.setVisible(true);
		radiusBox.setVisible(true);
		copyButton.setVisible(true);
		radiusBox.setText(Integer.toString(((Enemy2) selectedSprite).getRadius()));
		moveButton.setText("Move C");
		moveButton.setToolTipText("Press and click on a spot to move the center point to that location");
		xCoordText.setText("C X: "+((Enemy2) selectedSprite).getCenterX());
		xCoordText.setToolTipText("X coordinate of center point of the sprite");
		yCoordText.setText("C Y: "+((Enemy2) selectedSprite).getCenterY());
		yCoordText.setToolTipText("Y coordinate of center point of the sprite");
		widthBox.setText(Integer.toString(selectedSprite.getWidth()));
		heightBox.setText(Integer.toString(selectedSprite.getHeight()));
		radiusBox.setText(Integer.toString(((Enemy2) selectedSprite).getRadius()));
		repaint();
	}
	
	
	
	
	
	
	

	//--------------------------------------------------------------------------------------------------------------
	
	// Mouse and key listener methods
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	// Values used when dragging sprites around to make it more accurate
	private int coordAdjustX;
	private int coordAdjustY;
	
	/*
	 * Detects when mouse is pressed to start dragging to reposition process
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// compensate for border
		int x = e.getX() - 30;
		int y = e.getY() - 115;
		
		
		if (selectedSprite != null && !(selectedSprite instanceof Rectangle)) {
			if (selectedSprite.isInBounds(x-5,y-5,10,10)) {
				coordAdjustX = x - selectedSprite.getxCoord();
				coordAdjustY = y - selectedSprite.getyCoord();
				selectingSpot = true;
				if (selectedSprite instanceof Enemy) {
					moveButton.setText("Moving P1");
				} else if (selectedSprite instanceof Enemy2) {
					moveButton.setText("Moving C...");
				} else {
					moveButton.setText("Moving...");
				}
			} else if (selectedSprite instanceof Enemy)  {
				// Makes it possible to drag and move a sentry's end points
				if (Sprite.isInBounds(((Enemy) selectedSprite).getX1(), ((Enemy) selectedSprite).getY1(), selectedSprite.getWidth(), selectedSprite.getHeight(), x-5,y-5,10,10)) {
					coordAdjustX = x - selectedSprite.getxCoord();
					coordAdjustY = y - selectedSprite.getyCoord();
					selectingSpot = true;
					isEnd = false;
					moveButton.setText("Moving P1");
				} else if (Sprite.isInBounds(((Enemy) selectedSprite).getX2(), ((Enemy) selectedSprite).getY2(), selectedSprite.getWidth(), selectedSprite.getHeight(), x-5,y-5,10,10)) {
					coordAdjustX = x - selectedSprite.getxCoord();
					coordAdjustY = y - selectedSprite.getyCoord();
					selectingSpot = true;
					isEnd = true;
					moveButton.setText("Move P1");
					move2Button.setText("Moving P2");
				}
			} else if (selectedSprite instanceof Enemy2) {
				// Makes it possible to drag and move a spinner's center point
				if (Sprite.isInBounds(((Enemy2) selectedSprite).getCenterX(), ((Enemy2) selectedSprite).getCenterY(), selectedSprite.getWidth(), selectedSprite.getHeight(), x-5,y-5,10,10)) {
					coordAdjustX = x - selectedSprite.getxCoord();
					coordAdjustY = y - selectedSprite.getyCoord();
					selectingSpot = true;
					moveButton.setText("Moving C...");
				}
			}
		}
		
	}

	
	/*
	 * Detects when mouse is released to either reposition the sprite or select a new sprite
	 */
	@Override
	public void mouseReleased(MouseEvent me) {
		// compensate for border
		int x = me.getX() - 30;
		int y = me.getY() - 115;
		
		if (selectingSpot) {
			// Reposition the already selected sprite
			if (x >= 0 && x < boardWidth && y >= 0 && y < boardHeight) {
				if (selectedSprite instanceof Enemy) {
					if (isEnd) {
						((Enemy) selectedSprite).setX2(x);
						((Enemy) selectedSprite).setY2(y);
						isEnd = false;
						((Enemy) selectedSprite).setSpeed(((Enemy) selectedSprite).getSpeed());
					} else {
						((Enemy) selectedSprite).setX1(x - coordAdjustX);
						((Enemy) selectedSprite).setY1(y - coordAdjustY);
						((Enemy) selectedSprite).setSpeed(((Enemy) selectedSprite).getSpeed());
					}
					selectedSprite.setxCoord(((Enemy) selectedSprite).getX1());
					selectedSprite.setyCoord(((Enemy) selectedSprite).getY1());
					selectingSpot = false;
					moveButton.setText("Move P1");
					move2Button.setText("Move P2");
					repaint();
				} else if (selectedSprite instanceof Enemy2) {
					((Enemy2) selectedSprite).setCenterX(x);
					((Enemy2) selectedSprite).setCenterY(y);
					Vector v = ((Enemy2) selectedSprite).getNextMove(enemyTimer);
					selectedSprite.setxCoord(((Enemy2) selectedSprite).getCenterX() + v.getX());
					selectedSprite.setyCoord(((Enemy2) selectedSprite).getCenterY() + v.getY());
					selectingSpot = false;
					moveButton.setText("Move C");
					repaint();
				} else if (selectedSprite instanceof Start) {
					selectedSprite.setxCoord(x - coordAdjustX);
					selectedSprite.setyCoord(y - coordAdjustY);
					selectingSpot = false;
					moveButton.setText("Move");
					if (!appRunning) {
						for (AI current : population.getPopulation()) {
							current.setxCoord(selectedSprite.getxCoord());
							current.setyCoord(selectedSprite.getyCoord());
						}
					}
					repaint();	
				} else {
					selectedSprite.setxCoord(x - coordAdjustX);
					selectedSprite.setyCoord(y - coordAdjustY);
					selectingSpot = false;
					moveButton.setText("Move");
					repaint();	
				}
			}	
		} else {
			// Select a sprite
			if (start.isInBounds(x-5,y-5,10,10)) {
				selectedSprite = start;
				resetSelection();
				selectionName.setText("Start");
				displayBoard.setSelectedSprite(start);
				selectedFixed();
				return;
			}
			if (goal.isInBounds(x-5,y-5,10,10)) {
				selectedSprite = goal;
				resetSelection();
				displayBoard.setSelectedSprite(goal);
				selectionName.setText("Goal");
				selectedFixed();
				return;
			}
			for (int i = 0; i < enemies.size(); i++) {
				Enemy e = enemies.get(i);
				if (e.isInBounds(x-5,y-5,10,10) || Sprite.isInBounds(e.getX1(), e.getY1(), e.getWidth(), e.getHeight(), x-5,y-5,10,10)
												|| Sprite.isInBounds(e.getX2(), e.getY2(), e.getWidth(), e.getHeight(), x-5,y-5,10,10)) {
					selectedSprite = enemies.get(i);
					resetSelection();
					displayBoard.setSelectedSprite(enemies.get(i));
					selectedEnemy();
					return;
				}
			}
			for (int i = 0; i < enemies2.size(); i++) {
				Enemy2 e = enemies2.get(i);
				if (e.isInBounds(x-5,y-5,10,10) || Sprite.isInBounds(e.getCenterX(), e.getCenterY(), e.getWidth(), e.getHeight(), x-5,y-5,10,10)) {
					selectedSprite = enemies2.get(i);
					resetSelection();
					displayBoard.setSelectedSprite(enemies2.get(i));
					selectedEnemy2();
					return;
				}
			}
			for (int i = 0; i < walls.size(); i++) {
				if (walls.get(i).isInBounds(x-5,y-5,10,10)) {
					selectedSprite = walls.get(i);
					resetSelection();
					displayBoard.setSelectedSprite(walls.get(i));
					selectionName.setText("Wall");
					selectedRect();
					return;
				}
			}
			for (int i = board.getRects().size()-1; i >= 0; i--) {
				if (board.getRects().get(i).isInBounds(x-5,y-5,10,10)) {
					selectedSprite = board.getRects().get(i);

					resetSelection();
					displayBoard.setSelectedSprite(board.getRects().get(i));
					selectionName.setText("Board");
					selectedRect();
					return;
				}
			}
		}
	}
	
	
	/*
	 * Detect if user pressed any arrow keys to move the player
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_UP) {
	    	playerMoves[0] = 1;
	    }
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
	    	playerMoves[1] = 1;
	    }
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
	    	playerMoves[2] = 1;
	    }
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
	    	playerMoves[3] = 1;
	    }
	}

	/*
	 * Detect if user released any arrow keys to move the player
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_UP) {
	    	playerMoves[0] = 0;
	    }
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
	    	playerMoves[1] = 0;
	    }
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
	    	playerMoves[2] = 0;
	    }
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
	    	playerMoves[3] = 0;
	    }
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
