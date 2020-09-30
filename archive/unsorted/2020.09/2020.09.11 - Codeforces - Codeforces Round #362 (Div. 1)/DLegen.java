package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.ACAutomaton;
import template.utils.Buffer;
import template.utils.SequenceUtils;

import java.util.HashMap;
import java.util.Map;

public class DLegen {
    long inf = (long) 1e18;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long l = in.readLong();
        int[] a = new int[n];
        in.populate(a);

        char[][] s = new char[n][];
        for (int i = 0; i < n; i++) {
            s[i] = in.readString().toCharArray();
        }

        ACAutomaton ac = new ACAutomaton('a', 'z');
        for (int i = 0; i < n; i++) {
            ac.beginBuilding();
            for (char c : s[i]) {
                ac.build(c);
            }
            ac.getBuildLast().increaseCnt(a[i]);
        }
        ac.endBuilding();
        ACAutomaton.Node[] nodes = ac.getAllNodes().toArray(new ACAutomaton.Node[0]);
        m = nodes.length;
        long[][] mat = new long[m][m];
        SequenceUtils.deepFill(mat, -inf);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 'z' - 'a' + 1; j++) {
                mat[i][nodes[i].next[j].id] = nodes[i].next[j].getPreSum();
            }
        }

        long ans = 0;
        long[][] trans = pow(mat, l);
        for (int i = 0; i < m; i++) {
            ans = Math.max(ans, trans[0][i]);
        }

        out.println(ans);
    }

    int m;

    public long[][] mul(long[][] a, long[][] b) {
        long[][] ans = new long[m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                long x = -inf;
                for (int k = 0; k < m; k++) {
                    x = Math.max(x, a[i][k] + b[k][j]);
                }
                ans[i][j] = x;
            }
        }
        return ans;
    }

    public long[][] pow(long[][] x, long n) {
        if (n == 0) {
            long[][] ans = new long[m][m];
            SequenceUtils.deepFill(ans, -inf);
            for (int i = 0; i < m; i++) {
                ans[i][i] = 0;
            }
            return ans;
        }
        long[][] ans = pow(x, n / 2);
        ans = mul(ans, ans);
        if (n % 2 == 1) {
            ans = mul(ans, x);
        }
        return ans;
    }
}
