package es.uma.informatica.misia.ae.mkpga;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uma.informatica.misia.ae.mkpga.algorithm.EvolutionaryAlgorithm;
import es.uma.informatica.misia.ae.mkpga.algorithm.mutation.BitFlipMutation;
import es.uma.informatica.misia.ae.mkpga.problem.Individual;
import es.uma.informatica.misia.ae.mkpga.problem.MultidimensionalKnapsackProblem;
import es.uma.informatica.misia.ae.mkpga.util.MultidimensionalKnapsackProblemLoader;
import es.uma.informatica.misia.ae.mkpga.problem.Problem;

public class Main {

	public static void main(String args[]) {

		if (args.length < 4) {
			System.err.println("Invalid number of arguments");
			System.err.println(
					"Arguments: <population size> <function evaluations> <bitflip probability> <problem index> [<random seed>]");
			return;
		}

		String filePath = System.getenv("MKP_FILE_PATH");
		if (filePath == null) {
			System.err.println("MKP_FILE_PATH environment variable not set");
			return;
		}

		int problem_index = Integer.parseInt(args[3]);
		List<MultidimensionalKnapsackProblem> problems;
		try {
			problems = MultidimensionalKnapsackProblemLoader.loadInstances(filePath);
		} catch (IOException e) {
			System.err.println("Error loading problem instances: " + e.getMessage());
			return;
		}
		Problem problem = problems.get(problem_index);

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
		parameters.put(EvolutionaryAlgorithm.RANDOM_SEED_PARAM, (double) randomSeed);
		return parameters;
	}

}
