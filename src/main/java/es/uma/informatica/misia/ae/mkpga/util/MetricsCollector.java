package es.uma.informatica.misia.ae.mkpga.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import es.uma.informatica.misia.ae.mkpga.problem.Individual;
import es.uma.informatica.misia.ae.mkpga.problem.Problem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MetricsCollector {
	private Problem problem;
	private Map<String, Double> parameters;
	private List<Individual> generationBestIndividuals;
	private long startTime;
	private long endTime;
	private int numberOfEvaluations;
	private int numberOfGenerations;

	public MetricsCollector(Problem problem, Map<String, Double> parameters) {
		this.problem = problem;
		this.parameters = parameters;
		this.generationBestIndividuals = new ArrayList<>();
		this.numberOfEvaluations = 0;
		this.numberOfGenerations = 0;
	}

	public void startTimer() {
		this.startTime = System.currentTimeMillis();
	}

	public void stopTimer() {
		this.endTime = System.currentTimeMillis();
	}

	public void addGenerationBestIndividual(Individual generationBestIndividual) {
		this.generationBestIndividuals.add(generationBestIndividual);
	}

	public void incrementEvaluations() {
		this.numberOfEvaluations++;
	}

	public void incrementGenerations() {
		this.numberOfGenerations++;
	}

	public long getExecutionTime() {
		if (endTime == 0) {
			return System.currentTimeMillis() - startTime;
		}
		return endTime - startTime;
	}

	public Problem getProblem() {
		return problem;
	}

	public Map<String, Double> getParameters() {
		return parameters;
	}

	public List<Individual> getGenerationBestIndividuals() {
		return generationBestIndividuals;
	}

	public Individual getBestIndividual() {
		if (generationBestIndividuals.isEmpty()) {
			return null;
		}
		return generationBestIndividuals.get(generationBestIndividuals.size() - 1);
	}

	public int getNumberOfEvaluations() {
		return numberOfEvaluations;
	}

	public int getNumberOfGenerations() {
		return numberOfGenerations;
	}

	public void writeMetricsToJson(String filePath) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonObject json = new JsonObject();

		// Adding data to the JSON object
		JsonArray generationBestIndividualsArray = new JsonArray();
		for (Individual individual : generationBestIndividuals) {
			generationBestIndividualsArray.add(individual.getFitness());
		}
		json.add("bestIndividual", gson.toJsonTree(getBestIndividual()));
		json.addProperty("executionTime", getExecutionTime());
		json.addProperty("numberOfEvaluations", numberOfEvaluations);
		json.addProperty("numberOfGenerations", numberOfGenerations);
		json.add("problem", gson.toJsonTree(problem));
		json.add("parameters", gson.toJsonTree(parameters));
		json.add("generationBestIndividuals", generationBestIndividualsArray);

		// Writing JSON to file
		try (FileWriter file = new FileWriter(filePath)) {
			gson.toJson(json, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MetricsCollector{\n");
		sb.append("  bestIndividual=").append(getBestIndividual()).append(",\n");
		sb.append("  executionTime=").append(getExecutionTime()).append(" ms,\n");
		sb.append("  numberOfEvaluations=").append(numberOfEvaluations).append(",\n");
		sb.append("  numberOfGenerations=").append(numberOfGenerations).append("\n");
		sb.append("  problem=").append(problem).append(",\n");
		sb.append("  parameters=").append(parameters).append(",\n");
		sb.append("  generationBestIndividuals=").append(generationBestIndividuals).append(",\n");
		sb.append('}');
		return sb.toString();
	}

	public void writeSummaryReport() {
		System.out.printf("Best Individual: %s%s, Execution Time: %d ms, Evaluations: %d, Generations: %d%n",
				getBestIndividual().getFitness(),
				(getBestIndividual() != null && getBestIndividual().getFitness() == problem.getOptimalValue())
						? " (Optimum)"
						: "",
				getExecutionTime(), numberOfEvaluations, numberOfGenerations);
	}
}
