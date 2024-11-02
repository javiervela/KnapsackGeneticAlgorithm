package es.uma.informatica.misia.ae.mkpga.util;

import es.uma.informatica.misia.ae.mkpga.problem.Individual;
import es.uma.informatica.misia.ae.mkpga.problem.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MetricsCollector {
	private Problem problem;
	private Map<String, Double> parameters;
	private List<Individual> bestSolutions;
	private long startTime;
	private long endTime;
	private int numberOfEvaluations;
	private int numberOfGenerations;

	public MetricsCollector(Problem problem, Map<String, Double> parameters) {
		this.problem = problem;
		this.parameters = parameters;
		this.bestSolutions = new ArrayList<>();
		this.numberOfEvaluations = 0;
		this.numberOfGenerations = 0;
	}

	public void startTimer() {
		this.startTime = System.currentTimeMillis();
	}

	public void stopTimer() {
		this.endTime = System.currentTimeMillis();
	}

	public void addBestSolution(Individual bestSolution) {
		this.bestSolutions.add(bestSolution);
	}

	public void incrementEvaluations() {
		this.numberOfEvaluations++;
	}

	public void incrementGenerations() {
		this.numberOfGenerations++;
	}

	public long getExecutionTime() {
		return endTime - startTime;
	}

	public Problem getProblem() {
		return problem;
	}

	public Map<String, Double> getParameters() {
		return parameters;
	}

	public List<Individual> getBestSolutions() {
		return bestSolutions;
	}

	public int getNumberOfEvaluations() {
		return numberOfEvaluations;
	}

	public int getNumberOfGenerations() {
		return numberOfGenerations;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MetricsCollector{\n");
		sb.append("  problem=").append(problem).append(",\n");
		sb.append("  parameters=").append(parameters).append(",\n");
		sb.append("  bestSolutions=").append(bestSolutions).append(",\n");
		sb.append("  executionTime=").append(getExecutionTime()).append(" ms,\n");
		sb.append("  numberOfEvaluations=").append(numberOfEvaluations).append(",\n");
		sb.append("  numberOfGenerations=").append(numberOfGenerations).append("\n");
		sb.append('}');
		return sb.toString();
	}
	}
