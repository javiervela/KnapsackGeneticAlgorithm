import os
import matplotlib.pyplot as plt
import pandas as pd

# Define constants
FUNCTION_EVALUATIONS = [-1, 1000, 10000]
PROBLEM_INDEXES = list(range(7))
PLOT_FILENAME = "fitness_mean_std.png"
TEMP_PLOT_FILENAME = "fitness_mean_std_temp.png"
AGGREGATED_PLOT_FILENAME_TEMPLATE = "aggregated_fitness_mean_std_{}.png"

# Configure font sizes for plots
plt.rcParams.update(
    {
        "axes.titlesize": 18,
        "axes.labelsize": 16,
        "xtick.labelsize": 14,
        "ytick.labelsize": 14,
        "legend.fontsize": 14,
        "legend.title_fontsize": 16,
    }
)

for evaluations in FUNCTION_EVALUATIONS:
    ANALYSIS_DIR = os.path.join("analysis", f"{evaluations}_evaluations")
    PLOTS_DIR = os.path.join(ANALYSIS_DIR, "plots")
    AGGREGATED_STATS_FILE = os.path.join(
        ANALYSIS_DIR, f"fitness_aggregated_statistics_{evaluations}_evaluations.csv"
    )

    os.makedirs(PLOTS_DIR, exist_ok=True)

    # Load data
    stats_df = pd.read_csv(AGGREGATED_STATS_FILE)

    # Generate individual plots for each problem index
    for problem_index in PROBLEM_INDEXES:
        subset = stats_df[stats_df["problemIndex"] == problem_index]

        # Determine y-axis limits
        min_fitness_std = (subset["mean_fitness"] - subset["std_fitness"]).min()
        max_fitness_std = (subset["mean_fitness"] + subset["std_fitness"]).max()

        # Plot mean fitness with shaded standard deviation
        fig, ax = plt.subplots(figsize=(12, 8))
        for mutation_probability in subset["mutationProbability"].unique():
            mutation_subset = subset[
                subset["mutationProbability"] == mutation_probability
            ]
            ax.plot(
                mutation_subset["crossoverProbability"],
                mutation_subset["mean_fitness"],
                marker="o",
                label=f"Mutation Probability {mutation_probability}",
            )
            ax.fill_between(
                mutation_subset["crossoverProbability"],
                mutation_subset["mean_fitness"] - mutation_subset["std_fitness"],
                mutation_subset["mean_fitness"] + mutation_subset["std_fitness"],
                alpha=0.2,
            )

        # Plot the optimal value line
        optimal_value = subset["problem.optimalValue"].iloc[0]
        ax.axhline(y=optimal_value, color="r", linestyle="--", label="Optimal Value")

        # Configure labels, title, and legend
        ax.set_xlabel("Crossover Probability")
        ax.set_ylabel("Fitness (Mean ± Std Dev)")
        ax.grid(True)
        ax.set_title(
            f"Mean Fitness ± Std Dev for Problem {problem_index} ({evaluations} Evaluations)"
        )
        ax.legend(
            title="Mutation Probability", bbox_to_anchor=(1.05, 1), loc="upper left"
        )

        # Save plot with title and legend
        problem_dir = os.path.join(PLOTS_DIR, f"problem_{problem_index}")
        os.makedirs(problem_dir, exist_ok=True)
        fig.savefig(
            os.path.join(problem_dir, PLOT_FILENAME), bbox_inches="tight", dpi=300
        )

        # Save a temporary copy without title and legend for aggregation
        ax.get_legend().remove()
        ax.set_title(f"Problem {problem_index}")
        fig.savefig(
            os.path.join(problem_dir, TEMP_PLOT_FILENAME), bbox_inches="tight", dpi=300
        )
        plt.close(fig)

    # Create 3x3 aggregated plot
    fig, axs = plt.subplots(3, 3, figsize=(18, 18))
    axs = axs.flatten()

    # Load and display each temporary plot in the grid
    for i, problem_index in enumerate(PROBLEM_INDEXES):
        img = plt.imread(
            os.path.join(PLOTS_DIR, f"problem_{problem_index}", TEMP_PLOT_FILENAME)
        )
        axs[i].imshow(img)
        axs[i].axis("off")

    # Leave 8th cell empty, add legend in the 9th cell
    axs[7].axis("off")
    handles, labels = ax.get_legend_handles_labels()
    axs[8].legend(handles, labels, title="Mutation Probability", loc="center")
    axs[8].axis("off")

    # Set overall title and adjust layout
    fig.suptitle(f"Fitness Mean ± Std Dev for {evaluations} Evaluations", fontsize=20)
    plt.tight_layout(rect=[0, 0, 1, 0.75])

    # Save the aggregated plot
    fig.savefig(
        os.path.join(PLOTS_DIR, AGGREGATED_PLOT_FILENAME_TEMPLATE.format(evaluations)),
        bbox_inches="tight",
        dpi=300,
    )
    plt.close(fig)

    # Remove temporary files
    for problem_index in PROBLEM_INDEXES:
        temp_fig_path = os.path.join(
            PLOTS_DIR, f"problem_{problem_index}", TEMP_PLOT_FILENAME
        )
        if os.path.exists(temp_fig_path):
            os.remove(temp_fig_path)
