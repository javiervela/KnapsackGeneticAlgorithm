import os
import pandas as pd
from scipy.stats import wilcoxon
from itertools import combinations

# Define constants
FUNCTION_EVALUATIONS = [-1, 1000, 10000]
PROBLEM_INDEXES = list(range(7))
RESULTS_FILE = "./data/results.parquet"

# Load data
df = pd.read_parquet(RESULTS_FILE)

# Loop through evaluation levels
for evaluations in FUNCTION_EVALUATIONS:
    ANALYSIS_DIR = os.path.join("analysis", f"{evaluations}_evaluations")
    os.makedirs(ANALYSIS_DIR, exist_ok=True)
    PVALUES_CSV = os.path.join(ANALYSIS_DIR, "wilcoxon_pvalues.csv")

    # Filter data for the current evaluations level
    df_filtered = df[df["functionEvaluations"] == evaluations]

    # Prepare a list to store p-values
    pvalues = []

    # Loop through each problem index
    for problem_index in PROBLEM_INDEXES:
        # Filter data for the current problem index
        df_problem = df_filtered[df_filtered["problemIndex"] == problem_index]

        # Get all unique parameter combinations
        parameter_combinations = df_problem.groupby(
            ["crossoverProbability", "mutationProbability"]
        ).groups.keys()

        # Perform pairwise Wilcoxon tests for each combination
        for comb1, comb2 in combinations(parameter_combinations, 2):
            # Extract fitness values for the two parameter combinations
            fitness1 = df_problem[
                (df_problem["crossoverProbability"] == comb1[0])
                & (df_problem["mutationProbability"] == comb1[1])
            ]["bestIndividual.fitness"]

            fitness2 = df_problem[
                (df_problem["crossoverProbability"] == comb2[0])
                & (df_problem["mutationProbability"] == comb2[1])
            ]["bestIndividual.fitness"]

            # Perform Wilcoxon test
            try:
                stat, pvalue = wilcoxon(fitness1, fitness2, alternative="two-sided")
            except ValueError:
                # Handle cases where the fitness values are identical or have insufficient data
                pvalue = float("nan")

            # Store results
            pvalues.append(
                {
                    "problemIndex": problem_index,
                    "comb1_crossover": comb1[0],
                    "comb1_mutation": comb1[1],
                    "comb2_crossover": comb2[0],
                    "comb2_mutation": comb2[1],
                    "pvalue": pvalue,
                }
            )

    # Convert results to a DataFrame and save as CSV
    pvalues_df = pd.DataFrame(pvalues)
    pvalues_df.to_csv(PVALUES_CSV, index=False)

    print(f"P-values for {evaluations} evaluations saved to {PVALUES_CSV}")
