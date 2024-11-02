package es.uma.informatica.misia.ae.mkpga.algorithm.stopping;

import es.uma.informatica.misia.ae.mkpga.algorithm.EvolutionaryAlgorithm;

/**
 * Interface representing a stopping criterion for an evolutionary algorithm.
 * 
 * Implementations of this interface define the condition under which the
 * evolutionary algorithm should terminate.
 */
public interface StoppingCriterion {
	boolean isSatisfied(EvolutionaryAlgorithm evolutionaryAlgorithm);
}
