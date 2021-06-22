package on2021_06.on2021_06_20_Codeforces___Codeforces_LATOKEN_Round_1__Div__1___Div__2_.Brilliant_Sequence_of_Umbrellas;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;

public class BrilliantSequenceofUmbrellas {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        int k = (int) Math.ceil(2.0 / 3 * Math.sqrt(n));
        long[] a = new long[k];
        a[0] = 1;
        if (k > 1) {
            a[1] = 2;
        }
        long next = 3;
        for (int i = 2; i < k; i++) {
            while (GCDs.gcd(next, a[i - 2]) > 1) {
                next++;
            }
            a[i] = next++;
        }
        out.println(k);
        for(int i = 0; i < k; i++){
            long v = a[i] * (i > 0 ? a[i - 1] : 1);
            out.append(v).append(' ');
        }

    }
}
