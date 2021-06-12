package on2021_06.on2021_06_11_Luogu.P4717___________________FMT_FWT_;



import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.ModGenericFastWalshHadamardTransform;

public class P4717FMTFWT {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int len = 1 << n;
        int[] a = in.ri(len);
        int[] b = in.ri(len);
        int[][] res = new int[][]{or(a, b), and(a, b), xor(a, b)};
        for (int[] line : res) {
            for (int x : line) {
                out.append(x).append(' ');
            }
            out.println();
        }
    }

    int mod = 998244353;
    ModGenericFastWalshHadamardTransform fwt = new ModGenericFastWalshHadamardTransform(2, mod);

    int[] or(int[] a, int[] b) {
        a = a.clone();
        b = b.clone();
        fwt.maxFWT(a, 0, a.length - 1);
        fwt.maxFWT(b, 0, b.length - 1);
        fwt.dotMul(a, b, a, 0, a.length - 1);
        fwt.maxIFWT(a, 0, a.length - 1);
        return a;
    }

    int[] and(int[] a, int[] b) {
        a = a.clone();
        b = b.clone();
        fwt.minFWT(a, 0, a.length - 1);
        fwt.minFWT(b, 0, b.length - 1);
        fwt.dotMul(a, b, a, 0, a.length - 1);
        fwt.minIFWT(a, 0, a.length - 1);
        return a;
    }

    int[][] duplicate(int[] a) {
        int[][] ans = new int[2][];
        ans[0] = a.clone();
        ans[1] = new int[a.length];
        return ans;
    }

    int[] xor(int[] a, int[] b) {
        int[][] da = duplicate(a);
        int[][] db = duplicate(b);
        fwt.addFWT(da, 0, a.length - 1);
        fwt.addFWT(db, 0, a.length - 1);
        fwt.dotMul(da, db, da, 0, a.length - 1);
        fwt.addIFWT(da, 0, a.length - 1);
        return da[0];
    }
}
