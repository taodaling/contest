package on2021_11.on2021_11_14_Codeforces___Codeforces_Round__755__Div__1__based_on_Technocup_2022_Elimination_Round_2_.A__Two_Arrays;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToLongFunction;
import template.rand.Randomized;

import java.util.Arrays;

public class ATwoArrays {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int[] b = in.ri(n);
        Randomized.shuffle(a);
        Randomized.shuffle(b);
        Arrays.sort(a);
        Arrays.sort(b);
        for (int i = 0; i < n; i++) {
            if (a[i] != b[i] && a[i] + 1 != b[i]) {
                out.println("NO");
                return;
            }
        }
        out.println("YES");
    }
}
