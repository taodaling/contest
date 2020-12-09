package platform.pe;

import framework.io.FileUtils;
import net.egork.chelper.tester.StringInputStream;
import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.math.LongMillerRabin;
import template.math.LongRadix;
import template.math.MillerRabin;
import template.math.PermutationUtils;
import template.utils.Debug;

import java.io.File;
import java.io.StringReader;
import java.util.*;
import java.util.stream.IntStream;

public class Solution {
    Debug debug = new Debug(true);

    public long get(long i) {
        return i * (3 * i - 1) / 2;
    }

    long[] cand;

    public boolean is(long x) {
        return Arrays.binarySearch(cand, x) >= 0;
    }

    public void solve() {
        //String s = FileUtils.readFile(new File("C:\\Users\\dalt\\Downloads"), "p042_words.txt");
        //FastInput in = new FastInput(new StringInputStream(s.replaceAll("[^A-Z]", " ")));

        cand = new long[1000000];
        for (int i = 0; i < cand.length; i++) {
            cand[i] = get(i + 1);
        }
        long ans = (long)1e18;
        for (int i = 0; i < cand.length; i++) {
            for (int j = i - 1; j >= 0 && cand[i] - cand[j] < ans; j--) {
                if (is(cand[i] + cand[j]) && is(cand[i] - cand[j])) {
                    ans = Math.min(ans, cand[i] - cand[j]);
                }
            }
        }
        debug.debug("ans", ans);
    }

    public static void main(String[] args) {
        new Solution().solve();
    }
}
