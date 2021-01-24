package platform.pe;

import template.binary.Bits;
import template.math.BigCombination;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.math.BigInteger;
import java.util.*;

public class Solution {
    Debug debug = new Debug(true);

    public int extract(String s, int mask) {
        int ans = 0;
        int val = -1;
        for (int i = 0; i < s.length(); i++) {
            int v = s.charAt(i) - '0';
            if (Bits.get(mask, i) == 1) {
                if (val == -1) {
                    val = v;
                }
                if (val != v) {
                    return -1;
                }
            } else {
                ans = ans * 10 + v;
            }
        }
        return ans;
    }

    public int[] decompose(int x) {
        int[] ans = new int[10];
        while (x != 0) {
            ans[x % 10]++;
            x /= 10;
        }
        return ans;
    }

    int[][] comb;
    int limit = (int) 1e8;

    public int comb(int n, int m) {
        if (n < m) {
            return 0;
        }
        if (comb[n][m] == -1) {
            if (m == 0) {
                return comb[n][m] = 1;
            }
            comb[n][m] = comb(n - 1, m - 1) + comb(n - 1, m);
            comb[n][m] = Math.min(comb[n][m], limit);
        }
        return comb[n][m];
    }

    public void solve() {
        //String s = FileUtils.readFile(new File("C:\\Users\\dalt\\Downloads"), "p042_words.txt");
        //FastInput in = new FastInput(new StringInputStream(s.replaceAll("[^A-Z]", " ")));
        int ans = 0;
        comb = new int[101][101];
        SequenceUtils.deepFill(comb, -1);
        for (int i = 1; i <= 100; i++) {
            for (int j = 0; j <= i; j++) {
                if (comb(i, j) > 1e6) {
                    ans++;
                }
            }
        }
        System.out.println(ans);
    }

    public static void main(String[] args) {
        new Solution().solve();
    }
}
