package es.uma.informatica.misia.ae.mkpga.algorithm.replacement;

import java.util.List;

import es.uma.informatica.misia.ae.mkpga.problem.Individual;

public interface Replacement {

	List<Individual> replacement(List<Individual> population, List<Individual> offspring);

}