package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class RiolkusMockCCCS1WordBot {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int c = in.ri();
        int v = in.ri();
        int[] cnts = new int[2];
        boolean[] isV = new boolean[128];
        isV['a'] = isV['e'] = isV['i'] = isV['o'] = isV['u'] = true;
        int maxC = 0;
        int maxV = 0;
        for (int i = 0; i < n; i++) {
            char ch = in.rc();
            if (ch == 'y') {
                cnts[0]++;
                cnts[1]++;
            } else if (isV[ch]) {
                cnts[0]++;
                cnts[1] = 0;
            } else {
                cnts[1]++;
                cnts[0] = 0;
            }
            maxC = Math.max(maxC, cnts[1]);
            maxV = Math.max(maxV, cnts[0]);
        }
        if (maxV > v || maxC > c) {
            out.println("NO");
        } else {
            out.println("YES");
        }
    }
}
