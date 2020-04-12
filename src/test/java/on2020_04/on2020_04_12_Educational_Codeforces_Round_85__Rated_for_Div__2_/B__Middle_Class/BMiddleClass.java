package on2020_04.on2020_04_12_Educational_Codeforces_Round_85__Rated_for_Div__2_.B__Middle_Class;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class BMiddleClass {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long x = in.readInt();
        long[] save = new long[n];
        for (int i = 0; i < n; i++) {
            save[i] = in.readInt();
        }
        Randomized.shuffle(save);
        Arrays.sort(save);
        long sum = 0;
        int ans = 0;
        for (int i = n - 1; i >= 0; i--) {
            sum += save[i];
            if (sum >= x * (n - i)) {
                ans = n - i;
            }
        }
        out.println(ans);
    }
}
