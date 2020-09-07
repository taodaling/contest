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

    public String toString(int x) {
        if (x == 0) {
            return "";
        }
        return toString(x / 3) + (x % 3);
    }


    public int count(int R, int C, int B) {
        if (R < C) {
            int tmp = R;
            R = C;
            C = tmp;
        }
        int mod = (int) (1e9 + 7);
        int n = DigitUtils.ceilDiv(R * C, 2);

        int limit = radix.set(0, C, 1);
        boolean[] possible = new boolean[limit];
        for (int i = 0; i < limit; i++) {
            boolean valid = true;
            for (int j = 0; j < C; j++) {
                //any two 2 should be far than 1
                int cnt1 = 0;
                if (j > 0 && radix.get(i, j - 1) == 2) {
                    cnt1++;
                }
                if (radix.get(i, j) > 0) {
                    cnt1++;
                }
                if (radix.get(i, j + 1) == 2) {
                    cnt1++;
                }
                int cnt2 = 0;
                if (j > 0 && radix.get(i, j - 1) == 1) {
                    cnt2++;
                }
                if (radix.get(i, j) == 1) {
                    cnt2++;
                }
                if (radix.get(i, j + 1) == 1) {
                    cnt2++;
                }

                if (cnt1 > 1 || cnt2 > 1) {
                    valid = false;
                }
            }
            if (valid) {
                ways.add(i);
                possible[i] = true;
            }
        }
        int[] allPossible = ways.stream().mapToInt(Integer::intValue).toArray();

        int m = allPossible.length;
        int[] mask1 = new int[m];
        int[] mask2 = new int[m];
        int[] count2 = new int[m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < C; j++) {
                if (radix.get(allPossible[i], j) == 2) {
                    mask2[i] |= 1 << j;
                    count2[i]++;
                }
                if (radix.get(allPossible[i], j) == 1) {
                    mask1[i] |= 1 << j;
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
                if (mask2[i] != mask1[j]) {
                    continue;
                }
                boolean valid = true;
                for (int k = 0; k < C; k++) {
                    int ak = radix.get(a, k);
                    int bk = radix.get(b, k);
                    if (ak == 1 && bk == 2) {
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
        for (int i = 0; i < m; i++) {
            if (allPossible[i] == 0) {
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
