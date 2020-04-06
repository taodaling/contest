#include "../../libs/bits.h"
#include "../../libs/common.h"
#include "../../libs/subset.h"
#include "../../libs/debug.h"

vector<vector<vector<ll>>> dp;
int mask;
vector<bool> checker;

ll Dp(int i, int j, int k) {
  if (i < 0) {
    return j == 0 && k == 0 ? 1 : 0;
  }
  if (k < 0) {
    return 0;
  }
  if (dp[i][j][k] == -1) {
    dp[i][j][k] = 0;

    int remain = k - bits::CountOne((ui)j);

    if(!checker[j] || remain < 0){
      return dp[i][j][k];
    }

    subset::Subset ss(mask - (mask & (j | (j >> 1) | (j << 1))));
    while (ss.more()) {
      int next = ss.next();
      dp[i][j][k] += Dp(i - 1, next, remain);
    }
  }
  return dp[i][j][k];
}

void solve(int testId, istream &in, ostream &out) {
  int n, k;
  in >> n >> k;
  mask = (1 << n) - 1;
  checker.resize(mask + 1, true);
  for (int i = 0; i <= mask; i++) {
    for (int j = 1; j < n; j++) {
      if (bits::BitAt(i, j - 1) && bits::BitAt(i, j)) {
        checker[i] = false;
      }
    }
  }


  dp.resize(n, vector<vector<ll>>(mask + 1, vector<ll>(k + 1, -1)));

  ll ans = 0;
  for (int i = 0; i <= mask; i++) {
    ans += Dp(n - 1, i, k);
  }

  

  dbg(checker, dp);
  out << ans;
}

RUN_ONCE