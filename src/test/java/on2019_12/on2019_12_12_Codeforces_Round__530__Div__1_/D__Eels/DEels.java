package on2019_12.on2019_12_12_Codeforces_Round__530__Div__1_.D__Eels;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Log2;

import java.util.TreeMap;
import java.util.TreeSet;

public class DEels {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Log2 log2 = new Log2();

        int q = in.readInt();

        int limit = 30;
        TreeMap<Integer, Integer>[] maps = new TreeMap[limit];
        long[] sums = new long[limit];
        for (int i = 0; i < limit; i++) {
            maps[i] = new TreeMap<>();
        }

        int total = 0;
        for (int i = 0; i < q; i++) {
            char c = in.readChar();
            int x = in.readInt();
            int l = log2.floorLog(x);
            int cnt = maps[l].getOrDefault(x, 0);

            if (c == '+') {
                cnt++;
                sums[l] += x;
                total++;
            } else {
                cnt--;
                sums[l] -= x;
                total--;
            }

            if (cnt != 0) {
                maps[l].put(x, cnt);
            } else {
                maps[l].remove(x);
            }

            long prefix = 0;
            int ans = total;
            for (int j = 0; j < limit && prefix < (1 << limit); j++) {
                if (maps[j].isEmpty()) {
                    continue;
                }
                if (maps[j].firstKey() > 2 * prefix) {
                    ans--;
                }
                prefix += sums[j];
            }

            out.println(ans);
        }
    }
}
