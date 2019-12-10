//
// Created by daltao on 2019/12/10.
//

#ifndef JHELPER_EXAMPLE_PROJECT_ASSIGNMENT_PROBLEM_H
#define JHELPER_EXAMPLE_PROJECT_ASSIGNMENT_PROBLEM_H
#include "common.h"
#include "min_cost_max_flow.h"

template<typename C>
struct assignment_problem {
    int n;
    vector<vector<C>> _costs;

    assignment_problem(int _n = -1) : n(_n) {
        if (n > 0)
            _costs.assign(n, vector<long long>(n, 0));
    }

    template<typename T>
    assignment_problem(const vector<vector<T>> &costs) {
        build(costs);
    }

    template<typename T>
    void build(const vector<vector<T>> &costs) {
        n = costs.size();
        _costs.assign(n, vector<C>(n, 0));

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                _costs[i][j] = costs[i][j];
    }

    C solve() {
        int v = 2 * n + 2, source = v - 2, sink = v - 1;
        min_cost_flow<int, C> graph(v);

        for (int i = 0; i < n; i++) {
            graph.add_directional_edge(source, i, 1, 0);
            graph.add_directional_edge(n + i, sink, 1, 0);
        }

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                graph.add_directional_edge(i, n + j, 1, _costs[i][j]);

        return graph.solve_min_cost_flow(source, sink).second;
    }
};

#endif //JHELPER_EXAMPLE_PROJECT_ASSIGNMENT_PROBLEM_H
