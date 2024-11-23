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

# Iterate over different evaluation counts
for evaluations in FUNCTION_EVALUATIONS:
    ANALYSIS_DIR = os.path.join("analysis", f"{evaluations}_evaluations")
    os.makedirs(ANALYSIS_DIR, exist_ok=True)
    PVALUES_CSV = os.path.join(ANALYSIS_DIR, "wilcoxon_pvalues.csv")

    # Filter data for the current FUNCTION_EVALUATIONS
    df_filtered = df[df["functionEvaluations"] == evaluations]

    # Get unique parameter combinations of crossover and mutation probabilities
    parameter_combinations = df_filtered[
        ["crossoverProbability", "mutationProbability"]
    ].drop_duplicates()
    parameter_combinations = parameter_combinations.sort_values(
        by=["crossoverProbability", "mutationProbability"]
    )

    # Prepare to store p-values
    pvalues_data = []

    # Iterate over all pairs of parameter combinations
    for param1, param2 in combinations(
        parameter_combinations.itertuples(index=False), 2
    ):
        param1_crossover, param1_mutation = param1
        param2_crossover, param2_mutation = param2

        # Filter data for the two parameter combinations
        group1 = df_filtered[
            (df_filtered["crossoverProbability"] == param1_crossover)
            & (df_filtered["mutationProbability"] == param1_mutation)
        ]

        group2 = df_filtered[
            (df_filtered["crossoverProbability"] == param2_crossover)
            & (df_filtered["mutationProbability"] == param2_mutation)
        ]

        # Ensure both groups have results for all problem instances
        if set(group1["problemIndex"]) != set(PROBLEM_INDEXES) or set(
            group2["problemIndex"]
        ) != set(PROBLEM_INDEXES):
            continue

        # Pair samples by problemIndex
        fitness1 = group1.sort_values("problemIndex")["bestIndividual.fitness"].values
        fitness2 = group2.sort_values("problemIndex")["bestIndividual.fitness"].values

        # Perform Wilcoxon signed-rank test
        try:
            stat, pvalue = wilcoxon(fitness1, fitness2)
        except ValueError:
            # Wilcoxon test requires paired data; skip if there's an issue
            continue

        # Store results
        pvalues_data.append(
            {
                "param1_crossover": param1_crossover,
                "param1_mutation": param1_mutation,
                "param2_crossover": param2_crossover,
                "param2_mutation": param2_mutation,
                "pvalue": pvalue,
            }
        )

    # Save p-values to CSV
    if pvalues_data:
        pvalues_df = pd.DataFrame(pvalues_data)
        pvalues_df.to_csv(PVALUES_CSV, index=False)
