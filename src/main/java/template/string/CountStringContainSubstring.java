package template.string;

import template.math.DigitUtils;
import template.math.FastPow2;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given charset C and a substring T, find how many string with length n contain T as substring in
 * O(2n\log_2 T) time and memory complexity. It's efficient for large T
 * <br>
 * Of course there are some algorithm run in O(T^3\log_2n) with binary lifting
 */
public class CountStringContainSubstring {
    KMPAutomaton kam;
    long[][][] borderPresum;
    long[][] presum;
    int maxN;
    IntegerArrayList offset;
    List<int[]> seqInfoList = new ArrayList<>(60);

    public CountStringContainSubstring(int maxN) {
        this.maxN = maxN;
        borderPresum = new long[2][128][];
        presum = new long[2][maxN];
        kam = new KMPAutomaton(maxN);
        offset = new IntegerArrayList(maxN);
    }

    private void prepare(int B, int m) {
        for (int i = 0; i < B; i++) {
            for (int j = 0; j < 2; j++) {
                if (borderPresum[j][i] == null) {
                    borderPresum[j][i] = new long[maxN];
                } else {
                    Arrays.fill(borderPresum[j][i], 0, m, 0);
                }
            }
        }
    }

    public int solve(int C, int n, IntSequence T, int mod, FastPow2 powC) {
        assert C >= 0;
        assert n <= maxN;
        int tl = T.length();
        if (tl == 0) {
            return powC.pow(n);
        }
        if (tl > n) {
            return 0;
        }
        kam.init();
        for (int i = 0; i < tl; i++) {
            kam.build(T.get(i));
        }
        offset.clear();
        int cur = tl;
        while (cur != 0) {
            cur = kam.maxBorder(cur - 1);
            if (cur != 0) {
                offset.add(tl - cur);
            }
        }
        seqInfoList.clear();
        while (!offset.isEmpty()) {
            int r = offset.pop();
            int delta = 0;
            int l = r;
            while (!offset.isEmpty()) {
                int back = offset.tail();
                if (delta == 0) {
                    delta = r - back;
                }
                if (l - back != delta) {
                    break;
                }
                l = back;
                offset.pop();
            }
            seqInfoList.add(new int[]{l, r, delta});
        }
        int[][] blocks = seqInfoList.toArray(new int[0][]);
        int B = blocks.length;

        assert B <= 60;
        //prepare
        prepare(B, n);
        for (int i = 0; i < 2; i++) {
            Arrays.fill(presum[i], 0, n, 0);
        }

        //odd for plus, even for sub
        long ans = 0;
        presum[1][0] = 1;
        for (int i = 0; i < n; i++) {
            //push presum
            for (int j = 0; j < 2; j++) {
                long way = 0;
                presum[j][i] %= mod;
                if (i + 1 < n) {
                    presum[j][i + 1] += C * presum[j][i] % mod;
                }
                way += presum[j][i];
                for (int k = 0; k < B; k++) {
                    borderPresum[j][k][i] %= mod;
                    way += borderPresum[j][k][i];
                    if (i + blocks[k][2] < n && blocks[k][2] > 0) {
                        borderPresum[j][k][i + blocks[k][2]] += borderPresum[j][k][i];
                    }
                }
                way %= mod;
                if(way == 0){
                    continue;
                }
                //way 1, no overlap
                if (i + tl < n) {
                    presum[j ^ 1][i + tl] += way;
                }
                //have overlap, then only log boarders
                for (int k = 0; k < B; k++) {
                    int l = blocks[k][0];
                    int r = blocks[k][1] + blocks[k][2];
                    if (i + l < n) {
                        borderPresum[j ^ 1][k][i + l] += way;
                        if (l < r && i + r < n) {
                            borderPresum[j ^ 1][k][i + r] -= way;
                        }
                    }
                }

                //or no next occurrence, just count it
                if (i + tl <= n) {
                    //ok
                    long contrib = way * powC.pow(n - (i + tl)) % mod;
                    if ((j & 1) == 0) {
                        contrib = -contrib;
                    }
                    ans += contrib;
                }
            }
        }

        ans = DigitUtils.mod(ans, mod);
        return (int) ans;
    }
}
