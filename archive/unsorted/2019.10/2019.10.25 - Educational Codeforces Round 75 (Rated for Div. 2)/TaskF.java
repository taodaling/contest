package contest;


import template.*;

import java.util.Arrays;
import java.util.Map;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();

        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int[] b = new int[k];
        for (int i = 0; i < k; i++) {
            b[i] = in.readInt();
        }

        int limit = 300000;
        int[] cnts = new int[limit + 1];
        for (int i = 0; i < n; i++) {
            cnts[a[i]]++;
        }

        int[] dbCnt = new int[k];
        int[] sgCnt = new int[k];
        for (int i = 0; i < k; i++) {
            int db = 0;
            int sg = 0;
            for (int j = 0; j < b[i]; j++) {
                if (cnts[j] >= 2) {
                    db++;
                } else if (cnts[j] == 1) {
                    sg++;
                }
            }
            dbCnt[i] = db;
            sgCnt[i] = sg;
        }

        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
        NumberTheory.Composite comp = new NumberTheory.Composite(n, mod);
        DigitUtils.Log2 log2 = new DigitUtils.Log2();
        int proper = log2.ceilLog(n + 1) + 1;
        int[][] ways = new int[k][];
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod, 3);
        int[] rev = new int[1 << proper];
        ntt.prepareReverse(rev, proper);
        for (int i = 0; i < k; i++) {
            int[] buf1 = new int[1 << proper];
            int[] buf2 = new int[1 << proper];
            int sg = sgCnt[i];
            int db = dbCnt[i];
            int p2 = 1;
            for (int j = 0; j <= sg || j <= db; j++) {
                buf1[j] = mod.mul(comp.composite(sg, j), p2);
                buf2[j] = comp.composite(2 * db, j);
                p2 = mod.mul(p2, 2);
            }
            ntt.dft(rev, buf1, proper);
            ntt.dft(rev, buf2, proper);
            ntt.dotMul(buf1, buf2, buf1, proper);
            ntt.idft(rev, buf1, proper);
            ways[i] = buf1;
        }

        int q = in.readInt();
        for(int i = 0; i < q; i++){
            int qi = in.readInt() / 2 - 1;
            int ans = 0;
            for(int j = 0; j < k; j++){
                int num = qi - b[j];
                if(num < 0 || num >= ways[j].length){
                    continue;
                }
                ans = mod.plus(ans, ways[j][num]);
            }

            out.println(ans);
        }
    }
}
