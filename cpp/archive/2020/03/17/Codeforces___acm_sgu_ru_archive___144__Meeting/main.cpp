#include "../../libs/common.h"

//#define double long double

void solve(int testId, istream &in, ostream &out) {
  double x, y, z;
  in >> x >> y >> z;
  double d = (double)z / 60 / (y - x);

  //out << std::setprecision(7);
  if (d >= 1) {
    out << 1.0 << endl;
    return;
  }
  double area1 = d + (1 + d) * (1 - d) / 2;
  double area2 = (1 - d) * (1 - d) / 2;
  double ans = area1 - area2;
  out << ans;
}

RUN_ONCE