import os
from tqdm import tqdm
import matplotlib.pyplot as plt
import pandas as pd

# Define constants
FUNCTION_EVALUATIONS = [-1, 1000, 10000]
PROBLEM_INDEXES = list(range(7))
PLOT_FILENAME = "execution_time_mean_std.png"

for evaluations in FUNCTION_EVALUATIONS:
    ANALYSIS_DIR = os.path.join("analysis", f"{evaluations}_evaluations")
    PLOTS_DIR = os.path.join(ANALYSIS_DIR, "plots")
    AGGREGATED_STATS_FILE = os.path.join(
        ANALYSIS_DIR, f"exec_time_aggregated_statistics_{evaluations}_evaluations.csv"
    )

    # Create directories if they don't exist
    os.makedirs(PLOTS_DIR, exist_ok=True)

    # Load aggregated statistics from CSV
    stats_df = pd.read_csv(AGGREGATED_STATS_FILE)

    # Plotting execution time results by crossover and mutation probability
    for problem_index in tqdm(
        PROBLEM_INDEXES,
        desc=f"Plotting execution time results for {evaluations} evaluations",
    ):
        subset = stats_df[stats_df["problemIndex"] == problem_index]

        # Calculate min and max for mean execution time ± std
        min_time_std = (subset["mean_time"] - subset["std_time"]).min()
        max_time_std = (subset["mean_time"] + subset["std_time"]).max()

        # Plot mean execution time with shaded standard deviation
        plt.figure(figsize=(12, 8))
        for mutation_probability in subset["mutationProbability"].unique():
            mutation_subset = subset[
                subset["mutationProbability"] == mutation_probability
            ]
            plt.plot(
                mutation_subset["crossoverProbability"],
                mutation_subset["mean_time"],
                marker="o",
                label=f"Mutation Probability {mutation_probability}",
            )
            plt.fill_between(
                mutation_subset["crossoverProbability"],
                mutation_subset["mean_time"] - mutation_subset["std_time"],
                mutation_subset["mean_time"] + mutation_subset["std_time"],
                alpha=0.2,
            )

        # Labels and title for execution time plot
        plt.title(
            f"Mean Execution Time and Standard Deviation for Problem Index {problem_index} ({evaluations} Evaluations)"
        )
        plt.xlabel("Crossover Probability")
        plt.ylabel("Execution Time (Mean ± Std Dev) [ms]")
        plt.legend(
            title="Mutation Probability", bbox_to_anchor=(1.05, 1), loc="upper left"
        )
        plt.grid(True)

        # Set y-axis limits for better visualization
        y_margin = 0.05 * (max_time_std - min_time_std)
        plt.ylim(min_time_std - y_margin, max_time_std + y_margin)

        # Save execution time plot
        problem_dir = os.path.join(PLOTS_DIR, f"problem_{problem_index}")
        os.makedirs(problem_dir, exist_ok=True)

        plt.savefig(
            os.path.join(
                problem_dir,
                PLOT_FILENAME,
            ),
            bbox_inches="tight",
        )
        plt.close()
