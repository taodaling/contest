package on2021_04.on2021_04_14_Codeforces___Codeforces_Round__206__Div__1_.B__Game_with_Strings;



import chelper.BindInOutStream;
import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBinaryFunction;
import template.utils.Debug;

import java.util.Arrays;

public class BGameWithStrings {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[][] mat = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.rc();
            }
        }
        int inf = (int) 1e9;
        int[] prev = new int[1 << n];
        int[] next = new int[1 << n];
        Arrays.fill(prev, inf);
        prev[1 << n - 1] = 0;
        int charset = 'z' - 'a' + 1;
        int[] go = new int[charset];
        for (int i = 2 * n - 3; i >= 0; i--) {
            debug.debug("i", i);
            debug.debug("prev", prev);
            Arrays.fill(next, i % 2 == 1 ? -inf : inf);
            IntegerBinaryFunction merger = i % 2 == 1 ? Math::max : Math::min;
            int bit = 0;
            for (int j = 0; j < n; j++) {
                int y = i - j;
                if (y < 0 || y >= n) {
                    continue;
                }
                bit |= 1 << j;
            }
            Arrays.fill(go, 0);
            for (int k = 0; k < n; k++) {
                int x = k;
                int y = i - k + 1;
                if (y < 0 || y >= n) {
                    continue;
                }
                go[mat[x][y] - 'a'] |= 1 << x;
            }
            for (int j = bit; j > 0; j = (j - 1) & bit) {
                for (int k = 0; k < charset; k++) {
                    int set = (j | (j << 1)) & go[k];
                    if (Math.abs(prev[set]) > 1e8) {
                        continue;
                    }
                    next[j] = merger.apply(next[j], prev[set] + value(k + 'a'));
                }
            }

            int[] tmp = prev;
            prev = next;
            next = tmp;
        }

        int ans = prev[1];
        ans += value(mat[0][0]);
        out.println(ans > 0 ? "FIRST" : ans < 0 ? "SECOND" : "DRAW");
    }

    int value(int c) {
        return c == 'a' ? 1 : c == 'b' ? -1 : 0;
    }
}
