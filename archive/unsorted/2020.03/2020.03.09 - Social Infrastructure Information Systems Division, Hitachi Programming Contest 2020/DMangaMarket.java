package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DMangaMarket {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long t = in.readInt();
        List<Store> nonZero = new ArrayList<>(n);
        List<Store> zero = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            int a = in.readInt();
            int b = in.readInt();
            Store store = new Store();
            store.a = a;
            store.b = b;
            if (a == 0) {
                zero.add(store);
            } else {
                nonZero.add(store);
            }
        }

        zero.sort((a, b) -> Long.compare(a.b, b.b));
        nonZero.sort((a, b) -> Long.compare((a.b + 1) * b.a, (b.b + 1) * a.a));
        int m = nonZero.size();
        long[][] dp = new long[m + 1][30];
        long inf = (long) 1e18;
        SequenceUtils.deepFill(dp, inf);
        dp[0][0] = 0;
        for (int i = 1; i <= m; i++) {
            Store s = nonZero.get(i - 1);
            for (int j = 0; j < 30; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j > 0) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - 1] + 1 + DigitUtils.mul(dp[i - 1][j - 1] + 1, s.a, inf, inf) + s.b);
                }
            }
        }

        long sum = 0;
        TreeMap<Long, Integer> map = new TreeMap<>();
        for (int i = 0; i < zero.size(); i++) {
            Store s = zero.get(i);
            sum += s.b + 1;
            map.put(sum, i + 1);
        }

        long ans = 0;
        for (int i = 0; i < 30; i++) {
            if (dp[m][i] > t) {
                continue;
            }
            Map.Entry<Long, Integer> floor = map.floorEntry(t - dp[m][i]);
            ans = Math.max(ans, i + (floor == null ? 0 : floor.getValue()));
        }

        out.println(ans);
    }
}

class Store {
    long a;
    long b;
}
