package contest;

import com.sun.xml.internal.fastinfoset.util.PrefixArray;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SortUtils;
import template.utils.SequenceUtils;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Bolero {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Good[] goods = new Good[n];
        for (int i = 0; i < n; i++) {
            goods[i] = new Good();
            goods[i].p = in.readInt();
            goods[i].d = 100 - in.readInt();
        }

        int[] discounts = new int[100 + 1];
        Arrays.fill(discounts, n + 1);
        discounts[100] = 0;
        for (int i = 0; i < m; i++) {
            int k = in.readInt();
            int p = 100 - in.readInt();
            discounts[p] = Math.min(discounts[p], k);
        }

        long min = (long) 1e18;
        for (int i = 0; i <= 100; i++) {
            if (discounts[i] > n) {
                continue;
            }
            for (Good good : goods) {
                good.profit = (good.d - i) * good.p;
            }
            int l = 0;
            int r = n - 1;
            while (l <= r) {
                if (goods[r].profit < 0) {
                    r--;
                } else {
                    SequenceUtils.swap(goods, l, r);
                    l++;
                }
            }

            if (l < discounts[i]) {
                //need more
                int k = discounts[i] - l;
                SortUtils.theKthSmallestElement(goods, (a, b) -> -Integer.compare(a.profit, b.profit), l, n, k);
                l += k;
            }

            long sum = 0;
            for (int j = 0; j < n; j++) {
                if (j < l) {
                    sum += goods[j].p * i;
                } else {
                    sum += goods[j].p * goods[j].d;
                }
            }

            min = Math.min(sum, min);
        }

        out.println(min / 100D);
    }
}

class Good {
    int p;
    int d;
    int profit;
}
