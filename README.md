# Multidimensional Knapsack Problem Genetic Algorithm

A basic genetic algorithm implementation for solving the knapsack problem. This project explores the effects of mutation and crossover probabilities on optimization performance.

This project is a fork of a simple evolutionary algorithm originally created by Francisco Chicano (University of Malaga, Spain) for educational purposes. This fork adapts the project specifically for studying genetic algorithms applied to the knapsack problem.

- Report: [report.pdf](doc/report.pdf)

## Installation

### Prerequisites

- **Java JDK 1.8 or higher**:
- **Apache Maven**

### Build the Project

To compile the project and resolve dependencies, use Maven:

```bash
mvn clean install
```

This will download necessary dependencies and build the project.

## Running Instructions

The main program requires four arguments and accepts an optional fifth:

```bash
mvn exec:java -Dexec.mainClass="es.uma.informatica.misia.ae.mkpga.Main" -Dexec.args="<population size> <function evaluations> <crossover probability> <mutation probability> <problem index> [<random seed>]"
```

Replace `<population size>`, `<function evaluations>`, `<crossover probability>`, `<mutation probability>`, `<problem index>`, and `[<random seed>]` with your values.

### Example

```bash
mvn exec:java -Dexec.mainClass="es.uma.informatica.misia.ae.mkpga.Main" -Dexec.args="100 10000 0.01 50 12345"
```

- **population size**: Size of the population (e.g., `100`)
- **function evaluations**: Maximum number of function evaluations (e.g., `10000`). If set to a negative value, the algorithm will run until the optimal solution is found.
- **crossover probability**: Probability of crossover (e.g., `0.85`)
- **mutation probability**: Probability of mutation (e.g., `0.01`)
- **problem index**: Index of the problem instance in the `data/mknap1.txt` file (`0`-`6`)
- **random seed**: (Optional) Seed for random number generation (e.g., `12345`)

### Output

The program will output the best solution found, including its total profit and the items selected.

## Analysis

The project includes Python scripts to analyze the results of the experiments. The scripts calculate and plot statistics.

### Requirements

- **Python**
- **Poetry**: Python dependency management tool

### Install Dependencies

```bash
poetry install
```

## Project Structure

- `pom.xml`: Maven project configuration file.
- `pyproject.toml`: Poetry project configuration file.
- `poetry.lock`: Poetry lock file.
- `src/main/java/es/uma/informatica/misia/ae/mkpga/`: Contains the core classes for the evolutionary algorithm implementation.
  - `Main.java`: Entry point for the program, which parses parameters and initiates the algorithm.
  - `algorithm/`: Contains classes related to the genetic algorithm's structure and processes.
    - `EvolutionaryAlgorithm.java`: The main class representing the genetic algorithm's structure and processes, including initialization, evaluation, and evolution of the population.
    - `selection/`: Contains classes for selection operators.
      - `Selection.java`: Interface for selection operators.
      - `BinaryTournament.java`: Implements binary tournament selection, where two individuals are randomly selected and the one with higher fitness is chosen.
    - `crossover/`: Contains classes for crossover operators.
      - `Crossover.java`: Interface for crossover operators.
      - `SinglePointCrossover.java`: Implements single-point crossover, where a crossover point is selected and the genetic material is exchanged between two parents.
    - `mutation/`: Contains classes for mutation operators.
      - `Mutation.java`: Interface for mutation operators.
      - `BitFlipMutation.java`: Implements bit-flip mutation, where each bit in the individual's chromosome has a probability of being flipped.
    - `replacement/`: Contains classes for replacement operators.
      - `Replacement.java`: Interface for replacement operators.
      - `ElitistReplacement.java`: Implements elitist replacement, where the best individuals from the combined population of parents and offspring are selected for the next generation.
    - `stopping/`: Contains classes for stopping criteria.
      - `StoppingCriterion.java`: Interface for stopping criteria.
      - `OptimalSolutionCriterion.java`: Stops the algorithm when the optimal solution is found.
      - `MaxFunctionEvaluationsCriterion.java`: Stops the algorithm after a maximum number of function evaluations.
  - `problem/`: Contains classes representing the problem domain.
    - `Problem.java`: Interface for problem definitions.
    - `MultidimensionalKnapsackProblem.java`: Represents the multidimensional knapsack problem, including methods for evaluating solutions and generating random individuals.
    - `Individual.java`: Represents an individual solution in the population.
    - `BinaryString.java`: Represents the binary string chromosome of an individual.
  - `util/`: Contains utility classes and methods.
    - `MultidimensionalKnapsackProblemLoader.java`: Utility class for loading problem instances from a file.
    - `MetricsCollector.java`: Utility class for collecting and storing metrics during the algorithm's execution.
- `scripts/`:
  - `run.sh`: Bash script for running the program with different parameters.
  - `download_problem_data_mknap1.sh`: Bash script for downloading the problem instances.
  - `experiments/experiment_all.sh`: Bash script for running experiments with different parameter configurations.
  - `analysis/`: Python scripts to analyze the results of the experiments.
    - `0_load_results.py`: Load the results to the `results.parquet` file.
    - `1_exec_time_stats_calculate.py`: Calculate execution time statistics.
    - `1_fitness_stats_calculate.py`: Calculate fitness value statistics.
    - `2_exec_time_stats_plot.py`: Plot execution time statistics.
    - `2_fitness_stats_plot.py`: Plot fitness value statistics.
    - `3a_wilcoxon_test.py`: Perform Wilcoxon signed rank test between different parameter configurations.
    - `3b_wilcoxon_analysis.py`: Plot the results of the Wilcoxon signed rank test and best parameter configurations.
- `data/`:
  - `mknap1.txt`: Problem instances for the multidimensional knapsack problem.
  - `README.md`: Description of the problem instances.
  - `results.parquet`: Parquet file to store the results of the experiments.
