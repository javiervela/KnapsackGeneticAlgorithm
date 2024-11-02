package es.uma.informatica.misia.ae.mkpga.algorithm.selection;

import java.util.List;
import java.util.Random;

import es.uma.informatica.misia.ae.mkpga.problem.Individual;

public class BinaryTournament implements Selection {
	private Random rnd;

	public BinaryTournament(Random rnd) {
		this.rnd = rnd;
	}

	private Individual selectRandomIndividual(List<Individual> population) {
		int index = rnd.nextInt(population.size());
		return population.get(index);
	}

	@Override
	public Individual selectParent(List<Individual> population) {
		Individual firstSelection = selectRandomIndividual(population);
		Individual secondSelection = selectRandomIndividual(population);
		if (firstSelection.getFitness() > secondSelection.getFitness()) {
			return firstSelection;
		} else {
			return secondSelection;
		}
	}

}
