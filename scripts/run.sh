#!/bin/bash

# Check if the correct number of arguments is provided
if [ "$#" -lt 4 ]; then
	echo "Usage: ./run.sh <population size> <function evaluations> <mutation probability> <problem index> [<random seed>]"
	exit 1
fi

# Step 1: Clean and build the project
echo "Building the project with Maven..."
mvn clean install -q

# Check if the build was successful
if [ $? -ne 0 ]; then
	echo "Build failed. Exiting."
	exit 1
fi

RESULTS_DIR="$(pwd)/results"
if [ ! -d "$RESULTS_DIR" ]; then
	mkdir -p "$RESULTS_DIR"
fi
export RESULTS_FILE_PATH="$RESULTS_DIR/$(date +'%Y%m%d_%H%M%S').json"
export MKP_FILE_PATH="$(pwd)/data/mknap1.txt"

# Step 2: Run the main class with provided arguments
echo "Running the project with the following arguments:"
echo "  (env) RESULTS_FILE_PATH: $RESULTS_FILE_PATH"
echo "  (env) MKP_FILE_PATH: $MKP_FILE_PATH"
echo "  Population size: $1"
if [ "$2" -lt 0 ]; then
	echo "  Function evaluations: $2 (until optimal solution is found)"
else
	echo "  Function evaluations: $2"
fi
echo "  mutation probability: $3"
echo "  Problem index: $4"
if [ -n "$5" ]; then
	echo "  Random seed: $5"
else
	echo "  Random seed: Not provided"
fi
mvn exec:java -q -Dexec.mainClass="es.uma.informatica.misia.ae.mkpga.Main" \
	-Dexec.args="$1 $2 $3 $4 ${5:-}"
