#include "../../libs/common.h"
#include "../../libs/debug.h"

namespace quadtree {
/**
 * 00 01
 * 10 11
 */
class QuadTree {
 private:
  QuadTree *s00, *s01, *s10, *s11;
  static QuadTree *NIL;
  int cnt;
  int size;
  int tag;

  QuadTree() : cnt(0), size(0), tag(-1) {}
  void modify(int x) {
    if (this == NIL) {
      return;
    }
    tag = x;
    cnt = x * size;
  }
  void pushUp() {
    size = s00->size + s01->size + s10->size + s11->size;
    cnt = s00->cnt + s01->cnt + s10->cnt + s11->cnt;
  }
  void pushDown() {
    if (this == NIL) {
      return;
    }
    if (tag != -1) {
      s00->modify(tag);
      s01->modify(tag);
      s10->modify(tag);
      s11->modify(tag);
      tag = -1;
    }
  }

 public:
  static QuadTree *build(int x1, int x2, int y1, int y2) {
    if (x1 > x2 || y1 > y2) {
      return NIL;
    }
    QuadTree *ans = new QuadTree();
    int xm = (x1 + x2) >> 1;
    int ym = (y1 + y2) >> 1;
    if (x1 < x2 || y1 < y2) {
      ans->s00 = build(x1, xm, y1, ym);
      ans->s01 = build(x1, xm, ym + 1, y2);
      ans->s10 = build(xm + 1, x2, y1, ym);
      ans->s11 = build(xm + 1, x2, ym + 1, y2);
      ans->pushUp();
    } else {
      ans->size = 1;
    }
    return ans;
  }

#define NO_INTERSECTION (tx1 > x2 || tx2 < x1 || ty1 > y2 || ty2 < y1)
#define COVER (tx1 <= x1 && x2 <= tx2 && ty1 <= y1 && y2 <= ty2)

  void update(int tx1, int tx2, int ty1, int ty2, int x1, int x2, int y1,
              int y2, int color) {
    if (NO_INTERSECTION) {
      return;
    }
    if (COVER) {
      modify(color);
      return;
    }
    int mx = (x1 + x2) >> 1;
    int my = (y1 + y2) >> 1;
    pushDown();
    s00->update(tx1, tx2, ty1, ty2, x1, mx, y1, my, color);
    s01->update(tx1, tx2, ty1, ty2, x1, mx, my + 1, y2, color);
    s10->update(tx1, tx2, ty1, ty2, mx + 1, x2, y1, my, color);
    s11->update(tx1, tx2, ty1, ty2, mx + 1, x2, my + 1, y2, color);
    pushUp();
  }

  int query(int tx1, int tx2, int ty1, int ty2, int x1, int x2, int y1,
            int y2) {
    if (NO_INTERSECTION) {
      return 0;
    }
    if (COVER) {
      return cnt;
    }
    int mx = (x1 + x2) >> 1;
    int my = (y1 + y2) >> 1;
    pushDown();
    return s00->query(tx1, tx2, ty1, ty2, x1, mx, y1, my) +
           s01->query(tx1, tx2, ty1, ty2, x1, mx, my + 1, y2) +
           s10->query(tx1, tx2, ty1, ty2, mx + 1, x2, y1, my) +
           s11->query(tx1, tx2, ty1, ty2, mx + 1, x2, my + 1, y2);
  }
};

QuadTree *QuadTree::NIL = new QuadTree();
}  // namespace quadtree

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;
  quadtree::QuadTree *qt = quadtree::QuadTree::build(1, n, 1, n);
  qt->update(1, n, 1, n, 1, n, 1, n, 1);
  dbg2(qt->query(1, n, 1, n, 1, n, 1, n));
  for (int i = 0; i < m; i++) {
    int x1, y1, x2, y2;
    char c;
    in >> x1 >> y1 >> x2 >> y2 >> c;
    int t = min(x1, x2);
    int b = max(x1, x2);
    int l = min(y1, y2);
    int r = max(y1, y2);
    int color = c == 'b' ? 0 : 1;
    qt->update(t, b, l, r, 1, n, 1, n, color);
    dbg2(qt->query(1, n, 1, n, 1, n, 1, n));
  }

  int cnt = qt->query(1, n, 1, n, 1, n, 1, n);
  out << cnt;
}

RUN_ONCE