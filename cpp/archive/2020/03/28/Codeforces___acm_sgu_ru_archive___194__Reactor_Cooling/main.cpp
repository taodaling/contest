#include "../../libs/common.h"
#include "../../libs/max_flow.h"
#include "../../libs/debug.h"

using namespace max_flow;
using Edge = FlowEdge<int>;

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;

  int src = n;
  int dst = n + 1;
  vector<vector<Edge>> g(dst + 1);
  vector<int> fix(m);
  vector<pair<int, int>> edges(m);
  for (int i = 0; i < m; i++) {
    int u, v, l, r;
    in >> u >> v >> l >> r;
    u--;
    v--;
    fix[i] = l;
    edges[i] = make_pair(u, g[u].size());
    AddEdge(g, u, v, r - l);
    AddEdge(g, u, dst, l);
    AddEdge(g, src, v, l);
  }

  ISAP<int> isap;
  isap.send(g, src, dst, (int)1e9);
  bool valid = true;
  for (auto &e : g[src]) {
    if (g[e.to][e.rev].flow > 0) {
      valid = false;
    }
  }
  for (auto &e : g[dst]) {
    if (e.flow > 0) {
      valid = false;
    }
  }

  dbg(g);

  if (!valid) {
    out << "NO";
    return;
  }
  out << "YES" << endl;
  for (int i = 0; i < m; i++) {
    auto &e = g[edges[i].first][edges[i].second];
    int flow = fix[i] + e.flow;
    out << flow << endl;
  }
}

RUN_ONCE