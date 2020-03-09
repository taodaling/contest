#include "../../libs/common.h"
#include "../../libs/flow.h"
#include "../../libs/debug.h"

using namespace flow;

void solve(int testId, istream &in, ostream &out) {
  int n, m, s, t;
  in >> n >> m >> s >> t;
  vector<vector<FlowEdge<ll>>> g(n + 1);
  for(int i = 0; i < m; i++){
    int u, v, w;
    in >> u >> v >> w;
    dbg(u, v, w);
    AddEdge<ll>(g, u, v, w);
    dbg(g);
  }

  dbg(g);
  ll ans = ISAP<ll>().send(g, s, t, 1e18);
  dbg(g);
  out << ans;
}

RUN_ONCE