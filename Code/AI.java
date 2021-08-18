import java.util.ArrayList;

/*
 * This class is for the AI dots. Each object instantiated has its own "brain" which makes it take random steps
 * The directions are vectors that the AI will use to move
 */
public class AI extends Sprite {
	private ArrayList<Vector> directions;
	private int step;
	private int maxSteps;
	private double theta;
	
	private boolean isDead = false;
	private boolean reachedGoal = false;
	private boolean isBest = false;
	
	private double fitness = 0.0;
	/*
	 * Constructor, takes size to make that many random steps
	 */
	public AI(int size) {
		super(0,0,10,10);
		maxSteps = size;
		directions = new ArrayList<Vector>();
		// They start facing a random direction
		theta = Math.toRadians((int) (Math.random() * 360));
		// Every step, they turn a random amount. Then, a vector is calculated to represent the movement on an X and Y grid
		for (int i = 0; i < size; i++) {
			theta += -0.75 + (Math.random() * 1.5);
			int v1 = (int) (5 * Math.cos(theta));
			int v2 = (int) (5 * Math.sin(theta));
			directions.add(new Vector(v1,v2));
		}
	}
	/*
	 * Constructor, takes in directions to copy over and then fills to size
	 */
	public AI(int size, ArrayList<Vector> directions) {
		super(0,0,10,10);
		maxSteps = size;
		// Directions are copied over exactly
		this.directions = new ArrayList<Vector>();
		for (int i = 0; i < directions.size(); i++) {
			this.directions.add(directions.get(i));
		}
		// Fills the rest with random vectors
		for (int i = this.directions.size(); i < maxSteps; i++) {
			theta += -0.75 + (Math.random() * 1.5);
			int v1 = (int) (5 * Math.cos(theta));
			int v2 = (int) (5 * Math.sin(theta));
			this.directions.add(new Vector(v1,v2));
		}
		this.step = 0;
	}
	
	/*
	 * Mutates random steps, takes in frequency of these mutations
	 */
	public void mutate(double freq) {
		for (int i = 0; i < maxSteps; i++) {
			double chance = Math.random();
			// Chance is increased the more steps the AI dot has taken
			if (chance*(1-(i/(maxSteps*2))) < freq) {
				theta += -0.75 + (Math.random() * 1.5);
				int v1 = (int) (5 * Math.cos(theta));
				int v2 = (int) (5 * Math.sin(theta));
				directions.set(i,new Vector(v1,v2));
			}
		}
	}
	
	/*
	 * Returns the vector of the corresponding step number, also declares AI dead if ran out of steps
	 */
	public Vector getCurrentMove() {
		if (step < directions.size()) {
			return directions.get(step++);
		} else {
			isDead = true;
			return new Vector(0,0);
		}
	}
	
	
	//----------------------------------------------------------------------------------------------------------------
	
	/**
	 * @return the directions
	 */
	public ArrayList<Vector> getDirections() {
		return directions;
	}
	/**
	 * @param directions the directions to set
	 */
	public void setDirections(ArrayList<Vector> directions) {
		this.directions = directions;
	}
	/**
	 * @return the step
	 */
	public int getStep() {
		return step;
	}
	/**
	 * @param step the step to set
	 */
	public void setStep(int step) {
		this.step = step;
	}
	/**
	 * @return the maxSteps
	 */
	public int getMaxSteps() {
		return maxSteps;
	}
	/**
	 * @param maxSteps the maxSteps to set
	 */
	public void setMaxSteps(int maxSteps) {
		this.maxSteps = maxSteps;
	}
	/**
	 * @return the theta
	 */
	public double getTheta() {
		return theta;
	}
	/**
	 * @param theta the theta to set
	 */
	public void setTheta(double theta) {
		this.theta = theta;
	}
	/**
	 * @return the isDead
	 */
	public boolean getIsDead() {
		return isDead;
	}
	/**
	 * @param isDead the isDead to set
	 */
	public void setIsDead(boolean isDead) {
		this.isDead = isDead;
	}
	/**
	 * @return the reachedGoal
	 */
	public boolean getReachedGoal() {
		return reachedGoal;
	}
	/**
	 * @param reachedGoal the reachedGoal to set
	 */
	public void setReachedGoal(boolean reachedGoal) {
		this.reachedGoal = reachedGoal;
	}
	/**
	 * @return the isBest
	 */
	public boolean isBest() {
		return isBest;
	}
	/**
	 * @param isBest the isBest to set
	 */
	public void setBest(boolean isBest) {
		this.isBest = isBest;
	}
	/**
	 * @return the fitness
	 */
	public double getFitness() {
		return fitness;
	}
	/**
	 * @param fitness the fitness to set
	 */
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
}
