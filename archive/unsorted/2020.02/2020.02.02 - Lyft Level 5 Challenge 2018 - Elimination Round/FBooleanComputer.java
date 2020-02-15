package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Bits;
import template.math.DigitBase;
import template.utils.SequenceUtils;

public class FBooleanComputer {
    int[][] accepted = new int[128][0];

    {
        accepted['A'] = SequenceUtils.wrapArray(0, 1);
        accepted['O'] = SequenceUtils.wrapArray(0);
        accepted['X'] = SequenceUtils.wrapArray(0, 2);
        accepted['a'] = SequenceUtils.wrapArray(2);
        accepted['o'] = SequenceUtils.wrapArray(1, 2);
        accepted['x'] = SequenceUtils.wrapArray(1);
    }

    int w;
    int[] pairs;
    DigitBase base3 = new DigitBase(3);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        w = in.readInt();
        int n = in.readInt();
        int m = in.readInt();

        int[] cnts = new int[1 << w];
        for (int i = 0; i < n; i++) {
            cnts[in.readInt()]++;
        }


        pairs = new int[(int) base3.valueOfBit(w)];
        for (int i = 0; i < cnts.length; i++) {
            for (int j = 0; j < cnts.length; j++) {
                int val = 0;
                for (int k = 0; k < w; k++) {
                    val += (int) base3.valueOfBit(k) * (Bits.bitAt(i, k) + Bits.bitAt(j, k));
                }
                pairs[val] += cnts[i] * cnts[j];
            }
        }


        char[] cmd = new char[w];
        for (int i = 0; i < m; i++) {
            in.readString(cmd, 0);
            SequenceUtils.reverse(cmd, 0, w - 1);
            int ans = dfs(cmd, 0, 0);
            out.println(ans);
        }
    }

    public int dfs(char[] cmd, int scan, int val) {
        if (scan == cmd.length) {
            return pairs[val];
        }
        int sum = 0;
        for (int x : accepted[cmd[scan]]) {
            sum += dfs(cmd, scan + 1, val + (int) base3.valueOfBit(scan) * x);
        }
        return sum;
    }
}
