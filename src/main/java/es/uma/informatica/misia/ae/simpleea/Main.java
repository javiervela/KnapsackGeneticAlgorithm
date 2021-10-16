package es.uma.informatica.misia.ae.simpleea;

import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main (String args []) {
		
		if (args.length < 4) {
			System.err.println("Invalid number of arguments");
			System.err.println("Arguments: <population size> <function evaluations> <bitflip probability> <problem size> [<random seed>]");
			return;
		}
		
		int n = Integer.parseInt(args[3]);
		Problem problem = new Onemax(n);
		
		Map<String, Double> parameters = readEAParameters(args);
		EvolutionaryAlgorithm evolutionaryAlgorithm = new EvolutionaryAlgorithm(parameters, problem);
		
		Individual bestSolution = evolutionaryAlgorithm.run();
		System.out.println(bestSolution);
	}

	private static Map<String, Double> readEAParameters(String[] args) {
		Map<String, Double> parameters = new HashMap<>();
		parameters.put(EvolutionaryAlgorithm.POPULATION_SIZE_PARAM, Double.parseDouble(args[0]));
		parameters.put(EvolutionaryAlgorithm.MAX_FUNCTION_EVALUATIONS_PARAM, Double.parseDouble(args[1]));
		parameters.put(BitFlipMutation.BIT_FLIP_PROBABILITY_PARAM, Double.parseDouble(args[2]));
		
		long randomSeed = System.currentTimeMillis();
		if (args.length > 4) {
			randomSeed = Long.parseLong(args[4]);
		}
		parameters.put(EvolutionaryAlgorithm.RANDOM_SEED_PARAM, (double)randomSeed);
		return parameters;
	}

}
