#ifndef SPARSE_SEGMENT_H
#define SPARSE_SEGMENT_H
namespace rmq {
    template<typename R=int>
    class sparse_segment {
    public:
        sparse_segment() {
            _l = _r = 0;
        }

#define NO_INTERSECTION ql > r || qr < l
#define COVER ql <= l && qr >= r
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
        sparse_segment<R> *_l, *_r;

        sparse_segment<R> *get_left() {
            if (!_l) {
                _l = new sparse_segment();
            }
            return _l;
        }

        sparse_segment<R> *get_right() {
            if (!_r) {
                _r = new sparse_segment();
            }
            return _r;
        }

        void pushDown() {
        }

        void pushUp() {
        }

        void modify() {
        }
    };
}


#endif //SPARSE_SEGMENT_H
