package on2020_03.on2020_03_22_.UOJ020;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;

import java.util.Arrays;

public class UOJ020 {
    char[] buf = new char[100000];
    Debug debug = new Debug(false);

    int read(FastInput in, Modular mod) {
        int n = in.readString(buf, 0);
        int ans = 0;
        boolean sign = true;
        for (int i = 0; i < n; i++) {
            if (buf[i] == '-' || buf[i] == '+') {
                sign = buf[i] == '+';
                continue;
            }
            ans = mod.mul(ans, 10);
            ans = mod.plus(ans, buf[i] - '0');
        }
        if (!sign) {
            ans = mod.valueOf(-ans);
        }
        return ans;
    }

    public int apply(int[] seq, Modular mod, int x) {
        int ans = 0;
        for (int i = seq.length - 1; i >= 0; i--) {
            ans = mod.mul(ans, x);
            ans = mod.plus(ans, seq[i]);
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        Modular m1 = new Modular(1e9 + 7);

        int n = in.readInt();
        int m = in.readInt();
        int[] seq1 = new int[n + 1];

        for (int i = 0; i <= n; i++) {
            seq1[i] = read(in, m1);
        }
        debug.debug("seq1", seq1);


        IntegerList ans = new IntegerList(m);
        for (int i = 1; i <= m; i++) {
            if (apply(seq1, m1, i) == 0) {
                ans.add(i);
            }
        }

        out.println(ans.size());
        for (int i = 0; i < ans.size(); i++) {
            out.println(ans.get(i));
        }
    }
}
