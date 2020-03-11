#include "../../libs/common.h"
#include "../../libs/match.h"

#define MAX_N 1000
using GM = match::GeneralMatch<MAX_N>;

GM gm;

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;
  for (int i = 0; i < m; i++) {
    int u, v;
    in >> u >> v;
    u--;
    v--;
    gm.addEdge(u, v);
  }

  int ans = gm.maxMatch();
  out << ans << endl;
  for (int i = 0; i < n; i++) {
    out << gm.mateOf(i) + 1 << ' ';
  }
}

RUN_ONCE