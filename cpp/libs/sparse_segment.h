#ifndef SPARSE_SEGMENT_H
#define SPARSE_SEGMENT_H
namespace sparse_segment {
template <typename R = int>
class SparseSegment {
 public:
  SparseSegment() { _l = _r = 0; }

#define NO_INTERSECTION ql > r || qr < l
#define COVER ql <= l &&qr >= r
#define RANGE (min(qr, r) - max(l, ql) + 1)

  void update(R ql, R qr, R l, R r) {
    if (NO_INTERSECTION) {
      return;
    }
    if (COVER) {
      modify();
      return;
    }
    pushDown();
    R m = (l + r) >> 1;
    get_left()->update(ql, qr, l, m);
    get_right()->update(ql, qr, m + 1, r);
    pushUp();
  }

  void query(R ql, R qr, R l, R r) {
    if (NO_INTERSECTION) {
      return;
    }
    if (COVER) {
      return;
    }
    pushDown();
    R m = (l + r) >> 1;
    get_left()->query(ql, qr, l, m);
    get_right()->query(ql, qr, m + 1, r);
  }

#undef NO_INTERSECTION
#undef COVER

 private:
  SparseSegment<R> *_l, *_r;

  SparseSegment<R> *get_left() {
    if (!_l) {
      _l = new SparseSegment();
    }
    return _l;
  }

  SparseSegment<R> *get_right() {
    if (!_r) {
      _r = new SparseSegment();
    }
    return _r;
  }

  void pushDown() {}

  void pushUp() {}

  void modify() {}
};
}  // namespace sparse_segment

#endif  // SPARSE_SEGMENT_H
