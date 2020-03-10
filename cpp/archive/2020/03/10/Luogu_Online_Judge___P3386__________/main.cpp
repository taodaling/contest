#include "../../libs/common.h"
#include "../../libs/match.h"

void solve(int testId, istream &in, ostream &out) {
  int n, m, e;
  in >> n >> m >> e;
  match::BipartiteMatch match(n + 1, m + 1);
  for (int i = 0; i < e; i++) {
    int a, b;
    in >> a >> b;
    if (a > n || b > m || a < 0 || b < 0) {
      continue;
    }
    match.addEdge(a, b, true);
  }

  int ans = 0;
  for (int i = 0; i <= n; i++) {
    ans += match.matchLeft(i) != -1 ? 1 : 0;
  }

  out << ans << endl;
}

RUN_ONCE