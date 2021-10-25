package on2021_10.on2021_10_22_Codeforces___Codeforces_Round__749__Div__1___Div__2__based_on_Technocup_2022_Elimination_Round_1_.F__Defender_of_Childhood_Dreams;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntRadix;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.Arrays;

public class FDefenderOfChildhoodDreams {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        IntRadix radix = new IntRadix(k);
        IntegerArrayList list = new IntegerArrayList(n * n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int x = 10;
                while (radix.get(i, x) == radix.get(j, x)) {
                    x--;
                }
                list.add(x);
            }
        }
        int[] ans = list.toArray();
        int max = Arrays.stream(ans).max().orElse(-1);
        out.println(max + 1);
        for(int x : ans){
            out.append(x + 1).append(' ');
        }
        out.println();
    }
}
