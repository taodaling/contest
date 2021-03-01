package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

public class BSeaBattle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int a = in.ri();
        int b = in.ri();
        int k = in.ri();
        char[] s = new char[n];
        in.rs(s, 0);
        for (int i = 0; i < n; i++) {
            s[i] -= '0';
        }
        int spare = 0;
        IntegerArrayList cand = new IntegerArrayList(n);
        for (int i = 0; i < n; i++) {
            if (s[i] == 1) {
                continue;
            }
            int l = i;
            int r = i;
            while (r + 1 < n && s[r + 1] == 0) {
                r++;
            }
            i = r;
            int len = r - l + 1;
            spare += len / b;
            for (int j = l + b - 1; j <= r; j += b) {
                cand.add(j);
            }
        }
        int ans = Math.max(0, spare - a) + 1;
        out.println(ans);
        for (int i = 0; i < ans; i++) {
            out.append(cand.get(i) + 1).append(' ');
        }
    }
}
