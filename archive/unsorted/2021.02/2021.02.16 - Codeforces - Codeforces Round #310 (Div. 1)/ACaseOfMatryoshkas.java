package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

public class ACaseOfMatryoshkas {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[][] seq = new int[k][];
        for (int i = 0; i < k; i++) {
            int m = in.ri();
            seq[i] = in.ri(m);
        }
        int block = 0;
        int ans = 0;
        for (int i = 0; i < k; i++) {
            boolean detach = seq[i][0] != 1;
            block++;
            for (int j = 0; j + 1 < seq[i].length; j++) {
                if (seq[i][j] + 1 != seq[i][j + 1]) {
                    detach = true;
                }
                if (detach) {
                    block++;
                    ans++;
                }
            }
        }
        ans += block - 1;
        out.println(ans);
    }
}
