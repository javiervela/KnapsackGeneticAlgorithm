import os
import pandas as pd

# Define constants
FUNCTION_EVALUATIONS = [-1, 1000, 10000]
RESULTS_FILE = "./data/results.parquet"

# Load data
df = pd.read_parquet(RESULTS_FILE)

for evaluations in FUNCTION_EVALUATIONS:
    ANALYSIS_DIR = os.path.join("analysis", f"{evaluations}_evaluations")
    AGGREGATED_STATS_FILE = os.path.join(
        ANALYSIS_DIR, f"fitness_aggregated_statistics_{evaluations}_evaluations.csv"
    )

    # Create directories if they don't exist
    os.makedirs(ANALYSIS_DIR, exist_ok=True)

    # Filter data for specific function evaluations
    df_filtered = df[df["functionEvaluations"] == evaluations]

    # Calculate mean and standard deviation
    stats_df = (
        df_filtered.groupby(
            ["problemIndex", "crossoverProbability", "mutationProbability"]
        )
        .agg(
            mean_fitness=("bestIndividual.fitness", "mean"),
            std_fitness=("bestIndividual.fitness", "std"),
        )
        .reset_index()
    )

    # Merge with original dataframe to get optimalValue
    stats_df = stats_df.merge(
        df[["problemIndex", "problem.optimalValue"]].drop_duplicates(),
        on="problemIndex",
        how="left",
    )

    # Save aggregated statistics to CSV
    stats_df.to_csv(AGGREGATED_STATS_FILE, index=False)
