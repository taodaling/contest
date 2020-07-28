package template.math;

import template.datastructure.BitSet;

public class XorMatrix {
    BitSet[] mat;
    int n;
    int m;

    public XorMatrix(XorMatrix model) {
        n = model.n;
        m = model.m;
        mat = new BitSet[n];
        for (int i = 0; i < n; i++) {
            mat[i] = model.mat[i].clone();
        }
    }

    public XorMatrix(int n, int m) {
        this.n = n;
        this.m = m;
        mat = new BitSet[n];
        for (int i = 0; i < n; i++) {
            mat[i] = new BitSet(m);
        }
    }

    public XorMatrix(BitSet[] mat) {
        if (mat.length == 0 || mat[0].capacity() == 0) {
            throw new IllegalArgumentException();
        }
        this.n = mat.length;
        this.m = mat[0].capacity();
        this.mat = mat;
    }

    public void fill(boolean v) {
        for (int i = 0; i < n; i++) {
            mat[i].fill(v);
        }
    }

    public void asStandard() {
        fill(false);
        for (int i = 0; i < n && i < m; i++) {
            mat[i].set(i);
        }
    }

    public void set(int i, int j, boolean val) {
        mat[i].set(j, val);
    }

    public boolean get(int i, int j) {
        return mat[i].get(j);
    }

    public static XorMatrix region(XorMatrix x, int b, int t, int l, int r) {
        XorMatrix y = new XorMatrix(t - b + 1, r - l + 1);
        for (int i = b; i <= t; i++) {
            y.mat[i - b].copyInterval(x.mat[i], l, r);
        }
        return y;
    }


    /**
     * |x| while mod prime
     */
    public static int determinant(XorMatrix x) {
        if (x.n != x.m) {
            throw new RuntimeException("XorMatrix is not square");
        }
        int n = x.n;
        XorMatrix l = new XorMatrix(x);
        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int j = i; j < n; j++) {
                if (l.mat[j].get(i)) {
                    maxRow = j;
                    break;
                }
            }

            if (!l.mat[maxRow].get(i)) {
                return 0;
            }
            if (i != maxRow) {
                l.swapRow(i, maxRow);
            }

            for (int j = i + 1; j < n; j++) {
                if (j == i) {
                    continue;
                }
                if (!l.mat[j].get(i)) {
                    continue;
                }
                l.subtractRow(j, i);
            }
        }

        return 1;
    }

    public static XorMatrix inverse(XorMatrix x) {
        if (x.n != x.m) {
            throw new RuntimeException("XorMatrix is not square");
        }
        int n = x.n;
        XorMatrix l = new XorMatrix(x);
        XorMatrix r = new XorMatrix(n, n);
        r.asStandard();
        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int j = i; j < n; j++) {
                if (l.mat[j].get(i)) {
                    maxRow = j;
                    break;
                }
            }

            if (!l.mat[maxRow].get(i)) {
                throw new RuntimeException("Can't inverse current matrix");
            }
            r.swapRow(i, maxRow);
            l.swapRow(i, maxRow);

            for (int j = 0; j < n; j++) {
                if (j == i) {
                    continue;
                }
                if (!l.mat[j].get(i)) {
                    continue;
                }
                r.subtractRow(j, i);
                l.subtractRow(j, i);
            }
        }
        return r;
    }

    void swapRow(int i, int j) {
        BitSet row = mat[i];
        mat[i] = mat[j];
        mat[j] = row;
    }

    void subtractRow(int i, int j) {
        mat[i].xor(mat[j]);
    }

    public static XorMatrix mul(XorMatrix a, XorMatrix b) {
        XorMatrix c = new XorMatrix(a.n, b.m);
        for (int i = 0; i < c.n; i++) {
            for (int j = 0; j < c.m; j++) {
                for (int k = 0; k < a.m; k++) {
                    if (a.mat[i].get(k) && b.mat[k].get(j)) {
                        c.mat[i].flip(j);
                    }
                }
            }
        }
        return c;
    }

    public static XorMatrix fastMul(XorMatrix a, XorMatrix b) {
        XorMatrix c = new XorMatrix(a.n, b.m);
        b = transpose(b);
        BitSet buf = new BitSet(a.m);
        for (int i = 0; i < c.n; i++) {
            for (int j = 0; j < c.m; j++) {
                buf.copy(a.mat[i]);
                buf.and(b.mat[j]);
                c.mat[i].set(j, (buf.size() & 1) == 1);
            }
        }
        return c;
    }

    public static XorMatrix pow(XorMatrix x, long n) {
        if (n == 0) {
            XorMatrix r = new XorMatrix(x.n, x.m);
            r.asStandard();
            return r;
        }
        XorMatrix r = pow(x, n >> 1);
        r = XorMatrix.fastMul(r, r);
        if (n % 2 == 1) {
            r = XorMatrix.fastMul(r, x);
        }
        return r;
    }

    public static XorMatrix plus(XorMatrix a, XorMatrix b) {
        if (a.n != b.n || a.m != b.m) {
            throw new IllegalArgumentException();
        }
        XorMatrix ans = new XorMatrix(a.n, a.m);
        for (int i = 0; i < a.n; i++) {
            ans.mat[i].copy(a.mat[i]);
            ans.mat[i].xor(b.mat[i]);
        }
        return ans;
    }

    public static XorMatrix subtract(XorMatrix a, XorMatrix b) {
        return plus(a, b);
    }

    public static XorMatrix transpose(XorMatrix x) {
        int n = x.n;
        int m = x.m;
        XorMatrix t = new XorMatrix(m, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                t.mat[j].set(i, x.mat[i].get(j));
            }
        }
        return t;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                builder.append(mat[i].get(j) ? 1 : 0).append(' ');
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    public void asSame(XorMatrix matrix) {
        if (matrix.n != n || matrix.m != m) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < n; i++) {
            mat[i].copy(matrix.mat[i]);
        }
    }
}
