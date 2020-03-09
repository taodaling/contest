#ifndef LINEAR_INTEGER_RANGE_H
#define LINEAR_INTEGER_RANGE_H

#include "common.h"
#include "decimal.h"

namespace linear_integer_range {
inline void Invalidate(pair<ll, ll> &range) {
  range.first = 1;
  range.second = 0;
}
inline void Intersect(ll l, ll r, pair<ll, ll> &range) {
  range.first = max(range.first, l);
  range.second = min(range.second, r);
}
/**
 * kx + b >= c
 */
void GreaterThanOrEqualTo(ll k, ll b, ll c, pair<ll, ll> &range) {
  // kx + b > c => x >= (c - b) / k if k > 0
  if (k == 0) {
    if (b < c) {
      Invalidate(range);
    }
    return;
  }
  ll l = range.first;
  ll r = range.second;
  if (k < 0) {
    // x <= (c - b) / k
    // less than
    k = -k;
    b = -b;
    c = -c;
    r = decimal::FloorDiv(c - b, k);
  } else {
    // x >= (c - b) / k
    l = decimal::CeilDiv(c - b, k);
  }
  Intersect(l, r, range);
}
void LessThanOrEqualTo(ll k, ll b, ll c, pair<ll, ll> &range) {
  GreaterThanOrEqualTo(-k, -b, -c, range);
}
/**
 * kx + b > c
 */
void GreaterThan(ll k, ll b, ll c, pair<ll, ll> &range) {
  GreaterThanOrEqualTo(k, b, c + 1, range);
}
void LessThan(ll k, ll b, ll c, pair<ll, ll> &range) {
  LessThanOrEqualTo(k, b, c - 1, range);
}
/**
 * l <= kx + b <= r
 */
void Between(ll k, ll b, ll l, ll r, pair<ll, ll> &range) {
  GreaterThanOrEqualTo(k, b, l, range);
  LessThanOrEqualTo(k, b, r, range);
}

ll Length(pair<ll, ll> &range) {
  if (range.first > range.second) {
    return 0;
  }
  return range.second - range.first + 1;
}
}  // namespace linear_integer_range

#endif