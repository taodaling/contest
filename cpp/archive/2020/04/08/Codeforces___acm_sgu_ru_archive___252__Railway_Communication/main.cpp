#include "../../libs/common.h"
#include "../../libs/km_algo.h"
#include "../../libs/debug.h"

void solve(int testId, istream &in, ostream &out) {
  ll inf = 1e9;

  int n, m;
  in >> n >> m;
  vector<vector<ll>> g(n, vector<ll>(n, 0));
  for (int i = 0; i < m; i++) {
    int u, v, c;
    in >> u >> v >> c;
    u--;
    v--;
    g[u][v] = max<ll>(g[u][v], inf - c);
  }
  dbg(g);

  km_algo::KMAlgo<ll> km(g);
  ll cost = km.solve();
  dbg(km);
  int match = 0;
  for (int i = 0; i < n; i++) {
    int j = km.getRightPartner(i);
    if (g[j][i]) {
      match++;
    }
  }
  cost -= inf * match;

  out << n - match << ' ' << -cost << endl;
  for (int i = 0; i < n; i++) {
    int j = km.getRightPartner(i);
    if (g[j][i]) {
      continue;
    }
    vector<int> trace;
    int vertex = i;
    while (true) {
      trace.push_back(vertex);
      int r = km.getLeftPartner(vertex);
      if (g[vertex][r] == 0) {
        break;
      }
      vertex = r;
    }

    out << trace.size() << ' ';
    for (int x : trace) {
      out << x + 1 << ' ';
    }
    out << endl;
  }
}

RUN_ONCE