import os
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
from tqdm import tqdm

# Constants
FUNCTION_EVALUATIONS = [-1, 1000, 10000]
PROBLEM_INDEXES = list(range(7))
ANALYSIS_DIR = "./analysis"

# Load fitness data
RESULTS_FILE = "./data/results.parquet"
df = pd.read_parquet(RESULTS_FILE)

# Iterate through evaluations
for evaluations in FUNCTION_EVALUATIONS:
    # Directory and file paths
    PLOTS_DIR = os.path.join(ANALYSIS_DIR, f"{evaluations}_evaluations/plots")
    pvalue_file = os.path.join(
        ANALYSIS_DIR, f"{evaluations}_evaluations/wilcoxon_pvalues.csv"
    )
    os.makedirs(PLOTS_DIR, exist_ok=True)

    # Load p-value data
    pvalues_df = pd.read_csv(pvalue_file)

    # Iterate through problem indexes
    for problem_index in tqdm(
        PROBLEM_INDEXES, desc=f"Processing for {evaluations} evaluations"
    ):
        problem_dir = os.path.join(PLOTS_DIR, f"problem_{problem_index}")
        os.makedirs(problem_dir, exist_ok=True)

        # Filter fitness data for the problem index
        fitness_df = df[
            (df["problemIndex"] == problem_index)
            & (df["functionEvaluations"] == evaluations)
        ]

        # Initialize wins dictionary
        wins = {}

        # Loop through all combinations of parameter pairs
        for _, row in pvalues_df[
            pvalues_df["problemIndex"] == problem_index
        ].iterrows():
            comb1 = (row["comb1_crossover"], row["comb1_mutation"])
            comb2 = (row["comb2_crossover"], row["comb2_mutation"])
            pvalue = row["pvalue"]

            # Get mean fitness values for each combination
            fitness_comb1 = fitness_df[
                (fitness_df["crossoverProbability"] == comb1[0])
                & (fitness_df["mutationProbability"] == comb1[1])
            ]["bestIndividual.fitness"].mean()

            fitness_comb2 = fitness_df[
                (fitness_df["crossoverProbability"] == comb2[0])
                & (fitness_df["mutationProbability"] == comb2[1])
            ]["bestIndividual.fitness"].mean()

            # Consider a win only if p-value < 0.05 and fitness is better
            if pvalue < 0.05:
                if fitness_comb1 > fitness_comb2:
                    wins[comb1] = wins.get(comb1, 0) + 1
                elif fitness_comb2 > fitness_comb1:
                    wins[comb2] = wins.get(comb2, 0) + 1

        # Create a dataframe for visualization
        wins_df = pd.DataFrame(
            [{"crossover": k[0], "mutation": k[1], "wins": v} for k, v in wins.items()]
        )

        # Ensure 'mutation' column exists before pivoting
        if "mutation" in wins_df.columns and "crossover" in wins_df.columns:
            # Pivot table for heatmap
            heatmap_data = wins_df.pivot(
                index="mutation", columns="crossover", values="wins"
            ).fillna(0)
        else:
            heatmap_data = pd.DataFrame()

        # Plot heatmap if data is not empty
        if not heatmap_data.empty:
            plt.figure(figsize=(10, 8))
            sns.heatmap(
                heatmap_data,
                annot=True,
                fmt="g",
                cmap="YlGnBu",
                cbar_kws={"label": "Number of Wins"},
            )
            plt.title(
                f"Best Combinations for Problem {problem_index} ({evaluations} Evaluations)"
            )
            plt.xlabel("Crossover Probability")
            plt.ylabel("Mutation Probability")

            # Save plot
            plt.savefig(
                os.path.join(problem_dir, f"best_combinations_wins_{problem_index}.png")
            )
            plt.close()
