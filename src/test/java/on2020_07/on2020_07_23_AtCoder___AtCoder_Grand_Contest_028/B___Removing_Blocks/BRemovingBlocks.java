package on2020_07.on2020_07_23_AtCoder___AtCoder_Grand_Contest_028.B___Removing_Blocks;



import template.datastructure.ModPreSum;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.InverseNumber;
import template.math.ModPrimeInverseNumber;
import template.math.Modular;

public class BRemovingBlocks {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);

        Modular mod = new Modular(1e9 + 7);
        InverseNumber inv = new ModPrimeInverseNumber(n, mod);
        ModPreSum ps = new ModPreSum(n + 1, mod);
        ps.populate(i -> inv.inverse(i), n + 1);

        int sum = 0;
        for (int i = 0; i < n; i++) {
            int prob = 0;
            {
                int l = 1;
                int r = i + 1;
                prob = mod.plus(prob, ps.intervalSum(l, r));
            }
            if (i < n - 1) {
                int l = 2;
                int r = n - i;
                prob = mod.plus(prob, ps.intervalSum(l, r));
            }
            int contrib = mod.mul(a[i], prob);
            sum = mod.plus(sum, contrib);
        }
        for(int i = 1; i <= n; i++){
            sum = mod.mul(sum, i);
        }

        out.println(sum);
    }
}
