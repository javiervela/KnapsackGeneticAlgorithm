package es.uma.informatica.misia.ae.mkpga.algorithm.stopping;

import es.uma.informatica.misia.ae.mkpga.algorithm.EvolutionaryAlgorithm;

/**
 * A stopping criterion that stops when the algorithm has reached a maximum
 * number of function evaluations.
 */
public class MaxFunctionEvaluationsCriterion implements StoppingCriterion {

	private final int maxFunctionEvaluations;

	public MaxFunctionEvaluationsCriterion(int maxFunctionEvaluations) {
		this.maxFunctionEvaluations = maxFunctionEvaluations;
	}

	@Override
	public boolean isSatisfied(EvolutionaryAlgorithm algorithm) {
		return algorithm.getFunctionEvaluations() >= maxFunctionEvaluations;
	}

}
