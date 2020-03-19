#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/modular.h"
#include "../../libs/zalgorithm.h"

using Mint = modular::Modular<int, (int)1e9 + 7>;

vector<int> border;
vector<int> z;

void solve(int testId, istream &in, ostream &out) {
  string s;
  in >> s;
  int n = s.length();
  dbg(s);
  z.resize(n);
  border.resize(n + 1);
  fill(border.begin(), border.end(), 0);
  zalgorithm::Z<char>([&](int i) { return s[i]; }, z);

  for (int i = 1; i < n; i++) {
    int len = min(i, z[i]);
    border[i]++;
    border[i + len]--;
  }

  int sum = 0;
  for (int i = 0; i < n; i++) {
    sum += border[i];
    border[i] = sum;
  }

  dbg(border);

  Mint ans = 1;
  for (int i = 0; i < n; i++) {
    ans *= (1 + border[i]);
  }

  out << ans << endl;
}

RUN_MULTI