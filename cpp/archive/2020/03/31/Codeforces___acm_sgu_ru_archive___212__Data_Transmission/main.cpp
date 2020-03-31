#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/hlpp.h"

void solve(int testId, istream &in, ostream &out) {
  int n, m, l;
  in >> n >> m >> l;
  vector<int> levels(n);
  hlpp::push_relabel<int> pr(n);
  int src = -1;
  int sink = -1;
  for (int i = 0; i < n; i++) {
    in >> levels[i];
    if (levels[i] == 1) {
      src = i;
    }
    if (levels[i] == l) {
      sink = i;
    }
  }

  vector<pair<int, int>> edges(m);
  for (int i = 0; i < m; i++) {
    int a, b, c;
    in >> a >> b >> c;
    a--;
    b--;
    edges[i].first = a;
    edges[i].second = pr.g[a].size();
    pr.add(a, b, c);
  }

  int ans = pr.max_flow(src, sink);
  dbg(ans);
  for (auto &e : edges) {
    auto &edge = pr.g[e.first][e.second];
    int flow = pr.g[edge.to][edge.rev].cap;
    out << flow << endl;
  }
}

RUN_ONCE