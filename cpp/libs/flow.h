#ifndef FLOW_H
#define FLOW_H

#include "common.h"

namespace flow {

template <class T>
struct FlowEdge {
  FlowEdge *rev;
  T flow;
  bool real;
  int to;
};

template <class T>
struct CostFlowEdge : public FlowEdge<T> {
  T cost;
};

template <class T>
void Reset(vector<vector<FlowEdge<T>>> &g) {
  for (vector<FlowEdge<T>> &v : g) {
    for (FlowEdge<T> &t : v) {
      if (t.real) {
        t.rev->flow += t.flow;
        t.flow = 0;
      }
    }
  }
}

template <class T>
void AddEdge(vector<vector<FlowEdge<T>>> &g, int u, int v, T cap) {
  g[u].push_back(FlowEdge<int>{0, 0, true, v});
  g[v].push_back(FlowEdge<int>{&g[u].back(), cap, false, u});
  g[u].back().rev = &g[v].back();
}

template<class T>
void Send(FlowEdge<T> &e, T flow){
  e.flow += flow;
  e.rev->flow -= flow;
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
void AddEdge(vector<vector<CostFlowEdge<T>>> &g, int u, int v, T cap, T cost) {
  g[u].emplace_back(0, 0, true, v, cost);
  g[v].emplace_back(&g[u].back(), cap, false, u, -cost);
  g[u].back().rev = &g[v].back();
}

template <class T>
class MaxFlow {
 public:
  virtual T send(vector<vector<FlowEdge<T>>> &g, int src, int dst, T inf) = 0;
};

template <class T>
class ISAP : public MaxFlow<T> {
 private:
  vector<int> _dist;
  vector<int> _distCnt;
  int _src;
  int _dst;

  void bfs(vector<vector<FlowEdge<T>>> &g, int inf) {
    deque<int> dq;
    fill(_dist.begin(), _dist.end(), inf);
    _dist[_dst] = 0;
    dq.push_back(_dst);

    while (!dq.empty()) {
      int head = dq.front();
      dq.pop_front();
      for (FlowEdge<T> &e : g[head]) {
        if (e.flow == 0 || _dist[e.to] <= _dist[head] + 1) {
          continue;
        }
        _dist[e.to] = _dist[head] + 1;
        dq.push_back(e.to);
      }
    }
  }

  T dfs(vector<vector<FlowEdge<T>>> &g, int root, T flow) {
    if (root == _dst) {
      return flow;
    }
    T snapshot = flow;
    for (FlowEdge<T> &e : g[root]) {
      if (_dist[e.to] != _dist[root] - 1 || e.rev->flow == 0) {
        continue;
      }
      T sent = dfs(g, e.to, min(flow, e.rev->flow));
      flow -= sent;
      Send(e, sent);
      if (flow == 0) {
        break;
      }
    }
    if (flow > 0) {
      _distCnt[_dist[root]]--;
      _dist[root]++;
      _distCnt[_dist[root]]++;
      if (_distCnt[_dist[root] - 1] == 0) {
        _distCnt[_dist[_src]]--;
        _dist[_src] = g.size();
        _distCnt[_dist[_src]]++;
      }
    }

    return snapshot - flow;
  }

 public:
  T send(vector<vector<FlowEdge<T>>> &g, int src, int dst, T inf) {
    int n = g.size();
    _dist.resize(n);
    _distCnt.resize(n + 2);
    fill(_distCnt.begin(), _distCnt.end(), 0);
    _src = src;
    _dst = dst;
    bfs(g, n);
    for (int i = 0; i < n; i++) {
      _distCnt[_dist[i]]++;
    }

    T total = 0;
    while (total < inf && _dist[_src] < n) {
      total += dfs(g, _src, inf - total);
    }

    return total;
  }
};

}  // namespace flow

#endif