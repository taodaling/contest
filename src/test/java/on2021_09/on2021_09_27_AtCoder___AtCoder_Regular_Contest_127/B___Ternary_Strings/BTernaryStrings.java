package on2021_09.on2021_09_27_AtCoder___AtCoder_Regular_Contest_127.B___Ternary_Strings;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntRadix;

public class BTernaryStrings {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        L = in.ri();
        gen(new int[]{0, 1, 2}, n, 2, out);
        gen(new int[]{1, 2, 0}, n, 0, out);
        gen(new int[]{2, 0, 1}, n, 1, out);
    }

    int L;
    IntRadix radix = new IntRadix(3);

    public void gen(int[] perm, int N, int first, FastOutput out) {
        for (int i = 0; i < N; i++) {
            out.append(first);
            for (int j = L - 2; j >= 0; j--) {
                out.append(perm[radix.get(i, j)]);
            }
            out.println();
        }
    }
}
