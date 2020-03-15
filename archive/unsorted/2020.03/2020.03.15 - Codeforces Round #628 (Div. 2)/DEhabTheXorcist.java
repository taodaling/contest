package contest;

import template.algo.IntBinarySearch;
import template.algo.LongBinarySearch;
import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;

import java.util.Arrays;

public class DEhabTheXorcist {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long u = in.readLong();
        long v = in.readLong();


        boolean[] even = new boolean[60];
        for (int i = 0; i < 60; i++) {
            even[i] = Bits.bitAt(u, i) == 0;
        }
        int[] used = new int[60];
        IntBinarySearch ibs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                long remain = v;
                Arrays.fill(used, 0);
                for (int i = 0; i < 60; i++) {
                    if (!even[i]) {
                        if (mid == 0) {
                            return false;
                        }
                        remain -= (1L << i);
                    }
                }
                if (remain < 0) {
                    return false;
                }
                for (int i = 60 - 1; i >= 0; i--) {
                    int x = mid;
                    if (!even[i]) {
                        x--;
                    }
                    long unit = 1L << (i + 1);
                    int cnt = x / 2;
                    used[i] = (int) Math.min(cnt, remain / unit) * 2;
                    remain -= used[i] / 2 * unit;
                    if (!even[i]) {
                        used[i]++;
                    }
                }
                return remain == 0;
            }
        };

        int ans = ibs.binarySearch(0, (int) 1e9);
        if (!ibs.check(ans)) {
            out.println(-1);
            return;
        }

        debug.debug("used", used);
        out.println(ans);
        long[] seq = new long[ans];
        for (int i = 0; i < 60; i++) {
            for (int j = 0; j < used[i]; j++) {
                seq[j] = Bits.setBit(seq[j], i, true);
            }
        }

        for (long s : seq) {
            out.append(s).append(' ');
        }
        out.println();
    }
}
