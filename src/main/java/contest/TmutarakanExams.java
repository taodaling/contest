package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class TmutarakanExams {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        int s = in.readInt();

        SequenceUtils.deepFill(comb, -1L);
        long[] cnts = new long[s + 1];
        for (int i = 1; i <= s; i++) {
            int match = s / i;
            cnts[i] = comb(match, k);
        }

        //ie
        for (int i = s; i >= 1; i--) {
            for (int j = i + i; j <= s; j += i) {
                cnts[i] -= cnts[j];
            }
        }

        long ans = 0;
        for (int i = 2; i <= s; i++) {
            ans += cnts[i];
        }

        ans = Math.min(ans, limit);
        out.println(ans);
    }

    int limit = 10000;
    long[][] comb = new long[100][100];

    public long comb(int i, int j) {
        if (comb[i][j] == -1) {
            if (i < j) {
                return comb[i][j] = 0;
            }
            if (j == 0) {
                return comb[i][j] = 1;
            }
            comb[i][j] = comb(i - 1, j) + comb(i - 1, j - 1);
        }
        return comb[i][j];
    }
}
