import os
import pandas as pd
from scipy.stats import wilcoxon
import itertools
from tqdm import tqdm
import matplotlib.pyplot as plt
import seaborn as sns

# Define constants
FUNCTION_EVALUATIONS = [-1, 1000, 10000]
PROBLEM_INDEXES = list(range(7))
RESULTS_FILE = "./data/results.parquet"

# Load data
df = pd.read_parquet(RESULTS_FILE)

for evaluations in FUNCTION_EVALUATIONS:
    ANALYSIS_DIR = os.path.join("analysis", f"{evaluations}_evaluations")
    PLOTS_DIR = os.path.join(ANALYSIS_DIR, "plots/wilcoxon")
    PVALUES_CSV = os.path.join(ANALYSIS_DIR, "wilcoxon_pvalues.csv")
    os.makedirs(PLOTS_DIR, exist_ok=True)

    # Filter data for specific function evaluations
    df_filtered = df[df["functionEvaluations"] == evaluations]

    # Prepare data for Wilcoxon test
    unique_params = df_filtered[
        ["crossoverProbability", "mutationProbability"]
    ].drop_duplicates()
    parameter_pairs = list(
        itertools.combinations(unique_params.itertuples(index=False), 2)
    )

    pvalues_data = []

    for problem_index in PROBLEM_INDEXES:
        problem_df = df_filtered[df_filtered["problemIndex"] == problem_index]

        for param1, param2 in tqdm(
            parameter_pairs, desc=f"Wilcoxon for Problem {problem_index}"
        ):
            param1_df = problem_df[
                (problem_df["crossoverProbability"] == param1.crossoverProbability)
                & (problem_df["mutationProbability"] == param1.mutationProbability)
            ]["bestIndividual.fitness"]

            param2_df = problem_df[
                (problem_df["crossoverProbability"] == param2.crossoverProbability)
                & (problem_df["mutationProbability"] == param2.mutationProbability)
            ]["bestIndividual.fitness"]

            if len(param1_df) == len(param2_df) and len(param1_df) > 0:
                # Check for all-zero differences and handle gracefully
                if (param1_df.values == param2_df.values).all():
                    pvalue = 1.0  # No difference, assign p-value of 1.0
                else:
                    _, pvalue = wilcoxon(param1_df, param2_df)

                pvalues_data.append(
                    {
                        "problemIndex": problem_index,
                        "crossoverProbability_1": param1.crossoverProbability,
                        "mutationProbability_1": param1.mutationProbability,
                        "crossoverProbability_2": param2.crossoverProbability,
                        "mutationProbability_2": param2.mutationProbability,
                        "pvalue": pvalue,
                    }
                )

    # Save p-values to a CSV file
    pvalues_df = pd.DataFrame(pvalues_data)
    pvalues_df.to_csv(PVALUES_CSV, index=False)
