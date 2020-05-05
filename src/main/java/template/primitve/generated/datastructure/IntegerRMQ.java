package template.primitve.generated.datastructure;


import template.binary.Log2;

/**
 * O(n) space and pre-compute, O(logn) for query min element in interval
 */
public class IntegerRMQ {
    public static final int NIL = Integer.MIN_VALUE;
    int[] data;
    int[] vals;
    IntegerComparator comp;
    int n;


    private int left(int i) {
        return i << 1;
    }

    private int right(int i) {
        return (i << 1) | 1;
    }

    public IntegerRMQ(int[] vals, IntegerComparator comp) {
        this.comp = comp;
        n = vals.length;
        n = 1 << Log2.ceilLog(n);
        this.vals = vals;
        data = new int[2 * n];
        build(vals, 0, n - 1, 1);
    }

    private int merge(int a, int b) {
        if(a == NIL || b == NIL){
            return a == NIL ? b : a;
        }
        return comp.compare(vals[a], vals[b]) < 0 ? a : b;
    }

    private void build(int[] vals, int l, int r, int i) {
        if (l < r) {
            int m = (l + r) >> 1;
            build(vals, l, m, left(i));
            build(vals, m + 1, r, right(i));
            data[i] = merge(data[left(i)], data[right(i)]);
        } else {
            data[i] = l >= vals.length ? NIL : l;
        }
    }

    /**
     * Query the index of minimum elements in interval [l,r]
     */
    public int query(int l, int r) {
        return query(l, r, 0, n - 1, 1);
    }

    private int query(int ll, int rr, int l, int r, int i) {
        if (ll > r || l > rr) {
            return NIL;
        }
        if (ll <= l && r <= rr) {
            return data[i];
        }
        int m = (l + r) >> 1;
        return merge(query(ll, rr, l, m, left(i)),
                query(ll, rr, m + 1, r, right(i)));
    }
}
