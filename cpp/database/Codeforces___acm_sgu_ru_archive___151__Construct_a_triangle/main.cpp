#include "../../libs/common.h"
#include "../../libs/geo2.h"

using pt = geo2::Point<double>;

void Triangle(double a, double b, double c, tuple<pt, pt, pt> &triangle) {
  double cosA = (b * b + c * c - a * a) / (2 * b * c);
  double A = acos(cosA);
  get<0>(triangle) = pt(0, 0);
  get<1>(triangle) = pt(b, 0);
  get<2>(triangle) = pt(c, 0);
  geo2::Rotate(get<2>(triangle), A);
}

bool check(double a, double b, double c) {
  vector<double> vec{a, b, c};
  sort(vec.begin(), vec.end());
  return geo2::Sign(vec[0] + vec[1] - vec[2]) > 0;
}

void solve(int testId, istream &in, ostream &out) {
  double a, b, m;
  in >> a >> b >> m;
  if (a > b) {
    swap(a, b);
  }
  if (m >= b + a / 2) {
    out << "Mission impossible";
    return;
  }
  tuple<pt, pt, pt> ans;
  if (check(a / 2, b, m)) {
    
  }
}

RUN_ONCE