package on2021_09.on2021_09_04_DMOJ___DMOPC__21_Contest_1.P1___Partial_Game;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class P1PartialGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] a = in.rl(n);
        long[] rounds = new long[2];
        for (int i = 0; i < n; i++) {
            rounds[(int)(a[i] & 1)] += DigitUtils.ceilDiv(a[i], 2);
        }
        if(rounds[0] <= rounds[1]){
            out.println("Alice");
        }else{
            out.println("Duke");
        }
    }
}
