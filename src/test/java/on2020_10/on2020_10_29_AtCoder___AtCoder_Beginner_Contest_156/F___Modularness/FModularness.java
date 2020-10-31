package on2020_10.on2020_10_29_AtCoder___AtCoder_Beginner_Contest_156.F___Modularness;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class FModularness {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        int q = in.readInt();
        int[] d = new int[k];
        in.populate(d);
        int[] modD = new int[k];
        for (int i = 0; i < q; i++) {
            int n = in.readInt();
            int x = in.readInt();
            int m = in.readInt();
            x %= m;

            int cnt = 0;
            long sum = x;
            for (int j = 0; j < k; j++) {
                modD[j] = d[j] % m;
                //t % k = j && t < n - 1
                //j + zk < n - 1
                //j + zk <= n - 2
                int z = Math.max(0, DigitUtils.floorDiv(n - 2 - j, k) + 1);
                if (modD[j] == 0) {
                    cnt += z;
                }
                sum += (long)modD[j] * z;
            }
            cnt += sum / m;
            out.println(n - 1 - cnt);
        }
    }
}
