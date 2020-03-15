#include "../../libs/binary_index_tree.h"
#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/dsu.h"
#include "../../libs/geo2.h"
#include "../../libs/util.h"

using pt = geo2::Point<int>;
bit::BIT<int, 20020> bt;
dsu::DSU<20000> dset;

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<pt> pts(n);
  for (int i = 0; i < n; i++) {
    in >> pts[i];
    pts[i].x += 10010;
    pts[i].y += 10010;
  }
  dbg(pts);

  vector<int> sortByXY = util::Range(0, n - 1);
  vector<int> sortByYX = util::Range(0, n - 1);
  sort(sortByXY.begin(), sortByXY.end(), [&](int i, int j) {
    return make_tuple(pts[i].x, pts[i].y) < make_tuple(pts[j].x, pts[j].y);
  });
  sort(sortByYX.begin(), sortByYX.end(), [&](int i, int j) {
    return make_tuple(pts[i].y, pts[i].x) < make_tuple(pts[j].y, pts[j].x);
  });

  vector<pair<pt, pt>> horizontal;
  vector<pair<pt, pt>> vertical;
  horizontal.reserve(n);
  vertical.reserve(n);
  bool valid = true;
  ll ans = 0;
  for (int i = 0; i < n; i++) {
    int l = i;
    int r = l;
    while (r + 1 < n && pts[sortByXY[r + 1]].x == pts[sortByXY[l]].x) {
      r++;
    }
    dbg(l, r);
    valid = valid && (r - l + 1) % 2 == 0;
    i = r;
    for (int j = l; j + 1 <= r; j += 2) {
      vertical.emplace_back(pts[sortByXY[j]], pts[sortByXY[j + 1]]);
      dset.merge(sortByXY[j], sortByXY[j + 1]);
    }
  }

  for (int i = 0; i < n; i++) {
    int l = i;
    int r = l;
    while (r + 1 < n && pts[sortByYX[r + 1]].y == pts[sortByYX[l]].y) {
      r++;
    }
    dbg(l, r);
    valid = valid && (r - l + 1) % 2 == 0;
    i = r;
    for (int j = l; j + 1 <= r; j += 2) {
      horizontal.emplace_back(pts[sortByYX[j]], pts[sortByYX[j + 1]]);
      dset.merge(sortByYX[j], sortByYX[j + 1]);
    }
  }

  dbg(valid);
  dbg(vertical);
  dbg(horizontal);

  for (auto &v : vertical) {
    ans += v.second.y - v.first.y;
    assert(v.first.y < v.second.y);
  }
  for (auto &h : horizontal) {
    ans += h.second.x - h.first.x;
    assert(h.first.x < h.second.x);
  }

  for (int i = 1; i < n; i++) {
    valid = valid && dset.find(0) == dset.find(i);
  }
  priority_queue<tuple<int, int>, vector<tuple<int, int>>,
                 std::greater<tuple<int, int>>>
      add;
  priority_queue<tuple<int, int>, vector<tuple<int, int>>,
                 std::greater<tuple<int, int>>>
      sub;
  for (auto &v : vertical) {
    add.emplace(v.first.y, v.first.x);
    sub.emplace(v.second.y, v.second.x);
  }

  sort(horizontal.begin(), horizontal.end(),
       [](auto &a, auto &b) { return a.first.y < b.first.y; });

  for (auto &p : horizontal) {
    while (!add.empty() && get<0>(add.top()) < p.first.y) {
      bt.update(get<1>(add.top()), 1);
      add.pop();
    }
    while (!sub.empty() && get<0>(sub.top()) <= p.first.y) {
      bt.update(get<1>(sub.top()), -1);
      sub.pop();
    }
    int cnt = bt.query(p.first.x, p.second.x);
    if (cnt) {
      valid = false;
    }
  }

  if (!valid) {
    ans = 0;
  }

  out << ans;
}

RUN_ONCE