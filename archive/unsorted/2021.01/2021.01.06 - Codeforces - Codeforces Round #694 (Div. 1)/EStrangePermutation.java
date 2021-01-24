package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class EStrangePermutation {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int c = in.ri();
        int q = in.ri();
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.ri() - 1;
        }
        List<Perm> perms = new ArrayList<>(4 * n + 1);

        perms.add(new Perm(0, 0, p));
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j <= i + c && j < n; j++) {
                perms.add(new Perm(i, j, p));
            }
        }

        perms.sort((a, b) -> {
//            for(int i = 0; i < n; i++){
//                int delta = a.get(i) - b.get(i);
//                if(delta != 0){
//                    return delta;
//                }
//            }
//            return 0;
            int sign = 1;
            if (a.l > b.l) {
                Perm tmp = a;
                a = b;
                b = tmp;
                sign = -1;
            }
            for (int i = a.l; i <= a.r; i++) {
                int delta = a.get(i) - b.get(i);
                if (delta != 0) {
                    return delta * sign;
                }
            }

            for (int i = b.l; i <= b.r; i++) {
                int delta = a.get(i) - b.get(i);
                if (delta != 0) {
                    return delta * sign;
                }
            }
            return 0;
        });
        debug.debug("perms.size()", perms.size());
        debug.debug("perms", perms);
        for (int i = 0; i < q; i++) {
            int a = in.ri();
            int b = in.ri();
            if (b > perms.size()) {
                out.println(-1);
                continue;
            }
            Perm perm = perms.get(b - 1);
            out.println(perm.get(a - 1) + 1);
        }
    }

}

class Perm {
    int l;
    int r;
    int[] p;

    public Perm(int l, int r, int[] p) {
        this.l = l;
        this.r = r;
        this.p = p;
    }

    public int get(int i) {
        if (i < l || i > r) {
            return p[i];
        }
        return p[r - (i - l)];
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < p.length; i++) {
            b.append(get(i)).append(' ');
        }
        b.append('\n');
        return b.toString();
    }
}