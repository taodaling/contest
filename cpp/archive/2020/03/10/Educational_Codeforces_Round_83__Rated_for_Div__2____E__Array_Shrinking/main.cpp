#include "../../libs/common.h"

vector<vector<int>> dp;
vector<vector<int>> sum;

int dfs(int l, int r) {
  if (dp[l][r] == -1) {
    dp[l][r] = 0;
    if (l == r) {
      return dp[l][r] = 1;
    }
    for (int i = l; i < r; i++) {
      if (sum[l][i] != -1 && sum[l][i] == sum[i + 1][r] && dfs(l, i) &&
          dfs(i + 1, r)) {
        dp[l][r] = 1;
        break;
      }
    }
  }
  return dp[l][r];
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<int> a(n + 1);
  for (int i = 1; i <= n; i++) {
    in >> a[i];
  }
  sum.resize(n + 1, vector<int>(n + 1));
  vector<int> cnt(2000);
  for (int i = 1; i <= n; i++) {
    fill(cnt.begin(), cnt.end(), 0);
    int one = 0;
    int highest = 0;
    for (int j = i; j <= n; j++) {
      int v = a[j];
      while (cnt[v] == 1) {
        one--;
        cnt[v]--;
        v++;
      }
      cnt[v]++;
      one++;
      highest = max(highest, v);
      if (one == 1) {
        sum[i][j] = highest;
      } else {
        sum[i][j] = -1;
      }
    }
  }

  dp.resize(n + 1, vector<int>(n + 1, -1));

  vector<int> minLen(n + 1);
  minLen[0] = 0;
  for (int i = 1; i <= n; i++) {
    minLen[i] = i;
    if (dfs(1, i)) {
      minLen[i] = 1;
    }
    for (int j = i - 1; j >= 0; j--) {
      if (dfs(j + 1, i)) {
        minLen[i] = min(minLen[i], minLen[j] + 1);
      }
    }
  }

  out << minLen[n];
}

RUN_ONCE