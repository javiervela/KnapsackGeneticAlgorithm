package es.uma.informatica.misia.ae.mkpga.algorithm.crossover;

import java.util.Random;

import es.uma.informatica.misia.ae.mkpga.problem.BinaryString;
import es.uma.informatica.misia.ae.mkpga.problem.Individual;

public class SinglePointCrossover implements Crossover {

	private Random rnd;
	private double crossoverProbability;

	public SinglePointCrossover(Random rnd, double crossoverProbability) {
		this.rnd = rnd;
		this.crossoverProbability = crossoverProbability;
	}

	@Override
	public BinaryString apply(Individual individual1, Individual individual2) {
		if (rnd.nextDouble() < crossoverProbability) {
			return (BinaryString) individual1;
		}

		BinaryString binaryParent1 = (BinaryString) individual1;
		BinaryString binaryParent2 = (BinaryString) individual2;

		BinaryString child = new BinaryString(binaryParent1);
		int cutPoint = rnd.nextInt(binaryParent1.getChromosome().length + 1);

		for (int i = cutPoint; i < binaryParent1.getChromosome().length; i++) {
			child.getChromosome()[i] = binaryParent2.getChromosome()[i];
		}
		return child;
	}
}