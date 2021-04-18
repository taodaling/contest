package on2021_03.on2021_03_27_Codeforces___Codeforces_Round__228__Div__1_.A__Fox_and_Box_Accumulation;



import template.datastructure.MultiSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;
import java.util.TreeSet;

public class AFoxAndBoxAccumulation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] x = in.ri(n);
        Randomized.shuffle(x);
        Arrays.sort(x);
        MultiSet<Integer> set = new MultiSet<>();
        for (int t : x) {
            Integer last = set.floor(t);
            if (last == null) {
                set.add(1);
            } else {
                set.remove(last);
                set.add(last + 1);
            }
        }
        out.println(set.size());
    }
}
