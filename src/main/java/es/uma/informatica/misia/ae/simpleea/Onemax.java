package es.uma.informatica.misia.ae.simpleea;

import java.util.Random;

public class Onemax implements Problem{
	private int n;
	
	public Onemax(int n) {
		this.n=n;
	}

	public double evaluate(Individual individual) {
		BinaryString binaryString = (BinaryString)individual;
		double result = 0.0;
		for (int i=0; i < binaryString.getChromosome().length; i++) {
			result += binaryString.getChromosome()[i];
		}
		return result;
	}
	
	public BinaryString generateRandomIndividual(Random rnd) {
		return new BinaryString(n,rnd);
	}

}
