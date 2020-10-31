package on2020_10.on2020_10_23_CSES___CSES_Problem_Set.Two_Knights;



import template.io.FastInput;

import java.io.PrintWriter;
import java.util.Arrays;

public class TwoKnights {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] bf = new int[]{0, 6, 28};
        for (int i = 1; i <= n; i++) {
            if (i <= bf.length) {
                out.println(bf[i - 1]);
                continue;
            }
            long size = square(i);
            int[] cnts = new int[9];
            cnts[8] += square(i - 4);
            cnts[6] += (i - 4) * 4;
            cnts[4] += (i - 4) * 4;
            cnts[4] += 4;
            cnts[3] += 8;
            cnts[2] += 4;
            assert Arrays.stream(cnts).sum() == size;
            long ans = 0;
            for(int j = 1; j <= 8; j++){
                ans += (long)cnts[j] * (size - j - 1);
            }
            out.println(ans / 2);
        }
    }

    public int square(int x) {
        return x * x;
    }
}
