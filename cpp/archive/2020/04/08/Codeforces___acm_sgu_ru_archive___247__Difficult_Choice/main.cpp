#include "../../libs/bigint.h"
#include "../../libs/common.h"
#include "../../libs/modular.h"

using bi = bigint::BigInt;
using modular::Mod;

map<int, bi> cache;

bi Comp(int n, int m) {
  if (m == 0) {
    return 1;
  }
  return Comp(n - 1, m - 1) * n / m;
}

void solve(int testId, istream &in, ostream &out) {
  int p;
  in >> p;
  if (cache.find(p) != cache.end()) {
    out << cache[p] << endl;
    return;
  }
  bi c = Comp(2 * p, p);
  bi ans = (c - 2) / p + 2;
  out << ans << endl;
  cache[p] = ans;
}

RUN_MULTI