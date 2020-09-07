package contest;

import template.binary.Bits;
import template.math.DigitUtils;
import template.math.IntRadix;
import template.polynomial.FastWalshHadamardTransform;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BasePlacement {
    List<Integer> ways = new ArrayList<>();
    IntRadix radix = new IntRadix(3);

    public void dfs(int i, int n, int val, int lastTwo) {
        if (i == n) {
            ways.add(val);
            return;
        }
        if (lastTwo == 0) {
            dfs(i + 1, n, radix.set(val, i, 1), ((lastTwo << 1) | 1) & 3);
            dfs(i + 1, n, radix.set(val, i, 2), ((lastTwo << 1) | 1) & 3);
        }
        dfs(i + 1, n, val, (lastTwo << 1) & 3);
    }

    public String toString(int x) {
        if (x == 0) {
            return "";
        }
        return toString(x / 3) + (x % 3);
    }

    Debug debug = new Debug(false);
    public int count(int R, int C, int B) {
        if (R < C) {
            int tmp = R;
            R = C;
            C = tmp;
        }
        int mod = (int) (1e9 + 7);
        int n = DigitUtils.ceilDiv(R * C, 2);

        int limit = radix.set(0, C, 1);
        for(int i = 0; i < limit; i++){
            boolean valid = true;
            for(int j = 0; j < C; j++){
                int cnt = 0;
                if(j > 0 && radix.get(i, j - 1) > 0){
                    cnt++;
                }
                if(radix.get(i, j) > 0){
                    cnt++;
                }
                if(radix.get(i, j + 1) > 0){
                    cnt++;
                }
                if(cnt > 1){
                    valid = false;
                }
            }
            if(valid){
                ways.add(i);
            }
        }
        //dfs(0, C, 0, 0);
        int[] allPossible = ways.stream().mapToInt(Integer::intValue).toArray();

        debug.debug("allPossible", allPossible);
        int m = allPossible.length;
        int[] count2 = new int[m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < C; j++) {
                if (radix.get(allPossible[i], j) == 2) {
                    count2[i]++;
                }
            }
        }
        List<Integer>[] transfer = new List[m];
        for (int i = 0; i < m; i++) {
            transfer[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                int a = allPossible[i];
                int b = allPossible[j];
                boolean valid = true;
                for (int k = 0; k < C; k++) {
                    int ak = radix.get(a, k);
                    int bk = radix.get(b, k);
                    if (ak == 2 && bk != 1) {
                        valid = false;
                    }
                    if (ak == 1 && bk != 0) {
                        valid = false;
                    }
                    if(bk == 1 && ak != 2){
                        valid = false;
                    }
                }
                if (valid) {
                    transfer[i].add(j);
                }
            }
        }
        int[][] transferArray = new int[m][];
        for (int i = 0; i < m; i++) {
            transferArray[i] = transfer[i].stream().mapToInt(Integer::intValue).toArray();
        }

        long[][] last = new long[n + 1][m];
        long[][] next = new long[n + 1][m];
        for(int i = 0; i < m; i++){
            if(allPossible[i] == 0){
                last[0][i]++;
            }
        }
        for (int i = 0; i < R; i++) {
            SequenceUtils.deepFill(next, 0L);

            for (int j = 0; j <= n; j++) {
                for (int k = 0; k < m; k++) {
                    long w = last[j][k];
                    if (w == 0) {
                        continue;
                    }
                    for (int x : transferArray[k]) {
                        int t = count2[x] + j;
                        if (t > n) {
                            continue;
                        }
                        next[t][x] += w;
                    }
                }
            }

            for (int j = 0; j <= n; j++) {
                for (int k = 0; k < m; k++) {
                    next[j][k] %= mod;
                }
            }

            long[][] tmp = last;
            last = next;
            next = tmp;

            debug.debug("last", last);
        }

        long ans = 0;
        for (int i = B; i < last.length; i++) {
            for (int j = 0; j < m; j++) {
                ans += last[i][j];
            }
        }

        return (int) (ans % mod);
    }
}
