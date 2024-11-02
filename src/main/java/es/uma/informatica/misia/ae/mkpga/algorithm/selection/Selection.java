package es.uma.informatica.misia.ae.mkpga.algorithm.selection;

import java.util.List;

import es.uma.informatica.misia.ae.mkpga.problem.Individual;

public interface Selection {
	Individual selectParent(List<Individual> population);
}