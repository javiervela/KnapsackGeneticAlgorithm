# Multidimensional Knapsack Problem (MKP) - `mknap1` Problem Instances

The **Multidimensional Knapsack Problem (MKP)** is a combinatorial optimization problem that extends the classic knapsack problem to multiple dimensions. In this setting, each item consumes multiple resources, each with its own capacity constraint.

## Problem Structure

Each `mknap1` instance consists of multiple test problems, each defined as follows:

1. **Number of Items (`n`)**: Total items available to select from.
2. **Number of Constraints (`m`)**: Number of different resources that limit item selection.
3. **Optimal Solution Value**: Maximum known profit achievable for the instance (set to zero if unknown).

Each instance seeks to maximize the profit of selected items without exceeding resource capacities.

### Components of Each Test Problem

An MKP test problem includes the following:

#### 1. Items

- Each **item** has a **profit** and a **resource requirement** for each resource.
- Items are represented as binary decision variables:
  - `x(j)` = 1 if item `j` is included, 0 otherwise.

#### 2. Resources

- Each **resource** represents a type of limitation, such as weight, volume, or budget.
- For each resource, a maximum allowable amount (capacity) restricts total usage by the chosen items.

#### 3. Constraints

- Each **constraint** corresponds to a **resource limit**.
- For each resource, a constraint ensures that the sum of that resource’s usage across all selected items doesn’t exceed its capacity.

#### 4. Capacity (Right-Hand Side of Constraints)

- The **capacity** for each resource defines the maximum allowable total usage of that resource by the selected items.

## Objective Function and Constraints

The goal is to select items to maximize profit while adhering to all resource constraints:

```math
\text{Maximize } \sum_{j=1}^{n} p(j) \cdot x(j)
```

subject to

```math
\sum_{j=1}^{n} r(i,j) \cdot x(j) \leq b(i), \quad \text{for } i=1, \ldots, m
```

where:

- `p(j)`: Profit of item `j`
- `r(i,j)`: Amount of resource `i` used by item `j`
- `b(i)`: Capacity of resource `i`
- `x(j)`: Decision variable for item `j`

## Data Format for `mknap1` Instances

Each `mknap1` problem instance follows this format:

1. **Header**:

   - Number of items (`n`), number of constraints (`m`), and optimal solution value (if known).

2. **Profits**:

   - A line listing the profits of each item.

3. **Resource Requirements**:

   - For each constraint (resource), a line lists each item’s requirement for that resource.

4. **Resource Capacities**:
   - A final line specifies the capacity for each resource.

### Example Format

Below is an example of the `mknap1` data format:

```plaintext
6 10 3800 # Header: 6 items, 10 resources, optimal value = 3800
100 600 1200 2400 500 2000 # Profits of each item
8 12 13 64 22 41 # Resource requirements for constraint 1
8 12 13 75 22 41 # Resource requirements for constraint 2
...
80 96 20 36 44 48 10 18 22 24 # Capacities for each resource
```

In this example:

- **6 items** are available, each with a specific profit.
- **10 resources** are constrained, each with a capacity.
- The goal is to maximize total profit, selecting items such that resource usage for each constraint does not exceed the specified capacity.

## Summary

The `mknap1` file provides multiple MKP test problems. Each test problem consists of items with profits and resource requirements across multiple resources, along with constraints defining each resource's maximum allowable capacity. Solving an instance involves selecting items to maximize total profit without violating any resource constraints.
