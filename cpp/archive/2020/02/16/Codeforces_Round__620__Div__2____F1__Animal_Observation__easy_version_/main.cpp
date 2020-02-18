#include "../../libs/common.h"
#include "../../libs/presum.h"

using pre_sum::PreSum;

class Segment
{
public:
    Segment(int l, int r) : mx(0), dirty(0), _l(0), _r(0)
    {
        int m = (l + r) >> 1;
        if (l < r)
        {
            _l = new Segment(l, m);
            _r = new Segment(m + 1, r);
            pushUp();
        }
        else
        {
        }
    }

    void reset(int l, int r, vector<int> &vals)
    {
        int m = (l + r) >> 1;
        dirty = 0;
        if (l < r)
        {
            _l->reset(l, m, vals);
            _r->reset(m + 1, r, vals);
            pushUp();
        }
        else
        {
            mx = vals[l];
        }
    }

#define NO_INTERSECTION ql > r || qr < l
#define COVER ql <= l &&qr >= r
#define RANGE (min(qr, r) - max(l, ql) + 1)

    void update(int ql, int qr, int l, int r, int x)
    {
        if (NO_INTERSECTION)
        {
            return;
        }
        if (COVER)
        {
            modify(x);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        _l->update(ql, qr, l, m, x);
        _r->update(ql, qr, m + 1, r, x);
        pushUp();
    }

    int query(int ql, int qr, int l, int r)
    {
        if (NO_INTERSECTION)
        {
            return 0;
        }
        if (COVER)
        {
            return mx;
        }
        pushDown();
        int m = (l + r) >> 1;
        return max(_l->query(ql, qr, l, m),
                   _r->query(ql, qr, m + 1, r));
    }

#undef NO_INTERSECTION
#undef COVER

private:
    Segment *_l, *_r;
    int mx;
    int dirty;

    void pushDown()
    {
        _l->modify(dirty);
        _r->modify(dirty);
        dirty = 0;
    }

    void pushUp()
    {
        mx = max(_l->mx, _r->mx);
    }

    void modify(int x)
    {
        dirty += x;
        mx += x;
    }
};

void solve(int testId, istream &in, ostream &out)
{
    int n, m, k;
    in >> n >> m >> k;
    vector<vector<int>> dps(n, vector<int>(m));
    vector<vector<int>> mat(n + 1, vector<int>(m));
    vector<PreSum<int>> rows(n + 1);
    vector<int> left(m); //left[i] means leave 0,1,..,i not used
    vector<int> right(m);
    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < m; j++)
        {
            in >> mat[i][j];
        }
        rows[i].populate(mat[i]);
    }
    rows[n].populate(mat[n]);
    for (int i = 0; i < m; i++)
    {
        dps[0][i] = rows[0].interval(i, i + k - 1) + rows[1].interval(i, i + k - 1);
    }
    Segment segtree(0, m - 1);
    for (int i = 1; i < n; i++)
    {
        segtree.reset(0, m - 1, dps[i - 1]);
        for (int j = 0; j < m; j++)
        {
            int l = j - k + 1;
            int r = j;
            segtree.update(l, r, 0, m - 1, -mat[i][j]);
            left[j] = segtree.query(0, m - 1, 0, m - 1);
        }
        segtree.reset(0, m - 1, dps[i - 1]);
        for (int j = m - 1; j >= 0; j--)
        {
            int l = j - k + 1;
            int r = j;
            segtree.update(l, r, 0, m - 1, -mat[i][j]);
            right[j] = segtree.query(0, m - 1, 0, m - 1);
        }
        // for(int j = 0; j < m; j++){
        //     if(left[j] % 1000 || right[ m - 1 - j] % 1000){
        //         exit(1);
        //     }
        // }
        for (int j = 0; j < m; j++)
        {
            dps[i][j] = rows[i].interval(j, j + k - 1) + rows[i + 1].interval(j, j + k - 1);
            dps[i][j] += max(left[min(m - 1, j + k - 1)], right[j]);
        }
    }

// bool appear = false;
//     for (int i = 0; i < n; i++)
//     {
//         for (int j = 0; j < m; j++)
//         {
//             if(dps[i][j] % 1000 && !appear){
//             error(i, j, dps[i][j]);
//             appear = true;
//             }

//         }
//     }

    int ans = 0;
    for (int x : dps[n - 1])
    {
        ans = max(ans, x);
    }
    out << ans;
}

#include "../../libs/run_once.h"