# Knapsack Genetic Algorithm

A basic genetic algorithm implementation for solving the knapsack problem. This project explores the effects of mutation and crossover probabilities on optimization performance.

This project is a fork of a simple evolutionary algorithm originally created by Francisco Chicano (University of Malaga, Spain) for educational purposes. This fork adapts the project specifically for studying genetic algorithms applied to the knapsack problem.

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
mvn exec:java -Dexec.mainClass="es.uma.informatica.misia.ae.simpleea.Main" -Dexec.args="<population size> <function evaluations> <bitflip probability> <problem index> [<random seed>]"
```

Replace `<population size>`, `<function evaluations>`, `<bitflip probability>`, `<problem index>`, and `[<random seed>]` with your values.

### Example

```bash
mvn exec:java -Dexec.mainClass="es.uma.informatica.misia.ae.simpleea.Main" -Dexec.args="100 10000 0.01 50 12345"
```

- **population size**: Size of the population (e.g., `100`)
- **function evaluations**: Maximum number of function evaluations (e.g., `10000`). If set to a negative value, the algorithm will run until the optimal solution is found.
- **bitflip probability**: Probability of bitflip mutation (e.g., `0.01`)
- **problem index**: Index of the problem instance in the `data/mknap1.txt` file (`0`-`6`)
- **random seed**: (Optional) Seed for random number generation (e.g., `12345`)

### Output

The program will output the best solution found, including its total profit and the items selected.

## Project Structure

- `src/main/java/es/uma/informatica/misia/ae/simpleea/`: Contains the core classes for the evolutionary algorithm implementation.
  - `Main.java`: Entry point for the program, which parses parameters and initiates the algorithm.
  - `EvolutionaryAlgorithm.java`: The main class representing the genetic algorithm's structure and processes.
  - `Problem.java`, `MultidimensionalKnapsackProblem.java`: Classes representing the knapsack problem.
  - `Individual.java`, `BinaryString.java`: Classes representing individuals and its underlying binary representation.
  - `Selection.java`, `BinaryTournament.java`: Classes for selection operators.
  - `Crossover.java`, `SinglePointCrossover.java`: Classes for crossover operators.
  - `Mutation.java`, `BitFlipMutation.java`: Classes for mutation operators.
  - `Replacement.java`, `ElitistReplacement.java`: Classes for replacement operators.
  - `StoppingCriterion.java`, `OptimalSolutionCriterion.java`, `MaxFunctionEvaluationsCriterion.java`: Classes for stopping criteria.
  - `MultidimensionalKnapsackProblemLoader.java`: Utility class for loading problem instances.
- `scripts/`:
  - `run.sh`: Bash script for running the program with different parameters.
  - `download_problem_data_mknap1.sh`: Bash script for downloading the problem instances.
- `data`:
  - `mknap1.txt`: Problem instances for the multidimensional knapsack problem.
  - `README.md`: Description of the problem instances.
