#include "../../libs/common.h"
#include "../../libs/cost_flow.h"
#include "../../libs/debug.h"

using namespace costflow;

void solve(int testId, istream &in, ostream &out) {
  int n, m, s, t;
  in >> n >> m >> s >> t;
  vector<vector<CostFlowEdge<ll>>> g(n + 1);
  for (int i = 0; i < m; i++) {
    int u, v;
    ll w, f;
    in >> u >> v >> w >> f;
    AddEdge(g, u, v, w, f);
  }

  //dbg(g);
  pair<ll, ll> ans = SpfaMinCostFlow<ll>().send(g, s, t, 1e18);
  //dbg(g);
  out << ans.first << ' ' << ans.second << endl;
}

RUN_ONCE