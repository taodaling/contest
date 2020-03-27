#ifndef COST_FLOW_H
#define COST_FLOW_H

#include "common.h"

namespace cost_flow {

template <class T>
struct CostFlowEdge {
  int rev;
  T flow;
  bool real;
  int to;
  T cost;

  CostFlowEdge(int rv, T f, bool r, int t, T c)
      : rev(rv), flow(f), real(r), to(t), cost(c) {}
};

template <class T>
ostream &operator<<(ostream &os, CostFlowEdge<T> &e) {
  os << "{ flow = " << e.flow << ", real = " << e.real << ", to = " << e.to
     << "}";
  return os;
}

template <class T>
ostream &operator<<(ostream &os, vector<vector<CostFlowEdge<T>>> &g) {
  for (int i = 0; i < g.size(); i++) {
    for (CostFlowEdge<T> &e : g[i]) {
      if (e.real) {
        os << i << "-" << e.flow << "(" << g[e.to][e.rev].flow << ")"
           << "|" << e.cost << "->" << e.to << endl;
      }
    }
  }
  return os;
}

template <class T>
void Reset(vector<vector<CostFlowEdge<T>>> &g) {
  for (vector<CostFlowEdge<T>> &v : g) {
    for (CostFlowEdge<T> &t : v) {
      if (t.real) {
        t.rev->flow += t.flow;
        t.flow = 0;
      }
    }
  }
}

template <class T>
inline void Send(vector<vector<CostFlowEdge<T>>> &g, CostFlowEdge<T> &e,
                 T flow) {
  e.flow += flow;
  g[e.to][e.rev].flow -= flow;
}

template <class T>
void AddEdge(vector<vector<CostFlowEdge<T>>> &g, int u, int v, T cap, T cost) {
  if (u != v) {
    g[u].emplace_back(g[v].size(), 0, true, v, cost);
    g[v].emplace_back(g[u].size() - 1, cap, false, u, -cost);
  } else {
    g[u].emplace_back(g[u].size() + 1, 0, true, u, cost);
    g[u].emplace_back(g[u].size() - 1, cap, false, u, -cost);
  }
}

template <class T>
class MinCostFlow {
 public:
  virtual pair<T, T> send(vector<vector<CostFlowEdge<T>>> &g, int src, int dst,
                          T inf) = 0;
};

template <class T>
struct DijkstraState {
  int node;
  T dist;

  DijkstraState(int n, T d) : node(n), dist(d) {}
};

template <class T>
bool operator<(const DijkstraState<T> &a, const DijkstraState<T> &b) {
  return a.dist < b.dist;
}

template <class T>
class SpfaMinCostFlow : public MinCostFlow<T> {
 private:
  deque<int> dq;
  vector<T> dist;
  vector<bool> inq;
  vector<CostFlowEdge<T> *> prev;

  void spfa(vector<vector<CostFlowEdge<T>>> &g, int s) {
    T inf = numeric_limits<T>::max() / 4;
    fill(dist.begin(), dist.end(), inf);
    fill(inq.begin(), inq.end(), false);
    fill(prev.begin(), prev.end(), (CostFlowEdge<T>*)0);
    dist[s] = 0;
    inq[s] = true;
    dq.push_back(s);
    while (!dq.empty()) {
      int head = dq.front();
      dq.pop_front();
      inq[head] = false;
      for (auto &e : g[head]) {
        if(g[e.to][e.rev].flow == 0){
          continue;
        }
        T d = dist[head] + e.cost;
        if (dist[e.to] > d) {
          dist[e.to] = d;
          prev[e.to] = &g[e.to][e.rev];
          if (!inq[e.to]) {
            dq.push_back(e.to);
            inq[e.to] = true;
          }
        }
      }
    }
  }

 public:
  pair<T, T> send(vector<vector<CostFlowEdge<T>>> &g, int s, int t, T inf) {
    int n = g.size();
    dist.resize(n);
    inq.resize(n);
    prev.resize(n);
    T flow = 0;
    T cost = 0;
    while (flow < inf) {
      spfa(g, s);
      if (!prev[t]) {
        break;
      }
      T remain = inf - flow;
      for (auto trace = prev[t]; trace; trace = prev[trace->to]) {
        remain = min(remain, trace->flow);
      }
      for (auto trace = prev[t]; trace; trace = prev[trace->to]) {
        cost += trace->cost * -remain;
        Send(g, *trace, -remain);
      }
      flow += remain;
    }
    return mp(flow, cost);
  }
};
}  // namespace costflow

#endif