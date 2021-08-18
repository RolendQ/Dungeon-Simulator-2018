import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 * This class represents the group of AI dots that learn to solve the level
 * Contains an arraylist of AI objects
 */
public class Population {
	private ArrayList<AI> population;
	private int stepsIncrement;
	private double bestMutFreq;
	private double topMutFreq;
	private double randFreq;
	private double bestFreq;
	private double topFrac;
	private int maxPop;
	
	/*
	 * Constructor, takes in customizable settings for the learning algorithm
	 */
	public Population(int maxPop, int initialSteps, int stepsIncrement, double topFrac, double bestFreq, double randFreq, double bestMutFreq, double topMutFreq) {
		this.maxPop = maxPop;
		this.stepsIncrement = stepsIncrement;
		this.bestMutFreq = bestMutFreq;
		this.topMutFreq = topMutFreq;
		this.randFreq = randFreq;
		this.bestFreq = bestFreq;
		this.topFrac = topFrac;
		this.population = new ArrayList<AI>();
		for (int i = 0; i< maxPop; i++) {
			this.population.add(new AI(initialSteps));
		}
	}
	
	/*
	 * Returns if all AI dots are dead or have reached the goal
	 */
	public boolean isDone() {
		for (int i = 0; i < population.size(); i++) {
			if (!population.get(i).getIsDead() && !population.get(i).getReachedGoal()) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * Sets the spawn of all the AI dots
	 */
	public void setSpawn(Start start) {
		for (int i = 0; i < population.size(); i++) {
			population.get(i).setxCoord(start.getxCoord());
			population.get(i).setyCoord(start.getyCoord());
		}
	}


	/*
	 * Sorts the population based on each AI's fitness then returns the best
	 */
	public AI selectBest() {
		Collections.sort(population, new Comparator<AI>() {
	        public int compare(AI square1, AI square2) {
	            return (int) ((square2.getFitness() - square1.getFitness()) * 1000); // descending order
	        }

	    });
		AI best = population.get(0);
		return best;
	}
	
	/*
	 * Converts the old population into a new one by using the best/top AI from last population
	 */
	public void breed(AI bestSquare) {
		
		// Creates a list of the top AIs
		ArrayList<AI> oldPop = new ArrayList<AI>();
		int topAmount = (int) (topFrac*maxPop);
		for (int i = 1; i <= topAmount; i++) {
			oldPop.add(population.get(i));
		}
	
		int randRange = (int) (randFreq*maxPop);
		int bestRange = randRange + (int) (bestFreq*maxPop);
		
		// Keeps the best AI from the previous generation
		population.set(0, new AI(bestSquare.getMaxSteps()+stepsIncrement, bestSquare.getDirections()));
		
		// Random AI
		for (int i = 1; i < randRange; i++) {
			population.set(i, new AI(bestSquare.getMaxSteps()+stepsIncrement));
		}
		// Best AI
		for (int i = randRange; i < bestRange; i++) {
			AI temp = new AI(bestSquare.getMaxSteps()+stepsIncrement, bestSquare.getDirections());
			temp.mutate(bestMutFreq);
			population.set(i, temp);
		}
		// Top AI
		for (int i = bestRange; i < maxPop; i++) {
			int randomTop = (int) (Math.random() * topAmount);
			AI temp = new AI(oldPop.get(randomTop).getMaxSteps()+stepsIncrement, oldPop.get(randomTop).getDirections());
			temp.mutate(topMutFreq);
			population.set(i, temp);
		}
	}
	
	//-------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @return the population
	 */
	public ArrayList<AI> getPopulation() {
		return population;
	}

	/**
	 * @param population the population to set
	 */
	public void setPopulation(ArrayList<AI> population) {
		this.population = population;
	}
	
	
}