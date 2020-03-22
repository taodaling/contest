#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  double ab, ac, ad, bc, bd, cd;
  in >> ab >> ac >> ad >> bc >> bd >> cd;
  double U = ab, V = bc, W = ac, u = cd, v = ad, w = bd;

  double X = (w - U + v) * (U + v + w);
  double x = (U - v + w) * (v - w + U);
  double Y = (u - V + w) * (V + w + u);
  double y = (V - w + u) * (w - u + V);
  double Z = (v - W + u) * (W + u + v);
  double z = (W - u + v) * (u - v + W);
  double a = sqrt(x * Y * Z);
  double b = sqrt(y * Z * X);
  double c = sqrt(z * X * Y);
  double d = sqrt(x * y * z);

  double ans = sqrt((-a + b + c + d) * (a - b + c + d) * (a + b - c + d) *
                  (a + b + c - d)) /
             (192 * u * v * w);

  out << ans;
}

RUN_ONCE