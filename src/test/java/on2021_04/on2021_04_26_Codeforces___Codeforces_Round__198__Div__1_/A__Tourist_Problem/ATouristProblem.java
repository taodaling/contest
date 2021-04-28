package on2021_04.on2021_04_26_Codeforces___Codeforces_Round__198__Div__1_.A__Tourist_Problem;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;
import template.primitve.generated.datastructure.LongPreSum;
import template.rand.Randomized;

import java.util.Arrays;

public class ATouristProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] a = in.rl(n);
        Randomized.shuffle(a);
        Arrays.sort(a);
        LongPreSum ps = new LongPreSum(i -> a[i], n);
        long top = 0;
        long bot = n;
        for (int i = 0; i < n; i++) {
            top += ps.post(i + 1) - (n - 1 - i) * a[i];
        }
        top *= 2;
        top += ps.post(0);
        long g = GCDs.gcd(top, bot);
        out.append(top / g).append(' ').append(bot / g);
    }
}
