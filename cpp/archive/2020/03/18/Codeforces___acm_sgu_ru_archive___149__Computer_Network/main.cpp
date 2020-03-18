#include "../../libs/common.h"
#include "../../libs/debug.h"


vector<pair<int, int>> dists;
vector<int> farthest;
vector<vector<pair<int, int>>> g;

void Push(pair<int, int> &p, int x) {
  if (x > p.second) {
    p.second = x;
  }
  if (p.first < p.second) {
    swap(p.first, p.second);
  }
}

void Dfs2(int root, int p, int dist) {
  farthest[root] = max(dists[root].first, dist);
  for (auto &e : g[root]) {
    if (e.first == p) {
      continue;
    }
    int d = dist;
    if (dists[e.first].first + e.second == dists[root].first) {
      d = max(d, dists[root].second);
    } else {
      d = max(d, dists[root].first);
    }
    Dfs2(e.first, root, d + e.second);
  }
}

void Dfs1(int root, int p) {
  Push(dists[root], 0);
  for (auto &e : g[root]) {
    if (e.first == p) {
      continue;
    }
    Dfs1(e.first, root);
    Push(dists[root], dists[e.first].first + e.second);
  }
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  dists.resize(n);
  g.resize(n);
  farthest.resize(n);
  for (int i = 1; i < n; i++) {
    int p, w;
    in >> p >> w;
    p--;
    g[p].emplace_back(i, w);
    g[i].emplace_back(p, w);
  }

  Dfs1(0, -1);
  Dfs2(0, -1, 0);

  dbg(dists, farthest);
  for (int x : farthest) {
    out << x << endl;
  }
}

RUN_ONCE