#include "../../libs/cht.h"
#include "../../libs/common.h"
#include "../../libs/debug.h"

using cht::Line;

void solve(int testId, istream &in, ostream &out) {
  cht::ConvexHullTrick<ll> trick;

  int n;
  in >> n;
  vector<tuple<ll, ll, ll>> xya(n + 1);
  for (int i = 1; i <= n; i++) {
    ll x, y;
    ll a;
    in >> x >> y >> a;
    xya[i] = make_tuple(x, y, a);
  }

  sort(xya.begin(), xya.end(),
       [&](auto &a, auto &b) { return get<0>(a) < get<0>(b); });

  vector<ll> dp(n + 1);
  dp[0] = 0;
  trick.add(0, 0);
  for (int i = 1; i <= n; i++) {
    ll x = get<0>(xya[i]);
    ll y = get<1>(xya[i]);
    ll a = get<2>(xya[i]);
    dp[i] = trick(y) + x * y - a;
    trick.add(-x, dp[i]);
    dbg(i, trick);
  }
  dbg(xya);
  dbg(dp);
  ll ans = 0;
  for (int i = 1; i <= n; i++) {
    ans = max(ans, dp[i]);
  }
  out << ans;
}

RUN_ONCE