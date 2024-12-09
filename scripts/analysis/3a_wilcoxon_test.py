import os
import pandas as pd
import itertools
import numpy as np
from scipy.stats import wilcoxon

FUNCTION_EVALUATIONS = [-1, 1000, 10000]
PROBLEM_INDEXES = list(range(7))
RESULTS_FILE = "./data/results.parquet"
ALPHA = 0.05

df = pd.read_parquet(RESULTS_FILE)

# Normalize fitness
df["normalized_fitness"] = df["bestIndividual.fitness"] / df["problem.optimalValue"]

for evaluations in FUNCTION_EVALUATIONS:
    ANALYSIS_DIR = os.path.join("analysis", f"{evaluations}_evaluations")
    os.makedirs(ANALYSIS_DIR, exist_ok=True)

    # Filter data for this scenario
    df_sub = df[df["functionEvaluations"] == evaluations].copy()

    # Aggregate by (crossoverProbability, mutationProbability, problemIndex)
    agg = (
        df_sub.groupby(["crossoverProbability", "mutationProbability", "problemIndex"])[
            "normalized_fitness"
        ]
        .mean()
        .reset_index()
    )

    result_dict = {}
    configs = (
        agg[["crossoverProbability", "mutationProbability"]]
        .drop_duplicates()
        .values.tolist()
    )

    for cp, mp in configs:
        subset = agg[
            (agg["crossoverProbability"] == cp) & (agg["mutationProbability"] == mp)
        ]
        subset = subset.sort_values(by="problemIndex")
        perf_vector = subset["normalized_fitness"].values
        if len(perf_vector) == len(PROBLEM_INDEXES):
            result_dict[(cp, mp)] = perf_vector

    # Pairwise comparisons
    all_pairs = list(itertools.combinations(result_dict.keys(), 2))
    comparison_results = []

    for c1, c2 in all_pairs:
        data1 = result_dict[c1]
        data2 = result_dict[c2]

        # Check if all equal
        all_equal = all(abs(a - b) < 1e-14 for a, b in zip(data1, data2))

        if all_equal:
            stat = None
            p = 1.0
        else:
            # Use Wilcoxon test
            # If exact method fails, it will switch automatically
            stat, p = wilcoxon(
                data1,
                data2,
                zero_method="wilcox",
                alternative="two-sided",
                method="auto",
            )

        # Determine winner and loser for this comparison
        mean_c1 = np.mean(data1)
        mean_c2 = np.mean(data2)

        # If p < ALPHA, it is statistically significant
        # and we can determine a winner
        if p < ALPHA:
            if mean_c1 > mean_c2:
                winner = c1
                loser = c2
            else:
                winner = c2
                loser = c1
        else:
            winner = None
            loser = None

        comparison_results.append(
            {
                "config1_cp": c1[0],
                "config1_mp": c1[1],
                "config2_cp": c2[0],
                "config2_mp": c2[1],
                "p-value": p,
                "stat": stat,
                "winner_cp": winner[0] if winner else None,
                "winner_mp": winner[1] if winner else None,
                "loser_cp": loser[0] if loser else None,
                "loser_mp": loser[1] if loser else None,
            }
        )

    # Determine best config based on wins
    wins_count = {}
    for c in result_dict.keys():
        wins_count[c] = 0
    for r in comparison_results:
        if r["winner_cp"] is not None:
            wins_count[(r["winner_cp"], r["winner_mp"])] += 1

    max_wins = max(wins_count.values()) if len(wins_count) > 0 else 0
    best_configs = [c for c, w in wins_count.items() if w == max_wins]

    # If tie, break it by global mean performance
    if len(best_configs) > 1:
        perf_map = {c: np.mean(result_dict[c]) for c in best_configs}
        best_perf = max(perf_map.values())
        best_configs = [c for c, val in perf_map.items() if val == best_perf]

    # Save comparison results to CSV
    comp_df = pd.DataFrame(comparison_results)
    comp_df.to_csv(os.path.join(ANALYSIS_DIR, f"stat_test_results.csv"), index=False)

    # Save wins_count to CSV
    wc_data = []
    for c, w in wins_count.items():
        wc_data.append({"cp": c[0], "mp": c[1], "wins": w})
    wc_df = pd.DataFrame(wc_data)
    wc_df.to_csv(
        os.path.join(ANALYSIS_DIR, f"stat_test_wins_count.csv"),
        index=False,
    )

    # Print best combination(s) to stdout
    print(f"For evaluations={evaluations}, best combination(s): {best_configs}")
