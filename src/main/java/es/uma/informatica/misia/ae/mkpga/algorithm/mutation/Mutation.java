package es.uma.informatica.misia.ae.mkpga.algorithm.mutation;

import es.uma.informatica.misia.ae.mkpga.problem.Individual;

public interface Mutation {
	Individual apply(Individual original);

}