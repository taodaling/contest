#include "../../libs/common.h"
#include "../../libs/geo2.h"

using pt = geo2::Point<double>;

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<pt> ch(n);
  for (int i = 0; i < n; i++) {
    in >> ch[i];
  }
  if (n == 1) {
    out << 0;
    return;
  }

  geo2::ConvexHull(ch);
  n = ch.size();
  double ans = 0;
  for (int i = 0; i < n; i++) {
    ans += (ch[(i + 1) % n] - ch[i]).abs();
  }

  out << std::setprecision(2) << ans;
}

RUN_ONCE