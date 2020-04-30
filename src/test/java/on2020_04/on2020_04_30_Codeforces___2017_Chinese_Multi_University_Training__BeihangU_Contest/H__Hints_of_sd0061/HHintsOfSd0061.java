package on2020_04.on2020_04_30_Codeforces___2017_Chinese_Multi_University_Training__BeihangU_Contest.H__Hints_of_sd0061;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CompareUtils;

public class HHintsOfSd0061 {
    int[] ratings = new int[10000000];
    int x, y, z;

    int rng61() {
        int t;
        x = x ^ (x << 16);
        x = x ^ (x >>> 5);
        x = x ^ (x << 1);
        t = x;
        x = y;
        y = z;
        z = (t ^ x) ^ y;
        return z;
    }

    {
        CompareUtils.ensureIntSpace(10000000);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        if (!in.hasMore()) {
            throw new UnknownError();
        }
        out.printf("Case #%d: ", testNumber);
        int n = in.readInt();
        int m = in.readInt();
        x = (int) in.readLong();
        y = (int) in.readLong();
        z = (int) in.readLong();

        for (int i = 0; i < n; i++) {
            ratings[i] = rng61();
        }
        CompareUtils.radixSort(ratings, 0, n - 1);
        for (int i = 0; i < m; i++) {
            int x = in.readInt();
            long val = (long) ratings[x] & ((1L << 32) - 1);
            out.append(val).append(' ');
        }
        out.println();
    }
}
