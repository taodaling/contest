package on2021_10.on2021_10_13_TopCoder_SRM__815.EllysKeys;



import template.utils.Debug;

import java.util.Arrays;

public class EllysKeys {
//    Debug debug = new Debug(true);
    public int getMax(String[] keys) {
        int n = keys.length;
        int[] sets = new int[n];
        for (int i = 0; i < n; i++) {
            sets[i] = parse(keys[i]);
        }
        int L = 20;
        int[] prev = new int[1 << L];
        int[] next = new int[1 << L];
        int inf = (int) 1e9;
        Arrays.fill(prev, -inf);
        prev[0] = 0;

        for (int s : sets) {
            Arrays.fill(next, -inf);
            for (int i = 0; i < 1 << L; i++) {
                next[i] = Math.max(next[i], prev[i]);
                if ((i & s) == 0) {
                    next[i | s] = Math.max(next[i | s], prev[i] + 1);
                }
            }
            int[] tmp = prev;
            prev = next;
            next = tmp;

//            debug.debug("s", s);
//            debug.debug("prev", prev);
        }
        int ans = Arrays.stream(prev).max().getAsInt();
        return ans;
    }

    public int parse(String s) {
        int ans = 0;
        for (char c : s.toCharArray()) {
            ans *= 2;
            if (c == '^') {
                ans += 1;
            }
        }
        return ans;
    }
}
