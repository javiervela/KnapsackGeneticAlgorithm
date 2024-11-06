package es.uma.informatica.misia.ae.mkpga.algorithm.stopping;

import es.uma.informatica.misia.ae.mkpga.algorithm.EvolutionaryAlgorithm;
import es.uma.informatica.misia.ae.mkpga.problem.Individual;

/**
 * A stopping criterion that stops when the algorithm has found an optimal
 * solution.
 */
public class OptimalSolutionCriterion implements StoppingCriterion {
	private final double optimalValue;
	private final long timeLimit;

	public OptimalSolutionCriterion(double optimalValue, long timeLimit) {
		this.optimalValue = optimalValue;
		this.timeLimit = timeLimit;
	}

	@Override
	public boolean isSatisfied(EvolutionaryAlgorithm algorithm) {
		Individual bestSolution = algorithm.getBestSolution();
		long executionTime = algorithm.getMetricsCollector().getExecutionTime();
		return bestSolution != null && bestSolution.getFitness() >= optimalValue || executionTime >= timeLimit;
	}
}
