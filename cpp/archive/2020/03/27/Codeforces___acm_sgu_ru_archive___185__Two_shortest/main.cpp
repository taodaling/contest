#include "../../libs/common.h"
#include "../../libs/cost_flow.h"
#include "../../libs/debug.h"

using namespace cost_flow;
using Edge = CostFlowEdge<int>;
vector<vector<Edge>> g;

void output(ostream &out, int root, int target) {
  out << root + 1 << ' ';
  if (root == target) {
    return;
  }
  for (auto &e : g[root]) {
    if (e.real && e.flow > 0) {
      e.flow = 0;
      output(out, e.to, target);
      return;
    }
  }
}

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;
  g.resize(n);
  for (int i = 0; i < m; i++) {
    int u, v, l;
    in >> u >> v >> l;
    u--;
    v--;
    AddEdge(g, u, v, 1, l);
    AddEdge(g, v, u, 1, l);
  }

  SpfaMinCostFlow<int> mcf;
  pair<int, int> f1 = mcf.send(g, 0, n - 1, 1);
  pair<int, int> f2 = mcf.send(g, 0, n - 1, 1);

  dbg(f1, f2);
  if (f1.first + f2.first < 2 || f1.second != f2.second) {
    out << "No solution";
    return;
  }

  output(out, 0, n - 1);
  out << endl;
  output(out, 0, n - 1);
}

RUN_ONCE