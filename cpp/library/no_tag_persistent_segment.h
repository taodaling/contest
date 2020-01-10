//
// Created by daltao on 2019/12/10.
//

#ifndef NO_TAG_PERSISTENT_SEGMENT_H
#define NO_TAG_PERSISTENT_SEGMENT_H
namespace dalt
{
class no_tag_persistent_segment
{
public:
    no_tag_persistent_segment()
    {
        _l = _r = this;
    }

#define NO_INTERSECTION ql > r || qr < l
#define COVER ql <= l &&qr >= r
#define RANGE (min(qr, r) - max(l, ql) + 1)

    void update(int ql, int qr, int l, int r)
    {
        if (COVER)
        {
            modify();
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        if (!(m < ql || l > qr))
        {
            _l = new no_tag_persistent_segment(*_l);
            _l->update(ql, qr, l, m);
        }
        if (!(m + 1 > qr || r < ql))
        {
            _r = new no_tag_persistent_segment(*_r);
            _r->update(ql, qr, m + 1, r);
        }
        pushUp();
    }

    void query(int ql, int qr, int l, int r)
    {
        if (NO_INTERSECTION)
        {
            return;
        }
        if (COVER)
        {
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
    no_tag_persistent_segment *_l, *_r;
    void pushDown()
    {
    }

    void pushUp()
    {
    }

    void modify()
    {
    }
};
} // namespace dalt

#endif //SEGMENT_H
