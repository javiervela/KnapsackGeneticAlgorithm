package es.uma.informatica.misia.ae.simpleea;

import java.util.Arrays;
import java.util.Random;

public class Individual {
	private double fitness;
	private byte [] chromosome;
	
	public Individual(Individual individual) {
		chromosome = individual.chromosome.clone();
		fitness = individual.fitness;
	}
	
	public Individual(int n) {
		chromosome = new byte[n];
	}
	
	public Individual (int n, Random rnd) {
		this(n);
		for (int i=0; i < n; i++) {
			chromosome[i] = (byte)rnd.nextInt(2);
		}
	}
	
	public double getFitness() {
		return fitness;
	}
	public void setFitness(double fitness) {
		this.fitness = fitness;
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
