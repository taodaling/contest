#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<int> c(n);
  for (int i = 0; i < n; i++) {
    in >> c[i];
  }
  int v;
  in >> v;

  int inf = 1e9;
  vector<int> dp(v + 1, inf);
  dp[0] = 0;
  for (int i = 0; i < n; i++) {
    for (int j = 0; j <= v; j++) {
      if (j >= c[i]) {
        dp[j] = min(dp[j], dp[j - c[i]] + 1);
      }
    }
  }

  int ans = dp[v];
  if(ans >= inf){
    out << -1;
    return;
  }
  out << ans;
  
}

RUN_ONCE