package es.uma.informatica.misia.ae.mkpga.algorithm.crossover;

import es.uma.informatica.misia.ae.mkpga.problem.Individual;

public interface Crossover {
	Individual apply(Individual parent1, Individual parent2);

}