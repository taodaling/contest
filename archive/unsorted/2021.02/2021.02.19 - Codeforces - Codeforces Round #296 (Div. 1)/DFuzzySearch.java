package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.MultiMatch;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class DFuzzySearch {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        char[] S = new char[n];
        in.rs(S);
        char[] T = new char[m];
        in.rs(T);
        int[] transform = new int[128];
        transform['A'] = 0;
        transform['T'] = 1;
        transform['G'] = 2;
        transform['C'] = 3;
        for (int i = 0; i < n; i++) {
            S[i] = (char) transform[S[i]];
        }
        for (int i = 0; i < m; i++) {
            T[i] = (char) transform[T[i]];
        }
        int[][] source = new int[4][n];
        int[] pattern = new int[m];
        for (int i = 0; i < m; i++) {
            pattern[i] = T[i];
        }
        SequenceUtils.deepFill(source, 4);
        for (int i = 0; i < 4; i++) {
            int prev = -k - 1;
            for (int j = 0; j < n; j++) {
                if (S[j] == i) {
                    prev = j;
                }
                if (j - prev <= k) {
                    source[i][j] = i;
                }
            }
            int next = n + k;
            for (int j = n - 1; j >= 0; j--) {
                if (S[j] == i) {
                    next = j;
                }
                if (next - j <= k) {
                    source[i][j] = i;
                }
            }
        }
        debug.debugMatrix("source", source);
        debug.debugArray("pattern", pattern);

        boolean[] match = new MultiMatch().match(pattern, source, false);
        int ans = 0;
        for(boolean x : match){
            if(x){
                ans++;
            }
        }
        out.println(ans);
    }
}
