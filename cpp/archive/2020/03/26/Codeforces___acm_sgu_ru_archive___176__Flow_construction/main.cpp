#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/flow.h"

using namespace max_flow;
using Edge = FlowEdge<ll>;

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;
  int src = n;
  int dst = n + 1;
  int a = 0;
  int b = n - 1;
  vector<vector<Edge>> g(n + 2);
  vector<pair<int, int>> edges(m);
  vector<int> fix(m);
  for (int i = 0; i < m; i++) {
    int u, v, z, c;
    in >> u >> v >> z >> c;
    u--;
    v--;
    int l = c ? z : 0;
    edges[i].first = u;
    edges[i].second = g[u].size();
    fix[i] = l;
    AddEdge<ll>(g, u, v, z - l);
    AddEdge<ll>(g, u, dst, l);
    AddEdge<ll>(g, src, v, l);
  }

  AddEdge<ll>(g, b, a, (ll)1e18);
  bool valid = true;
  ISAP<ll> isap;
  isap.send(g, src, dst, (ll)1e18);

  dbg(g);
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

  if (!valid) {
    out << "Impossible";
    return;
  }

  ll f = g[b].back().flow;
  g[b].pop_back();
  g[a].pop_back();

  f -= isap.send(g, b, a, (ll)1e18);

  if (f < 0) {
    f += isap.send(g, a, b, -f);
  }

  dbg(g);

  out << f << endl;
  for (int i = 0; i < m; i++) {
    auto &e = g[edges[i].first][edges[i].second];
    ll flow = e.flow + fix[i];
    out << flow << ' ';
  }
}

RUN_ONCE