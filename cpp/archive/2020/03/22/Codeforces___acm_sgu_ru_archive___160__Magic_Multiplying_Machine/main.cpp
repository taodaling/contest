#include "../../libs/common.h"
#include "../../libs/debug.h"

inline int Mod(int x, int n) { return x % n; }

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;
  vector<int> a(n + 1);
  for (int i = 1; i <= n; i++) {
    in >> a[i];
    a[i] = Mod(a[i], m);
  }
  vector<vector<bool>> dp(n + 1, vector<bool>(m));
  vector<vector<int>> last(n + 1, vector<int>(m));
  dp[0][1] = true;
  for (int i = 1; i <= n; i++) {
    for (int j = 0; j < m; j++) {
      if (dp[i - 1][j]) {
        int next = Mod(j * a[i], m);
        dp[i][next] = true;
        dp[i][j] = true;
        last[i][next] = j;
        last[i][j] = j;
      }
    }
  }

  vector<int> ans;
  ans.reserve(n);
  int state = 0;
  for (int i = m - 1; i >= 0; i--) {
    if (dp[n][i]) {
      state = i;
      break;
    }
  }

  dbg(dp);
  dbg(last);

  out << state << endl;
  for (int i = n; i >= 1; i--) {
    if(last[i][state] == state){
      continue;
    }
    state = last[i][state];
    ans.push_back(i);
  }

  reverse(ans.begin(), ans.end());
  for (int x : ans) {
    out << x << ' ';
  }
}

RUN_ONCE