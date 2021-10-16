package es.uma.informatica.misia.ae.simpleea;

import java.util.Random;

public class BitFlipMutation implements Mutation {

	private double bitFlipProb;
	private Random rnd;
	
	public BitFlipMutation (Random rnd, double bitFlipProb) {
		this.rnd = rnd;
		this.bitFlipProb = bitFlipProb;
	}

	@Override
	public Individual apply(Individual original) {
		Individual mutated = new Individual(original);
		for (int i = 0; i < mutated.getChromosome().length; i++) {
			if (rnd.nextDouble() < bitFlipProb) {
				byte value = mutated.getChromosome()[i];
				mutated.getChromosome()[i] = (byte)(1 - value);
			}
		}
		return mutated;
	}

	public double getBitFlipProb() {
		return bitFlipProb;
	}

	public void setBitFlipProb(double bitFlipProb) {
		this.bitFlipProb = bitFlipProb;
	}

}
