#ifndef SOLVER_H
#define SOLVER_H

#include "gcd.h"
#include "linear_integer_range.h"

namespace solver {

ll CountInterval(ll l, ll r) { return l > r ? 0 : (r - l + 1); }
/**
 * Find how many pair (x,y) exists while following constraints obeyed.
 *
 * <pre>
 * ax+by=c
 * xl<=x<=xr
 * yl<=y<=yr
 * x is an integer
 * y is an integer
 * </pre>
 * <p>
 * The time complexity of this method is O(log2(max(a,b)))
 */
ll CountWay(ll a, ll b, ll c, ll x1, ll x2, ll y1, ll y2) {
  pair<ll, ll> interval(-1e18, 1e18);
  if (a == 0 && b == 0) {
    if (c == 0) {
      return CountInterval(x1, x2) * CountInterval(y1, y2);
    }
    return 0;
  }

  ll x0, y0;
  ll g = gcd::Extgcd(abs(a), abs(b), x0, y0);
  if (a < 0) {
    x0 = -x0;
  }
  if (b < 0) {
    y0 = -y0;
  }
  if (c % g != 0) {
    return 0;
  }
  x0 *= c / g;
  y0 *= c / g;

  linear_integer_range::Between(b / g, x0, x1, x2, interval);
  linear_integer_range::Between(-a / g, y0, y1, y2, interval);
  ll ans = CountInterval(interval.first, interval.second);
  return ans;
}
}  // namespace solver

#endif