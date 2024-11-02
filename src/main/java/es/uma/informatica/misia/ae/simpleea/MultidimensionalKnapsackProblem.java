package es.uma.informatica.misia.ae.simpleea;

import java.util.List;
import java.util.Random;

/**
 * This class represents a Multidimensional Knapsack Problem (MKP).
 * The MKP is a generalization of the knapsack problem where multiple
 * constraints are considered. Each item has a profit and multiple weights, and
 * the goal is to maximize the total profit while satisfying all constraints.
 * 
 * The class provides methods to get the number of items, number of
 * constraints, optimal value, profits, constraints, and capacities.
 */
public class MultidimensionalKnapsackProblem implements Problem {

	private int numberItems;
	private int numberConstraints;
	private double optimalValue;
	private List<Double> profits;
	private List<List<Integer>> constraints;
	private List<Integer> capacities;

	// Constructor to initialize all fields at once
	public MultidimensionalKnapsackProblem(int numberItems, int numberConstraints, double optimalValue,
			List<Double> profits, List<List<Integer>> constraints, List<Integer> capacities) {
		this.numberItems = numberItems;
		this.numberConstraints = numberConstraints;
		this.optimalValue = optimalValue;
		this.profits = profits;
		this.constraints = constraints;
		this.capacities = capacities;
	}

	// Getters
	public int getNumberItems() {
		return numberItems;
	}

	public int getNumberConstraints() {
		return numberConstraints;
	}

	public double getOptimalValue() {
		return optimalValue;
	}

	public List<Double> getProfits() {
		return profits;
	}

	public List<List<Integer>> getConstraints() {
		return constraints;
	}

	public List<Integer> getCapacities() {
		return capacities;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MultidimensionalKnapsackProblem{\n");
		sb.append("  numberItems=").append(numberItems).append(",\n");
		sb.append("  numberConstraints=").append(numberConstraints).append(",\n");
		sb.append("  optimalValue=").append(optimalValue).append(",\n");
		sb.append("  profits=").append(profits).append(",\n");
		sb.append("  constraints=").append("[\n");
		for (List<Integer> constraint : constraints) {
			sb.append("\t").append(constraint).append(",\n");
		}
		sb.append("  ],\n");
		sb.append("  capacities=").append(capacities).append("\n");
		sb.append('}');
		return sb.toString();
	}

	public double evaluate(Individual individual) {
		BinaryString binaryString = (BinaryString) individual;
		double totalProfit = 0.0;
		double[] totalWeights = new double[numberConstraints];

		for (int itemIndex = 0; itemIndex < binaryString.getChromosome().length; itemIndex++) {
			if (binaryString.getChromosome()[itemIndex] == 1) {
				totalProfit += profits.get(itemIndex);
				for (int constraintIndex = 0; constraintIndex < numberConstraints; constraintIndex++) {
					totalWeights[constraintIndex] += constraints.get(constraintIndex).get(itemIndex);
				}
			}
		}

		for (int constraintIndex = 0; constraintIndex < numberConstraints; constraintIndex++) {
			if (totalWeights[constraintIndex] > capacities.get(constraintIndex)) {
				// TODO what to do with not feasible solutions???
				// If any constraint is violated, return 0
				return 0.0;
			}
		}

		return totalProfit;
	}

	public BinaryString generateRandomIndividual(Random rnd) {
		return new BinaryString(numberItems, rnd);
	}
}
