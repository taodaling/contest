#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  int k, n;
  in >> k >> n;
  vector<int> cnts(10);
  for (int i = 0; i < n; i++) {
    string s;
    in >> s;
    cnts[s[0] - '0']++;
  }
  int ans = 2;
  for (int i = 0; i < 10; i++) {
    ans += (cnts[i] + k - 1) / k;
  }
  out << ans;
}

RUN_ONCE