#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/modular.h"

using modular::Mod;

int solve(int x0, int a, int b, int c, int m, int k) {
  if (k == 0) {
    return x0;
  }

  x0 = Mod(x0, m);
  a = Mod(a, m);
  b = Mod(b, m);
  c = Mod(c, m);

  vector<int> occur(m, -1);
  vector<int> vals;
  vals.reserve(m);

  int x = x0;

  int first;
  int second;
  for (int i = 0;; i++) {
    if (occur[x] >= 0) {
      first = occur[x];
      second = i;
      break;
    }
    vals.push_back(x);
    occur[x] = i;
    x = Mod<ll>((ll)x * x * a + b * x + c, m);
  }

  // dbg(vals);
  // dbg(first, second, vals[first]);

  if (k < first) {
    return vals[k];
  }

  int index = Mod(k - first, second - first) + first;
  return vals[index];
}

int bf(int x0, int a, int b, int c, int m, int k) {
  ll x = x0 % m;
  for (int i = 0; i < k; i++) {
    if (i <= 60) {
      // dbg(i, x);
    }

    x = (a * x * x + b * x + c) % m;
  }
  return x;
}

void stress() {
  while (true) {
    int A = uniform_int_distribution<int>(1, 10000)(rng);
    int a = uniform_int_distribution<int>(1, 100)(rng);
    int b = uniform_int_distribution<int>(1, 100)(rng);
    int c = uniform_int_distribution<int>(1, 100)(rng);
    int m = uniform_int_distribution<int>(1, 1000)(rng);
    int k = uniform_int_distribution<int>(1, 10000)(rng);

    if (solve(A, a, b, c, m, k) != bf(A, a, b, c, m, k)) {
      dbg(A, a, b, c, m, k);
      exit(1);
    }
  }
}

void solve(int testId, istream &in, ostream &out) {
  int x0, a, b, c, m, k;
  in >> x0 >> a >> b >> c >> m >> k;

  // stress();
  // dbg2(bf(x0, a, b, c, m, k));
  out << solve(x0, a, b, c, m, k);
}

RUN_ONCE