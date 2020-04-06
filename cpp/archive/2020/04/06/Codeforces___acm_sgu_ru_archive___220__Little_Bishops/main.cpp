#include "../../libs/bits.h"
#include "../../libs/common.h"
#include "../../libs/debug.h"

vector<vector<ll>> dp;
int mask;
vector<pair<int, int>> lrs;
vector<int> start(2);

ll Dp(int i, int j) {
  if (i < 0) {
    return j == 0 ? 1 : 0;
  }
  if (dp[i][j] == -1) {
    dp[i][j] = Dp(i - 2, j);
    int l = X(lrs[i]);
    int r = Y(lrs[i]);
    l = (l - start[i & 1]) >> 1;
    r = (r - start[i & 1]) >> 1;
    for (int k = l; k <= r; k += 1) {
      if (bits::BitAt(j, k)) {
        dp[i][j] += Dp(i - 2, bits::RemoveBit(j, k));
      }
    }
  }
  return dp[i][j];
}

void solve(int testId, istream &in, ostream &out) {
  int n, k;
  in >> n >> k;
  mask = (1 << n) - 1;

  if (n == 1) {
    out << 1;
    return;
  }

  dp.resize(2 * n - 1, vector<ll>(mask + 1, -1));
  lrs.resize(2 * n - 1);
  X(lrs[0]) = n - 1;
  Y(lrs[0]) = n - 1;

  for (int i = 1; i <= n - 1; i++) {
    X(lrs[i]) = X(lrs[i - 1]) - 1;
    Y(lrs[i]) = Y(lrs[i - 1]) + 1;
  }
  for (int i = n; i < 2 * n - 1; i++) {
    X(lrs[i]) = X(lrs[i - 1]) + 1;
    Y(lrs[i]) = Y(lrs[i - 1]) - 1;
  }

  if (n % 2 == 1) {
    start[0] = 0;
    start[1] = 1;
  } else {
    start[0] = 1;
    start[1] = 0;
  }

  dbg(lrs);
  dbg(start);

  vector<ll> sumA(n * n * 2);
  vector<ll> sumB(n * n * 2);
  for (int i = 0; i <= mask; i++) {
    sumA[bits::CountOne((ui)i)] += Dp(2 * n - 2, i);
    sumB[bits::CountOne((ui)i)] += Dp(2 * n - 3, i);
  }

  dbg(dp);
  dbg(sumA, sumB);

  ll ans = 0;
  for (int i = 0; i <= k; i++) {
    ans += sumA[i] * sumB[k - i];
  }
  out << ans;
}

RUN_ONCE