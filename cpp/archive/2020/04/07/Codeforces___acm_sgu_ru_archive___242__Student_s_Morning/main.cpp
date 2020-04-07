#include "../../libs/common.h"
#include "../../libs/match.h"

void solve(int testId, istream &in, ostream &out) {
  int n, k;
  in >> n >> k;
  match::BipartiteMatch bm(n, 2 * k);
  for (int i = 0; i < n; i++) {
    int m;
    in >> m;
    for (int j = 0; j < m; j++) {
      int x;
      in >> x;
      x--;
      bm.addEdge(i, x, true);
      bm.addEdge(i, x + k, true);
    }
  }

  int maxMatch = 0;
  for (int i = 0; i < n; i++) {
    maxMatch += bm.matchLeft(i) != -1;
  }

  if (maxMatch < k * 2) {
    out << "NO";
    return;
  }

  out << "YES" << endl;
  for (int i = 0; i < k; i++) {
    out << 2 << ' ' << bm.matchRight(i) + 1 << ' ' << bm.matchRight(i + k) + 1
        << endl;
  }


}

RUN_ONCE