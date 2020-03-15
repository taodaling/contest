#include "../../libs/common.h"

#define double long double

bool solve(vector<ll> &vec, double &ans) {
  ll a = 1;
  ll b = 0;
  for (ll v : vec) {
    a = -a;
    b = 2 * v - b;
  }
  if (a - 1 == 0) {
    if (b != 0) {
      return false;
    }
    ans = 0;
    return true;
  }

  ans = (double)-b / (a - 1);
  return true;
}

ll read(istream &in) {
  double x;
  in >> x;
  return (ll)(x * 1000);
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<ll> xs(n);
  vector<ll> ys(n);
  for (int i = 0; i < n; i++) {
    xs[i] = read(in);
    ys[i] = read(in);
  }
  double x0, y0;
  bool valid = solve(xs, x0) && solve(ys, y0);
  if (!valid) {
    out << "NO";
    return;
  }

  out << "YES" << endl;
  out << x0 / 1000 << ' ' << y0 / 1000 << endl;
  for (int i = 0; i < n - 1; i++) {
    x0 = 2 * xs[i] - x0;
    y0 = 2 * ys[i] - y0;
    out << x0 / 1000 << ' ' << y0 / 1000 << endl;
  }
}

RUN_ONCE