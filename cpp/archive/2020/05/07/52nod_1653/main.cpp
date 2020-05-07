#include "../../libs/common.h"
#include "../../libs/debug.h"

typedef long double real;

void solve(int testId, istream &in, ostream &out) {
  int S, W, A;
  real p1, p2;
  in >> S >> W >> A >> p1 >> p2;

  int n = S + W;
  int thresold = S - A;  // c2 => c1
  vector<real> a(n + 1);
  vector<real> b(n + 1);

  a[1] = 1;
  b[1] = 0;
  for (int i = 1; i < n; i++) {
    real p = i <= thresold ? p2 : p1;
    real l = 1 - p;
    real r = p;
    // p(i) = l * p(i - 1) + r * p(i + 1)
    // p(i + 1) = (l * p(i - 1) - p(i)) / r
    a[i + 1] = (a[i] - l * a[i - 1]) / r;
    b[i + 1] = (b[i] - l * b[i - 1]) / r;
  }

  // a[n] * x + b[n] = 1
  double x = (1 - b[n]) / a[n];
  double ans = a[S] * x + b[S];

  dbg(x);
  out << ans << endl;
}

RUN_MULTI