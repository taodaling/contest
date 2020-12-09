package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class CodeTanks {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] life = new int[n];
        int[] scores = new int[n];
        Arrays.fill(life, 100);
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            if (life[a] <= 0) {
                continue;
            }
            if (life[b] > 0) {
                scores[a] += 3;
            }
            life[b] -= 8;
        }
        for (int i = 0; i < n; i++) {
            if (life[i] > 0) {
                scores[i] += life[i] / 2;
            }
        }
        for (int i = 0; i < n; i++) {
            out.append(life[i]).append(' ').append(scores[i]).println();
        }
    }
}
