#include "../../libs/common.h"
#include "../../libs/debug.h"

#define PREC 1e-8
#define double long double

void solve(int testId, istream &in, ostream &out) {
  double a, b, m;
  in >> a >> b >> m;
  double x2 = (2 * m * m -(a * a + b * b) / 2) / a;
  double y2 = b * b - x2 * x2;

  dbg(x2, y2);
  if (y2 < -PREC) {
    out << "Mission impossible";
    return;
  }

  out << 0 << ' ' << 0 << endl;
  out << a << ' ' << 0 << endl;
  out << x2 << ' ' << sqrtl(max<double>(0, y2)) << endl;
}

RUN_ONCE