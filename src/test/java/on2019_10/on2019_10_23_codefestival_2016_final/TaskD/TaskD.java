package on2019_10.on2019_10_23_codefestival_2016_final.TaskD;



import java.util.HashMap;
import java.util.Map;

import template.FastInput;
import template.FastOutput;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Map<Integer, Integer> cntMap = new HashMap<>(n);
        int[] cnts = new int[m];
        int[] pairs = new int[m];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            cntMap.put(x, cntMap.getOrDefault(x, 0) + 1);
        }

        long ans = 0;
        for (Map.Entry<Integer, Integer> kv : cntMap.entrySet()) {
            int k = kv.getKey();
            int v = kv.getValue();
            cnts[k % m] += v % 2;
            pairs[k % m] += v / 2;
            ans += v / 2;
        }

        ans += cnts[0] / 2;
        for (int i = 1; i < m; i++) {
            int j = m - i;
            if (j == i) {
                ans += cnts[i] / 2;
                cnts[i] %= 2;
                continue;
            }
            int match = Math.min(cnts[i], cnts[j]);
            ans += match;
            cnts[i] -= match;
            cnts[j] -= match;
            if (cnts[i] == 0) {
                continue;
            }
            match = Math.min(cnts[i], pairs[j] * 2);
            if (match % 2 == 1) {
                match--;
            }
            ans = ans + match / 2;
            cnts[i] -= match;
            pairs[j] -= match / 2;
        }

        out.println(ans);
    }
}
