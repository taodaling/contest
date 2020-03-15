#include "../../libs/binary_index_tree.h"
#include "../../libs/common.h"
#include "../../libs/discretemap.h"

#define MAX_RANGE 40000

bit::BIT<int, MAX_RANGE> bt;

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<int> all;
  all.reserve(n * 2);
  vector<pair<int, int>> ab(n);
  for (int i = 0; i < n; i++) {
    in >> ab[i].first >> ab[i].second;
    all.push_back(ab[i].first);
    all.push_back(ab[i].second);
  }
  discretemap::DiscreteMap<int> dm(all);
  for (int i = 0; i < n; i++) {
    ab[i].first = dm.apply(ab[i].first) + 1;
    ab[i].second = dm.apply(ab[i].second) + 1;
  }
  sort(ab.begin(), ab.end(), [](auto &a, auto &b) {
    return make_tuple(a.first, a.second) < make_tuple(b.first, b.second);
  });

  int ans = 0;
  for (int i = 0; i < n; i++) {
    if (bt.query(ab[i].second + 1, MAX_RANGE)) {
      ans++;
    }
    bt.update(ab[i].second, 1);
  }
  out << ans;
}

RUN_ONCE