package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;
import java.util.Random;

public class BLastMinuteEnhancements {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] x = new int[n];
        in.populate(x);
        Randomized.shuffle(x);
        Arrays.sort(x);
        int last = 0;
        int ans = 0;
        for(int t : x){
            if(t > last){
                last = t;
                ans++;
            }else if(t + 1 > last){
                last = t + 1;
                ans++;
            }
        }
        out.println(ans);
    }
}
