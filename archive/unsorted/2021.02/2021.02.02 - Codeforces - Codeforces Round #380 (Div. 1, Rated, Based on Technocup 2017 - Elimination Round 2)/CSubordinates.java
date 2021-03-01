package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerPreSum;

public class CSubordinates {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int s = in.ri() - 1;
        int[] a = new int[n];
        in.populate(a);
        int extra = 0;
        if (a[s] != 0) {
            extra = 1;
            a[s] = 0;
        }
        int spare = 0;
        for (int i = 0; i < n; i++) {
            if (i == s) {
                continue;
            }
            if (a[i] == 0) {
                spare++;
                extra++;
            }
        }
        int[] cnts = new int[n];
        for (int x : a) {
            cnts[x]++;
        }
        IntegerPreSum emptyPs = new IntegerPreSum(i -> cnts[i] == 0 ? 1 : 0, n);
        IntegerPreSum cntPs = new IntegerPreSum(i -> cnts[i], n);
        int ans = n;
        for (int i = 0; i < n; i++) {
            int empty = emptyPs.prefix(i);
            int cnt = cntPs.post(i + 1);
            empty = Math.max(0, empty - spare);
            int cost = Math.max(cnt, empty);
            ans = Math.min(ans, cost);
        }
        ans += extra;
        out.println(ans);
    }
}
