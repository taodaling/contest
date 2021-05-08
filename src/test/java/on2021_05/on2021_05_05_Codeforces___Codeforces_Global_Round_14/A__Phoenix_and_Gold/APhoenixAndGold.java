package on2021_05.on2021_05_05_Codeforces___Codeforces_Global_Round_14.A__Phoenix_and_Gold;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class APhoenixAndGold {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int x = in.ri();
        int[] a = in.ri(n);
        Randomized.shuffle(a);
        Arrays.sort(a);
        int s = Arrays.stream(a).sum();
        if (s != x) {
            out.println("YES");
            int cur = 0;
            for (int i = 0; i < n; i++) {
                if (cur + a[i] == x) {
                    SequenceUtils.reverse(a, i, i + 1);
                }
                out.append(a[i]).append(' ');
                cur += a[i];
            }
            out.println();
        } else {
            out.println("NO");
        }
    }
}
