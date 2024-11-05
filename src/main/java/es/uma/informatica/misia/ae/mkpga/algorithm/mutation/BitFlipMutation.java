package es.uma.informatica.misia.ae.mkpga.algorithm.mutation;

import java.util.Random;

import es.uma.informatica.misia.ae.mkpga.problem.BinaryString;
import es.uma.informatica.misia.ae.mkpga.problem.Individual;

public class BitFlipMutation implements Mutation {

	private double probability;
	private Random rnd;

	public BitFlipMutation(Random rnd, double probability) {
		this.rnd = rnd;
		this.probability = probability;
	}

	@Override
	public Individual apply(Individual individual) {
		BinaryString original = (BinaryString) individual;
		BinaryString mutated = new BinaryString(original);
		for (int i = 0; i < mutated.getChromosome().length; i++) {
			if (rnd.nextDouble() < probability) {
				byte value = mutated.getChromosome()[i];
				mutated.getChromosome()[i] = (byte) (1 - value);
			}
		}
		return mutated;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

}
