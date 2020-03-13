#include "../../libs/common.h"
#include "../../libs/geo2.h"
#include "../../libs/util.h"
#include "../../libs/binary_index_tree.h"

using pt = geo2::Point<int>;
using bit = bit::BIT<int, 20010>;

void solve(int testId, istream &in, ostream &out) {
  int n;
  vector<pt> pts(n);
  for (int i = 0; i < n; i++) {
    in >> pts[i];
    pts[i].x += 10001;
    pts[i].y += 10001;
  }
  vector<int> sortByXY = util::Range(0, n - 1);
  vector<int> sortByYX = util::Range(0, n - 1);
  sort(sortByXY.begin(), sortByXY.end(), [](int i, int j) -> {
    return make_tuple(pts[i].x, pts[i].y) < make_tuple(pts[j].x, pts[j].y);
  });
  sort(sortByXY.begin(), sortByXY.end(), [](int i, int j) -> {
    return make_tuple(pts[i].y, pts[i].x) < make_tuple(pts[j].y, pts[j].x);
  });
}

RUN_ONCE