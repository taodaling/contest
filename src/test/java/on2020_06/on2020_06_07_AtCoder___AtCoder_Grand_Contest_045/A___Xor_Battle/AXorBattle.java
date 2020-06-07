package on2020_06.on2020_06_07_AtCoder___AtCoder_Grand_Contest_045.A___Xor_Battle;



import template.datastructure.LinearBasis;
import template.io.FastInput;
import template.io.FastOutput;

public class AXorBattle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] a = new long[n];
        in.populate(a);
        char[] s = in.readString().toCharArray();

        LinearBasis lb = new LinearBasis();
        for (int i = n - 1; i >= 0; i--) {
            if (s[i] == '0') {
                lb.add(a[i]);
            } else {
                if (!lb.contain(a[i])) {
                    out.println(1);
                    return;
                }
            }
        }

        out.println(0);
    }
}
