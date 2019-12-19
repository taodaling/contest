#include "../library/common.h"
#include "../library/debug.h"

using namespace dalt;

namespace dalt {
    class enhanced_segment {
    public:
        enhanced_segment(int l, int r) {
            int m = (l + r) >> 1;
            if (l < r) {
                _l = new enhanced_segment(l, m);
                _r = new enhanced_segment(m + 1, r);
                pushUp();
            } else {

            }
        }

#define NO_INTERSECTION ql > r || qr < l
#define COVER ql <= l && qr >= r
#define RANGE (min(qr, r) - max(l, ql) + 1)

        void update(int ql, int qr, int l, int r) {
            if (NO_INTERSECTION) {
                return;
            }
            if (COVER) {
                modify();
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            _l->update(ql, qr, l, m);
            _r->update(ql, qr, m + 1, r);
            pushUp();
        }

        void query(int ql, int qr, int l, int r) {
            if (NO_INTERSECTION) {
                return;
            }
            if (COVER) {
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            _l->query(ql, qr, l, m);
            _r->query(ql, qr, m + 1, r);
        }

#undef NO_INTERSECTION
#undef COVER

    private:
        enhanced_segment *_l, *_r;
        ll v;


        void pushDown() {
        }

        void pushUp() {
        }

        void modify() {
        }
    };
}

namespace dalt {
    struct segment_query_result{
        int index;
        int val;

        void init(){
            index = -1;
            val = -1;
        }

        void update(int index, int val){
            if(val > this->val){

            }
        }
    };
    class segment {
    public:
        segment(int l, int r) {
            int m = (l + r) >> 1;
            if (l < r) {
                _l = new segment(l, m);
                _r = new segment(m + 1, r);
                pushUp();
            } else {

            }
        }

#define NO_INTERSECTION ql > r || qr < l
#define COVER ql <= l && qr >= r
#define RANGE (min(qr, r) - max(l, ql) + 1)

        void update(int ql, int qr, int l, int r) {
            if (NO_INTERSECTION) {
                return;
            }
            if (COVER) {
                modify();
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            _l->update(ql, qr, l, m);
            _r->update(ql, qr, m + 1, r);
            pushUp();
        }

        void query(int ql, int qr, int l, int r) {
            if (NO_INTERSECTION) {
                return;
            }
            if (COVER) {
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            _l->query(ql, qr, l, m);
            _r->query(ql, qr, m + 1, r);
        }

#undef NO_INTERSECTION
#undef COVER

    private:
        segment *_l, *_r;
        int _v;
        int _index;

        void pushDown() {
        }

        void pushUp() {
            if(_l->_v >= _r->_v){
                _v = _l->_v
            }
        }

        void modify() {
        }
    };
}



class GRecursiveQueries {
public:

    void solve(std::istream &in, std::ostream &out) {
        int n, q;
        in >> n >> q;

    }

private:
};
