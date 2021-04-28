package on2021_04.on2021_04_18_AtCoder___AtCoder_Regular_Contest_117.B___ARC_Wrecker;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class BARCWrecker {
    int mod = (int)1e9 + 7;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        Randomized.shuffle(a);
        Arrays.sort(a);
        long ans = 1;
        int last = 0;
        for(int x : a){
            int delta = x - last;
            last = x;
            ans = ans * (delta + 1) % mod;
        }
        out.println(ans);
    }
}
