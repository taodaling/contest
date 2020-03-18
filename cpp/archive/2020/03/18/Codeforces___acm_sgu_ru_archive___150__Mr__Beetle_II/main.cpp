#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/geo2.h"

#define doouble long double
#define EPS 1e-8
using pt = geo2::Point<double>;


double Round(double x) {
  if (x < 0) {
    return -Round(-x);
  }
  if ((ll)(x + EPS) != (ll)(x)) {
    return ceil(x);
  }
  if ((ll)(x - EPS) != (ll)x) {
    return floor(x);
  }
  return x;
}

void solve(int testId, istream &in, ostream &out) {
  int x1, y1, x2, y2;
  int n;
  in >> x1 >> y1 >> x2 >> y2 >> n;
  if (x1 == x2 || y1 == y2) {
    out << "no solution";
    return;
  }
  pt src(x1, y1);
  pt dst(x2, y2);

  int step = x1 < x2 ? 1 : -1;
  for (int i = x1 + step; i != x2 + step; i += step) {
    pt l = geo2::Scale(src, (double)(i - x1 - step) / (double)(x2 - x1), dst);
    pt r = geo2::Scale(src, (double)(i - x1) / (double)(x2 - x1), dst);
    l.y = Round(l.y);
    r.y = Round(r.y);

    pair<int, int> p(floor(min(l.y, r.y)), ceil(max(l.y, r.y)));
    int cnt = p.second - p.first;

    if (cnt > 1) {
      dbg(l, r, i, p);
    }
    dbg(cnt);
    if (n > cnt) {
      n -= cnt;
    } else {
      if (y1 < y2) {
        out << min(i - step, i) << ' ' << p.first + n - 1 << endl;
      } else {
        out << min(i - step, i) << ' ' << p.second - n << endl;
      }
      return;
    }
  }

  out << "no solution";
}

RUN_ONCE