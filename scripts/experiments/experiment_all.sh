#!/bin/bash

# Using package instead of install to create JAR file and avoid executing through Maven
mvn clean install package -q
if [ $? -ne 0 ]; then
	echo "Build failed. Exiting."
	exit 1
fi

# Experiment parameters
PROBLEM_INDEXES=($(seq 0 6))
EXECUTIONS_N=($(seq 0 30))
export MKP_FILE_PATH="$(pwd)/data/mknap1.txt"

# Algorithm parameters
POPULATION_SIZE=10
FUNCTION_EVALUATIONS=1000 # (1000 10000 -1)
CROSSOVER_PROBABILITIES=(0.1 0.3 0.5 0.7 0.9 1)
MUTATION_PROBABILITIES=(0.01 0.05 0.1 0.2 0.3 0.5)

for problem_index in "${PROBLEM_INDEXES[@]}"; do
	for crossover_probability in "${CROSSOVER_PROBABILITIES[@]}"; do
		for mutation_probability in "${MUTATION_PROBABILITIES[@]}"; do
			RESULTS_DIR="$(pwd)/results/experiment_all/function_evaluations_$FUNCTION_EVALUATIONS/problem_$problem_index/crossover_$crossover_probability/mutation_$mutation_probability"
			mkdir -p "$RESULTS_DIR"

			for execution_i in "${EXECUTIONS_N[@]}"; do
				export RESULTS_FILE_PATH="$RESULTS_DIR/results_$execution_i.json"

				if [ -f "$RESULTS_FILE_PATH" ]; then
					echo "Results file $RESULTS_FILE_PATH already exists. Skipping execution."
					continue
				fi

				CROSSOVER_PROBABILITY=$crossover_probability
				MUTATION_PROBABILITY=$mutation_probability
				PROBLEM_INDEX=$problem_index
				RANDOM_SEED=$execution_i

				echo "Running experiment with parameters: POPULATION_SIZE=$POPULATION_SIZE, FUNCTION_EVALUATIONS=$FUNCTION_EVALUATIONS, CROSSOVER_PROBABILITY=$CROSSOVER_PROBABILITY, MUTATION_PROBABILITY=$MUTATION_PROBABILITY, PROBLEM_INDEX=$PROBLEM_INDEX, RANDOM_SEED=$RANDOM_SEED"

				java -cp $(pwd)/target/ae.mkpga-0.0.1-SNAPSHOT-jar-with-dependencies.jar es.uma.informatica.misia.ae.mkpga.Main $POPULATION_SIZE $FUNCTION_EVALUATIONS $CROSSOVER_PROBABILITY $MUTATION_PROBABILITY $PROBLEM_INDEX $RANDOM_SEED
			done
		done
	done
done
