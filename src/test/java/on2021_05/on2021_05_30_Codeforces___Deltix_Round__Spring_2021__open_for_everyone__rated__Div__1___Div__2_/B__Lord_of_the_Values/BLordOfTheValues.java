package on2021_05.on2021_05_30_Codeforces___Deltix_Round__Spring_2021__open_for_everyone__rated__Div__1___Div__2_.B__Lord_of_the_Values;



import template.io.FastInput;
import template.io.FastOutput;

public class BLordOfTheValues {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        out.println(3 * n);
        for (int i = 0; i < n; i += 2) {
            int l = i + 1;
            int r = i + 2;
            for (int round = 0; round < 2; round++) {
                out.append(2).append(' ').append(l).append(' ').append(r).println();
                out.append(1).append(' ').append(l).append(' ').append(r).println();
                out.append(2).append(' ').append(l).append(' ').append(r).println();
            }
        }
    }


}
