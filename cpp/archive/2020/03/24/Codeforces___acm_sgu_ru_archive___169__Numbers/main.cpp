#include "../../libs/common.h"
#include "../../libs/modular.h"
#include "../../libs/debug.h"
using modular::Mod;

void solve(int testId, istream &in, ostream &out) {
  int k;
  in >> k;
  vector<bool> ok(10);
  for (int r = 1; r <= 9; r++) {
    int val = 0;
    for (int i = 1; i < k; i++) {
      val = Mod(val * 10 + 1, r);
    }
    val = Mod(val * 10 + r, r);
    ok[r] = val == 0;
  }
  int ans = 0;
  for (int i = 1; i <= 8; i++) {
    if (ok[i] && ok[i + 1]) {
      ans++;
    }
  }
  dbg(ok);
  out << ans;
}

RUN_ONCE