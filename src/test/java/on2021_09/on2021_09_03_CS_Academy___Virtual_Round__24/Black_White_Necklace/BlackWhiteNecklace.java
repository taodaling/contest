package on2021_09.on2021_09_03_CS_Academy___Virtual_Round__24.Black_White_Necklace;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerPreSum;

public class BlackWhiteNecklace {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] s = new int[n * 2];
        int[] cnt = new int[2];
        for (int i = 0; i < n; i++) {
            s[i] = s[i + n] = in.ri();
            cnt[s[i]]++;
        }
        if (cnt[0] == 0 || cnt[1] == 0) {
            out.println(0);
            return;
        }
        long inf = (long) 1e18;
        long best = inf;
        IntegerPreSum ps = new IntegerPreSum(i -> s[i], n * 2);
        for (int i = 0; i < n; i++) {
            int l = i;
            int r = cnt[1] + l - 1;
            int total = ps.intervalSum(l, r);
            int req = cnt[1] - total;
            best = Math.min(best, req);
        }
        out.println(best);
    }
}
