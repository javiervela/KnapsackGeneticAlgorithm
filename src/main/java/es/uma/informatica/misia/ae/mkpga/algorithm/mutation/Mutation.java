package es.uma.informatica.misia.ae.mkpga.algorithm.mutation;

import es.uma.informatica.misia.ae.mkpga.problem.Individual;

public interface Mutation {
	public static final String MUTATION_PROBABILITY_PARAMETER = "mutationProbability";

	Individual apply(Individual original);
}