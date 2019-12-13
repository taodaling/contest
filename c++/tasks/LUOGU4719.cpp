#include "../library/common.h"


vector<int> states;
vector<int> weights;
vector<vector<int>> edges;
vector<int> sizes;
vector<int> heavies;
vector<int> segIds;
vector<int> parents;
vector<int> tops;
vector<int> bottoms;


struct Mat {
    Mat() {
        memset(mat, 0, sizeof(mat));
    }

    int mat[2][2];

    int *operator[](int i) {
        return mat[i];
    }

    const int *operator[](int i) const {
        return mat[i];
    }
};

void merge(const Mat &a, const Mat &b, Mat &mat) {
    for (int i = 0; i < 2; i++) {
        for (int j = 0; j < 2; j++) {
            mat[i][j] = 0;
        }
    }
    for (int i = 0; i < 2; i++) {
        for (int j = 0; j < 2; j++) {
            for (int ii = 0; ii < 2; ii++) {
                if (ii && j) {
                    continue;
                }
                for (int jj = 0; jj < 2; jj++) {
                    mat[i][jj] = max(mat[i][jj], a[i][j] + b[ii][jj]);
                }
            }
        }
    }
}
Mat merge(const Mat &a, const Mat &b){
   Mat ans;
   merge(a, b, ans);
   return ans;
}

class Segment {
public:
    Segment(int l, int r) {
        int m = (l + r) >> 1;
        if (l < r) {
            _l = new Segment(l, m);
            _r = new Segment(m + 1, r);
            pushUp();
        } else {

        }
    }

#define NO_INTERSECTION ql > r || qr < l
#define COVER ql <= l && qr >= r
#define RANGE (min(qr, r) - max(l, ql) + 1)

    void update(int ql, int qr, int l, int r, int oldZero, int oldOne, int newZero, int newOne) {
        if (NO_INTERSECTION) {
            return;
        }
        if (COVER) {
            modify(oldZero, oldOne, newZero, newOne);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        _l->update(ql, qr, l, m, oldZero, oldOne, newZero, newOne);
        _r->update(ql, qr, m + 1, r, oldZero, oldOne, newZero, newOne);
        pushUp();
    }

    void update(int ql, int qr, int l, int r, int val) {
        if (NO_INTERSECTION) {
            return;
        }
        if (COVER) {
            _mat[1][1] += val;
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        _l->update(ql, qr, l, m, val);
        _r->update(ql, qr, m + 1, r, val);
        pushUp();
    }

    Mat query(int ql, int qr, int l, int r) {
        if (NO_INTERSECTION) {
            return Mat();
        }
        if (COVER) {
            return _mat;
        }
        pushDown();
        int m = (l + r) >> 1;
        if(m >= qr){
            return _l->query(ql, qr, l, m);
        }else if(m < ql){
            return _r->query(ql, qr, m + 1, r);
        }else {
            return merge(_l->query(ql, qr, l, m),
                         _r->query(ql, qr, m + 1, r));
        }
    }

#undef NO_INTERSECTION
#undef COVER

private:
    Segment *_l, *_r;
    Mat _mat;

    void pushDown() {

    }

    void pushUp() {
        merge(_l->_mat, _r->_mat, _mat);
    }

    void modify(int oldZero, int oldOne, int newZero, int newOne) {
        _mat[1][1] += newZero - oldZero;
        _mat[0][0] += max(newZero, newOne) - max(oldZero, oldOne);
    }
};

void dfsForSize(int root, int p) {
    sizes[root] = 1;
    for (int node : edges[root]) {
        if (node == p) {
            continue;
        }
        dfsForSize(node, root);
        sizes[root] += sizes[node];
        if (sizes[heavies[root]] < sizes[node]) {
            heavies[root] = node;
        }
    }
}

int segIdAllocator = 0;

int hld(int root, int p, int top) {
    if (!top) {
        top = root;
    }
    parents[root] = p;
    tops[root] = top;
    segIds[root] = ++segIdAllocator;

    if (heavies[root]) {
        bottoms[root] = hld(heavies[root], root, top);
    } else {
        bottoms[root] = root;
    }

    for (int node : edges[root]) {
        if (node == p || node == heavies[root]) {
            continue;
        }
        hld(node, root, 0);
    }
    return bottoms[root];
}

Segment *segment;
int n;

Mat query(int node) {
    int bot = bottoms[node];

    int l = segIds[node];
    int r = segIds[bot];

    return segment->query(l, r, 1, n);
}

void afterUpdate(int root, int oldZero, int oldOne) {
    root = tops[root];
    if (!parents[root]) {
        return;
    }

    const Mat &pMat = query(tops[parents[root]]);
    int pOldZero = max(pMat[0][0], pMat[0][1]);
    int pOldOne = max(pMat[1][0], pMat[1][1]);

    const Mat &rootMat = query(root);
    int newZero = max(rootMat[0][0], rootMat[0][1]);
    int newOne = max(rootMat[1][0], rootMat[1][1]);

    if(newZero == oldZero && newOne == oldOne){
        return;
    }

    segment->update(segIds[parents[root]], segIds[parents[root]], 1, n,
            oldZero, oldOne, newZero, newOne);
    afterUpdate(parents[root], pOldZero, pOldOne);
}

void update(int root, int val){
    const Mat &pMat = query(tops[root]);
    int pOldZero = max(pMat[0][0], pMat[0][1]);
    int pOldOne = max(pMat[1][0], pMat[1][1]);

    segment->update(segIds[root], segIds[root],
            1, n, val - weights[root]);
    weights[root] = val;

    afterUpdate(root, pOldZero, pOldOne);
}

class LUOGU4719 {
public:

    void solve(std::istream &in, std::ostream &out) {
        int m;
        in >> n >> m;
        weights.resize(n + 1);
        edges.resize(n + 1);
        sizes.resize(n + 1);
        heavies.resize(n + 1);
        segIds.resize(n + 1);
        parents.resize(n + 1);
        tops.resize(n + 1);
        bottoms.resize(n + 1);
        states.resize(n + 1);

        for (int i = 1; i <= n; i++) {
            in >> states[i];
        }
        for (int i = 1; i < n; i++) {
            int a, b;
            in >> a >> b;
            edges[a].push_back(b);
            edges[b].push_back(a);
        }

        dfsForSize(1, 0);
        hld(1, 0, 0);
        segment = new Segment(1, n);

        for(int i = 1; i <= n; i++){
            update(i, states[i]);
        }

        for(int i = 0; i < m; i++){
            int x, y;
            in >> x >> y;
            update(x, y);
            Mat mat = query(1);
            int ans = 0;
            for(int i = 0; i < 2; i++){
                for(int j = 0; j < 2; j++){
                    ans = max(ans, mat[i][j]);
                }
            }
            out << ans << endl;
        }
    }

private:
};
