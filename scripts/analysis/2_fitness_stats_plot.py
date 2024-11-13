import os
from tqdm import tqdm
import matplotlib.pyplot as plt
import pandas as pd

# Define constants
FUNCTION_EVALUATIONS = [-1, 1000, 10000]
PROBLEM_INDEXES = list(range(7))
PLOT_FILENAME = "fitness_mean_std.png"

for evaluations in FUNCTION_EVALUATIONS:
    ANALYSIS_DIR = os.path.join("analysis", f"{evaluations}_evaluations")
    PLOTS_DIR = os.path.join(ANALYSIS_DIR, "plots")
    AGGREGATED_STATS_FILE = os.path.join(
        ANALYSIS_DIR, f"fitness_aggregated_statistics_{evaluations}_evaluations.csv"
    )

    # Create directories if they don't exist
    os.makedirs(PLOTS_DIR, exist_ok=True)

    # Load aggregated statistics from CSV
    stats_df = pd.read_csv(AGGREGATED_STATS_FILE)

    # Plotting fitness results by crossover and mutation probability
    for problem_index in tqdm(
        PROBLEM_INDEXES, desc=f"Plotting fitness results for {evaluations} evaluations"
    ):
        subset = stats_df[stats_df["problemIndex"] == problem_index]

        # Calculate min and max for mean fitness and mean fitness ± std
        min_fitness_std = (subset["mean_fitness"] - subset["std_fitness"]).min()
        max_fitness_std = (subset["mean_fitness"] + subset["std_fitness"]).max()

        # Plot mean fitness with shaded standard deviation area
        plt.figure(figsize=(12, 8))
        for mutation_probability in subset["mutationProbability"].unique():
            mutation_subset = subset[
                subset["mutationProbability"] == mutation_probability
            ]
            plt.plot(
                mutation_subset["crossoverProbability"],
                mutation_subset["mean_fitness"],
                marker="o",
                label=f"Mutation Probability {mutation_probability}",
            )
            plt.fill_between(
                mutation_subset["crossoverProbability"],
                mutation_subset["mean_fitness"] - mutation_subset["std_fitness"],
                mutation_subset["mean_fitness"] + mutation_subset["std_fitness"],
                alpha=0.2,
            )

        # Plot the optimal value line
        optimal_value = subset[subset["problemIndex"] == problem_index][
            "problem.optimalValue"
        ].iloc[0]
        plt.axhline(y=optimal_value, color="r", linestyle="--", label="Optimal Value")

        # Labels and title
        plt.title(
            f"Mean Fitness and Standard Deviation for Problem Index {problem_index} ({evaluations} Evaluations)"
        )
        plt.xlabel("Crossover Probability")
        plt.ylabel("Fitness (Mean ± Std Dev)")
        plt.legend(
            title="Mutation Probability", bbox_to_anchor=(1.05, 1), loc="upper left"
        )
        plt.grid(True)

        # Set y-axis limits
        y_margin = 0.05 * (max_fitness_std - min_fitness_std)
        plt.ylim(min_fitness_std - y_margin, max_fitness_std + y_margin)

        # Save plot
        problem_dir = os.path.join(PLOTS_DIR, f"problem_{problem_index}")
        os.makedirs(problem_dir, exist_ok=True)

        plt.savefig(
            os.path.join(problem_dir, PLOT_FILENAME),
            bbox_inches="tight",
        )
        plt.close()
