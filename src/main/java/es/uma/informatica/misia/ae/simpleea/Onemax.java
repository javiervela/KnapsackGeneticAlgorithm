package es.uma.informatica.misia.ae.simpleea;

import java.util.Random;

public class Onemax implements Problem{
	private int n;
	
	public Onemax(int n) {
		this.n=n;
	}

	public double evaluate(Individual individual) {
		double result = 0.0;
		for (int i=0; i < individual.getChromosome().length; i++) {
			result += individual.getChromosome()[i];
		}
		return result;
	}
	
	public Individual generateRandomIndividual(Random rnd) {
		return new Individual(n,rnd);
	}

}
