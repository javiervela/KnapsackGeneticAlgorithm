package es.uma.informatica.misia.ae.simpleea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The MultidimensionalKnapsackProblemLoader class provides functionality to
 * load instances of the Multidimensional Knapsack Problem from a specified
 * file. The file should contain the number of problems, followed by the details
 * of each problem including the number of items, number of constraints, optimal
 * value, profits, constraints, and capacities.
 * 
 * The file format and source of the data can be found at:
 * http://people.brunel.ac.uk/~mastjjb/jeb/orlib/mknapinfo.html and
 * https://people.brunel.ac.uk/~mastjjb/jeb/orlib/files/mknap1.txt
 */
public class MultidimensionalKnapsackProblemLoader {

	public static List<MultidimensionalKnapsackProblem> loadInstances(String filePath) throws IOException {
		List<MultidimensionalKnapsackProblem> instances = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			// Skip blank lines
			do {
				line = reader.readLine();
			} while (line != null && line.trim().isEmpty());

			// Number of test problems
			int numProblems = Integer.parseInt(line.trim());

			for (int i = 0; i < numProblems; i++) {
				// Skip blank lines
				do {
					line = reader.readLine();
				} while (line != null && line.trim().isEmpty());

				String[] header = line.trim().split(" ");

				// Number of items, number of constraints, and optimal value
				int numberItems = Integer.parseInt(header[0]);
				int numberConstraints = Integer.parseInt(header[1]);
				double optimalValue = Double.parseDouble(header[2]);

				// Read profits
				List<Double> profits = new ArrayList<>();
				// Read lines until all profits are read
				while (profits.size() < numberItems) {
					line = reader.readLine().trim();
					String[] profitValues = line.split(" ");
					for (String profit : profitValues) {
						if (!profit.isEmpty()) {
							profits.add(Double.parseDouble(profit));
						}
					}
				}

				// Read each constraint's item requirements
				List<List<Integer>> constraints = new ArrayList<>();
				// Read lines until all constraints
				for (int j = 0; j < numberConstraints; j++) {
					List<Integer> constraint = new ArrayList<>();
					// Read lines until all items in the constraint are read
					while (constraint.size() < numberItems) {
						line = reader.readLine().trim();
						String[] constraintValues = line.split(" ");
						for (String value : constraintValues) {
							if (!value.isEmpty()) {
								constraint.add(Integer.parseInt(value));
							}
						}
					}
					constraints.add(constraint);
				}

				// Read resource capacities
				List<Integer> capacities = new ArrayList<>();
				// Read lines until all capacities are read
				while (capacities.size() < numberConstraints) {
					line = reader.readLine().trim();
					String[] capacityValues = line.split(" ");
					for (String capacity : capacityValues) {
						if (!capacity.isEmpty()) {
							capacities.add(Integer.parseInt(capacity));
						}
					}
				}

				// Create instance of MultidimensionalKnapsackProblem with all data
				MultidimensionalKnapsackProblem instance = new MultidimensionalKnapsackProblem(
						numberItems, numberConstraints, optimalValue, profits, constraints, capacities);

				instances.add(instance);
			}
		}
		return instances;
	}
}
