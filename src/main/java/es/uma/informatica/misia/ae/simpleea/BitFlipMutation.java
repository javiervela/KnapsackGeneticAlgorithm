package es.uma.informatica.misia.ae.simpleea;

import java.util.Random;

public class BitFlipMutation implements Mutation {

	private double bitFlipProb;
	private Random rnd;
	public static final String BIT_FLIP_PROBABILITY_PARAM = "bitFlipProbability";
	
	public BitFlipMutation (Random rnd, double bitFlipProb) {
		this.rnd = rnd;
		this.bitFlipProb = bitFlipProb;
	}

	@Override
	public Individual apply(Individual individual) {
		BinaryString original = (BinaryString) individual;
		BinaryString mutated = new BinaryString(original);
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
