#ifndef MAX_FLOW_H
#define MAX_FLOW_H

#include "common.h"

namespace max_flow {

template <class T>
struct FlowEdge {
  int rev;
  T flow;
  bool real;
  int to;

  FlowEdge(int rv, T f, bool r, int t) : rev(rv), flow(f), real(r), to(t) {}
};

template <class T>
ostream &operator<<(ostream &os, FlowEdge<T> &e) {
  os << "{ flow = " << e.flow << ", real = " << e.real << ", to = " << e.to
     << "}";
  return os;
}

template <class T>
ostream &operator<<(ostream &os, vector<vector<FlowEdge<T>>> &g) {
  for (int i = 0; i < g.size(); i++) {
    for (FlowEdge<T> &e : g[i]) {
      if (e.real) {
        os << i << "-" << e.flow << "(" << g[e.to][e.rev].flow << ")"
           << "->" << e.to << endl;
      }
    }
  }
  return os;
}

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
inline void Send(vector<vector<FlowEdge<T>>> &g, FlowEdge<T> &e, T flow) {
  e.flow += flow;
  g[e.to][e.rev].flow -= flow;
}

template <class T>
void AddEdge(vector<vector<FlowEdge<T>>> &g, int u, int v, T cap) {
  if (u != v) {
    g[u].emplace_back(g[v].size(), 0, true, v);
    g[v].emplace_back(g[u].size() - 1, cap, false, u);
  } else {
    g[u].emplace_back(g[u].size() + 1, 0, true, u);
    g[u].emplace_back(g[u].size() - 1, cap, false, u);
  }
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
      if (_dist[e.to] != _dist[root] - 1 || g[e.to][e.rev].flow == 0) {
        continue;
      }
      T sent = dfs(g, e.to, min(flow, g[e.to][e.rev].flow));
      flow -= sent;
      Send(g, e, sent);
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