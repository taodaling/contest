#include "../../libs/binary_search.h"
#include "../../libs/common.h"
#include "../../libs/debug.h"

void solve(int testId, istream &in, ostream &out) {
  ll n;
  in >> n;

  int ans = binary_search::BinarySearch<int>(0, 60, [&](int mid) {
    ll x = mid;
    for (int i = 0; i < mid; i++) {
      x = 2 * x + 1;
      if (x >= n) {
        return true;
      }
    }
    if (n - x <= x + 1) {
      x = n;
    }
    return x >= n;
  });

  out << ans;
}

RUN_ONCE