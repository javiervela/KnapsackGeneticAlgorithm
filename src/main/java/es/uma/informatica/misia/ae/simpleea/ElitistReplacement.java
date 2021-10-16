package es.uma.informatica.misia.ae.simpleea;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ElitistReplacement implements Replacement {

	@Override
	public List<Individual> replacement(List<Individual> population, List<Individual> offspring) {
		int populationSize = population.size();
		
		List<Individual> auxiliaryPopulation = new ArrayList<>(population);
		auxiliaryPopulation.addAll(offspring);
		
		auxiliaryPopulation.sort(Comparator.comparing(Individual::getFitness).reversed());
		
		List<Individual> nextPopulation = auxiliaryPopulation.stream()
				.limit(populationSize)
				.collect(Collectors.toList());
		
		return nextPopulation;
	}

}
