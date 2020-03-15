#include "../../libs/bits.h"
#include "../../libs/common.h"
#include "../../libs/debug.h"

using namespace bits;

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;

  int mask = (1 << m) - 1;
  vector<vector<ll>> mapping(mask + 1, vector<ll>(mask + 1));

  mapping[0][0] = 1;
  for (int i = 0; i < m; i++) {
    int range = (1 << i) - 1;
    int m1 = 1 << i - 1;
    int m2 = 1 << i;
    int m3 = m1 | m2;
    for (int j = 0; j <= mask; j++) {
      for (int k = 0; k <= mask; k++) {
        if ((j & range) != j || (k & range) != k) {
          continue;
        }
        mapping[SetBit(j, i)][SetBit(k, i)] += mapping[j][k];
        if (i == 0) {
          continue;
        }
        if (!BitAt(j, i - 1)) {
          mapping[j | m3][k] += mapping[j][k];
        }
        if (!BitAt(j, i - 1) && !BitAt(k, i - 1)) {
          mapping[j | m3][k | m1] += mapping[j][k];
          mapping[j | m1][k | m3] += mapping[j][k];
        }
        if (!BitAt(k, i - 1)) {
          mapping[j | m2][k | m3] += mapping[j][k];
        }
        if (!BitAt(j, i - 1)) {
          mapping[j | m3][k | m2] += mapping[j][k];
        }
      }
    }
  }

  dbg(mapping);
  vector<ll> lastDp(mask + 1);
  lastDp[0] = 1;
  vector<ll> curDp(mask + 1);
  for (int i = 0; i < n; i++) {
    fill(curDp.begin(), curDp.end(), 0);

    for (int j = 0; j <= mask; j++) {
      for (int k = 0; k <= mask; k++) {
        curDp[k] += mapping[j][k] * lastDp[mask - j];
      }
    }

    curDp.swap(lastDp);
    dbg(lastDp);
  }

  ll ans = lastDp[0];
  out << ans;
}

RUN_ONCE