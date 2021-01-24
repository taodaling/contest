package on2021_01.on2021_01_16_CSES___CSES_Problem_Set.Coin_Arrangement;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.problem.CandyAssignProblemSimple;

import java.util.Arrays;

public class CoinArrangement {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int[] b = in.ri(n);
        for (int i = 0; i < n; i++) {
            a[i]--;
            b[i]--;
        }
        int[][] ab = new int[][]{a, b};
        IntegerDeque[] wait = new IntegerDeque[2];
        IntegerDeque[] cand = new IntegerDeque[2];
        for (int i = 0; i < 2; i++) {
            wait[i] = new IntegerDequeImpl(2 * n);
            cand[i] = new IntegerDequeImpl(2 * n);
        }
        long ans = 0;
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < 2; k++) {
                for (int j = 1; j <= ab[k][i]; j++) {
                    cand[k].addLast(i);
                }
                if (ab[k][i] < 0) {
                    wait[k].addLast(i);
                }
            }
            for (int k = 0; k < 2; k++) {
                while (!cand[k].isEmpty() && !wait[k].isEmpty()) {
                    ans += Math.abs(cand[k].removeFirst() - wait[k].removeFirst());
                }
            }
            for (int k = 0; k < 2; k++) {
                while (!cand[k].isEmpty() && !wait[k ^ 1].isEmpty()) {
                    ans += Math.abs(cand[k].removeFirst() - wait[k ^ 1].removeFirst()) + 1;
                }
            }
        }

        out.println(ans);
    }

}
