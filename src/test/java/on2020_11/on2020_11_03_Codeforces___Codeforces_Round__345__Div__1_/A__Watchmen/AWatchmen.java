package on2020_11.on2020_11_03_Codeforces___Codeforces_Round__345__Div__1_.A__Watchmen;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.HashMap;
import java.util.Map;

public class AWatchmen {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Map<Integer, Integer> rowMap = new HashMap<>();
        Map<Integer, Integer> colMap = new HashMap<>();
        Map<Long, Integer> ptMap = new HashMap<>();

        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            inc(rowMap, y);
            inc(colMap, x);
            inc(ptMap, DigitUtils.asLong(x, y));
        }

        long ans = 0;
        for (int v : rowMap.values()) {
            ans += pair(v);
        }
        for (int v : colMap.values()) {
            ans += pair(v);
        }
        for (int v : ptMap.values()) {
            ans -= pair(v);
        }

        out.println(ans);
    }

    public long pair(long n) {
        return n * (n - 1) / 2;
    }

    public <T> void inc(Map<T, Integer> map, T x) {
        map.put(x, map.getOrDefault(x, 0) + 1);
    }
}
