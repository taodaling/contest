#include "../../libs/common.h"
#include "../../libs/flow.h"

using namespace flow;

void solve(int testId, istream &in, ostream &out) {
  int n, m, s, t;
  in >> n >> m >> s >> t;
  vector<vector<FlowEdge<int>>> g(n + 1);
  for(int i = 0; i < m; i++){
    int u, v, w;
    in >> u >> v >> w;
    AddEdge(g, u, v, w);
  }
  int ans = ISAP<int>().send(g, s, t, 2e9);
  out << ans;
}

RUN_ONCE