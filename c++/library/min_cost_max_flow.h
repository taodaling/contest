//
// Created by daltao on 2019/12/10.
//

#ifndef JHELPER_EXAMPLE_PROJECT_MIN_COST_MAX_FLOW_H
#define JHELPER_EXAMPLE_PROJECT_MIN_COST_MAX_FLOW_H

#include "common.h"

namespace other {
    template<typename flow_t, typename cost_t>
    struct min_cost_flow {
        static const flow_t FLOW_INF;
        static const cost_t COST_INF;

        struct edge {
            int node, rev;
            flow_t capacity;
            cost_t cost;

            edge(int _node = -1, int _rev = -1, flow_t _capacity = 0, cost_t _cost = 0)
                    : node(_node), rev(_rev), capacity(_capacity), cost(_cost) {}
        };

        int V = -1;
        vector<vector<edge>> adj;
        vector<cost_t> dist;
        vector<int> prev;
        vector<edge *> prev_edge;

        min_cost_flow(int vertices = -1) {
            if (vertices >= 0)
                init(vertices);
        }

        void init(int vertices) {
            V = vertices;
            adj.assign(V, {});
            dist.resize(V);
            prev.resize(V);
            prev_edge.resize(V);
        }

        void add_directional_edge(int u, int v, flow_t capacity, cost_t cost) {
            edge uv_edge(v, adj[v].size() + (u == v ? 1 : 0), capacity, cost);
            edge vu_edge(u, adj[u].size(), 0, -cost);
            adj[u].push_back(uv_edge);
            adj[v].push_back(vu_edge);
        }

        edge &reverse_edge(const edge &e) {
            return adj[e.node][e.rev];
        }

        bool bellman_ford(int source, int sink) {
            for (int i = 0; i < V; i++) {
                dist[i] = COST_INF;
                prev[i] = -1;
                prev_edge[i] = nullptr;
            }

            vector<int> last_seen(V, -1);
            vector<int> nodes(1, source);
            dist[source] = 0;

            for (int iteration = 0; iteration < V; iteration++) {
                vector<int> next_nodes;

                for (int node : nodes)
                    for (edge &e : adj[node])
                        if (e.capacity > 0 && dist[node] + e.cost < dist[e.node]) {
                            dist[e.node] = dist[node] + e.cost;
                            prev[e.node] = node;
                            prev_edge[e.node] = &e;

                            if (last_seen[e.node] != iteration) {
                                last_seen[e.node] = iteration;
                                next_nodes.push_back(e.node);
                            }
                        }

                swap(nodes, next_nodes);
            }

            return prev[sink] != -1;
        }

        struct dijkstra_state {
            cost_t dist;
            int node;

            bool operator<(const dijkstra_state &other) const {
                return dist > other.dist;
            }
        };

        void dijkstra_check(int node, cost_t potential_dist, int previous, edge *previous_edge,
                            priority_queue<dijkstra_state> &pq) {
            if (potential_dist < dist[node]) {
                dist[node] = potential_dist;
                prev[node] = previous;
                prev_edge[node] = previous_edge;
                pq.push({dist[node], node});
            }
        }

        bool dijkstra(int source, int sink) {
            dist.assign(V, COST_INF);
            prev.assign(V, -1);
            prev_edge.assign(V, nullptr);

            priority_queue<dijkstra_state> pq;
            dijkstra_check(source, 0, -1, nullptr, pq);

            while (!pq.empty()) {
                dijkstra_state top = pq.top();
                pq.pop();

                if (top.dist > dist[top.node])
                    continue;

                for (edge &e : adj[top.node])
                    if (e.capacity > 0)
                        dijkstra_check(e.node, top.dist + e.cost, top.node, &e, pq);
            }

            return prev[sink] != -1;
        }

        void reduce_cost() {
            for (int i = 0; i < V; i++)
                for (edge &e : adj[i])
                    e.cost += dist[i] - dist[e.node];
        }

        pair<flow_t, cost_t> solve_min_cost_flow(int source, int sink, flow_t flow_goal = FLOW_INF) {
            assert(V >= 0);

            if (!bellman_ford(source, sink))
                return make_pair(0, 0);

            flow_t total_flow = 0;
            cost_t total_cost = 0;
            cost_t reduce_sum = 0;

            do {
                reduce_cost();
                reduce_sum += dist[sink];
                flow_t path_cap = flow_goal - total_flow;

                for (int node = sink; prev[node] != -1; node = prev[node])
                    path_cap = min(path_cap, prev_edge[node]->capacity);

                for (int node = sink; prev[node] != -1; node = prev[node]) {
                    edge *e = prev_edge[node];
                    assert(e->cost == 0);
                    e->capacity -= path_cap;
                    reverse_edge(*e).capacity += path_cap;
                }

                total_flow += path_cap;
                total_cost += reduce_sum * path_cap;
            } while (total_flow < flow_goal && dijkstra(source, sink));

            return make_pair(total_flow, total_cost);
        }
    };

    template<typename flow_t, typename cost_t>
    const flow_t min_cost_flow<flow_t, cost_t>::FLOW_INF = numeric_limits<flow_t>::max() / 2;
    template<typename flow_t, typename cost_t>
    const cost_t min_cost_flow<flow_t, cost_t>::COST_INF = numeric_limits<cost_t>::max() / 2;

}

#endif //JHELPER_EXAMPLE_PROJECT_MIN_COST_MAX_FLOW_H
