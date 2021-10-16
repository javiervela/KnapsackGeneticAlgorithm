package es.uma.informatica.misia.ae.simpleea;

import java.util.Arrays;
import java.util.Random;

public class BinaryString extends Individual {
	private byte [] chromosome;
	
	public BinaryString(BinaryString individual) {
		chromosome = individual.chromosome.clone();
		fitness = individual.fitness;
	}
	
	public BinaryString(int n) {
		chromosome = new byte[n];
	}
	
	public BinaryString (int n, Random rnd) {
		this(n);
		for (int i=0; i < n; i++) {
			chromosome[i] = (byte)rnd.nextInt(2);
		}
	}
	
	public byte[] getChromosome() {
		return chromosome;
	}
	public void setChromosome(byte[] chromosome) {
		this.chromosome = chromosome;
	}

	@Override
	public String toString() {
		return "Individual [fitness=" + fitness + ", chromosome=" + Arrays.toString(chromosome) + "]";
	}
	
	

}
