package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Radix;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.LongList;
import template.utils.Debug;

import java.util.Arrays;

public class CEverHungryKrakozyabra {
    Radix radix = new Radix(10);
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long l = in.readLong();
        long r = in.readLong();
        lBits = radix.decompose(l);
        rBits = radix.decompose(r);

        dfs(1, 0, 0);

        int ans = 0;
        int[] cnt = new int[10];
        debug.debug("m", all.size());
        //all.unique();
        for (int i = all.size() - 1; i >= 0; i--) {
            long val = all.get(i);
            int sum = 0;

            if (val >= l && val <= r) {
                ans++;
                continue;
            }

            while (val > 0) {
                cnt[(int) (val % 10)]++;
                val /= 10;
                sum++;
            }
            cnt[0] = (int) 1e9;
            if (test(cnt, sum, 18, true, true)) {
                ans++;
                // debug.debug("val", all.get(i));
            } else {
                Arrays.fill(cnt, 0);
            }
        }

        out.println(ans);
    }

    int[] lBits;
    int[] rBits;

    public boolean test(int[] cnts, int sum, int i, boolean ceil, boolean floor) {
        if (sum > i + 1) {
            return false;
        }
        if (i < 0 || !ceil && !floor) {
            return true;
        }

        int l = lBits[i];
        int r = rBits[i];
        int start = floor ? l : 0;
        int end = ceil ? r : 9;
        for (int j = start; j <= end; j++) {
            if (cnts[j] == 0) {
                continue;
            }
            cnts[j]--;
            if (test(cnts, sum - (j > 0 ? 1 : 0), i - 1, ceil && j == r, floor && j == l)) {
                return true;
            }
            cnts[j]++;
        }

        return false;
    }

    LongList all = new LongList(5000000);

    public void dfs(int val, int cnt, long built) {
        if (val == 10) {
            all.add(built);
            return;
        }
        for (; cnt <= 18; cnt++, built = built * 10 + val) {
            dfs(val + 1, cnt, built);
        }
    }
}
