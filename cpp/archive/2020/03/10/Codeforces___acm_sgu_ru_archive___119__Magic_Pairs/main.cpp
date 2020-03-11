#include "../../libs/common.h"
#include "../../libs/modular.h"
using modular::Mod;

int Merge(int a, int b) { return a * 10000 + b; }

void solve(int testId, istream &in, ostream &out) {
  int n, a0, b0;
  in >> n >> a0 >> b0;

  vector<int> ans;
  for (int i = 0; i < n; i++) {
    ans.push_back(Merge(Mod(i * a0, n), Mod(i * b0, n)));
  }

  sort(ans.begin(), ans.end());
  ans.erase(unique(ans.begin(), ans.end()), ans.end());

  out << ans.size() << endl;
  for (int x : ans) {
    out << x / 10000 << ' ' << x % 10000 << endl;
  }
}

RUN_ONCE