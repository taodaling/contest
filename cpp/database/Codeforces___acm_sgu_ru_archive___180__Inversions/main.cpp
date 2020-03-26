#include "../../libs/binary_index_tree.h"
#include "../../libs/common.h"
#include "../../libs/discretemap.h"

binary_index_tree::BIT<int, 65537> bit;

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<int> a(n);
  for (int i = 0; i < n; i++) {
    in >> a[i];
  }
  reverse(a.begin(), a.end());
  discretemap::DiscreteMap<int> dm(a);

  ll ans = 0;
  for (int i = 0; i < n; i++) {
    int val = dm.apply(a[i]) + 1;
    ans += bit.sum(val - 1);
    bit.update(val, 1);
  }
  out << ans;
}

RUN_ONCE