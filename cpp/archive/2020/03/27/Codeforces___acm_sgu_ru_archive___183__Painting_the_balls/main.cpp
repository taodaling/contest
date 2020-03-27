#include "../../libs/bits.h"
#include "../../libs/common.h"
#include "../../libs/debug.h"

int bf(int n, int m, vector<int> a) {
  int mask = (1 << m) - 1;
  int ans = 1e9;
  for (int i = 0; i < (1 << n); i++) {
    bool valid = true;
    for (int j = 0; j + m - 1 < n; j++) {
      if (bits::CountOne((unsigned int)(mask & (i >> j))) < 2) {
        valid = false;
        break;
      }
    }
    if (!valid) {
      continue;
    }
    int local = 0;
    for (int j = 0; j < n; j++) {
      if (bits::BitAt(i, j)) {
        local += a[j + 1];
      }
    }

    if (local == 85) {
      dbg(bitset<5>(i));
    }
    ans = min(ans, local);
  }
  return ans;
}

int solve(int n, int m, vector<int> a) {
  int inf = 1e9;
  vector<vector<int>> dp(n + 1, vector<int>(m - 1, inf));
  fill(dp[0].begin(), dp[0].end(), 0);

  for (int i = 1; i <= n; i++) {
    for (int j = 0; j < m - 1; j++) {
      int t = i - j - 1;
      int l = m - 2 - j;
      if (t < 0) {
        continue;
      }
      dp[i][j] = min(dp[i][j], dp[t][l] + a[i]);
    }
    for (int j = 1; j < m - 1; j++) {
      dp[i][j] = min(dp[i][j], dp[i][j - 1]);
    }
  }

  int ans = inf;
  for (int i = 1; i <= n; i++) {
    int r = n - i;
    for (int j = 0; j < m - 1; j++) {
      if (j + r <= m - 2) {
        ans = min(ans, dp[i][j]);
      }
    }
  }

dbg(dp);
  return ans;
}

void stress() {
  while (true) {
    int n = uniform_int_distribution<int>(2, 5)(rng);
    int m = uniform_int_distribution<int>(2, n)(rng);
    vector<int> a(n + 1);
    for (int i = 1; i <= n; i++) {
      a[i] = uniform_int_distribution<int>(1, 100)(rng);
    }
    int a1 = bf(n, m, a);
    int a2 = solve(n, m, a);
    if (a1 != a2) {
      dbg(n, m, a, a1, a2);
      exit(1);
    }
  }
}

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;

  vector<int> a(n + 1);
  for (int i = 1; i <= n; i++) {
    in >> a[i];
  }

 // stress();
  dbg2(bf(n, m, a));
  out << solve(n, m, a);
}

RUN_ONCE