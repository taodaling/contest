package on2020_06.on2020_06_28_Codeforces___Codeforces_Round__405__rated__Div__1__based_on_VK_Cup_2017_Round_1_.C__Bear_and_Company;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.utils.ArrayIndex;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class CBearAndCompany {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = in.readString().toCharArray();
        int V = 0;
        int K = 0;
        for (int i = 0; i < n; i++) {
            if (s[i] == 'V') {
                V++;
            }
            if (s[i] == 'K') {
                K++;
            }
        }

        int zero = n;
        ArrayIndex ai = new ArrayIndex(V + 1, K + 1, 2 * n + 1);
        int[] prev = new int[ai.totalSize()];
        int[] next = new int[ai.totalSize()];


        int inf = (int) 1e9;
        Arrays.fill(prev, inf);
        for (int i = 0; i <= zero; i++) {
            prev[ai.indexOf(0, 0, i)] = 0;
        }

        for (int i = 0; i < n; i++) {
            debug.debug("i", i);
            debug.debug("prev", prev, ai);
            Arrays.fill(next, inf);


            for (int a = 0; a <= V; a++) {
                for (int b = 0; b <= K; b++) {
                    for (int c = 0; c <= 2 * n; c++) {
                        int val = prev[ai.indexOf(a, b, c)];
                        if (s[i] == 'V' && a + 1 <= V) {
                            next[ai.indexOf(a + 1, b, c)] = Math.min(next[ai.indexOf(a + 1, b, c)], val + Math.abs(c - zero));
                        }
                        if (s[i] == 'K' && b + 1 <= K) {
                            next[ai.indexOf(a, b + 1, c)] = Math.min(next[ai.indexOf(a, b + 1, c)], val + Math.abs(c - zero) + a);
                        }
                        if (s[i] != 'V' && s[i] != 'K' && c + 1 <= 2 * n) {
                            next[ai.indexOf(a, b, c + 1)] = Math.min(next[ai.indexOf(a, b, c + 1)], val);
                        }
                    }
                }
            }

            for (int c = 2 * n; c >= 0; c--) {
                for (int a = 0; a <= V; a++) {
                    for (int b = 0; b <= K; b++) {
                        int val = next[ai.indexOf(a, b, c)];
                        //put down a e
                        if (c > 0) {
                            next[ai.indexOf(0, 0, c - 1)] = Math.min(next[ai.indexOf(0, 0, c - 1)], val);
                        }
                    }
                }
            }

            int[] tmp = prev;
            prev = next;
            next = tmp;
        }


        debug.debug("i", n);
        debug.debug("prev", prev, ai);

        int ans = inf;
        for (int i = 0; i <= V; i++) {
            for (int j = 0; j <= K; j++) {
                ans = Math.min(ans, prev[ai.indexOf(i, j, zero)]);
            }
        }

        out.println(ans);
    }
}
