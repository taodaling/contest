
#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/geo2.h"

using pt = geo2::Point<long double>;

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<pt> border(n);
  for (int i = 0; i < n; i++) {
    in >> border[i];
  }
  geo2::ConvexHull(border);
  dbg(border);
  n = border.size();
  int m;
  in >> m;
  for (int i = 0; i < m; i++) {
    pt a, b;
    in >> a >> b;
    double factor = 1;
    for (int i = 0; i < n; i++) {
      pt &s1 = border[i];
      pt &s2 = border[(i + 1) % n];
      if (geo2::Orient(s1, s2, a) <= 0 && geo2::Orient(s1, s2, b) <= 0) {
        factor = 0;
      } else if (geo2::Orient(s1, s2, a) >= 0 && geo2::Orient(s1, s2, b) >= 0) {
        continue;
      } else {
        pt inter;
        if (!geo2::Intersect(a, b, s1, s2, inter)) {
          continue;
        }
        if (geo2::Orient(s1, s2, a) < 0) {
          a = inter;
        }
        if (geo2::Orient(s1, s2, b) < 0) {
          b = inter;
        }
      }
    }
    double ans = factor * (b - a).abs();
    out << ans << endl;
  }
}

RUN_ONCE