#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  long double l;
  int n;
  in >> l >> n;
  ll factor = 10000;
  ll m = (l * factor);

  ll x = 0;
  for (int i = 0; i < n; i++) {
    ll t, v;
    in >> t >> v;
    x = (x + t * v * factor) % m;
  }

  ll len = min(x, m - x);
  out << (long double)len / factor;
}

RUN_ONCE