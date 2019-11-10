package on2019_11.on2019_11_09_NIKKEI_Programming_Contest_2019_2.B___Counting_of_Trees;



import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] d = new int[n];
        int[] depth = new int[n];
        for (int i = 0; i < n; i++) {
            d[i] = in.readInt();
            depth[d[i]]++;
        }
        boolean valid = true;
        if (d[0] != 0) {
            valid = false;
        }
        if (depth[0] > 1) {
            valid = false;
        }

        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
        NumberTheory.Power pow = new NumberTheory.Power(mod);
        int lastOne = 0;
        for (int i = 0; i < n; i++) {
            if (depth[i] > 0) {
                lastOne = i;
            }
        }

        int ans = 1;
        for (int i = lastOne; i >= 1; i--) {
            ans = mod.mul(ans, pow.pow(depth[i - 1], depth[i]));
        }

        if(!valid){
            ans = 0;
        }

        out.println(ans);
    }
}
