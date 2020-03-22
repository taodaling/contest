#include "../../libs/binary_search.h"
#include "../../libs/common.h"

using binary_search::BinarySearch;

ll TailZero(ll n) {
  if (n == 0) {
    return 0;
  }
  return TailZero(n / 5) + n / 5;
}

void solve(int testId, istream &in, ostream &out) {
  ll q;
  in >> q;
  ll ans = BinarySearch<ll>(1, (ll)1e18, [&](ll mid) { return TailZero(mid) >= q; });

  if(TailZero(ans) != q){
    out << "No solution";
    return;
  }

  out << ans;
}

RUN_ONCE