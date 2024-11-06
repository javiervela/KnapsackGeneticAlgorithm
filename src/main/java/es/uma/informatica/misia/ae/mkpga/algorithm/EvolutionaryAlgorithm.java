package es.uma.informatica.misia.ae.mkpga.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import es.uma.informatica.misia.ae.mkpga.algorithm.crossover.Crossover;
import es.uma.informatica.misia.ae.mkpga.algorithm.crossover.SinglePointCrossover;
import es.uma.informatica.misia.ae.mkpga.algorithm.mutation.BitFlipMutation;
import es.uma.informatica.misia.ae.mkpga.algorithm.mutation.Mutation;
import es.uma.informatica.misia.ae.mkpga.algorithm.replacement.ElitistReplacement;
import es.uma.informatica.misia.ae.mkpga.algorithm.replacement.Replacement;
import es.uma.informatica.misia.ae.mkpga.algorithm.selection.BinaryTournament;
import es.uma.informatica.misia.ae.mkpga.algorithm.selection.Selection;
import es.uma.informatica.misia.ae.mkpga.algorithm.stopping.MaxFunctionEvaluationsCriterion;
import es.uma.informatica.misia.ae.mkpga.algorithm.stopping.OptimalSolutionCriterion;
import es.uma.informatica.misia.ae.mkpga.algorithm.stopping.StoppingCriterion;
import es.uma.informatica.misia.ae.mkpga.problem.Individual;
import es.uma.informatica.misia.ae.mkpga.problem.Problem;
import es.uma.informatica.misia.ae.mkpga.util.MetricsCollector;

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

	private static final long STOPPING_LIMIT_TIME = 60000;

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

	private MetricsCollector metricsCollector;

	// Constructor
	public EvolutionaryAlgorithm(Map<String, Double> parameters, Problem problem) {
		this.metricsCollector = new MetricsCollector(problem, parameters);
		configureAlgorithm(parameters, problem);
	}

	// Configure the algorithm
	private void configureAlgorithm(Map<String, Double> parameters, Problem problem) {
		populationSize = parameters.get(POPULATION_SIZE_PARAM).intValue();
		maxFunctionEvaluations = parameters.get(MAX_FUNCTION_EVALUATIONS_PARAM).intValue();
		double mutationProbability = parameters.get(Mutation.MUTATION_PROBABILITY_PARAMETER);
		double crossoverProbability = parameters.get(Crossover.CROSSOVER_PROBABILITY_PARAM);
		long randomSeed = parameters.get(RANDOM_SEED_PARAM).longValue();

		this.problem = problem;

		rnd = new Random(randomSeed);

		selection = new BinaryTournament(rnd);
		replacement = new ElitistReplacement();
		// TODO - Make mutation and crossover method configurable
		mutation = new BitFlipMutation(rnd, mutationProbability);
		recombination = new SinglePointCrossover(rnd, crossoverProbability);

		if (maxFunctionEvaluations >= 0) {
			// TODO - Should we stop when the optimal solution is found?
			stoppingCriterion = new MaxFunctionEvaluationsCriterion(maxFunctionEvaluations);
		} else {
			stoppingCriterion = new OptimalSolutionCriterion(problem.getOptimalValue(), STOPPING_LIMIT_TIME);
		}
	}

	// Run the algorithm
	public Individual run() {
		metricsCollector.startTimer();
		population = generateInitialPopulation();
		functionEvaluations = 0;

		evaluatePopulation(population);
		metricsCollector.addGenerationBestIndividual(bestSolution);
		metricsCollector.incrementGenerations();

		while (!shouldStop()) {
			Individual parent1 = selection.selectParent(population);
			Individual parent2 = selection.selectParent(population);
			Individual child = recombination.apply(parent1, parent2);
			child = mutation.apply(child);
			evaluateIndividual(child);
			population = replacement.replacement(population, Arrays.asList(child));
			metricsCollector.addGenerationBestIndividual(bestSolution);
			metricsCollector.incrementGenerations();
		}
		metricsCollector.stopTimer();

		return bestSolution;
	}

	// Solution Evaluation
	private void evaluateIndividual(Individual individual) {
		double fitness = problem.evaluate(individual);
		individual.setFitness(fitness);
		functionEvaluations++;
		metricsCollector.incrementEvaluations();
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

	public MetricsCollector getMetricsCollector() {
		return metricsCollector;
	}
}
