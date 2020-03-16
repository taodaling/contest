#include "../../libs/common.h"
#include "../../libs/gcd.h"

void solve(int testId, istream &in, ostream &out) {
  int n, k;
  in >> n >> k;
  int base = k / n;
  k %= n;

  int a;
  int b;
  gcd::Extgcd(k, n, a, b);  // modular::Inverse(k, n);
  int step = (a % n + n) % n;

  vector<int> ans(n, base);
  for (int i = 0, j = 0; i < k; i++, j = (j + step) % n) {
    ans[j]++;
  }

  rotate(ans.begin(), ans.begin() + 1, ans.end());
  for (int x : ans) {
    out << x << ' ';
  }
}

RUN_ONCE