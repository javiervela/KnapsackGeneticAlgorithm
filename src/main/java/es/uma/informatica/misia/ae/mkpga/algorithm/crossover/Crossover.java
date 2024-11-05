package es.uma.informatica.misia.ae.mkpga.algorithm.crossover;

import es.uma.informatica.misia.ae.mkpga.problem.Individual;

public interface Crossover {
	public static final String CROSSOVER_PROBABILITY_PARAM = "crossoverProbability";

	Individual apply(Individual parent1, Individual parent2);
}