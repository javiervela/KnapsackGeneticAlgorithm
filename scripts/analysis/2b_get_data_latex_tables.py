import os
import pandas as pd

FUNCTION_EVALUATIONS = [-1, 1000, 10000]


# Function to generate LaTeX table for fitness and execution time
def generate_latex_tables(df):
    problem_indices = df["problemIndex"].unique()
    fitness_table = pd.DataFrame()
    exec_time_table = pd.DataFrame()

    for problem_id in problem_indices:
        subset = df[df["problemIndex"] == problem_id]

        fitness_subset = subset[
            [
                "crossoverProbability",
                "mutationProbability",
                "mean_fitness",
                "std_fitness",
            ]
        ]
        fitness_subset[f"\(i = {problem_id}\)"] = fitness_subset.apply(
            lambda row: f"{row['mean_fitness']:.1f} ± {row['std_fitness']:.1f}", axis=1
        )
        fitness_subset = fitness_subset.drop(columns=["mean_fitness", "std_fitness"])

        exec_time_subset = subset[
            [
                "crossoverProbability",
                "mutationProbability",
                "mean_time",
                "std_time",
            ]
        ]
        exec_time_subset[f"\(i = {problem_id}\)"] = exec_time_subset.apply(
            lambda row: f"{row['mean_time']:.0f} ± {row['std_time']:.0f}", axis=1
        )
        exec_time_subset = exec_time_subset.drop(columns=["mean_time", "std_time"])

        if fitness_table.empty:
            fitness_table = fitness_subset
            exec_time_table = exec_time_subset
        else:
            fitness_table = pd.merge(
                fitness_table,
                fitness_subset,
                on=["crossoverProbability", "mutationProbability"],
            )
            exec_time_table = pd.merge(
                exec_time_table,
                exec_time_subset,
                on=["crossoverProbability", "mutationProbability"],
            )

    fitness_latex_table = fitness_table.to_latex(
        index=False,
        caption="Fitness Table",
        label="tab:fitness_table",
    )

    exec_time_latex_table = exec_time_table.to_latex(
        index=False,
        caption="Execution Time Table",
        label="tab:exec_time_table",
    )

    return fitness_latex_table, exec_time_latex_table


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
    fitness_latex_table, exec_time_latex_table = generate_latex_tables(merged_df)

    # Save the fitness table to a .tex file
    fitness_filename = os.path.join(ANALYSIS_DIR, "fitness_table.tex")
    with open(fitness_filename, "w", encoding="utf-8") as file:
        file.write(fitness_latex_table)

    # Save the execution time table to a .tex file
    exec_time_filename = os.path.join(ANALYSIS_DIR, "exec_time_table.tex")
    with open(exec_time_filename, "w", encoding="utf-8") as file:
        file.write(exec_time_latex_table)
