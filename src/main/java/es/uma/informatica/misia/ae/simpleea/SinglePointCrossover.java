package es.uma.informatica.misia.ae.simpleea;

import java.util.Random;

public class SinglePointCrossover implements Crossover {
	
	private Random rnd;
	
	public SinglePointCrossover(Random rnd) {
		this.rnd=rnd;
	}

	@Override
	public Individual apply(Individual parent1, Individual parent2) {
		Individual child = new Individual (parent1);
		int cutPoint = rnd.nextInt(parent1.getChromosome().length+1);
		
		for (int i=cutPoint; i < parent1.getChromosome().length; i++) {
			child.getChromosome()[i] = parent2.getChromosome()[i];
		}
		return child;
	}

}
