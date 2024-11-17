import os

import pandas as pd


FUNCTION_EVALUATIONS = [-1, 1000, 10000]


# Function to generate LaTeX table for each problem index
def generate_latex_tables(df):
    problem_indices = df["problemIndex"].unique()
    tables = []

    for problem_id in problem_indices:
        subset = df[df["problemIndex"] == problem_id]

        latex_table = (
            subset[
                [
                    "crossoverProbability",
                    "mutationProbability",
                    "mean_fitness",
                    "std_fitness",
                    "mean_time",
                    "std_time",
                ]
            ]
            .rename(
                columns={
                    "crossoverProbability": "Crossover Probability",
                    "mutationProbability": "Mutation Probability",
                    "mean_fitness": "Mean Fitness",
                    "std_fitness": "Std Fitness",
                    "mean_time": "Mean Exec Time",
                    "std_time": "Std Exec Time",
                }
            )
            .to_latex(
                index=False,
                float_format="%.2f",
                caption=f"Problem Index {problem_id}",
                label=f"tab:problem_{problem_id}",
            )
        )
        tables.append(latex_table)

    return tables


# Load the execution time and fitness data files
for evaluations in FUNCTION_EVALUATIONS:
    ANALYSIS_DIR = os.path.join("analysis", f"{evaluations}_evaluations")
    AGGREGATED_FITNESS_STATS_FILE = os.path.join(
        ANALYSIS_DIR, f"fitness_aggregated_statistics_{evaluations}_evaluations.csv"
    )
    AGGREGATED_EXEC_TIME_STATS_FILE = os.path.join(
        ANALYSIS_DIR, f"exec_time_aggregated_statistics_{evaluations}_evaluations.csv"
    )

    exec_time_df = pd.read_csv(AGGREGATED_EXEC_TIME_STATS_FILE)
    fitness_df = pd.read_csv(AGGREGATED_FITNESS_STATS_FILE)

    # Merge the two dataframes on common columns
    merged_df = pd.merge(
        exec_time_df,
        fitness_df,
        on=["problemIndex", "crossoverProbability", "mutationProbability"],
        suffixes=("_time", "_fitness"),
    )

    # Generate LaTeX tables
    latex_tables = generate_latex_tables(merged_df)

    # Save each table to a separate .tex file
    for idx, table in enumerate(latex_tables):
        problem_index = merged_df["problemIndex"].unique()[idx]
        filename = os.path.join(ANALYSIS_DIR, f"problem_{problem_index}.tex")
        with open(filename, "w", encoding="utf-8") as file:
            file.write(table)
