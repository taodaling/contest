package contest;

import template.datastructure.GenericSegment;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class P33732 {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int p = in.readInt();
        Summary.mod = p;

        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = in.readInt();
        }
        GenericSegment<Summary, Modify> seg = new GenericSegment<>(1, n,
                Modify::new, Summary::new, i -> {
            Summary s = new Summary();
            s.sum = data[i - 1];
            s.size = 1;
            return s;
        });

       // seg.toString();

        debug.debug("seg", seg);
        for (int i = 0; i < m; i++) {
            int t = in.readInt();
            int l = in.readInt();
            int r = in.readInt();
            if (t == 1) {
                Modify mod = new Modify();
                mod.mul = in.readInt();
                seg.update(l, r, 1, n, mod);
//                for(int j = l - 1; j <= r - 1; j++){
//                    data[j] = (int) ((long)data[j] * mod.mul % p);
//                }
            } else if (t == 2) {
                Modify mod = new Modify();
                mod.plus = in.readInt();
                seg.update(l, r, 1, n, mod);
//                for(int j = l - 1; j <= r - 1; j++){
//                    data[j] = (int) (data[j] + mod.plus) % p;
//                }
            } else {
                Summary sum = new Summary();
                seg.query(l, r, 1, n, sum);
                out.println(sum.sum);
            }

//            Summary sum = new Summary();
//            seg.query(3, 4, 1, n, sum);
//            debug.debug("sum", sum);
//            debug.debug("seg", seg);
        }

        return;
    }
}

class Modify implements GenericSegment.Modify<Summary, Modify> {
    long mul = 1;
    long plus = 0;

    @Override
    public void modify(Summary summary) {
        summary.sum = (int) ((summary.sum * mul + summary.size * plus) % Summary.mod);
    }

    @Override
    public void merge(Modify modify) {
        mul = mul * modify.mul % Summary.mod;
        plus = (plus * modify.mul + modify.plus) % Summary.mod;
    }

    @Override
    public void clear() {
        mul = 1;
        plus = 0;
    }

    @Override
    public Modify clone() {
        Modify ans = new Modify();
        ans.mul = mul;
        ans.plus = plus;
        return ans;
    }
}

class Summary implements GenericSegment.Summary<Summary> {
    static int mod;
    int sum;
    int size;

    @Override
    public void merge(Summary a) {
        sum = (sum + a.sum) % mod;
        size += a.size;
    }

    @Override
    public void merge(Summary a, Summary b) {
        sum = (a.sum + b.sum) % mod;
        size = (a.size + b.size) % mod;
    }

    @Override
    public Summary clone() {
        Summary ans = new Summary();
        ans.sum = sum;
        ans.size = size;
        return ans;
    }

    @Override
    public String toString() {
        return "" + sum;
    }

}
