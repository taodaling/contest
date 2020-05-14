#include "../../libs/Reader.h"
#include "../../libs/common.h"
#include "../../libs/radix.h"

radix::Radix<ll> rd(10);
int lBits[19];
int rBits[19];
int cnts[10];
vector<ll> all;

bool test(int sum, int i, bool ceil, bool floor) {
  if (sum > i + 1) {
    return false;
  }
  if (i < 0 || !ceil && !floor) {
    return true;
  }

  int l = lBits[i];
  int r = rBits[i];
  int start = floor ? l : 0;
  int end = ceil ? r : 9;
  for (int j = start; j <= end; j++) {
    if (cnts[j] == 0) {
      continue;
    }
    cnts[j]--;
    if (test(sum - (j > 0 ? 1 : 0), i - 1, ceil && j == r, floor && j == l)) {
      return true;
    }
    cnts[j]++;
  }
  return false;
}

void dfs(int val, int cnt, ll built) {
  if (val == 10) {
    all.push_back(built);
    return;
  }
  for (; cnt <= 18; cnt++, built = built * 10 + val) {
    dfs(val + 1, cnt, built);
  }
}

void solve(int testId, istream &in, ostream &out) {
  all.reserve(5000000);

  ll l, r;
  in >> l >> r;
  ll lVal = l;
  ll rVal = r;
  for (int i = 0; i < 19; i++) {
    lBits[i] = lVal % 10;
    rBits[i] = rVal % 10;
    lVal /= 10;
    rVal /= 10;
  }

  dfs(1, 0, 0);

  int ans = 0;

  // all.unique();
  for (ll val : all) {
    int sum = 0;

    if (val >= l && val <= r) {
      ans++;
      continue;
    }
    for (int i = 1; i < 10; i++) {
      cnts[i] = 0;
    }
    while (val > 0) {
      cnts[(int)(val % 10)]++;
      val /= 10;
      sum++;
    }
    cnts[0] = (int)1e9;
    if (test(sum, 18, true, true)) {
      ans++;
      // debug.debug("val", all.get(i));
    }
  }

  out << ans;
}

RUN_ONCE