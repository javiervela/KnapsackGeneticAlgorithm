package es.uma.informatica.misia.ae.simpleea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * The EvolutionaryAlgorithm class implements a simple evolutionary algorithm
 * for solving optimization problems. It uses a combination of selection,
 * crossover, mutation, and replacement operators to evolve a population of
 * candidate solutions towards an optimal solution.
 * 
 * The algorithm is configured using a set of parameters provided in a map,
 * including the population size, maximum number of function evaluations, and
 * random seed. The algorithm stops when a specified stopping criterion is met,
 * such as reaching the maximum number of function evaluations or finding an
 * optimal solution.
 * 
 * The main components of the algorithm include:
 * - Selection: The process of selecting parents for reproduction.
 * - Crossover: The process of combining two parents to produce a child.
 * - Mutation: The process of introducing random variations into a child.
 * - Replacement: The process of replacing individuals in the population with
 * new individuals.
 * - StoppingCriterion: The condition that determines when the algorithm should
 * stop.
 */
public class EvolutionaryAlgorithm {
	public static final String MAX_FUNCTION_EVALUATIONS_PARAM = "maxFunctionEvaluations";
	public static final String RANDOM_SEED_PARAM = "randomSeed";
	public static final String POPULATION_SIZE_PARAM = "populationSize";

	private Problem problem;
	private int functionEvaluations;
	private int maxFunctionEvaluations;
	private List<Individual> population;
	private int populationSize;
	private Random rnd;

	private Individual bestSolution;

	private Selection selection;
	private Replacement replacement;
	private Mutation mutation;
	private Crossover recombination;
	private StoppingCriterion stoppingCriterion;

	// Constructor
	public EvolutionaryAlgorithm(Map<String, Double> parameters, Problem problem) {
		configureAlgorithm(parameters, problem);
	}

	// Configure the algorithm
	private void configureAlgorithm(Map<String, Double> parameters, Problem problem) {
		populationSize = parameters.get(POPULATION_SIZE_PARAM).intValue();
		maxFunctionEvaluations = parameters.get(MAX_FUNCTION_EVALUATIONS_PARAM).intValue();
		double bitFlipProb = parameters.get(BitFlipMutation.BIT_FLIP_PROBABILITY_PARAM);
		long randomSeed = parameters.get(RANDOM_SEED_PARAM).longValue();

		this.problem = problem;

		rnd = new Random(randomSeed);

		selection = new BinaryTournament(rnd);
		replacement = new ElitistReplacement();
		mutation = new BitFlipMutation(rnd, bitFlipProb);
		recombination = new SinglePointCrossover(rnd);

		if (maxFunctionEvaluations >= 0) {
			stoppingCriterion = new MaxFunctionEvaluationsCriterion(maxFunctionEvaluations);
		} else {
			stoppingCriterion = new OptimalSolutionCriterion(problem.getOptimalValue());
		}
	}

	// Run the algorithm
	public Individual run() {
		population = generateInitialPopulation();
		functionEvaluations = 0;

		evaluatePopulation(population);
		while (!shouldStop()) {
			Individual parent1 = selection.selectParent(population);
			Individual parent2 = selection.selectParent(population);
			Individual child = recombination.apply(parent1, parent2);
			child = mutation.apply(child);
			evaluateIndividual(child);
			population = replacement.replacement(population, Arrays.asList(child));
		}

		return bestSolution;
	}

	// Solution Evaluation
	private void evaluateIndividual(Individual individual) {
		double fitness = problem.evaluate(individual);
		individual.setFitness(fitness);
		functionEvaluations++;
		checkIfBest(individual);
	}

	private void checkIfBest(Individual individual) {
		if (bestSolution == null || individual.getFitness() > bestSolution.getFitness()) {
			bestSolution = individual;
		}
	}

	private void evaluatePopulation(List<Individual> population) {
		for (Individual individual : population) {
			evaluateIndividual(individual);
		}
	}

	// Generate Initial Population
	private List<Individual> generateInitialPopulation() {
		List<Individual> population = new ArrayList<>();
		for (int i = 0; i < populationSize; i++) {
			population.add(problem.generateRandomIndividual(rnd));
		}
		return population;
	}

	// Stopping Criterion
	private boolean shouldStop() {
		if (stoppingCriterion.isSatisfied(this)) {
			return true;
		}
		return false;
	}

	public int getFunctionEvaluations() {
		return functionEvaluations;
	}

	public Individual getBestSolution() {
		return bestSolution;
	}
}
