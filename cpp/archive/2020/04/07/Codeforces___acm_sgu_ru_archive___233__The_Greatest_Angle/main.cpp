#include "../../libs/binary_search.h"
#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/geo2.h"

#define double long double

using pt = geo2::Point<double>;

double Pow2(double x) { return x * x; }

double AngleC(double a, double b, double c) {
  double cosC = (a * a + b * b - c * c) / (2 * a * b);
  double C = acos(cosC);
  return C;
}

double AngleC(const pt &A, const pt &B, const pt &C) {
  return AngleC((B - C).abs(), (A - C).abs(), (A - B).abs());
}

void solve(int testId, istream &in, ostream &out) {
  out << std::setiosflags(std::ios::fixed);
  out << std::setprecision(6);

  pt center, a, b;
  double radius;
  in >> center >> radius >> a >> b;
  pt right = center + pt(radius, 0);

  pt castA(0, 0);
  pt castB(1, 0);
  pt castCenter = geo2::LinearTransformTo(a, castA, b, castB, center);
  pt castRight = geo2::LinearTransformTo(a, castA, b, castB, right);
  double castRadius = (castRight - castCenter).abs();

  bool rev = castCenter.y > 0;
  if (rev) {
    castCenter.y = -castCenter.y;
  }

  dbg(a, b, castCenter);

  double d = sqrt(castRadius * castRadius - castCenter.y * castCenter.y);
  double xl = castCenter.x - d;
  double xr = castCenter.x + d;

  dbg(xl, xr, castRadius);

  function<double(double)> func = [&](double x) {
    double y =
        castCenter.y + sqrt(castRadius * castRadius - Pow2(x - castCenter.x));
    double C = AngleC(castA, castB, pt(x, y));
    return C;
  };

  double x = binary_search::TernarySearch<double>(xl, xr, func, 1e-15, 1e-15);
  double y =
      castCenter.y + sqrt(castRadius * castRadius - Pow2(x - castCenter.x));

  pt ans(x, y);
  if (rev) {
    ans.y = -ans.y;
  }

  dbg2(ans);
  pt originAns = geo2::LinearTransformTo(castA, a, castB, b, ans);

  dbg2(AngleC(a, b, originAns));
  dbg2((originAns - center).abs());

  out << originAns.x << ' ' << originAns.y << endl;
}

RUN_MULTI