package on2021_09.on2021_09_12_Codeforces___Codeforces_Global_Round_16.C__MAX_MEX_Cut;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class CMAXMEXCut {
    public int mex(int s) {
        int ans = 0;
        while (Bits.get(s, ans) == 1) {
            ans++;
        }
        return ans;
    }

    char[] high = new char[(int) 1e5];
    char[] low = new char[(int) 1e5];

    public int col(int i) {
        return (1 << high[i] - '0') | (1 << low[i] - '0');
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        in.rs(high);
        in.rs(low);
        int inf = (int) 1e9;
        int[] prev = new int[1 << 2];
        int[] next = new int[1 << 2];
        Arrays.fill(prev, -inf);
        prev[col(0)] = 0;
        for (int i = 1; i < n; i++) {
            int c = col(i);
            Arrays.fill(next, -inf);
            for (int j = 0; j < 1 << 2; j++) {
                next[j | c] = Math.max(next[j | c], prev[j]);
                next[c] = Math.max(next[c], prev[j] + mex(j));
            }
            int[] tmp = prev;
            prev = next;
            next = tmp;
        }
        int ans = -inf;
        for(int i = 0; i < 1 << 2; i++){
            ans = Math.max(ans, mex(i) + prev[i]);
        }
        out.println(ans);
    }
}
