#ifndef HLPP_H
#define HLPP_H

#include <bits/stdc++.h>

namespace hlpp {

using namespace std;

// https://loj.ac/submission/777253
template <class T>
struct push_relabel {
  struct arc {
    int to, rev;
    T cap;
  };
  static constexpr T inf = numeric_limits<T>::max();
  const int n;
  vector<vector<arc>> g;
  vector<T> ex;
  vector<int> ptr, d;
  vector<vector<int>> active;
  vector<set<int>> vs;
  int highest, works;
  push_relabel(int _n) : n(_n), g(n), ex(n), ptr(n), d(n), active(n), vs(n) {}
  void add(int from, int to, T cap, bool directed = true) {
    assert(cap >= 0);
    if (from == to or cap == 0) return;
    g[from].push_back({to, (int)g[to].size(), cap});
    g[to].push_back({from, (int)g[from].size() - 1, directed ? 0 : cap});
  }
  void push(int v, arc& a) {
    if (ex[a.to] == 0) active[d[a.to]].push_back(a.to);
    T delta = min(ex[v], a.cap);
    ex[v] -= delta, ex[a.to] += delta;
    a.cap -= delta, g[a.to][a.rev].cap += delta;
  }
  void relabel(int v) {
    ++works;
    int mn = 2 * n;
    for (auto&& a : g[v])
      if (a.cap) mn = min(mn, d[a.to]);
    if (vs[d[v]].size() == 1) {
      for (int i = d[v]; i < n; ++i) {
        for (int u : vs[i]) d[u] = n;
        active[i].clear(), vs[i].clear();
      }
      return;
    }
    vs[d[v]].erase(v);
    if ((d[v] = mn + 1) < n) vs[highest = d[v]].insert(v);
  }
  void discharge(int v) {
    auto it = begin(g[v]) + ptr[v];
    while (ex[v]) {
      if (it == end(g[v])) {
        relabel(v), it = begin(g[v]);
        if (d[v] >= n) break;
      } else {
        if (it->cap and d[v] > d[it->to])
          push(v, *it);
        else
          ++it;
      }
    }
    ptr[v] = it - begin(g[v]);
  }
  void global_relabel(int t) {
    fill(begin(ptr), end(ptr), 0), fill(begin(d), end(d), n);
    for (int i = 0; i < n; ++i) active[i].clear(), vs[i].clear();
    highest = -1, works = 0;
    queue<int> que;
    d[t] = 0, que.push(t);
    while (not que.empty()) {
      int v = que.front();
      que.pop();
      vs[d[v]].insert(v);
      for (auto&& a : g[v])
        if (g[a.to][a.rev].cap and d[a.to] == n) {
          d[a.to] = d[v] + 1, que.push(a.to);
          if (ex[a.to]) active[highest = d[a.to]].push_back(a.to);
        }
    }
  }
  T max_flow(int s, int t) {
    ex[s] = inf, ex[t] = -inf;
    for (auto&& a : g[s]) push(s, a);
    global_relabel(t);
    for (; highest >= 0; --highest)
      while (not active[highest].empty()) {
        int v = active[highest].back();
        active[highest].pop_back();
        discharge(v);
        if (works > 4 * n) global_relabel(t);
      }
    return inf + ex[t];
  }
};

}  // namespace hlpp

#endif