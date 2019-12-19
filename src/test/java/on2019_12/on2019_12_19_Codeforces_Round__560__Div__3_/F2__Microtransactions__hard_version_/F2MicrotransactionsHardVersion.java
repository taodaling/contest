package on2019_12.on2019_12_19_Codeforces_Round__560__Div__3_.F2__Microtransactions__hard_version_;



import template.algo.IntBinarySearch;
import template.algo.LongBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class F2MicrotransactionsHardVersion {
    long inf = (long) 1e18;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] k = new int[n];
        int[] remains = new int[n];
        for (int i = 0; i < n; i++) {
            k[i] = in.readInt();
        }

        Discount[] discounts = new Discount[m];
        for (int j = 0; j < m; j++) {
            discounts[j] = new Discount();
            discounts[j].d = in.readInt();
            discounts[j].t = in.readInt() - 1;
        }

        Arrays.sort(discounts, (a, b) -> a.d - b.d);
        long[] lastDay = new long[n];
        Arrays.fill(lastDay, inf);
        for (int i = m - 1; i >= 0; i--) {
            discounts[i].nextDay = lastDay[discounts[i].t];
            lastDay[discounts[i].t] = discounts[i].d;
        }

        LongBinarySearch bs = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                long used = 0;
                System.arraycopy(k, 0, remains, 0, n);
                for (int i = 0; i < m && discounts[i].d <= mid; i++) {
                    if (discounts[i].nextDay <= mid) {
                        continue;
                    }
                    int consume = (int) Math.min(discounts[i].d - used, remains[discounts[i].t]);
                    used += consume;
                    remains[discounts[i].t] -= consume;
                }
                for (int i = 0; i < n; i++) {
                    used += 2 * remains[i];
                }
                return used <= mid;
            }
        };

        long ans = bs.binarySearch(0, (long) 1e11);
        out.println(ans);
    }
}

class Discount {
    int d;
    int t;
    long nextDay;
}