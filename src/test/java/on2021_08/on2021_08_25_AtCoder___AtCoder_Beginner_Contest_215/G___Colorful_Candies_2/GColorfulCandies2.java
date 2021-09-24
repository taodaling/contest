package on2021_08.on2021_08_25_AtCoder___AtCoder_Beginner_Contest_215.G___Colorful_Candies_2;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GColorfulCandies2 {
    int mod = 998244353;
    Combination comb = new Combination((int) 1e5, mod);
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] c = in.ri(n);
        Map<Integer, Integer> cnt = new HashMap<>(n);
        for (int x : c) {
            cnt.put(x, cnt.getOrDefault(x, 0) + 1);
        }
        Map<Integer, Integer> group = new HashMap<>(n);
        for (int x : cnt.values()) {
            group.put(x, group.getOrDefault(x, 0) + 1);
        }
        debug.debug("cnt", cnt);
        debug.debug("group", group);
        List<int[]> pairList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : group.entrySet()) {
            pairList.add(new int[]{entry.getKey(), entry.getValue()});
        }
        int[][] pairs = pairList.toArray(new int[0][]);
        debug.debug("pairs", pairs);
        for (int k = 1; k <= n; k++) {
            long contrib = 0;
            for (int[] p : pairs) {
                contrib += comb.combination(n - p[0], k) * (long) p[1];
            }
            contrib = contrib % mod * comb.invCombination(n, k) % mod;
            long ans = cnt.size() - contrib;
            ans = DigitUtils.mod(ans, mod);
            out.println(ans);
        }
    }
}
