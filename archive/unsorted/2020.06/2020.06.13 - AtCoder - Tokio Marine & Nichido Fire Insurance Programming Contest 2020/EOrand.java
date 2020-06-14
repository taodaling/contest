package contest;

import template.algo.SubsetGenerator;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class EOrand {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        k = in.readInt();
        int s = in.readInt();
        int t = in.readInt();
        IntegerList list = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            if ((x & s) != s || (x | t) != t) {
                continue;
            }
            list.add(x);
        }

        a = list.toArray();
        n = a.length;

        SequenceUtils.deepFill(comb, -1L);

        int[] clone = a.clone();
        int mask = (1 << 18) - 1;

        long ans = 0;
        for (int i = 0; i < n; i++) {
            int reqOne = t - clone[i];
            int reqZero = (clone[i]) & (mask ^ s);
            int req = reqOne | reqZero;

            a = Arrays.copyOfRange(clone, i + 1, clone.length);
            for (int j = 0; j < a.length; j++) {
                a[j] = (a[j] & reqOne) | (~a[j] & reqZero);
            }

            long local = 0;
            SubsetGenerator sg = new SubsetGenerator();
            sg.reset(req);

            while (sg.hasNext()) {
                int next = sg.next();
                local += ie(next);
            }

            ans += local;
            debug.debug("local", local);
        }

        out.println(ans);
    }

    Debug debug = new Debug(false);


    int k;
    long[][] comb = new long[100][100];
    int[] a;

    public long ie(int set) {
        int cnt = 0;
        for (int x : a) {
            if ((x & set) == 0) {
                cnt++;
            }
        }
        long ans = 0;
        for (int j = 0, min = Math.min(k - 1, cnt); j <= min; j++) {
            ans += comb(cnt, j);
        }
        if (Integer.bitCount(set) % 2 == 1) {
            ans = -ans;
        }
        return ans;
    }

    public long comb(int n, int m) {
        if (comb[n][m] == -1) {
            if (m > n) {
                return comb[n][m] = 0;
            }
            if (m == 0) {
                return comb[n][m] = 1;
            }
            comb[n][m] = comb(n - 1, m) + comb(n - 1, m - 1);
        }
        return comb[n][m];
    }
}
