package contest;

import com.sun.xml.internal.bind.v2.util.StackRecorder;
import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.stream.IntStream;

public class GOmkarAndPies {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        k = in.readInt();
        int src = 0;
        int dst = 0;
        for (int i = 0; i < k; i++) {
            src |= (in.readChar() - '0') << i;
        }
        for (int i = 0; i < k; i++) {
            dst |= (in.readChar() - '0') << i;
        }
        int[][] pairs = new int[2][n];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < 2; j++) {
                pairs[j][i] = in.readInt() - 1;
            }
        }

        int[] a = new int[n];
        int[] b = new int[n];
        int[] perm = IntStream.range(0, k).toArray();
        int[] invPerm = perm.clone();
        for (int i = 0; i < n; i++) {
            swap(perm, pairs[0][i], pairs[1][i]);
            inv(perm, invPerm);
            a[i] = perm(invPerm, src);
            if (i + 1 < n) {
                b[i + 1] = perm(invPerm, dst);
            }
            //a[i] = Bits.swap(i == 0 ? src : a[i - 1], pairs[0][i], pairs[1][i]);
        }
        b[0] = dst;
//        for (int i = 0; i < n - 1; i++) {
//            applyLeft(invPerm, pairs[0][i], pairs[1][i]);
//            b[i + 1] = perm(invPerm, dst);
//        }

        int[] L = new int[1 << k];
        int[] R = new int[1 << k];
        Arrays.fill(L, -1);
        Arrays.fill(R, -1);

        for (int i = 0; i < n; i++) {
            add(L, b[i], i);
            add(R, a[n - i - 1], n - i - 1);
        }
//        for (int i = 0; i < n; i++) {
//            debug.debug("a[" + i + "]", toString(k, a[i]));
//            debug.debug("b[" + i + "]", toString(k, b[i]));
//        }
//        for (int i = 0; i < n; i++) {
//            for (int j = i; j < n; j++) {
//                int cast = apply(pairs, i, j, src);
//                int common = Integer.bitCount(cast & dst);
//                int calc = Integer.bitCount(a[j] & b[i]);
//                if(common != calc){
//                    throw new RuntimeException();
//                }
//            }
//        }

        debug.debug("a", a);
        debug.debug("b", b);
        //debug.debug("L", L);
        //debug.debug("R", R);
        int ans = -1;
        int l = -1;
        int r = -1;
        for (int i = 0; i < 1 << k; i++) {
            if (L[i] == -1 || R[i] == -1) {
                continue;
            }
            if (R[i] - L[i] + 1 < m) {
                continue;
            }
            int bitcount = Integer.bitCount(i);
            if (ans < bitcount) {
                ans = bitcount;
                l = L[i];
                r = R[i];
            }
        }
        debug.debug("ans", ans);
        debug.debug("l", l);
        debug.debug("r", r);



        out.println(k - (Integer.bitCount(src) - ans) - (Integer.bitCount(dst) - ans));
        out.append(mirror(r, n) + 1).append(' ').append(mirror(l, n) + 1);
    }

    public String toString(int k, int x) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < k; i++, x /= 2) {
            sb.append(x % 2);
        }
        return sb.toString();
    }

    public int mirror(int i, int n) {
        return n - 1 - i;
    }

    int k;

    public int apply(int[][] pairs, int l, int r, int x) {
        for (int i = r; i >= l; i--) {
            x = Bits.swap(x, pairs[0][i], pairs[1][i]);
        }
        return x;
    }

    public int revApply(int[][] pairs, int l, int r, int x) {
        for (int i = l; i <= r; i++) {
            x = Bits.swap(x, pairs[0][i], pairs[1][i]);
        }
        return x;
    }

    public void add(int[] core, int x, int id) {
        if (core[x] != -1) {
            return;
        }
        core[x] = id;
        for (int i = 0; i < k; i++) {
            if (Bits.get(x, i) == 1) {
                add(core, Bits.clear(x, i), id);
            }
        }
    }

    public void swap(int[] perm, int a, int b) {
        SequenceUtils.swap(perm, a, b);
    }

    public void inv(int[] perm, int[] output) {
        for (int i = 0; i < perm.length; i++) {
            output[perm[i]] = i;
        }
    }

    public int perm(int[] p, int x) {
        int ans = 0;
        for (int i = 0; i < p.length; i++) {
            ans |= Bits.get(x, p[i]) << i;
        }
        return ans;
    }
}
