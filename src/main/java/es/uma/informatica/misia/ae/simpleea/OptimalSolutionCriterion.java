package es.uma.informatica.misia.ae.simpleea;

/**
 * A stopping criterion that stops when the algorithm has found an optimal
 * solution.
 */
public class OptimalSolutionCriterion implements StoppingCriterion {
	private final double optimalValue;

	public OptimalSolutionCriterion(double optimalValue) {
		this.optimalValue = optimalValue;
	}

	@Override
	public boolean isSatisfied(EvolutionaryAlgorithm algorithm) {
		Individual bestSolution = algorithm.getBestSolution();
		return bestSolution != null && bestSolution.getFitness() >= optimalValue;
	}
}
