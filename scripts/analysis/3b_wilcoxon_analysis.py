import os
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

# Parameters
FUNCTION_EVALUATIONS = [-1, 1000, 10000]

# For each evaluation scenario:
for evaluations in FUNCTION_EVALUATIONS:
    ANALYSIS_DIR = os.path.join("analysis", f"{evaluations}_evaluations")
    PLOTS_DIR = os.path.join(ANALYSIS_DIR, "plots")
    os.makedirs(PLOTS_DIR, exist_ok=True)

    wins_file = os.path.join(ANALYSIS_DIR, f"stat_test_wins_count.csv")
    wins_df = pd.read_csv(wins_file)

    # Determine best config by max wins
    max_wins = wins_df["wins"].max() if len(wins_df) > 0 else 0
    best_configs = wins_df[wins_df["wins"] == max_wins]

    # Print best combination(s) to stdout
    print(f"For evaluations={evaluations}, best combination(s) based on wins: ")
    print(best_configs)

    # Pivot for heatmap: rows=mp, cols=cp
    pivot = wins_df.pivot(index="mp", columns="cp", values="wins")

    plt.figure(figsize=(8, 6))
    sns.heatmap(pivot, annot=True, cmap="viridis")
    plt.title(f"Number of Wins by Configuration (Evaluations={evaluations})")
    plt.xlabel("Crossover Probability")
    plt.ylabel("Mutation Probability")
    plt.tight_layout()

    plot_name = os.path.join(PLOTS_DIR, f"stat_test_wins_heatmap.png")
    plt.savefig(plot_name)
    plt.close()
