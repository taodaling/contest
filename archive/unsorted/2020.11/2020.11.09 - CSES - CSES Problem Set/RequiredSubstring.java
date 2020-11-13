package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Power;
import template.string.ACAutomaton;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class RequiredSubstring {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = in.readString().toCharArray();
        ACAutomaton ac = new ACAutomaton('A', 'Z');
        int charset = 'z' - 'a' + 1;
        ac.beginBuilding();
        for (char c : s) {
            ac.build(c);
        }
        ac.endBuilding(false);
        ACAutomaton.Node[] nodes = ac.getAllNodes().toArray(new ACAutomaton.Node[0]);
        int m = nodes.length;
        int mod = (int) 1e9 + 7;
        long[] prev = new long[m];
        long[] next = new long[m];
        prev[0] = 1;
        for (int i = 0; i <= n; i++) {
            prev[m - 1] = 0;
            for (int j = 0; j < m; j++) {
                prev[j] %= mod;
            }
            if (i == n) {
                break;
            }
            SequenceUtils.deepFill(next, 0L);
            for (int j = 0; j < m; j++) {
                for (int c = 0; c < charset; c++) {
                    next[nodes[j].next[c].id] += prev[j];
                }
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }

        long invalid = Arrays.stream(prev).sum();
        long way = new Power(mod).pow(charset, n);
        long ans = DigitUtils.mod(way - invalid, mod);
        out.println(ans);
    }
}
